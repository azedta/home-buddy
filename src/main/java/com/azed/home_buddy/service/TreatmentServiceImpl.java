package com.azed.home_buddy.service;

import com.azed.home_buddy.exception.APIException;
import com.azed.home_buddy.exception.ResourceNotFoundException;
import com.azed.home_buddy.model.Medication;
import com.azed.home_buddy.model.Treatment;
import com.azed.home_buddy.model.TreatmentMedication;
import com.azed.home_buddy.payload.MedicationDTO;
import com.azed.home_buddy.payload.TreatmentDTO;
import com.azed.home_buddy.repository.MedicationRepository;
import com.azed.home_buddy.repository.TreatmentMedicationRepository;
import com.azed.home_buddy.repository.TreatmentRepository;
import com.azed.home_buddy.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private TreatmentMedicationRepository treatmentMedicationRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public TreatmentDTO addMedicationToTreatment(Long medicationId) {
        // Find existing one or Create Treatment
        Treatment treatment = createTreatment();
        // Retrieve Medication Details
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication", "id", medicationId));

        TreatmentMedication treatmentMedication = treatmentMedicationRepository.findTreatmentMedicationByMedicationIdAndTreatmentId(treatment.getTreatmentId(), medicationId);

        // Perform Validations
        if (treatmentMedication != null) {
            throw new APIException("Medication " + medication.getMedicationName() + " already exists in the cart");
        }
        // Save Treatment Medication
        TreatmentMedication savedTreatmentMedication = new TreatmentMedication();

        savedTreatmentMedication.setMedication(medication);
        savedTreatmentMedication.setTreatment(treatment);

        treatmentMedicationRepository.save(savedTreatmentMedication);

        treatment.setTotalMedications(treatment.getTotalMedications() + 1);

        treatmentRepository.save(treatment);
        // Return Updated Treatment
        TreatmentDTO treatmentDTO = modelMapper.map(treatment, TreatmentDTO.class);
        List<TreatmentMedication> treatmentMedications = treatment.getTreatmentMedications();

        Stream<MedicationDTO> medicationStream = treatmentMedications.stream().map(med -> {
            MedicationDTO map = modelMapper.map(med.getMedication(), MedicationDTO.class);
            return map;
        });

        treatmentDTO.setMedications(medicationStream.toList());

        return treatmentDTO;
    }

    @Override
    public List<TreatmentDTO> getAllTreatments() {
        List<Treatment> treatments = treatmentRepository.findAll();

        if(treatments.isEmpty()) {
            throw new APIException("No Treatments found");
        }

        List<TreatmentDTO> treatmentDTOs = treatments.stream().map(treatment -> {
            TreatmentDTO treatmentDTO = modelMapper.map(treatment, TreatmentDTO.class);

            List<MedicationDTO> medications = treatment.getTreatmentMedications().stream().map(treatmentMedication -> {
                MedicationDTO medicationDTO = modelMapper.map(treatmentMedication.getMedication(), MedicationDTO.class);
                return medicationDTO;
            }).collect(Collectors.toList());


            treatmentDTO.setMedications(medications);

            return treatmentDTO;

        }).collect(Collectors.toList());

        return treatmentDTOs;
    }

    @Override
    public TreatmentDTO getTreatment(String emailId, Long treatmentId) {
        Treatment  treatment = treatmentRepository.findTreatmentByEmailAndTreatmentId(emailId, treatmentId);
        if(treatment == null) {
            throw new ResourceNotFoundException("Treatment", "id", treatmentId);
        }
        TreatmentDTO treatmentDTO = modelMapper.map(treatment, TreatmentDTO.class);
        List<MedicationDTO> medications = treatment.getTreatmentMedications().stream()
                .map(p -> modelMapper.map(p.getMedication(), MedicationDTO.class))
                .toList();
        treatmentDTO.setMedications(medications);
        return treatmentDTO;
    }

    @Override
    public String deleteMedicationFromTreatment(Long treatmentId, Long medicationId) {
        Treatment treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment", "id", treatmentId));
        TreatmentMedication treatmentMedication = treatmentMedicationRepository.findTreatmentMedicationByMedicationIdAndTreatmentId(treatmentId, medicationId);
        if (treatmentMedication == null) {
            throw new ResourceNotFoundException("Medication", "id", medicationId);
        }

        treatment.setTotalMedications(treatment.getTotalMedications() - 1);
        treatmentMedicationRepository.deleteTreatmentMedicationByMedicationIdAndTreatmentId(treatmentId, medicationId);

        return "Medication " + treatmentMedication.getMedication().getMedicationId() + " removed from the treatment !";
    }

    private Treatment createTreatment() {

        Treatment userTreatment = treatmentRepository.findTreatmentByEmail(authUtil.loggedInEmail());
        if (userTreatment != null) {
            return userTreatment;
        }

        Treatment treatment = new Treatment();
        treatment.setUser(authUtil.loggedInUser());
        Treatment savedTreatment = treatmentRepository.save(treatment);

        return savedTreatment;
    }
}
