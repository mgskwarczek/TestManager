package dto.user;

import entity.TeamData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {

	private Long id;
	private String name;

	private TeamDto (Long id, String name){
		this.id = id;
		this.name = name;
	}

	public static TeamDto toDto(TeamData teamData) {
		return new TeamDto(
				teamData.getId(),
				teamData.getName()
		);
	}
}
