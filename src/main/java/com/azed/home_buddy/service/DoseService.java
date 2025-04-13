package com.azed.home_buddy.service;

import com.azed.home_buddy.model.User;
import com.azed.home_buddy.payload.DoseDTO;

import java.util.List;

public interface DoseService {

    DoseDTO createDose(DoseDTO doseDTO, User user);

    List<DoseDTO> getDoses();

    DoseDTO getDosesById(Long doseId);

    List<DoseDTO> getUserDoses(User user);

    DoseDTO updateDose(Long doseId, DoseDTO doseDTO);

    String deleteDose(Long doseId);
}
