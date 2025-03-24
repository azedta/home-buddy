package com.azed.home_buddy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDTO {

    private Long medicationId;
    private String medicationName;
    private String medicationForm;
    private String medicationStrength;
    private String medicationDescription;


}
