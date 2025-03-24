package com.azed.home_buddy.repository;

import com.azed.home_buddy.model.Medication;
import com.azed.home_buddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByUser(User user);
}
