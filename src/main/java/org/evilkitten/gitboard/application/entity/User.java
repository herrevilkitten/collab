package org.evilkitten.gitboard.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {
    private Integer id;

    @JsonIgnore
    private String email;

    private String displayName;

    @JsonIgnore
    private ProviderType providerType;

    @JsonIgnore
    private String providerId;
}
