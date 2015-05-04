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

import java.util.List;

import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.jclouds.chef.ChefApi;
import org.jclouds.rest.ApiContext;

public class ChefContextNameCompleter implements Completer {

    private final StringsCompleter delegate = new StringsCompleter();
    private List<ApiContext<ChefApi>> chefServices;

    @Override
    public int complete(String buffer, int cursor, List<String> candidates) {
        try {
            if (chefServices != null) {
                for (ApiContext<ChefApi> ctx : chefServices) {
                    String contextName = ctx.getName();
                    if (contextName != null) {
                        delegate.getStrings().add(contextName);
                    }
                }
            }
        } catch (Exception ex) {
            // noop
        }
        return delegate.complete(buffer, cursor, candidates);
    }

    public List<ApiContext<ChefApi>> getChefServices() {
        return chefServices;
    }

    public void setChefServices(List<ApiContext<ChefApi>> chefServices) {
        this.chefServices = chefServices;
    }
}