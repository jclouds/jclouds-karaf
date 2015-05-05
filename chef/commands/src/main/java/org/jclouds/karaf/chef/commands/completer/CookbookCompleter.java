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

package org.jclouds.karaf.chef.commands.completer;

import org.apache.karaf.shell.console.Completer;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.domain.CookbookVersion;
import org.jclouds.karaf.chef.core.ChefConstants;
import org.jclouds.karaf.utils.ServiceHelper;
import org.jclouds.rest.ApiContext;

public class CookbookCompleter extends ChefCompleterSupport implements Completer {

    public void init() {
        cache = cacheProvider.getProviderCacheForType(ChefConstants.COOKBOOK_CACHE);
    }

    @Override
    public void updateOnAdded(ApiContext<ChefApi> chefService) {
        if (chefService != null) {
            Iterable<? extends CookbookVersion> cookbookVersions = chefService.getApi().chefService()
               .listCookbookVersions();
            if (cookbookVersions != null) {
                for (CookbookVersion cookbookVersion : cookbookVersions) {
                    for (String cacheKey : ServiceHelper.findCacheKeysForService(chefService)) {
                        cache.putAll(cacheKey, cookbookVersion.getMetadata().getProviding().keySet());
                    }
                }
            }
        }
    }
}
