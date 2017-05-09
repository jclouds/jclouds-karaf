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
public class FeatureInstallationTest extends BasePaxExamTest {

   @Inject
   FeaturesService featuresService;

   @Before
   public void setUp() throws Exception {
      featuresService.addRepository(getFeaturesFile("features.xml").toURI());
   }

   @Test
   public void testJcloudsFeature() throws Exception {
      featuresService.installFeature("jclouds");
   }

   @Test
   public void testJcloudsComputeFeature() throws Exception {
      featuresService.installFeature("jclouds-compute");
   }

   @Test
   public void testJcloudsBlobstoreFeature() throws Exception {
      featuresService.installFeature("jclouds-blobstore");
   }

   @Test
   public void testJcloudsAllBlobstoreFeature() throws Exception {
      featuresService.installFeature("jclouds-all-blobstore");
   }

   @Test
   public void testJcloudsAllComputeFeature() throws Exception {
      featuresService.installFeature("jclouds-all-compute");
   }

   // Drivers
   @Test
   public void testDriverApachehcFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-apachehc");
   }

   @Test
   public void testDriverBouncycastleFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-bouncycastle");
   }

   @Test
   public void testDriverEnterpriseFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-enterprise");
   }

   @Test
   public void testDriverJodaFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-joda");
   }

   @Test
   public void testDriverJschFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-jsch");
   }

   @Test
   public void testDriverLog4jFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-log4j");
   }

   @Test
   public void testDriverNettyClientFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-netty");
   }

   @Test
   public void testDriverOkHttpClientFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-okhttp");
   }

   @Test
   public void testDriverSlf4jFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-slf4j");
   }

   @Test
   public void testDriverSshjFeature() throws Exception {
      featuresService.installFeature("jclouds-driver-sshj");
   }

   // SCRIPTBUILDER
   @Test
   public void testJcloudsScriptbuilderFeature() throws Exception {
      featuresService.installFeature("jclouds-scriptbuilder");
   }

   // APIS
   @Test
   public void testApiAtmosCoreFeature() throws Exception {
      featuresService.installFeature("jclouds-api-atmos");
   }

   @Test
   public void testApiByonFeature() throws Exception {
      featuresService.installFeature("jclouds-api-byon");
   }

   @Test
   public void testApiCloudstackFeature() throws Exception {
      featuresService.installFeature("jclouds-api-cloudstack");
   }

   @Test
   public void testApiCloudwatchFeature() throws Exception {
      featuresService.installFeature("jclouds-api-cloudwatch");
   }

   @Test
   public void testApiDockerFeature() throws Exception {
      featuresService.installFeature("jclouds-api-docker");
   }

   @Test
   public void testApiElasticStackFeature() throws Exception {
      featuresService.installFeature("jclouds-api-elasticstack");
   }

   @Test
   public void testApiFileSystemFeature() throws Exception {
      featuresService.installFeature("jclouds-api-filesystem");
   }

   @Test
   public void testApiOauthFeature() throws Exception {
      featuresService.installFeature("jclouds-api-oauth");
   }

   @Test
   public void testApiOpenstackCinderFeature() throws Exception {
      featuresService.installFeature("jclouds-api-openstack-cinder");
   }

   @Test
   public void testApiOpenstackKeystoneFeature() throws Exception {
      featuresService.installFeature("jclouds-api-openstack-keystone");
   }

   @Test
   public void testApiOpenstackNovaFeature() throws Exception {
      featuresService.installFeature("jclouds-api-openstack-nova");
   }

   @Test
   public void testApiOpenstackNovaEc2Feature() throws Exception {
      featuresService.installFeature("jclouds-api-openstack-nova-ec2");
   }

   @Test
   public void testApiOpenstackSwiftFeature() throws Exception {
      featuresService.installFeature("jclouds-api-openstack-swift");
   }

   @Test
   public void testApiOpenstackTroveFeature() throws Exception {
      featuresService.installFeature("jclouds-api-openstack-trove");
   }

   @Test
   public void testApiRackspaceCloudDnsFeature() throws Exception {
      featuresService.installFeature("jclouds-api-rackspace-clouddns");
   }

   @Test
   public void testApiRackspaceCloudFilesFeature() throws Exception {
      featuresService.installFeature("jclouds-api-rackspace-cloudfiles");
   }

   @Test
   public void testApiRackspaceCloudIdentityFeature() throws Exception {
      featuresService.installFeature("jclouds-api-rackspace-cloudidentity");
   }

   @Test
   public void testApiRackspaceCloudLoadbalancersFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudloadbalancers-us");
   }

   @Test
   public void testStsFeature() throws Exception {
      featuresService.installFeature("jclouds-api-sts");
   }

   // PROVIDERS
   @Test
   public void testAwsCloudwatchFeature() throws Exception {
      featuresService.installFeature("jclouds-aws-cloudwatch");
   }

   @Test
   public void testAwsEc2Feature() throws Exception {
      featuresService.installFeature("jclouds-aws-ec2");
   }

   @Test
   public void testAwsRoute53Feature() throws Exception {
      featuresService.installFeature("jclouds-aws-route53");
   }

   @Test
   public void testAwsS3Feature() throws Exception {
      featuresService.installFeature("jclouds-aws-s3");
   }

   @Test
   public void testAwsSqsFeature() throws Exception {
      featuresService.installFeature("jclouds-aws-sqs");
   }

   @Test
   public void testAwsStsFeature() throws Exception {
      featuresService.installFeature("jclouds-aws-sts");
   }

   @Test
   public void testAzureBlobFeature() throws Exception {
      featuresService.installFeature("jclouds-azureblob");
   }

   @Test
   public void testB2Feature() throws Exception {
      featuresService.installFeature("jclouds-b2");
   }

   @Test
   public void testDigitalOcean2Feature() throws Exception {
      featuresService.installFeature("jclouds-digitalocean2");
   }

   @Test
   public void testDynectFeature() throws Exception {
      featuresService.installFeature("jclouds-dynect");
   }

   @Test
   public void testElasticaHostsLonBFeature() throws Exception {
      featuresService.installFeature("jclouds-elastichosts-lon-b");
   }

   @Test
   public void testElasticaHostsLonPFeature() throws Exception {
      featuresService.installFeature("jclouds-elastichosts-lon-p");
   }

   @Test
   public void testElasticaHostsSatPtestStsFeature() throws Exception {
      featuresService.installFeature("jclouds-elastichosts-sat-p");
   }

   @Test
   public void testElasticaHostsLaxPFeature() throws Exception {
      featuresService.installFeature("jclouds-elastichosts-lax-p");
   }

   @Test
   public void testElasticaHostsTorPFeature() throws Exception {
      featuresService.installFeature("jclouds-elastichosts-tor-p");
   }

   @Test
   public void testGlesysFeature() throws Exception {
      featuresService.installFeature("jclouds-glesys");
   }

   @Test
   public void testGo2cloudJhb1Feature() throws Exception {
      featuresService.installFeature("jclouds-go2cloud-jhb1");
   }

   @Test
   public void testGogridFeature() throws Exception {
      featuresService.installFeature("jclouds-gogrid");
   }

   @Test
   public void testGoogleCloudStorageFeature() throws Exception {
      featuresService.installFeature("jclouds-google-cloud-storage");
   }

   @Test
   public void testGoogleComputeEngineFeature() throws Exception {
      featuresService.installFeature("jclouds-google-compute-engine");
   }

   @Test
   public void testOpenhostingEast1Feature() throws Exception {
      featuresService.installFeature("jclouds-openhosting-east1");
   }

   @Test
   public void testProfitbricksFeature() throws Exception {
      featuresService.installFeature("jclouds-profitbricks");
   }

   @Test
   public void testRackaspaceCloudstorageUkFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudblockstorage-uk");
   }

   @Test
   public void testRackaspaceCloudstorageUsFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudblockstorage-us");
   }

   @Test
   public void testRackaspaceCloudloadbalancerUsFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudloadbalancers-us");
   }

   @Test
   public void testRackaspaceCloudloadbalancerUkFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudloadbalancers-uk");
   }

   @Test
   public void testRackaspaceCloudserversUsFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudservers-us");
   }

   @Test
   public void testRackaspaceCloudserversUkFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudservers-uk");
   }

   @Test
   public void testRackaspaceCloudDnsUsFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-clouddns-us");
   }

   @Test
   public void testRackaspaceCloudDnsUkFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-clouddns-uk");
   }

   @Test
   public void testRackaspaceCloudfilesUkFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudfiles-uk");
   }

   @Test
   public void testRackaspaceCloudfilesUsFeature() throws Exception {
      featuresService.installFeature("jclouds-rackspace-cloudfiles-us");
   }

   @Test
   public void testServerloveZ1ManFeature() throws Exception {
      featuresService.installFeature("jclouds-serverlove-z1-man");
   }

   @Test
   public void testSkalicloudSdgMyFeature() throws Exception {
      featuresService.installFeature("jclouds-skalicloud-sdg-my");
   }

   @Test
   public void testSoftlayerFeature() throws Exception {
      featuresService.installFeature("jclouds-softlayer");
   }


   @Configuration
   public Option[] config() {
      MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf-minimal").versionAsInProject().type("tar.gz");
      return new Option[]{
              karafDistributionConfiguration().frameworkUrl(karafUrl).name("Apache Karaf").unpackDirectory(new File("target/exam")),
              configureSecurity().disableKarafMBeanServerBuilder(),
              keepRuntimeFolder(),
              editConfigurationFilePut("etc/system.properties", "features.xml", System.getProperty("features.xml")),
              editConfigurationFileExtend(
                      "etc/org.ops4j.pax.url.mvn.cfg",
                      "org.ops4j.pax.url.mvn.repositories",
                      "file:" + System.getProperty("features-repo") + "@id=local@snapshots@releases"),
              logLevel(LogLevelOption.LogLevel.INFO),
      };
   }
}
