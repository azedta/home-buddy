package com.azed.home_buddy.controller;


import com.azed.home_buddy.config.AppConstants;
import com.azed.home_buddy.payload.MedicationDTO;
import com.azed.home_buddy.payload.MedicationResponse;
import com.azed.home_buddy.service.MedicationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<MedicationDTO> addMedication(@Valid @RequestBody MedicationDTO medicationDTO,
                                                       @PathVariable Long userId) {
        MedicationDTO savedmedicationDTO = medicationService.addMedication(userId, medicationDTO);
        return new ResponseEntity<>(savedmedicationDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/medications")
    public ResponseEntity<MedicationResponse> getAllMedications(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_MEDICATIONS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
       MedicationResponse medicationResponse = medicationService.getAllMedications(pageNumber, pageSize, sortBy, sortOrder);
       return new ResponseEntity<>(medicationResponse, HttpStatus.OK);
    }

    @GetMapping("/public/users/{userId}/medications")
    public ResponseEntity<MedicationResponse> getMedicationsByUser(@PathVariable Long userId,
                                                                   @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                   @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                   @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_MEDICATIONS_BY, required = false) String sortBy,
                                                                   @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
         MedicationResponse medicationResponse = medicationService.searchByUser(userId, pageNumber, pageSize, sortBy, sortOrder);
         return new ResponseEntity<>(medicationResponse, HttpStatus.OK);
    }

    @GetMapping("/public/medications/keyword/{keyword}")
    public ResponseEntity<MedicationResponse> getMedicationsByKeyword(@PathVariable String keyword,
                                                                      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                      @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_MEDICATIONS_BY, required = false) String sortBy,
                                                                      @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        MedicationResponse medicationResponse = medicationService.searchByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(medicationResponse, HttpStatus.OK);
    }

    @PutMapping("/public/medications/{medicationId}")
    public ResponseEntity<MedicationDTO> updateMedication(@Valid @RequestBody MedicationDTO medicationDTO,
                                                          @PathVariable Long medicationId) {
        MedicationDTO updatedMedicationDTO = medicationService.updateMedication(medicationId, medicationDTO);
        return new ResponseEntity<>(updatedMedicationDTO, HttpStatus.OK);
    }

    @DeleteMapping("/public/medications/{medicationId}")
    public ResponseEntity<MedicationDTO> deleteMedication(@PathVariable Long medicationId) {
        MedicationDTO deletedMedication = medicationService.deleteMedication(medicationId);
        return new ResponseEntity<>(deletedMedication, HttpStatus.OK);
    }


}
