package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TSL_USERS_TEAMS")
public class UsersTeamsData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_UST_ID_SEQ")
	@SequenceGenerator(name = "PK_UST_ID_SEQ", sequenceName = "PK_UST_ID_SEQ", initialValue = 50)
	@Column(name = "UST_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "UST_USR_ID", nullable = false)
	private UserData user;

	@ManyToOne
	@JoinColumn(name = "UST_TMS_ID", nullable = false)
	private TeamData team;
}
