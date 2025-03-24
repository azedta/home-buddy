package com.azed.home_buddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;


@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long medicationId;

    @NotBlank
    @Size(min = 5, message = "Medication name must be at least 5 characters")
    private String medicationName;

    @NotBlank
    @Size(min = 3, message = "Medication form must be at least 3 characters")
    private String medicationForm;

    @NotBlank
    private Integer medicationStrength;

    private String medicationDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Medication(String medicationName, String medicationForm, Integer medicationStrength, String medicationDescription) {
        this.medicationName = medicationName;
        this.medicationForm = medicationForm;
        this.medicationStrength = medicationStrength;
        this.medicationDescription = medicationDescription;
    }
}
