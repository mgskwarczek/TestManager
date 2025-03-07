package repository;

import entity.ProjectData;
import entity.SuitData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SuitRepository extends JpaRepository<SuitData, Long> {

	@Query("SELECT s FROM SuitData s WHERE s.id = :id AND s.deletionDate IS NULL")
	SuitData findExistingById(@Param("id") Long id);

	@Query(
			"SELECT s FROM SuitData s WHERE " +
			"(s.project.id = :projectId) AND " +
			"(s.code LIKE %:code% OR :code IS NULL) AND " +
			"(s.title LIKE %:title% OR :title IS NULL) AND " +
			"(s.user.firstName LIKE %:firstName% OR :firstName IS NULL) AND " +
			"(s.user.lastName LIKE %:lastName% OR :lastName IS NULL) AND " +
			"s.deletionDate IS NULL"
	)
	List<SuitData> findAllByProjectIdAndCodeAndTitleAndUser(@Param("projectId") Long projectId, @Param("code") String code, @Param("title") String title, @Param("firstName") String firstName, @Param("lastName") String lastName);

	List<SuitData> findAllByProject(ProjectData project);

	@Query(
			"SELECT s FROM SuitData s WHERE " +
			"(s.user.id = :userId) AND " +
			"(s.title LIKE %:suitTitle% OR :suitTitle IS NULL) AND " +
			"(s.project.title LIKE %:projectTitle% OR :projectTitle IS NULL) AND " +
			"((COALESCE(s.modificationDate, s.creationDate) BETWEEN :lowerDate AND :upperDate) OR " +
			"(COALESCE(s.modificationDate, s.creationDate) <= :upperDate AND :lowerDate IS NULL) OR" +
			"(COALESCE(s.modificationDate, s.creationDate) >= :lowerDate AND :upperDate IS NULL) OR" +
			"(:lowerDate IS NULL AND :upperDate IS NULL)) AND " +
			"s.deletionDate IS NULL"
	)
	List<SuitData> findAllByUserIdAndTitleAndProjectTitleAndModificationDate(@Param("userId") Long userId, @Param("suitTitle") String suitTitle, @Param("projectTitle") String projectTitle, @Param("lowerDate") LocalDateTime lowerDate, @Param("upperDate") LocalDateTime upperDate);
}
