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
@Table(name = "TSL_CASE_PRIORITIES")
public class CasePriorityData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_CPR_ID_SEQ")
	@SequenceGenerator(name = "PK_CPR_ID_SEQ", sequenceName = "PK_CPR_ID_SEQ", initialValue = 50)
	@Column(name = "CPR_ID", length = 10, nullable = false)
	private Long id;

	@Column(name = "CPR_PRIORITY_NAME", length = 50, nullable = false)
	private String name;
}
