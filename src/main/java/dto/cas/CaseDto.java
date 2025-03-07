package dto.cas;

import dto.step.StepIdOrderDto;
import entity.CaseData;
import entity.StepData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CaseDto {

	private Long id;
	private LocalDateTime creationDate;
	private LocalDateTime modificationDate;
	private LocalDateTime deletionDate;
	private Long suitId;
	private Long userId;
	private String title;
	private Long typeId;
	private Long priorityId;
	private Long statusId;
	private BigDecimal time;
	private String preConditions;
	private String postConditions;
	private String code;
	private String summary;
	private int externalId;
	private List<dto.step.StepIdOrderDto> steps;

	public CaseDto(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long suitId, Long userId, String title, Long typeId, Long priorityId, Long statusId, BigDecimal time, String preConditions, String postConditions, String code, String summary, int externalId, Map<Long, StepData> steps) {
		this.id = id;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.deletionDate = deletionDate;
		this.suitId = suitId;
		this.userId = userId;
		this.title = title;
		this.typeId = typeId;
		this.priorityId = priorityId;
		this.statusId = statusId;
		this.time = time;
		this.preConditions = preConditions;
		this.postConditions = postConditions;
		this.code = code;
		this.summary = summary;
		this.externalId = externalId;
		this.steps = steps.values().stream()
				.map(dto.step.StepIdOrderDto::toDto)
				.collect(Collectors.toList());
	}

	public static CaseDto toDto(CaseData cas) {
		return new CaseDto(
				cas.getId(),
				cas.getCreationDate(),
				cas.getModificationDate(),
				cas.getDeletionDate(),
				cas.getSuitId(),
				cas.getUserId(),
				cas.getTitle(),
				cas.getTypeId(),
				cas.getPriorityId(),
				cas.getStatusId(),
				cas.getTime(),
				cas.getPreConditions(),
				cas.getPostConditions(),
				cas.getCode(),
				cas.getSummary(),
				cas.getExternalId(),
				cas.getSteps()
		);
	}

	public static CaseData toEntity(CaseDto caseDto) {
		return new CaseData(
				caseDto.getId(),
				caseDto.getCreationDate(),
				caseDto.getModificationDate(),
				caseDto.getDeletionDate(),
				caseDto.getSuitId(),
				caseDto.getUserId(),
				caseDto.getTitle(),
				caseDto.getTypeId(),
				caseDto.getPriorityId(),
				caseDto.getStatusId(),
				caseDto.getTime(),
				caseDto.getPreConditions(),
				caseDto.getPostConditions(),
				caseDto.getCode(),
				caseDto.getSummary(),
				caseDto.getExternalId(),
				caseDto.getSteps().stream()
						.map(StepIdOrderDto::toEntity)
						.collect(Collectors.toMap(StepData::getId, step -> step))
		);
	}
}
