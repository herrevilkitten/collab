package org.evilkitten.gitboard.application.services.json;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class JsonModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonTranscoder.class).to(JacksonJsonTranscoder.class).in(Singleton.class);
    }
}
