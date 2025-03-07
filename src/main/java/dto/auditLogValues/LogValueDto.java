package dto.auditLogValues;


import entity.AuditLogValuesData;

public record LogValueDto(String attribute,
						  String previousValue,
						  String newValue) {

	public static LogValueDto toDto(AuditLogValuesData value) {
		return new LogValueDto(
				value.getAttribute(),
				value.getPreviousValue(),
				value.getNewValue()
		);
	}

}
