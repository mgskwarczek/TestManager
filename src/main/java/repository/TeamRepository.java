package repository;

import entity.TeamData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<TeamData, Long> {

	@Query("SELECT t FROM TeamData t WHERE t.deletionDate IS NULL")
	List<TeamData> findAllActiveTeams();

	@Query("SELECT t FROM TeamData t WHERE " + "LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	Set<TeamData> findByName(@Param(value = "name") String name);
}
