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

package org.jclouds.karaf.chef.commands;

import org.apache.felix.gogo.commands.Command;
import org.jclouds.chef.ChefApi;
import org.jclouds.rest.ApiContext;

@Command(scope = "chef", name = "cookbook-list", description = "Lists the Chef Cook Books")
public class ChefCookbookListCommand extends ChefCommandWithOptions {

    @Override
    protected Object doExecute() throws Exception {
       ApiContext<ChefApi> service = null;
        try {
            service = getChefService();
        } catch (Throwable t) {
            System.err.println(t.getMessage());
            return null;
        }
        printCookbooks(service, service.getApi().chefService().listCookbookVersions(), System.out);
        return null;
    }
}
