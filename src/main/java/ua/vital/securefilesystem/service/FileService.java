package ua.vital.securefilesystem.service;

import org.springframework.core.io.InputStreamResource;
import ua.vital.securefilesystem.dto.file_dto.FileFilterDTO;
import ua.vital.securefilesystem.dto.file_dto.PaginationFilterFileDTO;
import ua.vital.securefilesystem.dto.file_dto.UploadFileDTO;
import ua.vital.securefilesystem.model.File;

import java.io.IOException;
import java.util.Map;

public interface FileService {
    Map<String, ?> getPaginatedAndFilteredFiles(PaginationFilterFileDTO fileDTO);

    File createFile(UploadFileDTO file);

    File updateFileByID(Integer id, UploadFileDTO fileDTO);

    InputStreamResource exportCSVReport(FileFilterDTO dto) throws IOException;
}