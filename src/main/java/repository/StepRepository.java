package repository;

import entity.StepData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<StepData, Long> {

	@Query("SELECT s FROM StepData s WHERE s.id = :id AND s.deletionDate IS NULL")
	StepData findExistingById(@Param("id") Long id);
}