package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doctor name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Specialty is required")
    @Size(min = 3, max = 50, message = "Specialty must be between 3 and 50 characters")
    private String specialty;

    @NotNull(message = "Email is required")
    @Email(message = "Please enter a valid email")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @ElementCollection
    private List<String> availableTimes;

}


