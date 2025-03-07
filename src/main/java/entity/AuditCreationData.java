package entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import utils.EntityIdentifiable;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditCreationData implements EntityIdentifiable {

	@Column(name = " ", nullable = false, updatable = false)
	private LocalDateTime creationDate;

}
