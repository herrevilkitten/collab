package org.evilkitten.collab.application.services.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class OAuthSignin implements Serializable {
    private String token;
    private String state;
}