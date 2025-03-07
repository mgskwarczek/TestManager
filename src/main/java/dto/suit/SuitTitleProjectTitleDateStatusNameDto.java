package dto.suit;

import entity.SuitData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SuitTitleProjectTitleDateStatusNameDto {

	private String suitTitle;
	private String projectTitle;
	private LocalDateTime modificationDate;
	private String statusName;
	private Long suitId;

	private SuitTitleProjectTitleDateStatusNameDto(String suitTitle, String projectTitle, LocalDateTime modificationDate, String statusName, Long suitId) {
		this.suitTitle = suitTitle;
		this.projectTitle = projectTitle;
		this.modificationDate = modificationDate;
		this.statusName = statusName;
		this.suitId = suitId;
	}

	public static SuitTitleProjectTitleDateStatusNameDto toDto(SuitData suit) {
		return new SuitTitleProjectTitleDateStatusNameDto(
				suit.getTitle(),
				suit.getProject().getTitle(),
				suit.getModificationDate(),
				suit.getStatus().getName(),
				suit.getId()
		);
	}
}
