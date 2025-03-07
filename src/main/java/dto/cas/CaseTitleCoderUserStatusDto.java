package dto.cas;

import dto.user.UserDto;
import entity.CaseData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaseTitleCoderUserStatusDto {

	private String title;
	private String code;
	private UserDto user;
	private String statusName;
	private Long caseId;

	public CaseTitleCoderUserStatusDto(String title, String code, UserDto user, String statusName, Long caseId) {
		this.title = title;
		this.code = code;
		this.user = user;
		this.statusName = statusName;
		this.caseId = caseId;
	}

	public static CaseTitleCoderUserStatusDto toDto(CaseData cas) {
		return new CaseTitleCoderUserStatusDto(
				cas.getTitle(),
				cas.getCode(),
				UserDto.toDto(cas.getUser()),
				cas.getStatus().getName(),
				cas.getId()
		);
	}
}
