package org.evilkitten.gitboard.application.services.json;

public interface JsonDecoder {
    Object fromJson(String json, Class<?> baseClass);
}
