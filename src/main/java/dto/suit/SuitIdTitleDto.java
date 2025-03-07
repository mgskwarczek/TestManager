package dto.suit;

import entity.SuitData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuitIdTitleDto {

	private Long id;
	private String title;

	public SuitIdTitleDto(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public static SuitIdTitleDto toDto(SuitData suit) {
		return new SuitIdTitleDto(
				suit.getId(),
				suit.getTitle()
		);
	}

	public static SuitData toEntity(SuitIdTitleDto suitIdTitleDto) {
		return new SuitData(
				suitIdTitleDto.getId(),
				suitIdTitleDto.getTitle()
		);
	}
}
