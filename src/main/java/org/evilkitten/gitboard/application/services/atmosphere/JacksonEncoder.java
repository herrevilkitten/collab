package org.evilkitten.gitboard.application.services.atmosphere;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.managed.Encoder;
import org.evilkitten.gitboard.application.services.atmosphere.message.GitboardMessage;

public class JacksonEncoder implements Encoder<GitboardMessage, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(GitboardMessage m) {
        try {
            String json = mapper.writeValueAsString(m);
            System.err.println(m + " => "  +json);
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
