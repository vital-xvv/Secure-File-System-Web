package ua.vital.securefilesystem.dto.file_dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PaginationFilterFileDTO {
    //Filter properties
    @Positive
    private Integer ownerId;
    private String extension;

    //Pagination properties
    @PositiveOrZero
    @NotNull
    private Integer page;
    @Positive
    @NotNull
    private Integer size;
}
