package org.evilkitten.gitboard.application.services.atmosphere;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.managed.Decoder;
import org.evilkitten.gitboard.application.services.atmosphere.message.ActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.GitboardMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonDecoder implements Decoder<String, GitboardMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(JacksonDecoder.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public GitboardMessage decode(String s) {
        try {
            System.err.println("Received " + s);

            Class<? extends GitboardMessage> messageClass = GitboardMessage.class;
            JsonNode root = mapper.readTree(s);
            if (root.has("type")) {
                String type = root.get("type").textValue();
                LOG.info("Message type is {}", type);

                messageClass = (Class<? extends GitboardMessage>) Class.forName(type);
            }
            return mapper.readValue(s, messageClass);
        } catch (IOException e) {
            LOG.error(e.toString(), e);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            LOG.error(e.toString(), e);
            throw new RuntimeException(e);
        }
    }

    private Class<? extends GitboardMessage> decodeActionMessage(String actionType) {
        Class<? extends GitboardMessage> messageClass = ActionMessage.class;
/*
        switch (actionType.toLowerCase()) {
            case "path":
                messageClass = PathActionMessageAdd.class;
                break;
            case "rect":
                messageClass = RectangleActionMessageAdd.class;
                break;
            case "ellipse":
                messageClass = EllipseActionMessageAdd.class;
                break;
            case "line":
                messageClass = LineActionMessageAdd.class;
                break;
        }
*/
        return messageClass;
    }
}
