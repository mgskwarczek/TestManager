package service;

import controller.UserController;
import exception.ApplicationException;
import exception.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	private final Logger logger = LogManager.getLogger(UserController.class);

	@Value("${environment.mail.username}")
	private String from;

	final String passwordResetSubject = "TestSuiteLife - Password reset email";

	public void sendPasswordResetMail(String to, String password){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(passwordResetSubject);
		String emailBody = "Password Reset\n" +
				"Hello,\n" +
				"Your new password for TestSuiteLife is: \n" +
				password + "\n" +
				"CHANGE IT IMMEDIATELY AFTER LOGGING IN.\n" +
				"Thank you!\n" +
				"TestSuiteLife Team";
		message.setText(emailBody);

		try {
			mailSender.send(message);
			logger.info("MailSender: Password reset email sent successfully to: {}", to);
		} catch (ApplicationException e) {
			logger.warn("Failed to send password reset email to: {}", to);
			logger.error("Error sending email: {}", e.getMessage());
			throw new ApplicationException(ErrorCode.FAILED_SENDING_EMAIL, e);
		}
	}
}
