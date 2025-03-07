package dto.user;

import entity.TeamData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TeamUpdateDto {

	private Long id;
	private String name;

	private TeamUpdateDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static TeamUpdateDto toDto(TeamData teamData) {
		return new TeamUpdateDto(
				teamData.getId(),
				teamData.getName()
		);
	}
}
