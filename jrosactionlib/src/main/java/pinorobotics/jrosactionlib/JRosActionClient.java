/*
 * Copyright 2021 jrosactionlib project
 * 
 * Website: https://github.com/pinorobotics/jrosactionlib
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pinorobotics.jrosactionlib;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosclient.TopicSubscriber;
import id.jrosmessages.Message;
import id.xfunction.lang.XThread;
import id.xfunction.logging.XLogger;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import pinorobotics.jrosactionlib.msgs.ActionDefinition;
import pinorobotics.jrosactionlib.msgs.ActionGoalMessage;
import pinorobotics.jrosactionlib.msgs.ActionResultMessage;
import pinorobotics.jrosactionlib.msgs.GoalId;

/**
 * Client which allows to interact with ROS Action Server. It communicates with it via a "ROS Action
 * Protocol"
 *
 * @see <a href="http://wiki.ros.org/actionlib/DetailedDescription">Actionlib</a>
 * @param <G> message type used to represent a goal
 * @param <R> message type sent by ActionServer upon goal completion
 */
public abstract class JRosActionClient<G extends Message, R extends Message> implements Closeable {

    private static final XLogger LOGGER = XLogger.getLogger(JRosActionClient.class);
    private JRosClient client;
    private String actionServerName;
    private ActionDefinition<G, R> actionDefinition;
    private boolean isActive;
    private TopicSubmissionPublisher<ActionGoalMessage> goalPublisher;
    private TopicSubscriber<ActionResultMessage> resultsDispatcher;
    private Map<GoalId, CompletableFuture<R>> pendingGoals = new HashMap<>();

    /**
     * Creates a new instance of the client
     *
     * @param client ROS client
     * @param actionDefinition message type definitions for an action
     * @param actionServerName name of the action server which will execute the actions
     */
    public JRosActionClient(
            JRosClient client, ActionDefinition<G, R> actionDefinition, String actionServerName) {
        this.client = client;
        this.actionDefinition = actionDefinition;
        this.actionServerName = actionServerName;
        goalPublisher =
                new TopicSubmissionPublisher<>(
                        (Class) actionDefinition.getActionGoalMessage(),
                        asSendGoalTopicName(actionServerName));
        resultsDispatcher =
                new TopicSubscriber<ActionResultMessage>(
                        (Class) actionDefinition.getActionResultMessage(),
                        actionServerName + "/result") {
                    @Override
                    public void onNext(ActionResultMessage item) {
                        LOGGER.entering("onNext " + actionServerName);
                        var future = pendingGoals.get(item.getStatus().goal_id);
                        future.complete((R) item.getResult());
                        // request next message
                        getSubscription().request(1);
                        LOGGER.exiting("onNext " + actionServerName);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        pendingGoals.values().forEach(fu -> fu.completeExceptionally(throwable));
                    }
                };
    }

    /**
     * Send new goal to action server to execute
     *
     * @return future which will be completed once action will be completed by an action server
     */
    public CompletableFuture<R> sendGoal(G goal) throws Exception {
        LOGGER.entering("sendGoal " + actionServerName);
        var actionGoal = actionDefinition.getActionGoalMessage().getConstructor().newInstance();
        if (!isActive) {
            client.publish(goalPublisher);
            client.subscribe(resultsDispatcher);
            isActive = true;
        }
        while (goalPublisher.getNumberOfSubscribers() == 0) {
            LOGGER.fine("No subscribers");
            XThread.sleep(100);
        }

        var goalId = createGoalId();
        actionGoal.withGoalId(goalId);
        actionGoal.withGoal(goal);

        LOGGER.info("Sending goal with id {0}", goalId);
        goalPublisher.submit(actionGoal);

        // register a new subscriber
        var future = new CompletableFuture<R>();
        pendingGoals.put(goalId, future);

        LOGGER.exiting("sendGoal" + actionServerName);
        return future;
    }

    protected abstract String asSendGoalTopicName(String actionServerName);

    protected abstract GoalId createGoalId();

    @Override
    public void close() throws IOException {
        LOGGER.entering("close " + actionServerName);
        if (isActive) {
            goalPublisher.close();
            client.unpublish(goalPublisher.getTopic());
            resultsDispatcher.getSubscription().cancel();
        }
        isActive = false;
        LOGGER.exiting("close " + actionServerName);
    }
}
