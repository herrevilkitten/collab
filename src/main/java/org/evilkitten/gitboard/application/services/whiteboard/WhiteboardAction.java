package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.Calendar;
import java.util.GregorianCalendar;

import lombok.Data;

@Data
public class WhiteboardAction {
    private final Calendar timestamp = GregorianCalendar.getInstance();
    private String actor;
    private String id;
    private String type;
}