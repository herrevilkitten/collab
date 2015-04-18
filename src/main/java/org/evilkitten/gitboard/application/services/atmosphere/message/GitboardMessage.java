package org.evilkitten.gitboard.application.services.atmosphere.message;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@XmlRootElement
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract public class GitboardMessage {
    private final Calendar timestamp = GregorianCalendar.getInstance();
    private Integer boardId;
}
