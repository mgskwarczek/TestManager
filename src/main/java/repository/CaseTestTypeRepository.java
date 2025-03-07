package repository;

import entity.CaseTestTypeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseTestTypeRepository extends JpaRepository<CaseTestTypeData, Long> {
}
