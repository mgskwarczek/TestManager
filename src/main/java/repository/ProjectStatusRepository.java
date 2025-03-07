package repository;

import entity.ProjectStatusData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatusData, Long> {
}
