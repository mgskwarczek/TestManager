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
@Table(name = "TSL_SUIT_STATUSES")
public class SuitStatusData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_SUS_ID_SEQ")
	@SequenceGenerator(name = "PK_SUS_ID_SEQ", sequenceName = "PK_SUS_ID_SEQ", initialValue = 50)
	@Column(name = "SUS_ID", length = 10, nullable = false)
	private Long id;

	@Column(name = "SUS_STATUS_NAME", length = 50, nullable = false)
	private String name;
}