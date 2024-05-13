package ua.vital.securefilesystem.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import ua.vital.securefilesystem.dto.file_dto.*;
import ua.vital.securefilesystem.model.File;

import java.io.IOException;

public interface FileService {
    PagedAndFilteredFilesDTO getPaginatedAndFilteredFiles(PaginationFilterFileDTO fileDTO);

    File createFile(UploadFileDTO file);

    ResponseEntity<?> updateFileByID(Integer id, UploadFileDTO fileDTO);

    File findFileById(Integer id);

    ResponseEntity<?> deleteFileById(Integer id);

    InputStreamResource exportCSVReport(FileFilterDTO dto) throws IOException;
}
