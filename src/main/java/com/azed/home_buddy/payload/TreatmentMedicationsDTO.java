package com.azed.home_buddy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentMedicationsDTO {

    private Long treatmentMedicationId;
    private TreatmentDTO treatment;
    private MedicationDTO medicationDTO;

}
