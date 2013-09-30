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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.CommandException;
import org.apache.felix.gogo.commands.Option;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.BlobMetadata;
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

   @Option(name = "-d", aliases = "--details", description = "Display blob details", required = false, multiValued = false)
   boolean details;

   private static final PrintStream out = System.out;

   @Override
   protected Object doExecute() throws Exception {
      final BlobStore blobStore = getBlobStore();

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
         if (details) {
            ListeningExecutorService executor = blobStore.getContext().utils().userExecutor();
            Collection<ListenableFuture<BlobMetadata>> futures = Lists.newArrayList();
            for (final String blobName : blobNames) {
               futures.add(executor.submit(new Callable<BlobMetadata>() {
                  @Override
                  public BlobMetadata call() {
                     return blobStore.blobMetadata(containerName, blobName);
                  }
               }));
            }
            Collection<BlobMetadata> metadatas = Futures.allAsList(futures).get();

            for (BlobMetadata metadata : metadatas) {
                out.println(metadata.getName() + ":");
                BlobStoreCommandWithOptions.printMetadata(out, metadata.getContentMetadata());
                out.println();
            }
         } else {
            for (String blobName : blobNames) {
               out.println(blobName);
            }
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
