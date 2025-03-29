package com.azed.home_buddy.service;

import com.azed.home_buddy.payload.TreatmentDTO;


import java.util.List;


public interface TreatmentService {
    TreatmentDTO addMedicationToTreatment(Long medicationId);

    List<TreatmentDTO> getAllTreatments();

    TreatmentDTO getTreatment(String emailId, Long treatmentId);

    String deleteMedicationFromTreatment(Long treatmentId, Long medicationId);
}
