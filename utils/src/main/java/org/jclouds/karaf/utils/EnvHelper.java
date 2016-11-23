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

package org.jclouds.karaf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.jclouds.Constants;
import org.jclouds.domain.Credentials;
import org.jclouds.googlecloud.GoogleCredentialsFromJson;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;
import com.google.common.io.Files;

public class EnvHelper {

    public static final String JCLOUDS_COMPUTE_PROVIDER = "JCLOUDS_COMPUTE_PROVIDER";
    public static final String JCLOUDS_COMPUTE_API = "JCLOUDS_COMPUTE_API";
    public static final String JCLOUDS_COMPUTE_IDENTITY = "JCLOUDS_COMPUTE_IDENTITY";
    public static final String JCLOUDS_COMPUTE_CREDENTIAL = "JCLOUDS_COMPUTE_CREDENTIAL";
    public static final String JCLOUDS_COMPUTE_ENDPOINT = "JCLOUDS_COMPUTE_ENDPOINT";


    public static final String JCLOUDS_BLOBSTORE_PROVIDER = "JCLOUDS_BLOBSTORE_PROVIDER";
    public static final String JCLOUDS_BLOBSTORE_API = "JCLOUDS_BLOBSTORE_API";
    public static final String JCLOUDS_BLOBSTORE_IDENTITY = "JCLOUDS_BLOBSTORE_IDENTITY";
    public static final String JCLOUDS_BLOBSTORE_CREDENTIAL = "JCLOUDS_BLOBSTORE_CREDENTIAL";
    public static final String JCLOUDS_BLOBSTORE_ENDPOINT = "JCLOUDS_BLOBSTORE_ENDPOINT";

    public static final String JCLOUDS_USER = "JCLOUDS_USER";
    public static final String JCLOUDS_PASSWORD = "JCLOUDS_PASSWORD";

    private EnvHelper() {
        //Utility Class
    }

    /**
     * Returns the provider value and falls back to env if the specified value is null.
     *
     * @param provider
     * @return
     */
    public static String getComputeProvider(String provider) {
        return getValueOrPropertyOrEnvironmentVariable(
            provider, Constants.PROPERTY_PROVIDER, JCLOUDS_COMPUTE_PROVIDER);
    }

    /**
     * Returns the provider value and falls back to env if the specified value is null.
     * @param api
     * @return
     */
    public static String getComputeApi(String api) {
        return getValueOrPropertyOrEnvironmentVariable(
            api, Constants.PROPERTY_API, JCLOUDS_COMPUTE_API);
    }

    /**
     * Returns the provider value and falls back to env if the specified value is null.
     *
     * @param identity
     * @return
     */
    public static String getComputeIdentity(String identity) {
        return getValueOrPropertyOrEnvironmentVariable(
            identity, Constants.PROPERTY_IDENTITY, JCLOUDS_COMPUTE_IDENTITY);
    }

    /**
     * Returns the credential value and falls back to env if the specified value is null.
     *
     * @param The provider id
     * @param credential
     * @return
     */
    public static String getComputeCredential(String provider, String credential) {
        return getCredentialValue(provider, getValueOrPropertyOrEnvironmentVariable(
            credential, Constants.PROPERTY_CREDENTIAL, JCLOUDS_COMPUTE_CREDENTIAL));
    }

    /**
     * Extracts the credential value from the Google Cloud credentials json file.
     * @param credentialValue
     * @return
     */
   private static String getGoogleCredentialFromJsonFileIfPath(String credentialValue) {
      File credentialsFile = new File(credentialValue);
      if (credentialsFile.exists()) {
         try {
            String fileContents = Files.toString(credentialsFile, Charsets.UTF_8);
            Supplier<Credentials> credentialSupplier = new GoogleCredentialsFromJson(fileContents);
            return credentialSupplier.get().credential;
         } catch (IOException e) {
            return null;
         }
      } else {
         return credentialValue;
      }
   }

    /**
     * Returns the endpoint value and falls back to env if the specified value is null.
     *
     * @param endpoint
     * @return
     */
    public static String getComputeEndpoint(String endpoint) {
        return getValueOrPropertyOrEnvironmentVariable(
            endpoint, Constants.PROPERTY_ENDPOINT, JCLOUDS_COMPUTE_ENDPOINT);
    }

    /**
     * Returns the provider value and falls back to env if the specified value is null.
     *
     * @param provider
     * @return
     */
    public static String getBlobStoreProvider(String provider) {
        return getValueOrPropertyOrEnvironmentVariable(
            provider, Constants.PROPERTY_PROVIDER, JCLOUDS_BLOBSTORE_PROVIDER);
    }

    /**
     * Returns the provider value and falls back to env if the specified value is null.
     * @param api
     * @return
     */
    public static String getBlobStoreApi(String api) {
        return getValueOrPropertyOrEnvironmentVariable(
            api, Constants.PROPERTY_API, JCLOUDS_BLOBSTORE_API);
    }

    /**
     * Returns the provider value and falls back to env if the specified value is null.
     *
     * @param identity
     * @return
     */
    public static String getBlobStoreIdentity(String identity) {
        return getValueOrPropertyOrEnvironmentVariable(
            identity, Constants.PROPERTY_IDENTITY, JCLOUDS_BLOBSTORE_IDENTITY);
    }

    /**
     * Returns the credential value and falls back to env if the specified value is null.
     *
     * @param The provider id
     * @param credential
     * @return
     */
    public static String getBlobStoreCredential(String provider, String credential) {
        return getCredentialValue(provider, getValueOrPropertyOrEnvironmentVariable(
            credential, Constants.PROPERTY_CREDENTIAL, JCLOUDS_BLOBSTORE_CREDENTIAL));
    }

    /**
     * Returns the endpoint value and falls back to env if the specified value is null.
     *
     * @param endpoint
     * @return
     */
    public static String getBlobStoreEndpoint(String endpoint) {
        return getValueOrPropertyOrEnvironmentVariable(
            endpoint, Constants.PROPERTY_ENDPOINT, JCLOUDS_BLOBSTORE_ENDPOINT);
    }

    /**
     * Returns the user value and falls back to env if the specified value is null.
     *
     * @param user
     * @return
     */
    // TODO: which property to use?
    public static String getUser(String user) {
        if (user == null) {
            user = System.getenv(JCLOUDS_USER);
        }
        return user;
    }

    /**
     * Returns the password value and falls back to env if the specified value is null.
     *
     * @param password
     * @return
     */
    // TODO: which property to use?
    public static String getPassword(String password) {
        if (password == null) {
            password = System.getenv(JCLOUDS_PASSWORD);
        }
        return password;
    }

    public static void loadProperties(File fileName) throws IOException {
        Properties properties = new Properties();
        InputStream is = new FileInputStream(fileName);
        try {
            properties.load(is);
        } finally {
            is.close();
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            if (System.getProperty(key) != null) {
                // allow system properties to override properties file
                continue;
            }
            System.setProperty(key, (String) entry.getValue());
        }
    }

    private static String getValueOrPropertyOrEnvironmentVariable(
            String value, String propertyName, String environmentName) {
        if (value == null) {
            value = System.getProperty(propertyName);
        }
        if (value == null) {
            value = System.getenv(environmentName);
        }
        return value;
    }
    
   private static String getCredentialValue(String provider, String credential) {
      return credential != null && isGoogleCloud(provider) ? getGoogleCredentialFromJsonFileIfPath(credential) : credential;
   }
   
   private static boolean isGoogleCloud(String provider) {
      return provider != null && provider.startsWith("google");
   }
}
