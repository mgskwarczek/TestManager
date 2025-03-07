package entity;


import dto.auditLogValues.LogValueDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "creationDate", column = @Column(name = "CAS_CRE_DATE", nullable = false, updatable = false)),
		@AttributeOverride(name = "modificationDate", column = @Column(name = "CAS_MOD_DATE")),
		@AttributeOverride(name = "deletionDate", column = @Column(name = "CAS_DEL_DATE"))
})
@Table(name = "TSL_CASES")
public class CaseData extends AuditFieldsData<CaseData> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_CAS_ID_SEQ")
	@SequenceGenerator(name = "PK_CAS_ID_SEQ", sequenceName = "PK_CAS_ID_SEQ", initialValue = 50)
	@Column(name = "CAS_ID", length = 10, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAS_SUI_ID", nullable = false, updatable = false, insertable = false)
	private SuitData suit;

	@Column(name = "CAS_SUI_ID", nullable = false)
	private Long suitId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAS_USR_ID", referencedColumnName = "USR_ID", nullable = false, updatable = false, insertable = false)
	private UserData user;

	@Column(name = "CAS_USR_ID", nullable = false)
	private Long userId;

	@Column(name = "CAS_TITLE", length = 100, nullable = false)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAS_CTT_ID", referencedColumnName = "CTT_ID", nullable = false, updatable = false, insertable = false)
	private CaseTestTypeData type;

	@Column(name = "CAS_CTT_ID", nullable = false)
	private Long typeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAS_CPR_ID", referencedColumnName = "CPR_ID", nullable = false, updatable = false, insertable = false)
	private CasePriorityData priority;

	@Column(name = "CAS_CPR_ID", nullable = false)
	private Long priorityId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CAS_CST_ID", referencedColumnName = "CST_ID", nullable = false, updatable = false, insertable = false)
	private CaseStatusData status;

	@Column(name = "CAS_CST_ID", nullable = false)
	private Long statusId;

	@Column(name = "CAS_TIME", length = 5, scale = 2)
	private BigDecimal time;

	@Column(name = "CAS_PRE_CONDITIONS", length = 1000)
	private String preConditions;

	@Column(name = "CAS_POST_CONDITIONS", length = 1000)
	private String postConditions;

	@Column(name = "CAS_CODE", length = 30, nullable = false)
	private String code;

	@Column(name = "CAS_SUMMARY", length = 1000)
	private String summary;

	@Column(name = "CAS_EXTERNAL_ID", length = 10)
	private int externalId;

	@OneToMany(mappedBy = "cas", fetch = FetchType.LAZY)
	@MapKey(name = "id")
	@Where(clause = "STP_DEL_DATE IS NULL")
	private Map<Long, StepData> steps = new HashMap<>();

	public CaseData(Long suitId, Long userId, String title, Long typeId, Long priorityId, Long statusId, BigDecimal time, String preConditions, String postConditions, String code, String summary, int externalId) {
		this.suitId = suitId;
		this.userId = userId;
		this.title = title;
		this.typeId = typeId;
		this.priorityId = priorityId;
		this.statusId = statusId;
		this.time = time;
		this.preConditions = preConditions;
		this.postConditions = postConditions;
		this.code = code;
		this.summary = summary;
		this.externalId = externalId;

	}

	public CaseData(Long id, Long userId, String title, Long typeId, Long priorityId, Long statusId, BigDecimal time, String preConditions, String postConditions, String code, String summary) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.typeId = typeId;
		this.priorityId = priorityId;
		this.statusId = statusId;
		this.time = time;
		this.preConditions = preConditions;
		this.postConditions = postConditions;
		this.code = code;
		this.summary = summary;
	}

	public CaseData(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public CaseData(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long suitId, Long userId, String title, Long typeId, Long priorityId, Long statusId, BigDecimal time, String preConditions, String postConditions, String code, String summary, int externalId, Map<Long, StepData> steps) {
		this.id = id;
		setCreationDate(creationDate);
		setModificationDate(modificationDate);
		setDeletionDate(deletionDate);
		this.suitId = suitId;
		this.userId = userId;
		this.title = title;
		this.typeId = typeId;
		this.priorityId = priorityId;
		this.statusId = statusId;
		this.time = time;
		this.preConditions = preConditions;
		this.postConditions = postConditions;
		this.code = code;
		this.summary = summary;
		this.externalId = externalId;
		this.steps = steps;
	}

	public void addStep(StepData step) {
		steps.put(step.getId(), step);
	}

	public void removeStep(Long id) {
		steps.remove(id);
	}

	@Override
	public List<LogValueDto> compare(CaseData updatedCase) {
		List<LogValueDto> changes = new ArrayList<>();
		if (updatedCase.getUserId() != null && !userId.equals(updatedCase.getUserId())) {
			changes.add(new LogValueDto("userId", user.getId().toString(), updatedCase.getUserId().toString()));
		}
		if (updatedCase.getTitle() != null && !title.equals(updatedCase.getTitle())) {
			changes.add(new LogValueDto("title", title, updatedCase.getTitle()));
		}
		if (updatedCase.getTypeId() != null && !typeId.equals(updatedCase.getTypeId())) {
			changes.add(new LogValueDto("typeId", type.getId().toString(), updatedCase.getTypeId().toString()));
		}
		if (updatedCase.getPriorityId() != null && !priorityId.equals(updatedCase.getPriorityId())) {
			changes.add(new LogValueDto("priorityId", priorityId.toString(), updatedCase.getPriorityId().toString()));
		}
		if (updatedCase.getStatusId() != null && !statusId.equals(updatedCase.getStatusId())) {
			changes.add(new LogValueDto("statusId", statusId.toString(), updatedCase.getStatusId().toString()));
		}
		if (updatedCase.getTime() != null && !time.equals(updatedCase.getTime())) {
			changes.add(new LogValueDto("time", time.toString(), updatedCase.getTime().toString()));
		}
		if (updatedCase.getPreConditions() != null && !preConditions.equals(updatedCase.getPreConditions())) {
			changes.add(new LogValueDto("preConditions", preConditions, updatedCase.getPreConditions()));
		}
		if (updatedCase.getPostConditions() != null && !postConditions.equals(updatedCase.getPostConditions())) {
			changes.add(new LogValueDto("postConditions", postConditions, updatedCase.getPostConditions()));
		}
		if (updatedCase.getCode() != null && !code.equals(updatedCase.getCode())) {
			changes.add(new LogValueDto("code", code, updatedCase.getCode()));
		}
		if (updatedCase.getSummary() != null && !summary.equals(updatedCase.getSummary())) {
			changes.add(new LogValueDto("summary", summary, updatedCase.getSummary()));
		}

		for (Map.Entry<Long, StepData> step : updatedCase.getSteps().entrySet()) {
			if (!steps.containsKey(step.getKey())) {
				changes.add(new LogValueDto("steps", null, step.getValue().toString()));
			}
		}

		for (Map.Entry<Long, StepData> step : steps.entrySet()) {
			if (!updatedCase.getSteps().containsKey(step.getKey())) {
				changes.add(new LogValueDto("steps", step.getValue().toString(), null));
			}
		}

		return changes;
	}
}
