package entity;

import dto.auditLogValues.LogValueDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "creationDate", column = @Column(name = "ATM_CRE_DATE", nullable = false, updatable = false)),
		@AttributeOverride(name = "modificationDate", column = @Column(name = "ATM_MOD_DATE")),
		@AttributeOverride(name = "deletionDate", column = @Column(name = "ATM_DEL_DATE"))
})
@Table(name = "TSL_ATTACHMENTS")
public class AttachmentData extends AuditFieldsData<AttachmentData> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_ATM_ID_SEQ")
	@SequenceGenerator(name = "PK_ATM_ID_SEQ", sequenceName = "PK_ATM_ID_SEQ", initialValue = 50)
	@Column(name = "ATM_ID", length = 10, nullable = false)
	private Long id;

	@Lob
	@Column(name = "ATM_DATA", nullable = false)
	private byte[] data;

	@Column(name = "ATM_NAME", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATM_STP_ID", nullable = false, updatable = false, insertable = false)
	private StepData step;

	@Column(name = "ATM_STP_ID", nullable = false)
	private Long stepId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ATM_ADT_ID", referencedColumnName = "ADT_ID", nullable = false, updatable = false, insertable = false)
	private AttachmentDataTypeData type;

	@Column(name = "ATM_ADT_ID", nullable = false)
	private Long typeId;

	public AttachmentData(Long id, byte[] data, Long typeId, String name) {
		this.id = id;
		this.data = data;
		this.typeId = typeId;
		this.name = name;
	}

	@Override
	public List<LogValueDto> compare(AttachmentData entity) {
		List<LogValueDto> changes = new ArrayList<>();
		if (entity.getName() != null && !name.equals(entity.getName())) {
			changes.add(new LogValueDto("name", name, entity.getName()));
		}
		if (entity.getTypeId() != null && !typeId.equals(entity.getTypeId())) {
			changes.add(new LogValueDto("typeId", typeId.toString(), entity.getTypeId().toString()));
		}
		return changes;
	}

	@Override
	public String toString() {
		return "attachmentId: "    + id     + "\n " +
				"attachmentName: " + name   + "\n " +
				"stepId: "         + stepId + "\n";
	}
}