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

import java.util.Date;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.net.HttpHeaders;

/**
 * Reads the time from the blobstore, optionally enforcing a maximum clock
 * skew.  Improperly configured private blobstores can have sufficient skew
 * to impact operations like signed URLs with timeouts.
 *
 * @author Andrew Gaul
 */
@Command(scope = "jclouds", name = "blobstore-time", description = "Reads time from the blobstore")
public class TimeCommand extends BlobStoreCommandWithOptions {

   @Argument(index = 0, name = "containerName", description = "The name of the container", required = true, multiValued = false)
   String containerName;

   @Argument(index = 1, name = "blobName", description = "The name of the blob", required = true, multiValued = false)
   String blobName;

   @Option(name = "-m", aliases = "--max-skew", description = "Maximum clock skew, in seconds", required = false, multiValued = false)
   int maxSkew = 0;

   @Override
   protected Object doExecute() throws Exception {
      BlobStore blobStore = getBlobStore();

      Blob blob = blobStore.blobBuilder(blobName)
            .payload(ByteSource.empty())
            .contentLength(0)
            .build();
      blobStore.putBlob(containerName, blob);
      try {
         blob = blobStore.getBlob(containerName, blobName);
         blob.getPayload().close();
      } finally {
         blobStore.removeBlob(containerName, blobName);
      }

      String dateString = Iterables.getFirst(blob.getAllHeaders().get(
            HttpHeaders.DATE), /*defaultValue=*/ null);
      if (dateString == null) {
         throw new Exception("Request does not have date header");
      }

      Date blobStoreTime = blobStore.getContext().utils().date()
            .rfc822DateParse(dateString);
      System.out.println(blobStoreTime);

      if (maxSkew != 0) {
         Date localTime = new Date(System.currentTimeMillis());
         if (Math.abs(blobStoreTime.getTime() - localTime.getTime()) >
               1000 * maxSkew) {
            throw new Exception("Maximum clock skew exceeded" +
                  ", blobstore time: " + blobStoreTime +
                  " local time: " + localTime);
         }
      }

      return null;
   }
}
