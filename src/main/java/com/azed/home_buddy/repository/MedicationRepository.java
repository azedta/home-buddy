package com.azed.home_buddy.repository;

import com.azed.home_buddy.model.Medication;
import com.azed.home_buddy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication>  {
    Page<Medication> findByUser(User user, Pageable pageDetails);

    Page<Medication> findByMedicationNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
