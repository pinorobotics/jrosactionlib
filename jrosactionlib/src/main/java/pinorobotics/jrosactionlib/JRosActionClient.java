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
package pinorobotics.jrosactionlib;

import id.jrosmessages.Message;
import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

/**
 * Client which allows to interact with ROS Action Server. It communicates with it via a "ROS Action
 * Protocol"
 *
 * @see <a href="http://wiki.ros.org/actionlib/DetailedDescription">Actionlib</a>
 * @param <G> message type used to represent a goal
 * @param <R> message type sent by ActionServer upon goal completion
 */
public interface JRosActionClient<G extends Message, R extends Message> extends Closeable {

    /**
     * Send new goal to action server to execute
     *
     * @return future which will be completed once action will be completed by an action server
     */
    CompletableFuture<R> sendGoalAsync(G goal) throws Exception;
}
