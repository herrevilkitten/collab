package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.evilkitten.gitboard.application.entity.User;

@Data
@EqualsAndHashCode(callSuper = true)
abstract public class ActionMessage extends GitboardMessage {
    private User actor;
}
