package dto.user;

import entity.UserData;

public record UserSendDto(
		String firstName,
		String lastName,
		String email) {

	public static UserSendDto toDto(UserData user) {
		return new UserSendDto(
				user.getFirstName(),
				user.getLastName(),
				user.getEmail()
		);
	}

	public static UserData toEntity(UserSendDto userSendDto) {
		return new UserData(
				userSendDto.firstName(),
				userSendDto.lastName(),
				userSendDto.email()
		);
	}
}
