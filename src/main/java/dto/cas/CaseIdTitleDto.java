package dto.cas;

import entity.CaseData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaseIdTitleDto {

	private Long id;
	private String title;

	public CaseIdTitleDto(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public static CaseIdTitleDto toDto(CaseData cas) {
		return new CaseIdTitleDto(
				cas.getId(),
				cas.getTitle()
		);
	}

	public static CaseData toEntity(CaseIdTitleDto caseIdTitleDto) {
		return new CaseData(
				caseIdTitleDto.getId(),
				caseIdTitleDto.getTitle()
		);
	}
}
