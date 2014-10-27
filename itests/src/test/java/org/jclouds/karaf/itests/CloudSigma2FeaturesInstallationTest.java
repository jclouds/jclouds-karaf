/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jclouds.karaf.itests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class CloudSigma2FeaturesInstallationTest extends JcloudsFeaturesTestSupport {

    @Before
    public void setUp() {
        System.err.println(executeCommand("features:addurl " + getJcloudsKarafFeatureURL()));
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testCloudSigma2Honolulu() throws Exception {
        installAndCheckFeature("jclouds-cloudsigma2-hnl");
    }

    @Test
    public void testCloudSigma2LasVegas() throws Exception {
        installAndCheckFeature("jclouds-cloudsigma2-lvs");
    }

    @Test
    public void testCloudSigma2SanJose() throws Exception {
        installAndCheckFeature("jclouds-cloudsigma2-sjc");
    }

    @Test
    public void testCloudSigma2Washington() throws Exception {
        installAndCheckFeature("jclouds-cloudsigma2-wdc");
    }

    @Test
    public void testCloudSigma2Zurich() throws Exception {
        installAndCheckFeature("jclouds-cloudsigma2-zrh");
    }
}
