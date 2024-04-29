package ua.vital.securefilesystem.dto.file_dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ua.vital.securefilesystem.model.Language;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UploadFileDTO {
    @NotBlank(message = "FileName can not be null nor empty.")
    private String fileName;
    @NotNull(message = "Size can not be null.")
    @PositiveOrZero(message = "Size should be equal or greater than zero.")
    private Integer size;
    private String extension;
    @NotEmpty(message = "File should be written in at least one language.")
    private List<Language> languages;
    @NotNull(message = "File owner can not be null.")
    @Positive(message = "File owner id should be greater than zero.")
    private Integer ownerId;
}
