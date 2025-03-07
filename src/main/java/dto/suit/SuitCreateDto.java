package dto.suit;

import entity.SuitData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuitCreateDto {

	private Long id;
	private Long projectId;
	private Long userId;
	private Long statusId;
	private String title;
	private String description;
	private String code;

	public SuitCreateDto(Long id, Long projectId, Long userId, Long statusId, String title, String description, String code) {
		this.id = id;
		this.projectId = projectId;
		this.userId = userId;
		this.statusId = statusId;
		this.title = title;
		this.description = description;
		this.code = code;
	}

	public static SuitCreateDto toDto(SuitData suit) {
		return new SuitCreateDto(
				suit.getId(),
				suit.getProject().getId(),
				suit.getUser().getId(),
				suit.getStatus().getId(),
				suit.getTitle(),
				suit.getDescription(),
				suit.getCode()
		);
	}

	public static SuitData toEntity(SuitCreateDto suitCreateDto) {
		return new SuitData(
				suitCreateDto.getId(),
				suitCreateDto.getProjectId(),
				suitCreateDto.getUserId(),
				suitCreateDto.getStatusId(),
				suitCreateDto.getTitle(),
				suitCreateDto.getDescription(),
				suitCreateDto.getCode()
		);
	}
}
