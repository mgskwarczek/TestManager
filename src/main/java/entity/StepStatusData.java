package entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TSL_STEP_STATUSES")
public class StepStatusData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_SST_ID_SEQ")
	@SequenceGenerator(name = "PK_SST_ID_SEQ", sequenceName = "PK_SST_ID_SEQ", initialValue = 50)
	@Column(name = "SST_ID", length = 10, nullable = false)
	private Long id;

	@Column(name = "SST_STATUS_NAME", length = 50, nullable = false)
	private String name;
}
