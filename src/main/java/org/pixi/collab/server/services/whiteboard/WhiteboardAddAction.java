package org.pixi.collab.server.services.whiteboard;

import lombok.Data;
import org.pixi.collab.server.services.atmosphere.message.ActionMessage;

@Data
public class WhiteboardAddAction extends WhiteboardAction {
    ActionMessage object;
}
