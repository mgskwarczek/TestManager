package dto.suit;

import dto.user.UserSendDto;
import entity.SuitData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SuitTitleCodeUserDto {

	private String suitTitle;
	private String code;
	private UserSendDto user;
	private Long suitId;

	private SuitTitleCodeUserDto(String suitTitle, String code, UserSendDto user, Long suitId) {
		this.suitTitle = suitTitle;
		this.code = code;
		this.user = user;
		this.suitId = suitId;
	}

	public static SuitTitleCodeUserDto toDto(SuitData suit) {
		return new SuitTitleCodeUserDto(suit.getTitle(), suit.getCode(), UserSendDto.toDto(suit.getUser()), suit.getId());
	}

	public static SuitData toEntity(SuitTitleCodeUserDto suitTitleCodeUserDTO) {
		return new SuitData(
				suitTitleCodeUserDTO.getSuitTitle(),
				suitTitleCodeUserDTO.getCode(),
				suitTitleCodeUserDTO.getUser(),
				suitTitleCodeUserDTO.getSuitId()
		);
	}
}
