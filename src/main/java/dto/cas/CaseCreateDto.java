package dto.cas;

import dto.step.StepCreateUpdateDto;
import entity.CaseData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CaseCreateDto {

	private Long suitId;
	private Long userId;
	private String title;
	private Long typeId;
	private Long priorityId;
	private Long statusId;
	private BigDecimal time;
	private String preConditions;
	private String postConditions;;
	private String code;
	private String summary;
	private int externalId;
	private List<StepCreateUpdateDto> steps;

	public CaseCreateDto(Long suitId, Long userId, String title, Long typeId, Long priorityId, Long statusId, BigDecimal time, String preConditions, String postConditions, String code, String summary, int externalId, List<StepCreateUpdateDto> steps) {
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
		this.steps = steps;
	}

	public static CaseCreateDto toDto(CaseData cas) {
		return new CaseCreateDto(
				cas.getSuit().getId(),
				cas.getUser().getId(),
				cas.getTitle(),
				cas.getType().getId(),
				cas.getPriority().getId(),
				cas.getStatus().getId(),
				cas.getTime(),
				cas.getPreConditions(),
				cas.getPostConditions(),
				cas.getCode(),
				cas.getSummary(),
				cas.getExternalId(),
				cas.getSteps().values().stream()
						.map(StepCreateUpdateDto::toDto)
						.collect(Collectors.toList())
		);
	}

	public static CaseData toEntity(CaseCreateDto caseCreateDto) {
		return new CaseData(
				caseCreateDto.getSuitId(),
				caseCreateDto.getUserId(),
				caseCreateDto.getTitle(),
				caseCreateDto.getTypeId(),
				caseCreateDto.getPriorityId(),
				caseCreateDto.getStatusId(),
				caseCreateDto.getTime(),
				caseCreateDto.getPreConditions(),
				caseCreateDto.getPostConditions(),
				caseCreateDto.getCode(),
				caseCreateDto.getSummary(),
				caseCreateDto.getExternalId()
		);
	}
}
