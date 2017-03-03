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

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureSecurity;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFileExtend;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;

import javax.inject.Inject;

import org.apache.karaf.features.FeaturesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class FeatureLabsInstallationTest extends BasePaxExamTest {

   @Inject
   FeaturesService featuresService;

   @Before
   public void setUp() throws Exception {
      featuresService.addRepository(getFeaturesFile("features-labs.xml").toURI());
   }

   @Test
   public void testLabsAzureComputeArmFeature() throws Exception {
      featuresService.installFeature("jclouds-azurecompute-arm");
   }

   @Test
   public void testLabsB2Feature() throws Exception {
      featuresService.installFeature("jclouds-b2");
   }

   @Test
   public void testLabsCloudsigma2HnlFeature() throws Exception {
      featuresService.installFeature("jclouds-cloudsigma2-hnl");
   }

   @Test
   public void testLabsCloudsigma2LvsFeature() throws Exception {
      featuresService.installFeature("jclouds-cloudsigma2-lvs");
   }

   @Test
   public void testLabsCloudsigma2SjcFeature() throws Exception {
      featuresService.installFeature("jclouds-cloudsigma2-sjc");
   }

   @Test
   public void testLabsCloudsigma2WdcFeature() throws Exception {
      featuresService.installFeature("jclouds-cloudsigma2-wdc");
   }

   @Test
   public void testLabsCloudsigma2ZrhFeature() throws Exception {
      featuresService.installFeature("jclouds-cloudsigma2-zrh");
   }

   @Test
   public void testLabsGoogleCloudStorageFeature() throws Exception {
      featuresService.installFeature("jclouds-google-cloud-storage");
   }

   @Test
   public void testLabsPacketFeature() throws Exception {
      featuresService.installFeature("jclouds-packet");
   }

   @Test
   public void testLabsVagrantFeature() throws Exception {
      featuresService.installFeature("jclouds-vagrant");
   }


   @Configuration
   public Option[] config() {
      MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf-minimal").versionAsInProject().type("tar.gz");
      return new Option[]{
              karafDistributionConfiguration().frameworkUrl(karafUrl).name("Apache Karaf").unpackDirectory(new File("target/exam")),
              configureSecurity().disableKarafMBeanServerBuilder(),
              keepRuntimeFolder(),
              editConfigurationFilePut("etc/system.properties", "features-labs.xml", System.getProperty("features-labs.xml")),
              editConfigurationFileExtend(
                      "etc/org.ops4j.pax.url.mvn.cfg",
                      "org.ops4j.pax.url.mvn.repositories",
                      "file:" + System.getProperty("features-repo") + "@id=local@snapshots@releases"),
              logLevel(LogLevelOption.LogLevel.INFO),
      };
   }

}
