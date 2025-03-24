package com.azed.home_buddy.controller;


import com.azed.home_buddy.model.Medication;
import com.azed.home_buddy.payload.MedicationDTO;
import com.azed.home_buddy.payload.MedicationResponse;
import com.azed.home_buddy.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MedicationController {

    @Autowired
    MedicationService medicationService;

    @PostMapping("/public/users/{userId}/medication")
    public ResponseEntity<MedicationDTO> addMedication(@RequestBody Medication medication,
                                                       @PathVariable Long userId) {
        MedicationDTO medicationDTO = medicationService.addMedication(userId, medication);
        return new ResponseEntity<>(medicationDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/medications")
    public ResponseEntity<MedicationResponse> getAllMedications() {
       MedicationResponse medicationResponse = medicationService.getAllProducts();
       return new ResponseEntity<>(medicationResponse, HttpStatus.OK);
    }

    @GetMapping("/public/users/{userId}/medications")
    public ResponseEntity<MedicationResponse> getMedicationsByUser(@PathVariable Long userId) {
         MedicationResponse medicationResponse = medicationService.searchByUser(userId);
         return new ResponseEntity<>(medicationResponse, HttpStatus.OK);
    }


}
