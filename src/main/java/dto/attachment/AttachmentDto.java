package dto.attachment;

import entity.AttachmentData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttachmentDto {

	private Long id;
	private byte[] data;
	private Long typeId;
	private AttachmentState attachmentState;
	private String name;

	public AttachmentDto(Long id, byte[] data, Long typeId, String name) {
		this.id = id;
		this.data = data;
		this.typeId = typeId;
		this.name = name;
	}

	public static AttachmentDto toDto(AttachmentData attachment) {
		return new AttachmentDto(
				attachment.getId(),
				attachment.getData(),
				attachment.getTypeId(),
				attachment.getName()
		);
	}

	public static AttachmentData toEntity(AttachmentDto attachmentDto) {
		return new AttachmentData(
				attachmentDto.getId(),
				attachmentDto.getData(),
				attachmentDto.getTypeId(),
				attachmentDto.getName()
		);
	}
}
