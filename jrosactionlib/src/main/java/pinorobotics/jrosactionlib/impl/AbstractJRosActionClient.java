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
package pinorobotics.jrosactionlib.impl;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.Message;
import id.xfunction.Preconditions;
import id.xfunction.lang.XThread;
import id.xfunction.logging.XLogger;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import pinorobotics.jrosactionlib.JRosActionClient;
import pinorobotics.jrosactionlib.msgs.ActionDefinition;
import pinorobotics.jrosactionlib.msgs.ActionGoalIdMessage;
import pinorobotics.jrosactionlib.msgs.ActionGoalMessage;
import pinorobotics.jrosactionlib.msgs.ActionResultMessage;

/**
 * @param <I> message type used for goal id
 * @param <G> message type used to represent a goal
 * @param <R> message type sent by ActionServer upon goal completion
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public abstract class AbstractJRosActionClient<
                I extends ActionGoalIdMessage, G extends Message, R extends Message>
        implements JRosActionClient<G, R> {

    private static final XLogger LOGGER = XLogger.getLogger(AbstractJRosActionClient.class);
    private JRosClient client;
    private String actionServerName;
    private ActionDefinition<G, R> actionDefinition;
    private int status; // 0 - not started, 1 - started, 2 - stopped
    private TopicSubmissionPublisher<ActionGoalMessage<?>> goalPublisher;

    /**
     * Creates a new instance of the client
     *
     * @param client ROS client
     * @param actionDefinition message type definitions for an action
     * @param actionServerName name of the action server which will execute the actions
     */
    @SuppressWarnings("unchecked")
    protected AbstractJRosActionClient(
            JRosClient client, ActionDefinition<G, R> actionDefinition, String actionServerName) {
        this.client = client;
        this.actionDefinition = actionDefinition;
        this.actionServerName = actionServerName;
        goalPublisher =
                new TopicSubmissionPublisher<>(
                        (Class) actionDefinition.getActionGoalMessage(),
                        asSendGoalTopicName(actionServerName));
    }

    @Override
    public CompletableFuture<R> sendGoalAsync(G goal) throws Exception {
        LOGGER.entering("sendGoal " + actionServerName);
        startLazy();
        var actionGoal = actionDefinition.getActionGoalMessage().getConstructor().newInstance();
        while (goalPublisher.getNumberOfSubscribers() == 0) {
            LOGGER.fine("No subscribers");
            XThread.sleep(100);
        }

        var goalId = createGoalId();
        actionGoal.withGoalId(goalId);
        actionGoal.withGoal(goal);

        LOGGER.info("Sending goal with id {0}", goalId);
        goalPublisher.submit(actionGoal);

        var future = callGetResult(goalId);

        LOGGER.exiting("sendGoal" + actionServerName);
        return future.thenApply(ActionResultMessage::getResult);
    }

    protected abstract CompletableFuture<ActionResultMessage<R>> callGetResult(I goalId)
            throws Exception;

    protected abstract String asSendGoalTopicName(String actionServerName);

    protected abstract I createGoalId();

    protected void start() throws Exception {
        Preconditions.isTrue(status == 0, "Can be started only once");
        status++;
        client.publish(goalPublisher);
    }

    @Override
    public void close() throws IOException {
        LOGGER.entering("close " + actionServerName);
        if (status == 1) {
            goalPublisher.close();
            // TODO
            // client.unpublish(goalPublisher.getTopic());
            status++;
        }
        LOGGER.exiting("close " + actionServerName);
    }

    private void startLazy() throws Exception {
        if (status == 0) {
            start();
        } else if (status != 1) {
            throw new IllegalStateException("Already stopped");
        }
    }
}
