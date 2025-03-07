package repository;

import entity.CaseStatusData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseStatusRepository extends JpaRepository<CaseStatusData, Long> {
}
