package repository;

import entity.CasePriorityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasePriorityRepository extends JpaRepository<CasePriorityData, Long> {
}
