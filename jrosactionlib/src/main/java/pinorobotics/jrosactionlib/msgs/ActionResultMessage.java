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
import pinorobotics.jrosactionlib.actionlib_msgs.StatusType;

/**
 * Base interface for all actionlib result messages.
 *
 * <p>Each actionlib result message should consist from status of goal and the result {@link
 * Message}.
 *
 * <p>ROS2: This class represents the Send Goal requests which usually defined in
 * action/dds_connext/[ACTION_NAME]_.idl files inside *::dds_::[ACTION_NAME]_GetResult_Response_
 *
 * @see <a href="https://design.ros2.org/articles/actions.html">Actions</a>
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public interface ActionResultMessage<R extends Message> extends Message {
    StatusType getStatus();

    R getResult();
}
