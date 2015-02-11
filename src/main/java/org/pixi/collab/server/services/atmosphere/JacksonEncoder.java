package org.pixi.collab.server.services.atmosphere;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.managed.Encoder;
import org.pixi.collab.server.services.atmosphere.message.CollabMessage;

public class JacksonEncoder implements Encoder<CollabMessage, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(CollabMessage m) {
        try {
            String json = mapper.writeValueAsString(m);
            System.err.println(m + " => "  +json);
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
