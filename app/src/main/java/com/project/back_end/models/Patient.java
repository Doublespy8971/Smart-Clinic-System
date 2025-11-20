package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotNull(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

}
