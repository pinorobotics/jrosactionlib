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
package pinorobotics.jrosactionlib.actionlib_msgs;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.XJson;
import java.util.Objects;
import pinorobotics.jrosactionlib.msgs.ActionGoalIdMessage;

/** Definition for actionlib_msgs/GoalStatus */
@MessageMetadata(name = GoalStatusMessage.NAME, md5sum = "b6758985eced8e08e99ce12a55072791")
public class GoalStatusMessage implements Message {

    static final String NAME = "actionlib_msgs/GoalStatus";

    public ActionGoalIdMessage goal_id;

    public byte status;

    /**
     * sent over the wire by an action server Allow for the user to associate a string with
     * GoalStatus for debugging
     */
    public StringMessage text = new StringMessage();

    public GoalStatusMessage withGoalId(ActionGoalIdMessage goal_id) {
        this.goal_id = goal_id;
        return this;
    }

    public GoalStatusMessage withStatus(StatusType status) {
        this.status = (byte) status.ordinal();
        return this;
    }

    public GoalStatusMessage withText(StringMessage text) {
        this.text = text;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(goal_id, status, text);
    }

    @Override
    public boolean equals(Object obj) {
        var other = (GoalStatusMessage) obj;
        return Objects.equals(goal_id, other.goal_id)
                && status == other.status
                && Objects.equals(text, other.text);
    }

    @Override
    public String toString() {
        return XJson.asString(
                "goal_id", goal_id,
                "status", status,
                "text", text);
    }
}
