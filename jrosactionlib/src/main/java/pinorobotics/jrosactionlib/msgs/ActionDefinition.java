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

/**
 * Actionlib requires for each action to have 3 separate messages: Goal, Feedback, Result.
 *
 * <p>On top of that it will create for each such message an actionlib message where the original
 * message + actionlib metadata is stored.
 *
 * <p>Since there are many message types involved it is easy to accidently mix-up message types from
 * different actions.
 *
 * <p>To prevent this happening we use this iface. It consolidates all message types for each action
 * and helps to detect any type issues during compile time.
 *
 * @param <G> message type used to represent a goal
 * @param <R> message type sent by ActionServer upon goal completion
 */
public interface ActionDefinition<G extends Message, R extends Message> {

    /** Actionlib message type for a goal */
    Class<? extends ActionGoalMessage<G>> getActionGoalMessage();

    /** Actionlib message type for a result */
    Class<? extends ActionResultMessage<R>> getActionResultMessage();
}
