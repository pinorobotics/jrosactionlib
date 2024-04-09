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
/**
 * Interfaces and classes which are agnostic to ROS version of <a
 * href="http://wiki.ros.org/actionlib/">ROS Action Server</a>.
 *
 * @see <a href="https://github.com/pinorobotics/jros2actionlib">Java client for ROS2 Action
 *     Server</a>
 * @see <a href="https://github.com/pinorobotics/jros1actionlib">Java client for ROS1 Action
 *     Server</a>
 * @see <a
 *     href="https://github.com/pinorobotics/jrosactionlib/blob/main/jrosactionlib/release/CHANGELOG.md">Releases</a>
 * @see <a href="https://github.com/pinorobotics/jrosactionlib">GitHub repository</a>
 * @author aeon_flux aeon_flux@eclipso.ch
 */
module jrosactionlib {

    // since many of our API relies on jrosclient classes we need to ensure
    // that all modules reading this module also read jrosclient
    requires transitive jrosclient;
    requires id.xfunction;

    exports pinorobotics.jrosactionlib;
    exports pinorobotics.jrosactionlib.exceptions;
    exports pinorobotics.jrosactionlib.msgs;
    exports pinorobotics.jrosactionlib.impl to
            jros1actionlib,
            jros2actionlib;
}
