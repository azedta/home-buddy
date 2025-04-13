package com.azed.home_buddy.service;

import com.azed.home_buddy.payload.MedicationDTO;
import com.azed.home_buddy.payload.MedicationResponse;
import org.springframework.stereotype.Service;

@Service
public interface MedicationService {
    MedicationDTO addMedication(MedicationDTO medication);

    MedicationResponse getAllMedications(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword);

    MedicationResponse searchByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    MedicationResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    MedicationDTO updateMedication(Long medicationId, MedicationDTO medication);

    MedicationDTO deleteMedication(Long medicationId);
}
