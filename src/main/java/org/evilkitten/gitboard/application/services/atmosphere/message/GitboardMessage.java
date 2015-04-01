package org.evilkitten.gitboard.application.services.atmosphere.message;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@Data
abstract public class GitboardMessage {
    private final Calendar timestamp = GregorianCalendar.getInstance();
    private Integer boardId;
    private String message;

    abstract public String getType();
}
