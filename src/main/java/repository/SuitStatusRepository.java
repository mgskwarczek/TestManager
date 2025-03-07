package repository;

import entity.SuitStatusData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuitStatusRepository extends JpaRepository<SuitStatusData, Long> {

	SuitStatusData findByName(String statusName);
}
