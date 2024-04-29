package ua.vital.securefilesystem.dto.file_dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileFilterDTO {
    //Filter properties
    @Positive
    private Integer ownerId;
    private String extension;
}
