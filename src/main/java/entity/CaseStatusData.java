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
@Table(name = "TSL_CASE_STATUSES")
public class CaseStatusData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_CST_ID_SEQ")
	@SequenceGenerator(name = "PK_CST_ID_SEQ", sequenceName = "PK_CST_ID_SEQ", initialValue = 50)
	@Column(name = "CST_ID", length = 10, nullable = false)
	private Long id;

	@Column(name = "CST_STATUS_NAME", length = 50, nullable = false)
	private String name;
}
