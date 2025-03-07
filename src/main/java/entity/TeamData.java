package entity;

import dto.auditLogValues.LogValueDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "TSL_TEAMS")
@NoArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "creationDate", column = @Column(name = "TMS_CRE_DATE", nullable = false, updatable = false)),
		@AttributeOverride(name = "modificationDate", column = @Column(name = "TMS_MOD_DATE")),
		@AttributeOverride(name = "deletionDate", column = @Column(name = "TMS_DEL_DATE"))
})
public class TeamData extends AuditFieldsData<TeamData> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_TMS_ID_SEQ")
	@SequenceGenerator(name = "PK_TMS_ID_SEQ", sequenceName = "PK_TMS_ID_SEQ", initialValue = 50)
	@Column(name = "TMS_ID")
	private Long id;

	@Column(name = "TMS_NAME", nullable = false, length = 100)
	private String name;

	@ManyToMany(mappedBy = "teams")
	private Set<UserData> users = new HashSet<>();

	public TeamData(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public List<LogValueDto> compare(TeamData entity) {
		List<LogValueDto> changes = new ArrayList<>();

		if (entity.getName() != null && !name.equals(entity.getName())) {
			changes.add(new LogValueDto("name", name, entity.getName()));
		}
		return changes;
	}
}
