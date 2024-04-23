package ua.vital.securefilesystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.vital.securefilesystem.dto.file_dto.FileFilterDTO;
import ua.vital.securefilesystem.dto.file_dto.PaginationFilterFileDTO;
import ua.vital.securefilesystem.dto.file_dto.UploadFileDTO;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.repository.FileRepository;
import ua.vital.securefilesystem.service.FileService;

import java.util.Map;

@Slf4j
@RequestMapping("/api/file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final FileService fileService;

    @PostMapping
    public File uploadFile(@Valid @RequestBody UploadFileDTO file){
        return fileService.createFile(file);
    }

    @GetMapping("/{id}")
    public File getFileById(@PathVariable(name = "id") @Positive Integer id){
        return fileRepository.findById(id).orElse(File.builder().build());
    }

    @DeleteMapping("/{id}")
    public void deleteFileById(@PathVariable(name = "id") @Positive Integer id){
        fileRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public File updateFileById(@PathVariable(name = "id") @Positive Integer id,
                               @Valid @RequestBody UploadFileDTO fileDTO){
        return fileService.updateFileByID(id, fileDTO);
    }

    @PostMapping("/_list")
    public Map<String, ?> findFilesWithFilterAndPagination(
            @Valid @RequestBody PaginationFilterFileDTO dto){
        return fileService.getPaginatedAndFilteredFiles(dto);
    }

    @PostMapping("/_report")
    public ResponseEntity<?> getFileReportWithFilter(
            @RequestBody(required = false) FileFilterDTO dto){
        byte[] fileBytes = "a,b".getBytes();
        String fileName = "test.csv";
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileBytes);
    }

}
