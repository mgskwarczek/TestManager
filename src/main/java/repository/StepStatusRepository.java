package repository;

import entity.StepStatusData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepStatusRepository extends JpaRepository<StepStatusData, Long> {
}
