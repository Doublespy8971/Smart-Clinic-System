package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be empty")
    @Column(unique = true)
    private String username;

    @NotNull(message = "Enter the Password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
