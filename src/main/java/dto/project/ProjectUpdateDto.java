package dto.project;

import entity.ProjectData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectUpdateDto {

	private Long id;
	private Long statusId;
	private String title;

	public ProjectUpdateDto(Long id, Long statusId, String title) {
		this.id = id;
		this.statusId = statusId;
		this.title = title;
	}

	public static ProjectUpdateDto toDto(ProjectData project) {
		return new ProjectUpdateDto(
				project.getId(),
				project.getStatus().getId(),
				project.getTitle()
		);
	}

	public static ProjectData toEntity(ProjectUpdateDto projectUpdateDto) {
		return new ProjectData(
				projectUpdateDto.getId(),
				projectUpdateDto.getTitle()
		);
	}
}
