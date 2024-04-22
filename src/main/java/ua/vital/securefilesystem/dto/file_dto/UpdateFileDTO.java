package ua.vital.securefilesystem.dto.file_dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ua.vital.securefilesystem.model.File;

import java.util.List;

@Data
public class UpdateFileDTO {
    @NotBlank
    //@Pattern(regexp = "\\w{2,}")
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
