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

package org.jclouds.karaf.commands.compute;

import com.google.common.collect.Lists;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.ec2.compute.options.EC2TemplateOptions;
import org.jclouds.karaf.core.Constants;
import org.jclouds.karaf.recipe.RecipeManager;
import org.jclouds.karaf.recipe.RecipeManagerImpl;
import org.jclouds.karaf.utils.ServiceHelper;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.scriptbuilder.domain.StatementList;
import org.jclouds.scriptbuilder.statements.login.AdminAccess;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:gnodet[at]gmail.com">Guillaume Nodet (gnodet)</a>
 */
@Command(scope = "jclouds", name = "node-create", description = "Creates a node.", detailedDescription = "classpath:node-create.txt")
public class NodeCreateCommand extends ComputeCommandWithOptions {
   @Option(name = "--adminAccess", description = "Sets up a user account with passwordless sudo access and copies ssh keys.")
   private boolean adminAccess;

   @Option(name = "--smallest", description = "Uses the smallest possible hardware.")
   private boolean smallest;

   @Option(name = "--fastest", description = "Uses the fastest possible hardware.")
   private boolean fastest;

   @Option(name = "--biggest", description = "Uses the biggest possible hardware." )
   private boolean biggest;

   @Option(name = "--hardwareId", description = "Uses the hardware id specified. You can see the available values with jclouds:hardware-list.")
   private String hardwareId;

   @Option(name = "--ec2-security-groups", multiValued = true)
   private List<String> ec2SecurityGroups;

   @Option(name = "--ec2-key-pair")
   private String ec2KeyPair;

   @Option(name = "--ec2-no-key-pair")
   private String ec2NoKeyPair;

   @Option(name = "--os-family", multiValued = false, required = false, description = "OS Family predicate. When using this option the command will try to find an operating system of the family provided.")
   private String osFamily;

   @Option(name = "--os-version", multiValued = false, required = false, description = "OS Version predicate. When using this option the command will try to find an operating system version matching the one provided.")
   private String osVersion;

   @Option(name = "--imageId", multiValued = false, required = false, description = "Uses the image id specified. You can see the available values with jclouds:image-list.")
   private String imageId;

   @Option(name = "--locationId", multiValued = false, required = false, description = "Uses the location/region specified. You can see the available values with jclouds:location-list.")
   private String locationId;

   @Option(name = "--recipe", multiValued = true, required = false, description = "The recipe to use to create the node. Can be used multiple times (e.g. --recipe chef/java::openjdk --recipe chef/mysql)")
   private String[] recipes;

   @Argument(name = "group", index = 0, multiValued = false, required = true, description = "Node group.")
   private String group;

   @Argument(name = "number", index = 1, multiValued = false, required = false, description = "Number of nodes to create.")
   private Integer number = 1;

   private RecipeManager recipeManager = new RecipeManagerImpl();

   @Override
   protected Object doExecute() throws Exception {
      ComputeService service = null;
      try {
         service = getComputeService();
      } catch (Throwable t) {
         System.err.println(t.getMessage());
         return null;
      }

      TemplateBuilder builder = service.templateBuilder();
      builder.any();
      if (smallest) {
         builder.smallest();
      }
      if (fastest) {
         builder.fastest();
      }
      if (biggest) {
         builder.biggest();
      }
      if (locationId != null) {
         builder.locationId(locationId);
      }
      if (imageId != null) {
         builder.imageId(imageId);
      }
      if (hardwareId != null) {
         builder.hardwareId(hardwareId);
      }

      if (osFamily != null) {
         builder.osFamily(OsFamily.fromValue(osFamily));
      }

      if (osVersion != null) {
         builder.osVersionMatches(osVersion);
      }

      TemplateOptions options = service.templateOptions();
      List<Statement> statements = Lists.newLinkedList();

      if (adminAccess) {
         statements.add(AdminAccess.standard());
      }
       if (recipes != null) {
           for (String recipe : recipes) {
               statements.add(recipeManager.createStatement(recipe, group));
           }
       }
      if (ec2SecurityGroups != null) {
         options.as(EC2TemplateOptions.class).securityGroups(ec2SecurityGroups);
      }
      if (ec2KeyPair != null) {
         options.as(EC2TemplateOptions.class).keyPair(ec2KeyPair);
      }
      if (ec2NoKeyPair != null) {
         options.as(EC2TemplateOptions.class).noKeyPair();
      }

      Set<? extends NodeMetadata> metadatas = null;

      if (!statements.isEmpty()) {
          options.runScript(new StatementList(statements));
      }

      try {
         metadatas = service.createNodesInGroup(group, number, builder.options(options).build());
      } catch (RunNodesException ex) {
         System.out.println("Failed to create nodes:" + ex.getMessage());
      }

     if (metadatas != null && !metadatas.isEmpty()) {
       System.out.println("Created nodes:");
       printNodes(service, metadatas, System.out);

       for (NodeMetadata node : metadatas) {
         for (String cacheKey : ServiceHelper.findCacheKeysForService(service)) {
           cacheProvider.getProviderCacheForType(Constants.ACTIVE_NODE_CACHE).put(cacheKey, node.getId());
           cacheProvider.getProviderCacheForType(Constants.INACTIVE_NODE_CACHE).put(cacheKey, node.getId());
           cacheProvider.getProviderCacheForType(Constants.SUSPENDED_NODE_CACHE).put(cacheKey, node.getId());
         }
       }
     }

      return null;
   }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public void setRecipeManager(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }
}
