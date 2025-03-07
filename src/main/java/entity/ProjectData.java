package entity;

import dto.auditLogValues.LogValueDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "creationDate", column = @Column(name = "PRJ_CRE_DATE", nullable = false, updatable = false)),
		@AttributeOverride(name = "modificationDate", column = @Column(name = "PRJ_MOD_DATE")),
		@AttributeOverride(name = "deletionDate", column = @Column(name = "PRJ_DEL_DATE"))
})
@Table(name = "TSL_PROJECTS")
public class ProjectData extends AuditFieldsData<ProjectData> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_PRJ_ID_SEQ")
	@SequenceGenerator(name = "PK_PRJ_ID_SEQ", sequenceName = "PK_PRJ_ID_SEQ", initialValue = 50)
	@Column(name = "PRJ_ID", length = 10, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PRJ_PST_ID", referencedColumnName = "PST_ID", nullable = false, updatable = false, insertable = false)
	private ProjectStatusData status;

	@Column(name = "PRJ_PST_ID")
	private Long statusId;

	@Column(name = "PRJ_TITLE", length = 100, nullable = false)
	private String title;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SuitData> suits;

	public ProjectData(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public ProjectData(Long id, Long statusId, String title) {
		this.id = id;
		this.statusId = statusId;
		this.title = title;
	}

	public ProjectData(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long statusId, String title, List<SuitData> suits) {
		this.id = id;
		setCreationDate(creationDate);
		setModificationDate(modificationDate);
		setDeletionDate(deletionDate);
		this.statusId = statusId;
		this.title = title;
		this.suits = suits;
	}

	@Override
	public List<LogValueDto> compare(ProjectData entity) {
		List<LogValueDto> changes = new ArrayList<>();
		if (entity.getStatusId() != null && !statusId.equals(entity.getStatusId())) {
			changes.add(new LogValueDto("statusId", statusId.toString(), entity.getStatusId().toString()));
		}
		if (entity.getTitle() != null && !title.equals(entity.getTitle())) {
			changes.add(new LogValueDto("title", title, entity.getTitle()));
		}
		return changes;
	}
}
