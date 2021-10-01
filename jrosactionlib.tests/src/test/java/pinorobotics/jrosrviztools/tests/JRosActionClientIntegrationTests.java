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
package pinorobotics.jrosrviztools.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosmessages.std_msgs.Int32Message;
import id.xfunction.ResourceUtils;
import id.xfunction.logging.XLogger;
import jrosactionlib.tests.actionlib_tutorials_msgs.FibonacciActionDefinition;
import jrosactionlib.tests.actionlib_tutorials_msgs.FibonacciGoalMessage;
import jrosactionlib.tests.actionlib_tutorials_msgs.FibonacciResultMessage;
import pinorobotics.jrosactionlib.JRosActionClient;

public class JRosActionClientIntegrationTests {

    private static final ResourceUtils resourceUtils = new ResourceUtils();
    private static JRosClient client;
    private JRosActionClient<FibonacciGoalMessage, FibonacciResultMessage> actionClient;

    @BeforeEach
    public void setup() throws MalformedURLException {
       XLogger.load("logging-test.properties");
        client = new JRosClient("http://localhost:11311/");
        actionClient = new JRosActionClient<>(
                client, new FibonacciActionDefinition(), "/fibonacci");
    }

    @AfterEach
    public void clean() throws Exception {
        actionClient.close();
        client.close();
    }
    
    @Test
    public void test_sendGoal() throws Exception {
        var goal = new FibonacciGoalMessage()
                .withOrder(new Int32Message().withData(5));
        var result = actionClient.sendGoal(goal).get();
        System.out.println(result);
        assertEquals(resourceUtils.readResource("test_sendGoal"), result.toString());
    }
}
