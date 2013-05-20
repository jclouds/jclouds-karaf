package org.jclouds.karaf.core;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.jclouds.domain.Credentials;

import javax.inject.Singleton;
import java.util.Map;

public class CredentialStore extends AbstractModule {

    protected Map<String, Credentials> store;

    /**
     * Configures a {@link com.google.inject.Binder} via the exposed methods.
     */
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public Map<String, Credentials> getStore() {
        return store;
    }

    public void setStore(Map<String, Credentials> store) {
        this.store = store;
    }
}
