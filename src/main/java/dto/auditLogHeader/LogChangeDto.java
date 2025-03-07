package dto.auditLogHeader;

import dto.auditLogValues.LogValueDto;
import dto.user.UserSendDto;
import entity.AuditLogHeaderData;
import entity.UserData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;



public record LogChangeDto(Long objectId,
						   LocalDateTime changeDate,
						   Long changedObjectId,
						   UserSendDto user,
						   List<LogValueDto> changeValues) {

	public static LogChangeDto toDto(AuditLogHeaderData auditLogHeader) {
		UserData user = auditLogHeader.getUser();
		return new LogChangeDto(
				auditLogHeader.getId(),
				auditLogHeader.getCreationDate(),
				auditLogHeader.getRecordPK(),
				UserSendDto.toDto(user),
				auditLogHeader.getValues()
						.stream()
						.map(LogValueDto::toDto)
						.collect(Collectors.toList())
		);
	}
}

