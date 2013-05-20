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

package org.jclouds.karaf.urlhandler;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.inject.Module;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.karaf.utils.ServiceHelper;
import org.osgi.service.url.AbstractURLStreamHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlobUrlHandler extends AbstractURLStreamHandlerService {

    private static final String BLOBSTORE_TMP_FOLDER = System.getProperty("karaf.data") + File.separatorChar + "blobstore";

    private final Logger logger = LoggerFactory.getLogger(BlobUrlHandler.class);

    private static String SYNTAX = "blob:provider/container/blob?id=?????";

    private List<BlobStore> blobStores = new LinkedList<BlobStore>();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Open the connection for the given URL.
     *
     * @param url the url from which to open a connection.
     * @return a connection on the specified URL.
     * @throws java.io.IOException if an error occurs or if the URL is malformed.
     */
    @Override
    public URLConnection openConnection(URL url) throws IOException {
        if (url.getPath() == null || url.getPath().trim().length() == 0 || !url.getPath().contains("/")) {
            throw new MalformedURLException("Container / Blob cannot be null or empty. Syntax: " + SYNTAX);
        }
        String[] parts = url.getPath().split("/");

        if (parts.length == 2 && (url.getHost() == null || url.getHost().trim().length() == 0)) {
            throw new MalformedURLException("Provider cannot be null or empty. Syntax: " + SYNTAX);
        }

        logger.debug("Blob Protocol URL is: [" + url + "]");
        return new Connection(url);
    }

    public class Connection extends URLConnection {
        final String id;
        final String providerOrApi;
        final String containerName;
        final String blobName;
        final URL url;

        public Connection(URL url) {
            super(url);
            this.url = url;
            int index = 0;
            String[] parts = url.getPath().split("/");
            if (url.getHost() == null || url.getHost().trim().length() == 0) {
                this.providerOrApi = parts[index++];
            } else {
                this.providerOrApi = url.getHost();
            }
            this.containerName = parts[index++];
            StringBuilder builder = new StringBuilder();
            builder.append(parts[index++]);

            for (int i = index; i < parts.length; i++) {
                builder.append("/").append(parts[i]);
            }
            this.blobName = builder.toString();
            //Parse the query string for id.
            Map<String, String> parameters = parseUrlParameters(url);
            if (parameters != null && parameters.containsKey("id")) {
                id = parameters.get("id");
            } else {
                id = null;
            }
        }

        @Override
        public void connect() throws IOException {
        }

        @Override
        public InputStream getInputStream() throws IOException {
            try {
                BlobStore blobStore = ServiceHelper.getService(id, providerOrApi, blobStores);
                if (blobStore == null && url.getUserInfo() != null) {
                    String userInfo = url.getUserInfo();
                    String[] ui = userInfo.split(":");
                    if (ui != null && ui.length == 2) {
                        String identity = ui[0];
                        String credential = ui[1];
                        blobStore = createBlobStore(providerOrApi, identity, credential, new LinkedHashSet<Module>(), new Properties());
                        blobStores.add(blobStore);
                    }
                }
                if (blobStore == null) {
                    throw new IOException("BlobStore service not available for provider " + providerOrApi);
                }
                if (!blobStore.containerExists(containerName)) {
                    throw new IOException("Container " + containerName + " does not exists");
                } else if (!blobStore.blobExists(containerName, blobName)) {
                    throw new IOException("Blob " + blobName + " does not exists");
                }

                Blob blob = blobStore.getBlob(containerName, blobName);

                return blob.getPayload().getInput();
            } catch (Exception e) {
                throw (IOException) new IOException("Error opening blob protocol url").initCause(e);
            }
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            try {
                final BlobStore blobStore = ServiceHelper.getService(id, providerOrApi, blobStores);
                if (!blobStore.containerExists(containerName)) {
                    blobStore.createContainerInLocation(null, containerName);
                }

                final CountDownLatch readLatch = new CountDownLatch(1);
                final File tmpDir = Files.createTempDir();
                final File tmpBlob = File.createTempFile("blob", null, tmpDir);

                FileOutputStream out = new FileOutputStream(tmpBlob) {
                    @Override
                    public void close() throws IOException {
                        readLatch.countDown();
                    }
                };

                Runnable putBlob = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            readLatch.await();
                            Blob blob = blobStore.blobBuilder(blobName).payload(tmpBlob).build();
                            blobStore.putBlob(containerName, blob);
                            tmpBlob.delete();
                            tmpDir.delete();
                        } catch (InterruptedException e) {
                            logger.error("Interrupted while waiting on blob read.", e);
                        }
                    }
                };
                executorService.submit(putBlob);
                return out;
            } catch (Exception e) {
                throw (IOException) new IOException("Error opening blob protocol url").initCause(e);
            }
        }

        protected Map<String, String> parseUrlParameters(URL url) {
            Map<String, String> map = new HashMap<String, String>();
            if (url != null && url.getQuery() != null) {
                String[] params = url.getQuery().split("&");
                for (String param : params) {
                    String name = param.split("=")[0];
                    String value = param.split("=")[1];
                    map.put(name, value);
                }
            }
            return map;
        }
    }

    private BlobStore createBlobStore(String providerOrApi, String identity, String credential, Iterable<? extends Module> modules, Properties props) {
        ContextBuilder builder = ContextBuilder.newBuilder(providerOrApi).credentials(identity, credential).modules(modules).overrides(props);
        BlobStoreContext context = builder.build(BlobStoreContext.class);
        BlobStore blobStore = context.getBlobStore();
        return blobStore;
    }


    public void setBlobStores(List<BlobStore> blobStores) {
        this.blobStores = blobStores;
    }

    public List<BlobStore> getBlobStores() {
        return blobStores;
    }
}
