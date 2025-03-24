package com.azed.home_buddy.service;

import com.azed.home_buddy.exception.ResourceNotFoundException;
import com.azed.home_buddy.model.Medication;
import com.azed.home_buddy.model.User;
import com.azed.home_buddy.payload.MedicationDTO;
import com.azed.home_buddy.payload.MedicationResponse;
import com.azed.home_buddy.repository.MedicationRepository;
import com.azed.home_buddy.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MedicationDTO addMedication(Long userId, Medication medication) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        medication.setUser(user);
        Medication savedMedication = medicationRepository.save(medication);
        return modelMapper.map(savedMedication, MedicationDTO.class);
    }

    @Override
    public MedicationResponse getAllProducts() {
        List<Medication> medications = medicationRepository.findAll();
        List<MedicationDTO> medicationDTOS = medications.stream()
                .map(medication -> modelMapper.map(medication, MedicationDTO.class))
                .toList();
        MedicationResponse medicationResponse = new MedicationResponse();
        medicationResponse.setContent(medicationDTOS);
        return medicationResponse;
    }

    @Override
    public MedicationResponse searchByUser(Long userId) {

        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        List<Medication> medications = medicationRepository.findByUser(user);
        List<MedicationDTO> medicationDTOS = medications.stream()
                .map(medication -> modelMapper.map(medication, MedicationDTO.class))
                .toList();
        MedicationResponse medicationResponse = new MedicationResponse();
        medicationResponse.setContent(medicationDTOS);
        return medicationResponse;
    }


}
