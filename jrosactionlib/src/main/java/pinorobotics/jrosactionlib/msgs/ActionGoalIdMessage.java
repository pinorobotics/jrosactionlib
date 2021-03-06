/*
 * Copyright 2022 jrosactionlib project
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
 * In different ROS versions the GoalId is different so this interface is used to abstract them from
 * <b>jrosactionlib</b>
 *
 * <p>Users usually does not interact with this interface directly instead they rely on its ROS
 * version specific extensions (see GoalIdMessage for ROS1).
 *
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public interface ActionGoalIdMessage extends Message {

    /** Require goal ids messages to implement equality */
    @Override
    boolean equals(Object obj);

    /** Require goal ids messages to implement equality */
    @Override
    int hashCode();
}
