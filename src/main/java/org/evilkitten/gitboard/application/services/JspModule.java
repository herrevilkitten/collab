package org.evilkitten.gitboard.application.services;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.RequestScoped;

public class JspModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JspWeb.class).in(RequestScoped.class);
    }
}
