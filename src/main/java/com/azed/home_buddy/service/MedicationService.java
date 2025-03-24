package com.azed.home_buddy.service;

import com.azed.home_buddy.model.Medication;
import com.azed.home_buddy.payload.MedicationDTO;
import com.azed.home_buddy.payload.MedicationResponse;
import org.springframework.stereotype.Service;

@Service
public interface MedicationService {
    MedicationDTO addMedication(Long userId, Medication medication);

    MedicationResponse getAllProducts();

    MedicationResponse searchByUser(Long userId);
}
