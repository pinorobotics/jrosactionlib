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
import id.xfunction.XJson;
import java.util.Objects;

/**
 * Base interface for all actionlib goal messages.
 *
 * <p>Each actionlib goal message should consist from actionlib metadata ({@link GoalId}) and the
 * goal {@link Message}.
 *
 * <p>ROS2: This class represents the Send Goal requests which usually defined in
 * action/dds_connext/[ACTION_NAME]_.idl files inside *::dds_::[ACTION_NAME]_SendGoal_Request_
 *
 * @see <a href="https://design.ros2.org/articles/actions.html">Actions</a>
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public class ActionGoalMessage<G extends Message> implements Message {

    public GoalId goal_id;

    public G goal;

    public ActionGoalMessage<G> withGoalId(GoalId goal_id) {
        this.goal_id = goal_id;
        return this;
    }

    public ActionGoalMessage<G> withGoal(G goal) {
        this.goal = goal;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(goal_id, goal);
    }

    @Override
    public boolean equals(Object obj) {
        var other = (ActionGoalMessage<?>) obj;
        return Objects.equals(goal_id, other.goal_id) && Objects.equals(goal, other.goal);
    }

    @Override
    public String toString() {
        return XJson.asString(
                "goal_id", goal_id,
                "goal", goal);
    }
}
