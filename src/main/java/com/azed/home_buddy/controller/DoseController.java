package com.azed.home_buddy.controller;

import com.azed.home_buddy.model.User;
import com.azed.home_buddy.payload.DoseDTO;
import com.azed.home_buddy.service.DoseService;
import com.azed.home_buddy.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class DoseController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    DoseService doseService;

    @PostMapping("/public/doses")
    public ResponseEntity<DoseDTO> createDose(@Valid @RequestBody DoseDTO doseDTO){
        User user = authUtil.loggedInUser();
        DoseDTO savedDoseDTO = doseService.createDose(doseDTO, user);
        return new ResponseEntity<>(savedDoseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/doses")
    public ResponseEntity<List<DoseDTO>> getDoses(){
        List<DoseDTO> doseList = doseService.getDoses();
        return new ResponseEntity<>(doseList, HttpStatus.OK);
    }

    @GetMapping("/public/doses/{doseId}")
    public ResponseEntity<DoseDTO> getDoseById(@PathVariable Long doseId){
        DoseDTO doseDTO = doseService.getDosesById(doseId);
        return new ResponseEntity<>(doseDTO, HttpStatus.OK);
    }


    @GetMapping("/public/users/doses")
    public ResponseEntity<List<DoseDTO>> getUserDoses(){
        User user = authUtil.loggedInUser();
        List<DoseDTO> doseList = doseService.getUserDoses(user);
        return new ResponseEntity<>(doseList, HttpStatus.OK);
    }

    @PutMapping("/public/doses/{doseId}")
    public ResponseEntity<DoseDTO> updateDose(@PathVariable Long doseId
            , @RequestBody DoseDTO doseDTO){
        DoseDTO updatedDose = doseService.updateDose(doseId, doseDTO);
        return new ResponseEntity<>(updatedDose, HttpStatus.OK);
    }

    @DeleteMapping("/doses/{doseId}")
    public ResponseEntity<String> deleteDose(@PathVariable Long doseId){
        String status = doseService.deleteDose(doseId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
