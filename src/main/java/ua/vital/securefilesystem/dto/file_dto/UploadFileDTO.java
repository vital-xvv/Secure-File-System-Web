package ua.vital.securefilesystem.dto.file_dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ua.vital.securefilesystem.model.File;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UploadFileDTO {
    @NotBlank
    private String fileName;
    @NotNull
    @PositiveOrZero
    private Integer size;
    @NotBlank
    private String extension;
    @NotEmpty
    private List<File.Language> languages;
    @NotNull
    @Positive
    private Integer ownerId;
}
