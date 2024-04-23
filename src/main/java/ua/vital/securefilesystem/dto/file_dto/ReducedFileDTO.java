package ua.vital.securefilesystem.dto.file_dto;

import lombok.Builder;
import lombok.Data;
import ua.vital.securefilesystem.dto.user_dto.ReducedUserDTO;
import ua.vital.securefilesystem.model.File;

import java.util.List;

@Data
@Builder
public class ReducedFileDTO {
    private Integer id;
    private String fileName;
    private Integer size;
    private String extension;
    private List<File.Language> languages;
    private ReducedUserDTO owner;

    public static ReducedFileDTO fromFile(File file){
        return ReducedFileDTO.builder()
                    .id(file.getId())
                    .fileName(file.getFileName())
                    .size(file.getSize())
                .languages(file.getLanguages())
                .extension(file.getExtension())
                .owner(ReducedUserDTO.fromUser(file.getOwner()))
                .build();
    }
}
