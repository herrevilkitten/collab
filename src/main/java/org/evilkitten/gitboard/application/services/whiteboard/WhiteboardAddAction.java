package org.evilkitten.gitboard.application.services.whiteboard;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.evilkitten.gitboard.application.services.atmosphere.message.ActionMessage;

@Data
@EqualsAndHashCode(callSuper = true)
public class WhiteboardAddAction extends WhiteboardAction {
    ActionMessage object;
}
