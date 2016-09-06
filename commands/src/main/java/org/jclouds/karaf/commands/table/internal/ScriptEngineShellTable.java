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

package org.jclouds.karaf.commands.table.internal;

import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.jclouds.karaf.commands.blobstore.BlobStoreCommandBase;
import org.jclouds.karaf.commands.table.BasicShellTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * A shell table implementation that works with groovy expressions.
 */
public class ScriptEngineShellTable<D extends Object> extends BasicShellTable<D> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngineShellTable.class);
   
  private final ScriptEngineManager scriptEngineFactory = new ScriptEngineManager();
  private ScriptEngine scriptEngine;

  /**
   * Constructor
   * @param engine
   */
  public ScriptEngineShellTable(String engine) {
    this.scriptEngine = scriptEngineFactory.getEngineByName(engine);
    if (scriptEngine == null) {
       LOGGER.warn("Could not load a script engine for {}. Will fallback to Groovy", engine);
       scriptEngine = new GroovyScriptEngineImpl();
    }
  }

  /**
   * Evaluates an expression.
   * @param obj
   * @param expression
   * @return
   */
  public String evaluate(Object obj, String expression) {
    String result = "";
    try {
      scriptEngine.put(getType(), obj);
      result = String.valueOf(scriptEngine.eval(expression));
    } catch (Exception ex) {
      //Ignore
    }
    return result;
  }
}
