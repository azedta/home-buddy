package com.azed.home_buddy.repository;

import com.azed.home_buddy.model.Dose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoseRepository extends JpaRepository<Dose, Long> {
}
