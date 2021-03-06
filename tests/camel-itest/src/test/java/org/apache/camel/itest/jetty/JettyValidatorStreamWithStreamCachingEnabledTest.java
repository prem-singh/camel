/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.itest.jetty;

import java.io.InputStream;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @version 
 */
public class JettyValidatorStreamWithStreamCachingEnabledTest extends CamelTestSupport {

    private int port;

    @Test
    public void testValideRequestAsStream() throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream("ValidRequest.xml");
        assertNotNull("the inputStream should not be null", inputStream);

        String response = template.requestBody("http://localhost:" + port + "/test", inputStream, String.class);
        assertEquals("The response should be ok", response, "<ok/>");
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        port = AvailablePortFinder.getNextAvailable(8000);

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                context.setStreamCaching(true);

                from("jetty:http://localhost:" + port + "/test")
                    .to("validator:OptimizationRequest.xsd")
                    .transform(constant("<ok/>"));
            }
        };
    }
}