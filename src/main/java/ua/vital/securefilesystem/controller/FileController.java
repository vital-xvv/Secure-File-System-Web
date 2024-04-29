package ua.vital.securefilesystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.vital.securefilesystem.dto.file_dto.*;
import ua.vital.securefilesystem.json.CustomJsonParser;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.repository.FileRepository;
import ua.vital.securefilesystem.service.FileService;


import java.io.IOException;
import java.util.Map;

@Slf4j
@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final CustomJsonParser jsonParser;

    @PostMapping
    public File uploadFile(@Valid @RequestBody UploadFileDTO file){
        return fileService.createFile(file);
    }

    @GetMapping("/{id}")
    public File getFileById(@PathVariable(name = "id") Integer id){
        return fileRepository.findById(id).orElse(File.builder().build());
    }

    @DeleteMapping("/{id}")
    public void deleteFileById(@PathVariable(name = "id") Integer id){
        fileRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public File updateFileById(@PathVariable(name = "id") Integer id,
                               @Valid @RequestBody UploadFileDTO fileDTO){
        return fileService.updateFileByID(id, fileDTO);
    }

    @PostMapping("/_list")
    public Map<String, ?> findFilesWithFilterAndPagination(
            @Valid @RequestBody PaginationFilterFileDTO dto){
        return fileService.getPaginatedAndFilteredFiles(dto);
    }

    @PostMapping("/_report")
    public ResponseEntity<?> getCSVFileReportWithFilter(
            @Valid @RequestBody FileFilterDTO dto) throws IOException {
        String fileName = "files_report.csv";
        InputStreamResource data = fileService.exportCSVReport(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(data);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadJsonWithFileEntities(
            @RequestParam("file") MultipartFile file)
            throws IOException {
        FileParseResultDTO results = jsonParser.parseFileEfficient(file.getInputStream());
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

}
