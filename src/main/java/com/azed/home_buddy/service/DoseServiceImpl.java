package com.azed.home_buddy.service;

import com.azed.home_buddy.exception.ResourceNotFoundException;
import com.azed.home_buddy.model.Dose;
import com.azed.home_buddy.model.User;
import com.azed.home_buddy.payload.DoseDTO;
import com.azed.home_buddy.repository.DoseRepository;
import com.azed.home_buddy.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoseServiceImpl implements DoseService {

    @Autowired
    private DoseRepository doseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public DoseDTO createDose(DoseDTO doseDTO, User user) {
        Dose dose = modelMapper.map(doseDTO, Dose.class);
        dose.setUser(user);
        List<Dose> dosesList = user.getDoses();
        dosesList.add(dose);
        user.setDoses(dosesList);
        Dose savedDose = doseRepository.save(dose);
        return modelMapper.map(savedDose, DoseDTO.class);
    }

    @Override
    public List<DoseDTO> getDoses() {
        List<Dose> doses = doseRepository.findAll();
        return doses.stream()
                .map(dose -> modelMapper.map(dose, DoseDTO.class))
                .toList();
    }

    @Override
    public DoseDTO getDosesById(Long doseId) {
        Dose dose = doseRepository.findById(doseId)
                .orElseThrow(() -> new ResourceNotFoundException("Dose", "doseId", doseId));
        return modelMapper.map(dose, DoseDTO.class);
    }

    @Override
    public List<DoseDTO> getUserDoses(User user) {
        List<Dose> doses = user.getDoses();
        return doses.stream()
                .map(dose -> modelMapper.map(dose, DoseDTO.class))
                .toList();
    }

    @Override
    public DoseDTO updateDose(Long doseId, DoseDTO doseDTO) {
        Dose doseFromDB = doseRepository.findById(doseId)
                .orElseThrow(() -> new ResourceNotFoundException("Dose", "doseId", doseId));

        doseFromDB.setMedicationName(doseDTO.getMedicationName());
        doseFromDB.setTimeFrequency(doseDTO.getTimeFrequency());
        doseFromDB.setDays(doseDTO.getDays());
        doseFromDB.setTime(doseDTO.getTime());
        doseFromDB.setPeriod(doseDTO.getPeriod());

        Dose updatedDose = doseRepository.save(doseFromDB);

        User user = doseFromDB.getUser();
        user.getDoses().removeIf(dose -> dose.getDoseId().equals(doseId));
        user.getDoses().add(updatedDose);
        userRepository.save(user);

        return modelMapper.map(updatedDose, DoseDTO.class);
    }

    @Override
    public String deleteDose(Long doseId) {
        Dose doseFromDB = doseRepository.findById(doseId)
                .orElseThrow(() -> new ResourceNotFoundException("Dose", "doseId", doseId));

        User user = doseFromDB.getUser();
        user.getDoses().removeIf(dose -> dose.getDoseId().equals(doseId));
        userRepository.save(user);

        doseRepository.delete(doseFromDB);

        return "Dose deleted successfully with doseId: " + doseId;
    }
}
