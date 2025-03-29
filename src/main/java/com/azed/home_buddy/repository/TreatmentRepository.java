package com.azed.home_buddy.repository;

import com.azed.home_buddy.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query("SELECT t FROM Treatment t WHERE t.user.email = ?1")
    Treatment findTreatmentByEmail(String email);

    @Query("SELECT t FROM Treatment t WHERE t.user.email = ?1 AND t.treatmentId = ?2")
    Treatment findTreatmentByEmailAndTreatmentId(String emailId, Long treatmentId);

}
