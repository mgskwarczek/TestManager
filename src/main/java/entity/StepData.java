package entity;


import dto.auditLogValues.LogValueDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "creationDate", column = @Column(name = "STP_CRE_DATE", nullable = false, updatable = false)),
		@AttributeOverride(name = "modificationDate", column = @Column(name = "STP_MOD_DATE")),
		@AttributeOverride(name = "deletionDate", column = @Column(name = "STP_DEL_DATE"))
})
@Table(name = "TSL_STEPS")
public class StepData extends AuditFieldsData<StepData> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_STP_ID_SEQ")
	@SequenceGenerator(name = "PK_STP_ID_SEQ", sequenceName = "PK_STP_ID_SEQ", initialValue = 50)
	@Column(name = "STP_ID", length = 10, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STP_CAS_ID", nullable = false, updatable = false, insertable = false)
	private CaseData cas;

	@Column(name = "STP_CAS_ID", nullable = false)
	private Long caseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STP_SST_ID", referencedColumnName = "SST_ID", nullable = false, updatable = false, insertable = false)
	private StepStatusData status;

	@Column(name = "STP_SST_ID", nullable = false)
	private Long statusId;

	@Column(name = "STP_ACTION", length = 100, nullable = false)
	private String action;

	@Column(name = "STP_ORDER", length = 4, nullable = false)
	private Integer order;

	@Column(name = "STP_COMMENT", length = 1000)
	private String comment;

	@Column(name = "STP_RESULT", length = 1000)
	private String result;

	@OneToMany(mappedBy = "step", fetch = FetchType.LAZY)
	@MapKey(name = "id")
	@Where(clause = "ATM_DEL_DATE IS NULL")
	private Map<Long, AttachmentData> attachments = new HashMap<>();

	public StepData(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long caseId, Long statusId, String action, Integer order, String comment, String result, Map<Long, AttachmentData> attachments) {
		this.id = id;
		setCreationDate(creationDate);
		setModificationDate(modificationDate);
		setDeletionDate(deletionDate);
		this.caseId = caseId;
		this.statusId = statusId;
		this.action = action;
		this.order = order;
		this.comment = comment;
		this.result = result;
		this.attachments = attachments;
	}

	public StepData(Long id, Long statusId, String action, Integer order, String comment, String result) {
		this.id = id;
		this.statusId = statusId;
		this.action = action;
		this.order = order;
		this.comment = comment;
		this.result = result;
	}

	public StepData(Long id, Integer order) {
		this.id = id;
		this.order = order;
	}

	public void addAttachment(AttachmentData attachment) {
		attachments.put(attachment.getId(), attachment);
	}

	public void removeAttachment(Long id) {
		attachments.remove(id);
	}

	public void clearAttachments() {
		attachments.clear();
	}

	@Override
	public List<LogValueDto> compare(StepData updatedStep) {
		List<LogValueDto> changes = new ArrayList<>();
		if (updatedStep.getStatusId() != null && !statusId.equals(updatedStep.getStatusId())) {
			changes.add(new LogValueDto("statusId", statusId.toString(), updatedStep.getStatusId().toString()));
		}
		if (updatedStep.getAction() != null && !action.equals(updatedStep.getAction())) {
			changes.add(new LogValueDto("action", action, updatedStep.getAction()));
		}
		if (updatedStep.getOrder() != null && !order.equals(updatedStep.getOrder())) {
			changes.add(new LogValueDto("order", order.toString(), updatedStep.getOrder().toString()));
		}
		if (updatedStep.getComment() != null && !comment.equals(updatedStep.getComment())) {
			changes.add(new LogValueDto("comment", comment, updatedStep.getComment()));
		}
		if (updatedStep.getResult() != null && !result.equals(updatedStep.getResult())) {
			changes.add(new LogValueDto("result", result, updatedStep.getResult()));
		}

		for (Map.Entry<Long, AttachmentData> attachment : attachments.entrySet()) {
			if (!updatedStep.getAttachments().containsKey(attachment.getKey())) {
				changes.add(new LogValueDto("attachments", null, attachment.getValue().toString()));
			}
		}

		for (Map.Entry<Long, AttachmentData> attachment : updatedStep.getAttachments().entrySet()) {
			if (!attachments.containsKey(attachment.getKey())) {
				changes.add(new LogValueDto("attachments", attachment.getValue().toString(), null));
			}
		}

		return changes;
	}

	@Override
	public String toString() {
		return "StepId: " + id + "\n " +
				"CaseId: " + caseId + "\n " +
				"Status: " + statusId;
	}
}