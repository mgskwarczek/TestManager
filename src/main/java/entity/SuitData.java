package entity;

import dto.auditLogValues.LogValueDto;
import dto.user.UserSendDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "creationDate", column = @Column(name = "SUI_CRE_DATE", nullable = false, updatable = false)),
		@AttributeOverride(name = "modificationDate", column = @Column(name = "SUI_MOD_DATE")),
		@AttributeOverride(name = "deletionDate", column = @Column(name = "SUI_DEL_DATE"))
})
@Table(name = "TSL_SUITS")
public class SuitData extends AuditFieldsData<SuitData> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_SUI_ID_SEQ")
	@SequenceGenerator(name = "PK_SUI_ID_SEQ", sequenceName = "PK_SUI_ID_SEQ", initialValue = 50)
	@Column(name = "SUI_ID", length = 10, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUI_PRJ_ID", nullable = false, updatable = false, insertable = false)
	private ProjectData project;

	@Column(name = "SUI_PRJ_ID", nullable = false)
	private Long projectId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUI_USR_ID", referencedColumnName = "USR_ID", nullable = false, updatable = false, insertable = false)
	private UserData user;

	@Column(name = "SUI_USR_ID", nullable = false)
	private Long userId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SUI_SUS_ID", referencedColumnName = "SUS_ID", nullable = false, updatable = false, insertable = false)
	private SuitStatusData status;

	@Column(name = "SUI_SUS_ID", nullable = false)
	private Long statusId;

	@Column(name = "SUI_TITLE", length = 100, nullable = false)
	private String title;

	@Column(name = "SUI_DESCRIPTION", length = 1000)
	private String description;

	@Column(name = "SUI_CODE", length = 30, nullable = false)
	private String code;

	@OneToMany(mappedBy = "suit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CaseData> cases;

	public SuitData(Long id, Long statusId, String title, String description, String code) {
		this.id = id;
		this.title = title;
		this.statusId = statusId;
		this.description = description;
		this.code = code;
	}

	public SuitData(String title, String code, UserSendDto user, Long id) {
		this.title = title;
		this.code = code;
		this.user = UserSendDto.toEntity(user);
		this.id = id;
	}

	public SuitData(Long id, Long projectId, Long userId, Long statusId, String title, String description, String code) {
		this.id = id;
		this.projectId = projectId;
		this.userId = userId;
		this.statusId = statusId;
		this.title = title;
		this.description = description;
		this.code = code;
	}

	public SuitData(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long projectId, Long userId, Long statusId, String title, String description, String code, List<CaseData> cases) {
		this.id = id;
		setCreationDate(creationDate);
		setModificationDate(modificationDate);
		setDeletionDate(deletionDate);
		this.projectId = projectId;
		this.userId = userId;
		this.statusId = statusId;
		this.title = title;
		this.description = description;
		this.code = code;
		this.cases = cases;
	}

	public SuitData(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	@Override
	public List<LogValueDto> compare(SuitData entity) {
		List<LogValueDto> changes = new ArrayList<>();
		if (entity.getUserId() != null && !userId.equals(entity.getUserId())) {
			changes.add(new LogValueDto("userId", userId.toString(), entity.getUserId().toString()));
		}
		if (entity.getStatusId() != null && !statusId.equals(entity.getStatusId())) {
			changes.add(new LogValueDto("statusId", statusId.toString(), entity.getStatusId().toString()));
		}
		if (entity.getTitle() != null && !title.equals(entity.getTitle())) {
			changes.add(new LogValueDto("title", title, entity.getTitle()));
		}
		if (entity.getDescription() != null && !description.equals(entity.getDescription())) {
			changes.add(new LogValueDto("description", description, entity.getDescription()));
		}
		if (entity.getCode() != null && !code.equals(entity.getCode())) {
			changes.add(new LogValueDto("code", code, entity.getCode()));
		}
		return changes;
	}
}