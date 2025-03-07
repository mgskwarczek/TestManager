package dto.project;


import entity.ProjectData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectCreateDto {

	private Long id;
	private Long statusId;
	private String title;

	public ProjectCreateDto(Long id, Long statusId, String title) {
		this.id = id;
		this.statusId = statusId;
		this.title = title;
	}

	public static ProjectCreateDto toDto(ProjectData project) {
		return new ProjectCreateDto(
				project.getId(),
				project.getStatusId(),
				project.getTitle()
		);
	}

	public static ProjectData toEntity(ProjectCreateDto projectCreateDto) {
		return new ProjectData(
				projectCreateDto.getId(),
				projectCreateDto.getStatusId(),
				projectCreateDto.getTitle()
		);
	}
}
