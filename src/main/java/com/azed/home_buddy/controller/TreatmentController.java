package com.azed.home_buddy.controller;

import com.azed.home_buddy.model.Treatment;
import com.azed.home_buddy.payload.TreatmentDTO;
import com.azed.home_buddy.repository.TreatmentRepository;
import com.azed.home_buddy.service.TreatmentService;
import com.azed.home_buddy.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TreatmentController {

    @Autowired
    private TreatmentService treatmentService;
    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("treatments/medications/{medicationId}")
    public ResponseEntity<TreatmentDTO> addMedicationToTreatment(@PathVariable Long medicationId) {
        TreatmentDTO treatmentDTO = treatmentService.addMedicationToTreatment(medicationId);
        return new ResponseEntity<TreatmentDTO>(treatmentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/treatments")
    public ResponseEntity<List<TreatmentDTO>> getTreatments() {
        List<TreatmentDTO> treatmentDTOS = treatmentService.getAllTreatments();
        return new ResponseEntity<List<TreatmentDTO>>(treatmentDTOS, HttpStatus.FOUND);
    }

    @GetMapping("/treatments/users/treatment")
    public ResponseEntity<TreatmentDTO> getTreatmentByUser() {
        String emailId = authUtil.loggedInEmail();
        Treatment treatment = treatmentRepository.findTreatmentByEmail(emailId);
        Long treatmentId = treatment.getTreatmentId();
        TreatmentDTO treatmentDTO = treatmentService.getTreatment(emailId, treatmentId);
        return new ResponseEntity<TreatmentDTO>(treatmentDTO, HttpStatus.OK);
    }

    @GetMapping("/treatments/{treatmentId}/medication/{medicationId}")
    public ResponseEntity<String> deleteMedicationFromTreatment( @PathVariable Long treatmentId,
                                                                 @PathVariable Long medicationId) {
        String status = treatmentService.deleteMedicationFromTreatment(treatmentId, medicationId);
        return new ResponseEntity<String>(status, HttpStatus.OK);

    }



}
