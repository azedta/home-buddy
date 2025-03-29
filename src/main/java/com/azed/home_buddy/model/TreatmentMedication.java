package com.azed.home_buddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "treatment_medications")
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treatmentMedicationId;

    @ManyToOne
    @JoinColumn(name="treatment_id")
    private Treatment treatment;

    @ManyToOne
    @JoinColumn(name="medication_id")
    private Medication medication;

}
