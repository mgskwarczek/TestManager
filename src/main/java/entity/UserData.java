package entity;

import dto.auditLogValues.LogValueDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.String;
import java.util.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TSL_USERS")
@AttributeOverrides({
		@AttributeOverride(name = "creationDate", column = @Column(name = "USR_CRE_DATE", nullable = false, updatable = false)),
		@AttributeOverride(name = "modificationDate", column = @Column(name = "USR_MOD_DATE")),
		@AttributeOverride(name = "deletionDate", column = @Column(name = "USR_DEL_DATE"))
})
public class UserData extends AuditFieldsData<UserData> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_USR_ID_SEQ")
	@SequenceGenerator(name = "PK_USR_ID_SEQ", sequenceName = "PK_USR_ID_SEQ", initialValue = 50)

	@Column(name = "USR_ID")
	private Long id;

	@Column(name = "USR_FIRST_NAME", length = 100)
	private String firstName;

	@Column(name = "USR_LAST_NAME", length = 100)
	private String lastName;

	@Column(name = "USR_EMAIL", nullable = false)
	private String email;

	@Column(name = "USR_PASSWORD", nullable = false)
	private String password;

	@Column(name = "USR_SALT")
	private String salt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "TSL_USERS_TEAMS", joinColumns = {@JoinColumn(name = "UST_USR_ID")},
			inverseJoinColumns = {@JoinColumn(name = "UST_TMS_ID")})
	@MapKey(name = "id")
	private Map<Long, TeamData> teams = new HashMap<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CaseData> cases;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SuitData> suits;

	@ManyToOne
	@JoinColumn(name = "USR_ROL_ID", referencedColumnName = "ROL_ID", updatable = false, insertable = false)
	private RoleData role;

	@Column(name = "USR_ROL_ID", nullable = false)
	private Long roleId;

	public UserData(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public UserData(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public UserData(String newPassword) {
		this.password = newPassword;
	}

	public UserData(String firstName, String lastName, String email, String password, Long roleId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roleId = roleId;
	}

	public UserData(String firstName, String lastName, String email, Long roleId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roleId = roleId;
	}

	public UserData(Long id, String firstName, String lastName, String email, String password, Long roleId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roleId = roleId;
	}

	public UserData(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@Override
	public List<LogValueDto> compare(UserData updatedUser) {
		List<LogValueDto> changes = new ArrayList<>();
		if (updatedUser.getFirstName() != null && !firstName.equals(updatedUser.getFirstName())) {
			changes.add(new LogValueDto("firstName", firstName, updatedUser.getFirstName()));
		}

		if (updatedUser.getLastName() != null && !lastName.equals(updatedUser.getLastName())) {
			changes.add(new LogValueDto("lastName", lastName, updatedUser.getLastName()));
		}

		if (updatedUser.getEmail() != null && !email.equals(updatedUser.getEmail())) {
			changes.add(new LogValueDto("email", email, updatedUser.getEmail()));
		}

		if (updatedUser.getRoleId() != null && !roleId.equals(updatedUser.getRoleId())) {
			changes.add(new LogValueDto("role", role.getName(), updatedUser.getRole().getName()));
		}

		if (updatedUser.getPassword() != null && !password.equals(updatedUser.getPassword())) {
			changes.add(new LogValueDto("password", "********", "********"));
		}

 		if (updatedUser.getTeams().size() > teams.size()) {
			for (Map.Entry<Long, TeamData> team : updatedUser.getTeams().entrySet()) {
				if (!teams.containsKey(team.getKey())) {
					TeamData teamData = team.getValue();
					changes.add(new LogValueDto("teams", null, "User added to team " + teamData.getName() + " with id: " + teamData.getId()));
				}
			}
		} else {
			Map<Long, TeamData> updatedUserTeams = updatedUser.getTeams();
			for (Map.Entry<Long, TeamData> team : teams.entrySet()) {
				if (!updatedUserTeams.containsKey(team.getKey())) {
					TeamData teamData = team.getValue();
					changes.add(new LogValueDto("teams", this.toString(), "User deleted from team " + teamData.getName() + " with id: " + teamData.getId()));
				}
			}
		}
		return changes;
	}

	@Override
	public String toString() {
		return "Id: "         + id        + " \n" +
				"FirstName: " + firstName + " \n" +
				"LastName: "  + lastName  + " \n" +
				"Email: "     + email     + " \n";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserData userData = (UserData) o;
		return Objects.equals(email, userData.email)
				&& Objects.equals(firstName, userData.firstName)
				&& Objects.equals(lastName, userData.lastName)
				&& Objects.equals(password, userData.password)
				&& Objects.equals(roleId, userData.roleId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, firstName, lastName, password, roleId);
	}
}
