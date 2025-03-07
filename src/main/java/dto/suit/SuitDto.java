package dto.suit;

import dto.cas.CaseIdTitleDto;
import entity.CaseData;
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
public class SuitDto {

	private Long id;
	private LocalDateTime creationDate;
	private LocalDateTime modificationDate;
	private LocalDateTime deletionDate;
	private Long projectId;
	private Long userId;
	private Long statusId;
	private String title;
	private String description;
	private String code;
	private List<CaseIdTitleDto> cases;

	public SuitDto(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long projectId, Long userId, Long statusId, String title, String description, String code, List<CaseData> cases) {
		this.id = id;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.deletionDate = deletionDate;
		this.projectId = projectId;
		this.userId = userId;
		this.statusId = statusId;
		this.title = title;
		this.description = description;
		this.code = code;
		this.cases = cases.stream()
				.map(CaseIdTitleDto::toDto)
				.collect(Collectors.toList());
	}

	public static SuitDto toDto(SuitData suit) {
		return new SuitDto(
				suit.getId(),
				suit.getCreationDate(),
				suit.getModificationDate(),
				suit.getDeletionDate(),
				suit.getProjectId(),
				suit.getUserId(),
				suit.getStatusId(),
				suit.getTitle(),
				suit.getDescription(),
				suit.getCode(),
				suit.getCases()
		);
	}

	public static SuitData toEntity(SuitDto suitDto) {
		return new SuitData(
				suitDto.getId(),
				suitDto.getCreationDate(),
				suitDto.getModificationDate(),
				suitDto.getDeletionDate(),
				suitDto.getProjectId(),
				suitDto.getUserId(),
				suitDto.getStatusId(),
				suitDto.getTitle(),
				suitDto.getDescription(),
				suitDto.getCode(),
				suitDto.getCases().stream()
						.map(CaseIdTitleDto::toEntity)
						.collect(Collectors.toList())
		);
	}
}
