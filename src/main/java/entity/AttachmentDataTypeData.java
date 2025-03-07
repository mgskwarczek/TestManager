package entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TSL_ATTACHMENTS_DATA_TYPE")
public class AttachmentDataTypeData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_ADT_ID_SEQ")
	@SequenceGenerator(name = "PK_ADT_ID_SEQ", sequenceName = "PK_ADT_ID_SEQ", initialValue = 50)
	@Column(name = "ADT_ID", length = 10, nullable = false)
	private Long id;

	@Column(name = "ADT_DATA_TYPE", length = 50, nullable = false)
	private String type;
}