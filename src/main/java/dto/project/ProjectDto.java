package dto.project;

import dto.suit.SuitIdTitleDto;
import entity.ProjectData;
import entity.SuitData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDto {

	private Long id;
	private LocalDateTime creationDate;
	private LocalDateTime modificationDate;
	private LocalDateTime deletionDate;
	private Long statusId;
	private String title;
	private List<SuitIdTitleDto> suits;

	public ProjectDto(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long statusId, String title, List<SuitData> suits) {
		this.id = id;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.deletionDate = deletionDate;
		this.statusId = statusId;
		this.title = title;
		this.suits = suits.stream()
				.map(SuitIdTitleDto::toDto)
				.collect(Collectors.toList());
	}

	public static ProjectDto toDto(ProjectData project) {
		return new ProjectDto(
				project.getId(),
				project.getCreationDate(),
				project.getModificationDate(),
				project.getDeletionDate(),
				project.getStatusId(),
				project.getTitle(),
				project.getSuits()
		);
	}

	public static ProjectData toEntity(ProjectDto projectDto) {
		return new ProjectData(
				projectDto.getId(),
				projectDto.getCreationDate(),
				projectDto.getModificationDate(),
				projectDto.getDeletionDate(),
				projectDto.getStatusId(),
				projectDto.getTitle(),
				projectDto.getSuits().stream()
						.map(SuitIdTitleDto::toEntity)
						.collect(Collectors.toList())
		);
	}
}
