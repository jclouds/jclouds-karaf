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

package org.jclouds.karaf.commands.compute.completer;

import java.util.Set;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.karaf.core.Constants;
import org.jclouds.karaf.utils.ServiceHelper;


public class HardwareCompleter extends ComputeCompleterSupport {

   public void init() {
      cache = cacheProvider.getProviderCacheForType(Constants.HARDWARE_CACHE);
   }

   @Override
   public void updateOnAdded(ComputeService computeService) {
      if (computeService != null) {
         Set<? extends Hardware> hardwares = computeService.listHardwareProfiles();
         if (hardwares != null) {
            for (Hardware hardware : hardwares) {
              for (String cacheKey : ServiceHelper.findCacheKeysForService(computeService)) {
                cache.put(cacheKey, hardware.getId());
              }
            }
         }
      }
   }
}
