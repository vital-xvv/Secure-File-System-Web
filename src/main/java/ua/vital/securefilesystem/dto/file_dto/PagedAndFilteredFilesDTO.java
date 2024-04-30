package ua.vital.securefilesystem.dto.file_dto;


import java.util.List;

public record PagedAndFilteredFilesDTO(List<ReducedFileDTO> list, Integer totalPages, Long totalElements) {
}
