package com.azed.home_buddy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentDTO {

    private Long treatmentId;
    private Integer totalMedications = 0;
    private List<MedicationDTO> medications = new ArrayList<>();
}
