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
/*
 * Authors:
 * - aeon_flux <aeon_flux@eclipso.ch>
 */
package pinorobotics.jrosactionlib.msgs;

import id.jrosmessages.Message;
import pinorobotics.jrosactionlib.actionlib_msgs.GoalStatusMessage;

/**
 * <p>Base interface for all actionlib result messages.
 * <p>Contains actionlib metadata + result itself.
 */
public interface ActionResultMessage<R extends Message> extends Message {
    GoalStatusMessage getStatus();
    R getResult();
}