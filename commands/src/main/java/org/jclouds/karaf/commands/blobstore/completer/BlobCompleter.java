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

package org.jclouds.karaf.commands.blobstore.completer;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.karaf.utils.ServiceHelper;

public class BlobCompleter extends BlobStoreCompleterSupport {

   public void init() {
      cache = cacheProvider.getProviderCacheForType("blob");
   }

   @Override
   public void updateOnAdded(BlobStore blobStore) {
      for (String container : listContainers(blobStore)) {
        for (String cacheKey : ServiceHelper.findCacheKeysForService(blobStore)) {
          cache.putAll(cacheKey, listBlobs(blobStore, container));
        }
      }
   }
}
