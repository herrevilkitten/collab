package org.evilkitten.gitboard.application.services.whiteboard.shape;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract public class BaseShape {
    private int id;
    private String stroke;
    private Date creationTime;
    private int layer;
    private String layerName;
}
