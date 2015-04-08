package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.Calendar;
import java.util.GregorianCalendar;

import lombok.Data;
import org.evilkitten.gitboard.application.entity.User;

@Data
public class WhiteboardAction {
    private final Calendar timestamp = GregorianCalendar.getInstance();
    private String id;
    private User actor;
    private String type;
}