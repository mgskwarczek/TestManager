package dto.project;

import entity.ProjectData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectIdTitleDto {

	private Long id;
	private String title;

	public ProjectIdTitleDto(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public static ProjectIdTitleDto toDto(ProjectData project) {
		return new ProjectIdTitleDto(project.getId(), project.getTitle());
	}

	public static ProjectData toEntity(ProjectIdTitleDto projectIdTitleDTO) {
		return new ProjectData(
				projectIdTitleDTO.getId(),
				projectIdTitleDTO.getTitle()
		);
	}
}
