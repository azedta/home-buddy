package com.azed.home_buddy.service;

import com.azed.home_buddy.exception.APIException;
import com.azed.home_buddy.exception.ResourceNotFoundException;
import com.azed.home_buddy.model.Medication;
import com.azed.home_buddy.model.User;
import com.azed.home_buddy.payload.MedicationDTO;
import com.azed.home_buddy.payload.MedicationResponse;
import com.azed.home_buddy.repository.MedicationRepository;
import com.azed.home_buddy.repository.UserRepository;
import com.azed.home_buddy.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;


    @Override
    public MedicationDTO addMedication(MedicationDTO medicationDTO) {

        User currentUser = authUtil.loggedInUser();

        // Check if medication already present or not
        boolean isMedicationNotPresent = true;

        List<Medication> medications = currentUser.getMedications();
        for (Medication value : medications) {
            if (value.getMedicationName()
                    .equals(medicationDTO.getMedicationName())) {
                isMedicationNotPresent = false;
                break;
            }
        }

        if (isMedicationNotPresent) {
            Medication medication = modelMapper.map(medicationDTO, Medication.class);
            medication.setUser(currentUser);
            Medication savedMedication = medicationRepository.save(medication);
            return modelMapper.map(savedMedication, MedicationDTO.class);
        } else {
            throw new APIException("Medication already exists");
        }


    }

    @Override
    public MedicationResponse getAllMedications(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Specification<Medication> spec = Specification.where(null);
        if(keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("medicationName")), "%" + keyword.toLowerCase() + "%"));
        }

        Page<Medication> pageMedications = medicationRepository.findAll(spec, pageDetails);

        List<Medication> medications = pageMedications.getContent();

        List<MedicationDTO> medicationDTOS = medications.stream()
                .map(medication -> modelMapper.map(medication, MedicationDTO.class))
                .toList();

        // Check if  Medications size is 0
        if(medications.isEmpty()){
            throw new APIException("No medications found");
        }

        MedicationResponse medicationResponse = new MedicationResponse();
        medicationResponse.setContent(medicationDTOS);
        medicationResponse.setPageNumber(pageMedications.getNumber());
        medicationResponse.setPageSize(pageMedications.getSize());
        medicationResponse.setTotalElements(pageMedications.getTotalElements());
        medicationResponse.setTotalPages(pageMedications.getTotalPages());
        medicationResponse.setLastPage(pageMedications.isLast());

        return medicationResponse;
    }

    @Override
    public MedicationResponse searchByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Medication> pageMedications = medicationRepository.findByUser(user,pageDetails);

        List<Medication> medications = pageMedications.getContent();

        List<MedicationDTO> medicationDTOS = medications.stream()
                .map(medication -> modelMapper.map(medication, MedicationDTO.class))
                .toList();

        // Check if  Medications size is 0
        if(medications.isEmpty()){
            throw new APIException(user.getUserName() + " user does not have any medications");
        }

        MedicationResponse medicationResponse = new MedicationResponse();
        medicationResponse.setContent(medicationDTOS);
        medicationResponse.setPageNumber(pageMedications.getNumber());
        medicationResponse.setPageSize(pageMedications.getSize());
        medicationResponse.setTotalElements(pageMedications.getTotalElements());
        medicationResponse.setTotalPages(pageMedications.getTotalPages());
        medicationResponse.setLastPage(pageMedications.isLast());
        return medicationResponse;
    }

    @Override
    public MedicationResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Medication> pageMedications = medicationRepository.findByMedicationNameLikeIgnoreCase('%' + keyword + '%',pageDetails);

        List<Medication> medications = pageMedications.getContent();

        List<MedicationDTO> medicationDTOS = medications.stream()
                .map(medication -> modelMapper.map(medication, MedicationDTO.class))
                .toList();

        // Check if  Medications size is 0
        if(medications.isEmpty()){
            throw new APIException("Medications not found with keyword: " + keyword);
        }

        MedicationResponse medicationResponse = new MedicationResponse();
        medicationResponse.setContent(medicationDTOS);
        medicationResponse.setPageNumber(pageMedications.getNumber());
        medicationResponse.setPageSize(pageMedications.getSize());
        medicationResponse.setTotalElements(pageMedications.getTotalElements());
        medicationResponse.setTotalPages(pageMedications.getTotalPages());
        medicationResponse.setLastPage(pageMedications.isLast());
        return medicationResponse;
    }

    @Override
    public MedicationDTO updateMedication(Long medicationId, MedicationDTO medicationDTO) {
        User currentUser = authUtil.loggedInUser();

        // Get the existing medication from DB
        Medication medicationFromDB = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication", "medicationId", medicationId));

        Medication medication = modelMapper.map(medicationDTO, Medication.class);

        if(!Objects.equals(medication.getUser(), currentUser)){
            throw new APIException("User does not have permission to update medication");
        }

        // Update the medication info with the one in request body
        medicationFromDB.setMedicationName(medication.getMedicationName());
        medicationFromDB.setMedicationForm(medication.getMedicationForm());
        medicationFromDB.setMedicationStrength(medication.getMedicationStrength());
        medicationFromDB.setMedicationDescription(medication.getMedicationDescription());
        medicationFromDB.setQuantity(medication.getQuantity());
        // Save to database
        Medication savedMedication = medicationRepository.save(medicationFromDB);
        return modelMapper.map(savedMedication, MedicationDTO.class);
    }

    @Override
    public MedicationDTO deleteMedication(Long medicationId) {
        Medication medication = medicationRepository.findById(medicationId).
                orElseThrow(() -> new ResourceNotFoundException("Medication", "medicationId", medicationId));
        medicationRepository.delete(medication);
        return modelMapper.map(medication, MedicationDTO.class);
    }


}
