package com.azed.home_buddy.repository;

import com.azed.home_buddy.model.TreatmentMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TreatmentMedicationRepository extends JpaRepository<TreatmentMedication, Long> {

    @Query("SELECT tm FROM TreatmentMedication tm WHERE tm.treatment.treatmentId = ?1 AND tm.medication.medicationId = ?2")
    TreatmentMedication findTreatmentMedicationByMedicationIdAndTreatmentId(Long treatmentId, Long medicationId);

    @Modifying
    @Query("DELETE FROM TreatmentMedication tm WHERE tm.treatment.treatmentId = ?1 AND tm.medication.medicationId = ?2")
    void deleteTreatmentMedicationByMedicationIdAndTreatmentId(Long treatmentId, Long medicationId);
}
