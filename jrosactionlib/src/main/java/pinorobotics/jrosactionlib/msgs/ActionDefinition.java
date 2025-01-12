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
package pinorobotics.jrosactionlib.msgs;

import id.jrosmessages.Message;
import id.jrosmessages.MessageDescriptor;

/**
 * Actionlib action definition.
 *
 * <p>ROS Actionlib allow users to define custom actions in action/*.action files. For each action
 * it requires to have 3 separate messages: Goal, Feedback, Result.
 *
 * <p>On top of that it will create for each such message an actionlib message where the original
 * message (Goal, Feedback, Result) + actionlib metadata is stored.
 *
 * <p>This interface acts similar to *.action files. Users describe ROS action by implementing it
 * and <b>jrosclient</b> will map it to ROS Actionlib action.
 *
 * <p>Since there are many message types involved it is easy to accidently mix-up message types from
 * different actions. This interface helps to address this too. It consolidates all message types
 * for each action and helps to detect any type issues during compile time.
 *
 * @param <I> message type used to represent a goal id (it is not the same across ROS versions)
 * @param <G> message type used to represent a goal
 * @param <R> message type sent by ActionServer upon goal completion
 * @see <a
 *     href="https://docs.ros.org/en/galactic/Tutorials/Intermediate/Creating-an-Action.html#defining-an-action">Defining
 *     actions in ROS2</a>
 * @see <a href="http://wiki.ros.org/actionlib/#A.action_File">Defining actions in ROS1</a>
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public interface ActionDefinition<
        I extends ActionGoalIdMessage, G extends Message, R extends Message> {

    /** Actionlib message type for a goal */
    MessageDescriptor<? extends ActionGoalMessage<I, G>> getActionGoalMessage();

    /** Actionlib message type for a result */
    MessageDescriptor<? extends ActionResultMessage<R>> getActionResultMessage();
}
