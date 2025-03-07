package dto.user;

import entity.TeamData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCreateDto {

	private Long id;
	private String name;

	public TeamCreateDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static TeamCreateDto toDto(TeamData teamData) {
		return new TeamCreateDto(
				teamData.getId(),
				teamData.getName()
		);
	}

	public static TeamData toEntity(TeamCreateDto teamCreateDto) {
		return new TeamData(
				teamCreateDto.getId(),
				teamCreateDto.getName()
		);
	}
}
