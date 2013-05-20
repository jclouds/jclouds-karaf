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

import java.util.List;

import com.google.common.reflect.TypeToken;
import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.apis.Apis;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.providers.Providers;

public class BlobStoreProviderCompleter implements Completer {

   private final StringsCompleter delegate = new StringsCompleter();
   private List<? extends BlobStore> blobStoreServices;

   private final boolean displayProvidersWithoutService;

   public BlobStoreProviderCompleter(boolean displayProvidersWithoutService) {
     this.displayProvidersWithoutService = displayProvidersWithoutService;
   }

   @Override
   public int complete(String buffer, int cursor, List<String> candidates) {
      try {
        if (displayProvidersWithoutService) {
          for (ProviderMetadata providerMetadata : Providers.viewableAs(TypeToken.of(BlobStoreContext.class))) {
            delegate.getStrings().add(providerMetadata.getId());
          }
        } if (blobStoreServices != null) {
            for (BlobStore blobStore : blobStoreServices) {
               String id = blobStore.getContext().unwrap().getId();
               if (Providers.withId(id) != null) {
                  delegate.getStrings().add(id);
               }
            }
         }
      } catch (Exception ex) {
         // noop
      }
      return delegate.complete(buffer, cursor, candidates);
   }

   public List<? extends BlobStore> getBlobStoreServices() {
      return blobStoreServices;
   }

   public void setBlobStoreServices(List<? extends BlobStore> blobStoreServices) {
      this.blobStoreServices = blobStoreServices;
   }
}
