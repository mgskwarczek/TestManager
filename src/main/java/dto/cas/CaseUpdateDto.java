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
public class CaseUpdateDto {

	private Long id;
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
	private List<StepCreateUpdateDto> steps;

	public CaseUpdateDto(Long id, Long userId, String title, Long typeId, Long priorityId, Long statusId, BigDecimal time, String preConditions, String postConditions, String code, String summary, List<StepCreateUpdateDto> steps) {
		this.id = id;
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
		this.steps = steps;
	}

	public static CaseUpdateDto toDto(CaseData cas) {
		return new CaseUpdateDto(
				cas.getId(),
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
				cas.getSteps().values().stream()
						.map(StepCreateUpdateDto::toDto)
						.collect(Collectors.toList())
		);
	}

	public static CaseData toEntity(CaseUpdateDto caseUpdateDto) {
		return new CaseData(
				caseUpdateDto.getId(),
				caseUpdateDto.getUserId(),
				caseUpdateDto.getTitle(),
				caseUpdateDto.getTypeId(),
				caseUpdateDto.getPriorityId(),
				caseUpdateDto.getStatusId(),
				caseUpdateDto.getTime(),
				caseUpdateDto.getPreConditions(),
				caseUpdateDto.getPostConditions(),
				caseUpdateDto.getCode(),
				caseUpdateDto.getSummary()
		);
	}
}
