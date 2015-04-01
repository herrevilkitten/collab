package org.evilkitten.gitboard.application.services.atmosphere;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.managed.Decoder;
import org.evilkitten.gitboard.application.services.atmosphere.message.ActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.GitboardMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.EllipseActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.HeartbeatMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.LineActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.PathActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.QueryMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.RectangleActionMessage;
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
                if (type.startsWith("action.")) {
                    LOG.info("Action type is {}", type.substring(7));
                    messageClass = decodeActionMessage(type.substring(7));
                } else if (type.equalsIgnoreCase("heartbeat")) {
                    messageClass = HeartbeatMessage.class;
                } else if (type.equalsIgnoreCase("query")) {
                    messageClass = QueryMessage.class;
                }
            }
            return mapper.readValue(s, messageClass);
        } catch (IOException e) {
            LOG.error(e.toString(), e);
            throw new RuntimeException(e);
        }
    }

    private Class<? extends GitboardMessage> decodeActionMessage(String actionType) {
        Class<? extends GitboardMessage> messageClass = ActionMessage.class;

        switch (actionType.toLowerCase()) {
            case "path":
                messageClass = PathActionMessage.class;
                break;
            case "rect":
                messageClass = RectangleActionMessage.class;
                break;
            case "ellipse":
                messageClass = EllipseActionMessage.class;
                break;
            case "line":
                messageClass = LineActionMessage.class;
                break;
        }

        return messageClass;
    }
}
