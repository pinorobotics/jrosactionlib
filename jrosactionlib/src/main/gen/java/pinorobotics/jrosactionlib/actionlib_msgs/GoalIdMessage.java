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
import id.jrosmessages.primitives.Time;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.XJson;
import java.util.Objects;

/** Definition for actionlib_msgs/GoalID */
@MessageMetadata(type = GoalIdMessage.NAME, md5sum = "80cf13439fb52033034dd028f646e989")
public class GoalIdMessage implements Message {

    static final String NAME = "actionlib_msgs/GoalID";

    /**
     * The stamp should store the time at which this goal was requested. It is used by an action
     * server when it tries to preempt all goals that were requested before a certain time
     */
    public Time stamp = new Time();

    /**
     * The id provides a way to associate feedback and result message with specific goal requests.
     * The id specified must be unique.
     */
    public StringMessage id = new StringMessage();

    public GoalIdMessage withStamp(Time stamp) {
        this.stamp = stamp;
        return this;
    }

    public GoalIdMessage withId(StringMessage id) {
        this.id = id;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stamp, id);
    }

    @Override
    public boolean equals(Object obj) {
        var other = (GoalIdMessage) obj;
        return Objects.equals(stamp, other.stamp) && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return XJson.asString(
                "stamp", stamp,
                "id", id);
    }
}
