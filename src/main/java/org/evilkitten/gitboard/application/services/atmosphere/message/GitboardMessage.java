package org.evilkitten.gitboard.application.services.atmosphere.message;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.Calendar;
import java.util.GregorianCalendar;

import lombok.Data;

@XmlRootElement
@Data
public class GitboardMessage {
    private final Calendar timestamp = GregorianCalendar.getInstance();
    private String author;
    private String message;
    private String type = "";
}
