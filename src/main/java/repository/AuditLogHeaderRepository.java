package repository;

import entity.AuditLogHeaderData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogHeaderRepository extends JpaRepository<AuditLogHeaderData, Long> {

	List<AuditLogHeaderData> findHeadersByRecordPKAndEntityName(Long recordPK, String entityName);

}
