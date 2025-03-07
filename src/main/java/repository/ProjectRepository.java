package repository;

import entity.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectData, Long> {

	List<ProjectData> findByTitleContaining(String title);

	ProjectData findByTitle(String projectTitle);

	@Query("SELECT p FROM ProjectData p WHERE p.id = :id AND p.deletionDate IS NULL")
	ProjectData findExistingById(@Param("id") Long id);

}
