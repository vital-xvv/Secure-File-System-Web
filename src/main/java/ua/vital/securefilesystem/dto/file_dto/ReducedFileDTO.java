package ua.vital.securefilesystem.dto.file_dto;

import lombok.Builder;
import lombok.Data;
import ua.vital.securefilesystem.dto.user_dto.ReducedUserDTO;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.model.Language;
import ua.vital.securefilesystem.model.User;

import java.util.List;

@Data
@Builder
public class ReducedFileDTO {
    private Integer id;
    private String fileName;
    private Integer size;
    private String extension;
    private List<Language> languages;
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

    public File toFile(){
        return File.builder()
                .id(getId())
                .fileName(getFileName())
                .extension(getExtension())
                .size(getSize())
                .owner(User.builder().id(getOwner().getId()).build())
                .languages(getLanguages())
                .build();
    }
}
