package utils;


import exception.ApplicationException;
import org.apache.logging.log4j.Logger;

public class Helpers {
	public static ApplicationException throwAndLogError(Logger logger, ApplicationException appException) {
		logger.error(appException.getMessage());
		throw appException;
	}
}
