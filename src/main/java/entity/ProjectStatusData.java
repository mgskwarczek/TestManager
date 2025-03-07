package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TSL_PROJECT_STATUSES")
public class ProjectStatusData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_PST_ID_SEQ")
	@SequenceGenerator(name = "PK_PST_ID_SEQ", sequenceName = "PK_PST_ID_SEQ", initialValue = 50)
	@Column(name = "PST_ID", length = 10, nullable = false)
	private Long id;

	@Column(name = "PST_STATUS_NAME", length = 50, nullable = false)
	private String name;
}
