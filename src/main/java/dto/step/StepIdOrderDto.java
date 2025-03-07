package dto.step;

import entity.StepData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StepIdOrderDto {
	private Long id;
	private Integer order;

	public StepIdOrderDto(Long id, Integer order) {
		this.id = id;
		this.order = order;
	}

	public static StepIdOrderDto toDto(StepData step) {
		return new StepIdOrderDto(
				step.getId(),
				step.getOrder()
		);
	}

	public static StepData toEntity(StepIdOrderDto stepIdOrderDto) {
		return new StepData(
				stepIdOrderDto.getId(),
				stepIdOrderDto.getOrder()
		);
	}
}
