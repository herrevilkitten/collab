package org.evilkitten.gitboard.application.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String displayName;
    private ProviderType providerType;
    private String providerId;
}