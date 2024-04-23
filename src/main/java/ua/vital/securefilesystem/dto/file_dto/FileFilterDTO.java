package ua.vital.securefilesystem.dto.file_dto;

import jakarta.validation.constraints.Positive;
import ua.vital.securefilesystem.model.File;

import java.util.List;

public class FileFilterDTO {
    //Filter properties
    @Positive
    private Integer ownerId;
    private List<File.Language> languageList;
    private String extension;
}
