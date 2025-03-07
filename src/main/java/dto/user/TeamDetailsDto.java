package dto.user;

import entity.TeamData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TeamDetailsDto {

	private Long id;
	private String name;
	private LocalDateTime creationDate;
	private LocalDateTime modificationDate;
	private LocalDateTime deletionDate;

	public TeamDetailsDto(Long id, String name, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate) {
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.deletionDate = deletionDate;
	}

	public static TeamDetailsDto toDto(TeamData teamData) {
		return new TeamDetailsDto(
				teamData.getId(),
				teamData.getName(),
				teamData.getCreationDate(),
				teamData.getModificationDate(),
				teamData.getDeletionDate()
		);
	}
}
