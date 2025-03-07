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
@Table(name = "TSL_CASE_TEST_TYPES")
public class CaseTestTypeData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_CTT_ID_SEQ")
	@SequenceGenerator(name = "PK_CTT_ID_SEQ", sequenceName = "PK_CTT_ID_SEQ", initialValue = 50)
	@Column(name = "CTT_ID", length = 10, nullable = false)
	private Long id;

	@Column(name = "CTT_TYPE_NAME", length = 50, nullable = false)
	private String name;
}
