package dto.suit;

import entity.SuitData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuitUpdateDto {

	private Long id;
	private Long statusId;
	private String title;
	private String description;
	private String code;

	public SuitUpdateDto(Long id, Long statusId, String title, String description, String code) {
		this.id = id;
		this.statusId = statusId;
		this.title = title;
		this.description = description;
		this.code = code;
	}

	public static SuitUpdateDto toDto(SuitData suit) {
		return new SuitUpdateDto(
				suit.getId(),
				suit.getStatusId(),
				suit.getTitle(),
				suit.getDescription(),
				suit.getCode()
		);
	}

	public static SuitData toEntity(SuitUpdateDto suitUpdateDTO) {
		return new SuitData(
				suitUpdateDTO.getId(),
				suitUpdateDTO.getStatusId(),
				suitUpdateDTO.getTitle(),
				suitUpdateDTO.getDescription(),
				suitUpdateDTO.getCode()
		);
	}
}
