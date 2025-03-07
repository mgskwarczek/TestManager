package repository;

import entity.AttachmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentData, Long> {

	@Query("SELECT a FROM AttachmentData a WHERE a.id = :id AND a.deletionDate IS NULL")
	AttachmentData findExistingById(@Param("id") Long id);
}
