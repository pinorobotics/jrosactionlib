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
import id.jroscommon.RosName;
import id.jrosmessages.Message;
import id.jrosmessages.RosInterfaceType;
import id.xfunction.Preconditions;
import id.xfunction.logging.XLogger;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import pinorobotics.jrosactionlib.JRosActionClient;
import pinorobotics.jrosactionlib.exceptions.JRosActionLibException;
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
    private RosName actionServerName;
    private ActionDefinition<I, G, R> actionDefinition;
    private int status; // 0 - not started, 1 - started, 2 - stopped
    private TopicSubmissionPublisher<ActionGoalMessage<I, G>> goalPublisher;

    /**
     * Creates a new instance of the client
     *
     * @param client ROS client
     * @param actionDefinition message type definitions for an action
     * @param actionServerName name of the action server which will execute the actions
     */
    @SuppressWarnings("unchecked")
    protected AbstractJRosActionClient(
            JRosClient client,
            ActionDefinition<I, G, R> actionDefinition,
            RosName actionServerName) {
        this.client = client;
        this.actionDefinition = actionDefinition;
        this.actionServerName = actionServerName;
        goalPublisher =
                new TopicSubmissionPublisher(
                        actionDefinition.getActionGoalMessage(), actionServerName);
    }

    @Override
    public CompletableFuture<R> sendGoalAsync(G goal) throws JRosActionLibException {
        LOGGER.entering("sendGoal " + actionServerName);
        startLazy();
        try {
            var actionGoal =
                    actionDefinition
                            .getActionGoalMessage()
                            .getMessageClass()
                            .getConstructor()
                            .newInstance();
            var goalId = createGoalId();
            actionGoal.withGoalId(goalId);
            actionGoal.withGoal(goal);

            submitGoal(goalId, actionGoal);

            var future = callGetResult(goalId);
            LOGGER.exiting("sendGoal" + actionServerName);
            return future.thenApply(ActionResultMessage::getResult);
        } catch (JRosActionLibException e) {
            throw e;
        } catch (Exception e) {
            throw new JRosActionLibException(e);
        }
    }

    protected void submitGoal(I goalId, ActionGoalMessage<I, G> actionGoal) {
        LOGGER.info("Sending goal with id {0}", goalId);
        goalPublisher.submit(actionGoal);
    }

    @Override
    public void close() throws IOException {
        LOGGER.entering("close " + actionServerName);
        if (status == 1) {
            goalPublisher.close();
            // TODO
            // client.unpublish(goalPublisher.getTopic());
            status++;
            onClose();
        }
        LOGGER.exiting("close " + actionServerName);
    }

    protected TopicSubmissionPublisher<ActionGoalMessage<I, G>> getGoalPublisher() {
        return goalPublisher;
    }

    protected abstract CompletableFuture<? extends ActionResultMessage<R>> callGetResult(I goalId);

    protected abstract I createGoalId();

    protected void onStart() throws Exception {}

    protected void onClose() {}

    private void start() {
        Preconditions.isTrue(status == 0, "Can be started only once");
        Preconditions.equals(
                RosInterfaceType.ACTION,
                actionDefinition.getActionGoalMessage().readInterfaceType(),
                "ActionGoalMessage should have ACTION interfaceType");
        Preconditions.equals(
                RosInterfaceType.ACTION,
                actionDefinition.getActionResultMessage().readInterfaceType(),
                "ActionResultMessage should have ACTION interfaceType");
        status++;
        try {
            client.publish(goalPublisher);
            onStart();
        } catch (Exception e) {
            throw new JRosActionLibException(e);
        }
    }

    private void startLazy() {
        if (status == 0) {
            start();
        } else if (status != 1) {
            throw new JRosActionLibException("Already stopped");
        }
    }
}
