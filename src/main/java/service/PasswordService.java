package service;

import exception.ApplicationException;
import exception.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import static utils.Helpers.throwAndLogError;


@Service
public class PasswordService {
	private final Logger logger = LogManager.getLogger(PasswordService.class);

	@Autowired
	private UserRepository userRepository;

	private final int MIN_PASSWORD_LENGTH = 8;
	private final int MAX_PASSWORD_LENGTH = 50;
	private final Pattern UPPER_CASE_PATTERN = java.util.regex.Pattern.compile(".*[A-Z].*");
	private final Pattern LOWER_CASE_PATTERN = Pattern.compile(".*[a-z].*");
	private final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
	private final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[^a-zA-Z0-9].*");
	private final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
	private final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String digits = "0123456789";
	private final String specialCharacters = "!@#$%^&*()_-+=<>?";
	private final int passwordLength = 14;

	public void validatePassword(String password) {
		if (password == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_NULL));
		}

		if (password.length() < MIN_PASSWORD_LENGTH) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_TOO_SHORT));
		}

		if (password.length() > MAX_PASSWORD_LENGTH) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_TOO_LONG));
		}

		if (!UPPER_CASE_PATTERN.matcher(password).find()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_NO_UPPERCASE));
		}

		if (!LOWER_CASE_PATTERN.matcher(password).find()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_NO_LOWERCASE));
		}

		if (!DIGIT_PATTERN.matcher(password).find()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_NO_DIGIT));
		}

		if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_NO_SPECIAL_CHAR));
		}

		logger.info("Password successfully validated.");
	}

	public String generateRandomPassword() {
		String allCharacters = lowerCase + upperCase + digits + specialCharacters;
		StringBuilder password = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();

		password.append(lowerCase.charAt(secureRandom.nextInt(lowerCase.length())));
		password.append(upperCase.charAt(secureRandom.nextInt(upperCase.length())));
		password.append(digits.charAt(secureRandom.nextInt(digits.length())));
		password.append(specialCharacters.charAt(secureRandom.nextInt(specialCharacters.length())));

		for (int i = 4; i < passwordLength; i++) {
			int randomIndex = secureRandom.nextInt(allCharacters.length());
			password.append(allCharacters.charAt(randomIndex));
		}
		return password.toString();
	}
}





















