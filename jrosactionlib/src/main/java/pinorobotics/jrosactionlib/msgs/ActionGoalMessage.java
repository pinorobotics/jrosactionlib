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
 * Base interface for all actionlib goal messages.
 *
 * <p>Each actionlib goal message should consist from actionlib metadata ({@link
 * ActionGoalIdMessage}) and the goal {@link Message}.
 *
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public interface ActionGoalMessage<I extends ActionGoalIdMessage, G extends Message>
        extends Message {

    ActionGoalMessage<I, G> withGoalId(I goal_id);

    ActionGoalMessage<I, G> withGoal(G goal);

    I getGoalId();

    G getGoal();
}
