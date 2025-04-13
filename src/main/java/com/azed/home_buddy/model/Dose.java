package com.azed.home_buddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "doses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doseId;

    @NotBlank
    @Size(min = 3, message = "Medication Name must be at least 5 characters")
    private String medicationName;

    @NotBlank
    @Size(min = 3, message = "Time Frequency must be at least 5 characters")
    private String timeFrequency;

    @NotBlank
    @Size(min = 3, message = "Days must be at least 3 characters")
    private String days;

    @NotBlank
    private String time;

    @NotNull
    private Integer period;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Dose(String timeFrequency, String days, String time, Integer period) {
        this.timeFrequency = timeFrequency;
        this.days = days;
        this.time = time;
        this.period = period;
    }
}
