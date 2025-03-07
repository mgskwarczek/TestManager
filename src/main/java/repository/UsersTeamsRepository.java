package repository;

import entity.TeamData;
import entity.UserData;
import entity.UsersTeamsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsersTeamsRepository extends JpaRepository<UsersTeamsData, Long> {

	UsersTeamsData findByTeamAndUser(TeamData team, UserData user);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
			"FROM UsersTeamsData u " +
			"WHERE u.user.id = :userId AND u.team.id = :teamId")
	boolean existsByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);

	@Query("SELECT u FROM UserData u JOIN UsersTeamsData ut ON u.id = ut.user.id " +
			"WHERE ut.team.id = :teamId " +
			"AND (:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
			"AND (:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
			"AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
	List<UserData> searchTeamMembers (@Param("teamId") Long teamId,
									  @Param("firstName") String firstName,
									  @Param("lastName") String lastName,
									  @Param("email") String email);

	@Query("SELECT t FROM TeamData t JOIN UsersTeamsData ut ON t.id = ut.team.id WHERE ut.user.id = :userId")
	List<TeamData> findTeamsByUserId(@Param("userId") Long userId);

	@Query("SELECT u FROM UserData u WHERE u.id NOT IN (" +
			"SELECT ut.user.id FROM UsersTeamsData ut WHERE ut.team.id = :teamId)" +
			"AND (:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
			"AND (:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
			"AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
	List<UserData> searchUsersNotInTeam(@Param("teamId")Long teamId,
										@Param("firstName") String firstName,
										@Param("lastName") String lastName,
										@Param("email") String email);
}