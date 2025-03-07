package repository;

import entity.CaseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<CaseData, Long> {

	@Query("SELECT c FROM CaseData c WHERE c.id = :id AND c.deletionDate IS NULL")
	CaseData findExistingById(@Param("id") Long id);

	@Query(
			"SELECT c FROM CaseData c WHERE " +
			"(c.suit.id = :suitId) AND " +
			"(c.code LIKE %:code% OR :code IS NULL) AND " +
			"(c.title LIKE %:title% OR :title IS NULL) AND " +
			"(c.statusId = :statusId OR :statusId IS NULL) AND " +
			"(c.user.firstName LIKE %:firstName% OR :firstName IS NULL) AND " +
			"(c.user.lastName LIKE %:lastName% OR :lastName IS NULL) AND " +
			"c.deletionDate IS NULL"
	)
	List<CaseData> findAllBySuitIdAndCodeAndTitleAndStatusIdAndUser(@Param("suitId") Long suitId, @Param("code") String code, @Param("title") String title, @Param("statusId") Long statusId, @Param("firstName") String firstName, @Param("lastName") String lastName);
}
