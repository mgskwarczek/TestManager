package exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	OBJECT_NOT_FOUND("Object {0} with id {1} not found", HttpStatus.NOT_FOUND),
	NULL_VALUE_FORBIDDEN("{0} from {1} can not be null", HttpStatus.BAD_REQUEST),
	TOO_LONG_VALUE("{0} with length {1} is too long. Max length = {2}", HttpStatus.BAD_REQUEST),
	INVALID_VALUE("Value {0} is invalid", HttpStatus.BAD_REQUEST),
	SPECIAL_SIGNS_OR_WHITE_SPACES_FOUND("Special signs or whitespaces were found in table name {0}", HttpStatus.BAD_REQUEST),
	TOO_LONG_STRING_FOUND("String length is too long: {0} has length of {1}", HttpStatus.BAD_REQUEST),
	EMPTY_VALUE_FORBIDDEN("{0} cannot be empty", HttpStatus.BAD_REQUEST),
	DEPRECATED_MODIFICATION("The modification date of the new object cannot be older than modification date of the current object", HttpStatus.BAD_REQUEST),
	NULL_ENTITY_FORBIDDEN("Entities cannot be null to be updated", HttpStatus.BAD_REQUEST),

	EMPTY_SEARCH_CRITERIA("One search criteria must be provided", HttpStatus.NOT_FOUND),
	INVALID_EMAIL_FORMAT("Email address is not valid.", HttpStatus.BAD_REQUEST),
	EMAIL_IS_TAKEN("Email address {0} is already taken.", HttpStatus.CONFLICT),
	NO_USERS_FOUND_WITH_CRITERIA("No users found with provided criteria.", HttpStatus.NOT_FOUND),
	NO_TEAMS_FOUND_WITH_CRITERIA("No teams with provided criteria.", HttpStatus.NOT_FOUND),
	USER_NOT_FOUND("User with id {0} not found.", HttpStatus.NOT_FOUND),
	TEAM_NOT_FOUND("Team with id {0} not found.", HttpStatus.NOT_FOUND),
	TEAM_NAME_IS_TAKEN("The name {0} is already taken.", HttpStatus.CONFLICT),
	FAILED_TO_CREATE_USER("Failed to create user.", HttpStatus.BAD_REQUEST),
	FAILED_TO_UPDATE_USER("Failed to update user.", HttpStatus.BAD_REQUEST),
	FAILED_TO_DELETE_USER("Failed to delete user.", HttpStatus.BAD_REQUEST),
	FAILED_TO_CREATE_TEAM("Failed to create team.", HttpStatus.BAD_REQUEST),
	FAILED_TO_UPDATE_TEAM("Failed to update team.", HttpStatus.BAD_REQUEST),
	FAILED_TO_DELETE_TEAM("Failed to delete team.", HttpStatus.BAD_REQUEST),
	FAILED_TO_ADD_USER_TO_TEAM("Failed to add user to team.", HttpStatus.BAD_REQUEST),
	FAILED_TO_REMOVE_USER_FROM_TEAM("Failed to remove user from team.", HttpStatus.BAD_REQUEST),
	USER_ALREADY_IN_TEAM("User is already in this team.", HttpStatus.BAD_REQUEST),
	USER_NOT_IN_TEAM("User does not belong to this team.", HttpStatus.BAD_REQUEST),
	PASSWORD_TOO_SHORT("Password should be longer than 8 characters.", HttpStatus.BAD_REQUEST),
	PASSWORD_TOO_LONG("Password is too long.", HttpStatus.BAD_REQUEST),
	PASSWORD_NO_UPPERCASE("Password must contain at least one uppercase letter.", HttpStatus.BAD_REQUEST),
	PASSWORD_NO_LOWERCASE("Password must contain at least one lowercase letter.", HttpStatus.BAD_REQUEST),
	PASSWORD_NO_DIGIT("Password must contain at least one digit.", HttpStatus.BAD_REQUEST),
	PASSWORD_NO_SPECIAL_CHAR("Password must contain at least one special character.", HttpStatus.BAD_REQUEST),
	PASSWORD_NULL("You must provide a password.", HttpStatus.BAD_REQUEST),
	FAILED_TO_RESET_PASSWORD("Failed to reset password.", HttpStatus.BAD_REQUEST),
	FAILED_TO_CHANGE_PASSWORD("Failed to change password.", HttpStatus.BAD_REQUEST),
	INCORRECT_PASSWORD("This password is incorrect.", HttpStatus.BAD_REQUEST),
	ROLE_NOT_FOUND("Role not found.", HttpStatus.NOT_FOUND),
	INVALID_ROLE("Role must be MEMBER OR LEADER.", HttpStatus.BAD_REQUEST),
	ADMIN_EXISTS("Admin exists.", HttpStatus.BAD_REQUEST),
	NOT_AUTHORIZED("User does not have access.", HttpStatus.FORBIDDEN),
	PASSWORD_SAME_AS_OLD("Password must be different.", HttpStatus.BAD_REQUEST),
	PASSWORD_TAKEN("The provided password is already in use. Please choose a different password.", HttpStatus.BAD_REQUEST),
	ERROR_DURING_HASHING("Error during password hashing.", HttpStatus.BAD_REQUEST),
	FAILED_SENDING_EMAIL("Failed to send password reset email.", HttpStatus.BAD_REQUEST);


	private final String message;
	private final HttpStatus httpStatus;

	ErrorCode(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
