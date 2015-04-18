package org.evilkitten.gitboard.application.services.atmosphere.message;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@XmlRootElement
@Data
abstract public class GitboardMessage {
    private final Calendar timestamp = GregorianCalendar.getInstance();
    private Integer boardId;

    @JsonIgnore
    private String type;

    @JsonProperty("type")
    public String getType() {
        return getClass().getSimpleName().replace("Message", "").toLowerCase();
    }
}
