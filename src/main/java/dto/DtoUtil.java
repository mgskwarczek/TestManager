package dto;

public class DtoUtil {

	public static boolean isFieldNull(Object obj) {
		return obj == null;
	}

	public static boolean isStringNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isStringTooLong(String str, Integer length) {
		return str.length() > length;
	}
}
