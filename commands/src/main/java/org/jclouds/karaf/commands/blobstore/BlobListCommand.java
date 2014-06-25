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

package org.jclouds.karaf.commands.blobstore;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.CommandException;
import org.apache.felix.gogo.commands.Option;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.ListContainerOptions;

/**
 * List blobs in a container.
 *
 * @author: iocanel
 */
@Command(scope = "jclouds", name = "blobstore-list", description = "Lists blobs in a container")
public class BlobListCommand extends BlobStoreCommandWithOptions {

   @Argument(index = 0, name = "containerName", description = "The name of the container", required = true)
   String containerName;

   @Argument(index = 1, name = "directoryPath", description = "List blobs only in this directory path", required = false)
   String directoryPath;

   @Override
   protected Object doExecute() throws Exception {
      BlobStore blobStore = getBlobStore();

      ListContainerOptions options = ListContainerOptions.Builder.recursive();
      if (directoryPath != null) {
         options = options.inDirectory(directoryPath);
      }

      while (true) {
         PageSet<? extends StorageMetadata> blobStoreMetadatas = blobStore.list(containerName, options);
         List<String> blobNames = Lists.newArrayList();

         for (StorageMetadata blobMetadata : blobStoreMetadatas) {
            String blobName = blobMetadata.getName();
            // do not add to cacheProvider since long lists will cause OutOfMemoryError
            blobNames.add(blobName);
         }

         Collections.sort(blobNames);
         for (String blobName : blobNames) {
            System.out.println(blobName);
         }

         String marker = blobStoreMetadatas.getNextMarker();
         if (marker == null) {
            break;
         }

         options = options.afterMarker(marker);
      }
      return null;
   }
}
