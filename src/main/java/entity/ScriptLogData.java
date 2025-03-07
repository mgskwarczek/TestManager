package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TSL_SCRIPT_LOG")
@AttributeOverride(name = "creationDate", column = @Column(name = "SCL_CRE_DATE"))
public class ScriptLogData extends AuditCreationData {

	@Id
	@Column(name = "SCL_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TSL_SCRIPT_LOG_SEQ")
	@SequenceGenerator(name = "TSL_SCRIPT_LOG_SEQ", sequenceName = "TSL_SCRIPT_LOG_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "SCL_MSG", nullable = false)
	private String message;

	@Column(name = "SCL_FILENAME", nullable = false)
	private String filename;

}
