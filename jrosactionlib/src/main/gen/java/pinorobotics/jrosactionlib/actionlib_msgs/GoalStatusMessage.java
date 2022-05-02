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
import pinorobotics.jrosactionlib.msgs.GoalId;

/** Definition for actionlib_msgs/GoalStatus */
@MessageMetadata(name = GoalStatusMessage.NAME, md5sum = "b6758985eced8e08e99ce12a55072791")
public class GoalStatusMessage implements Message {

    static final String NAME = "actionlib_msgs/GoalStatus";

    public enum StatusType {
        /** The goal has yet to be processed by the action server */
        PENDING,

        /** The goal is currently being processed by the action server */
        ACTIVE,

        /**
         * The goal received a cancel request after it started executing and has since completed its
         * execution (Terminal State)
         */
        PREEMPTED,

        /** The goal was achieved successfully by the action server (Terminal State) */
        SUCCEEDED,

        /**
         * The goal was aborted during execution by the action server due to some failure (Terminal
         * State)
         */
        ABORTED,

        /**
         * The goal was rejected by the action server without being processed, because the goal was
         * unattainable or invalid (Terminal State)
         */
        REJECTED,

        /**
         * The goal received a cancel request after it started executing and has not yet completed
         * execution
         */
        PREEMPTING,

        /**
         * The goal received a cancel request before it started executing, but the action server has
         * not yet confirmed that the goal is canceled
         */
        RECALLING,

        /**
         * The goal received a cancel request before it started executing and was successfully
         * cancelled (Terminal State)
         */
        RECALLED,

        /** An action client can determine that a goal is LOST. This should not be */
        LOST,
    }

    public GoalId goal_id;

    public byte status;

    /**
     * sent over the wire by an action server Allow for the user to associate a string with
     * GoalStatus for debugging
     */
    public StringMessage text = new StringMessage();

    public GoalStatusMessage withGoalId(GoalId goal_id) {
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
