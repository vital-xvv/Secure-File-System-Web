package ua.vital.securefilesystem.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.vital.securefilesystem.dto.file_dto.*;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.model.HttpResponse;
import ua.vital.securefilesystem.model.User;
import ua.vital.securefilesystem.repository.FileRepository;
import ua.vital.securefilesystem.repository.UserRepository;
import ua.vital.securefilesystem.service.FileService;
import ua.vital.securefilesystem.service.UserService;
import ua.vital.securefilesystem.specification.FileSpecificationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Override
    public File createFile(UploadFileDTO file) {
        return fileRepository.save(
                File.builder()
                    .fileName(file.getFileName())
                    .extension(file.getExtension())
                    .size(file.getSize())
                    .owner(User.builder().id(file.getOwnerId()).build())
                    .languages(file.getLanguages())
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build());
    }

    @Override
    public ResponseEntity<?> updateFileByID(Integer id, UploadFileDTO fileDTO) {
        boolean userExists = userRepository.existsById(fileDTO.getOwnerId());
        if(userExists) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ReducedFileDTO.fromFile(fileRepository.save(
                    File.builder()
                            .id(id)
                            .fileName(fileDTO.getFileName())
                            .createdAt(
                                    fileRepository.findById(id).orElse(
                                            File.builder()
                                                    .createdAt(LocalDateTime.now())
                                                    .build()).getCreatedAt())
                            .modifiedAt(LocalDateTime.now())
                            .owner(User.builder().id(fileDTO.getOwnerId()).build())
                            .extension(fileDTO.getExtension())
                            .size(fileDTO.getSize())
                            .languages(fileDTO.getLanguages())
                            .build())));
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .message("User with id %d does not exist".formatted(fileDTO.getOwnerId()))
                        .httpStatus(HttpStatus.BAD_REQUEST.name())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());

    }

    @Override
    public File findFileById(Integer id) {
        return fileRepository.findById(id).orElse(File.builder().build());
    }

    @Override
    public ResponseEntity<?> deleteFileById(Integer id) {
        boolean fileExists = fileRepository.existsById(id);
        if(fileExists) {
            fileRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError()
                    .body(HttpResponse.builder()
                            .message("File with id: %d does not exist".formatted(id))
                            .timestamp(LocalDateTime.now())
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.name())
                            .build());
        }
    }

    @Override
    @Transactional
    public InputStreamResource exportCSVReport(FileFilterDTO fileDTO) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT);
        csvPrinter.printRecord("Id", "Filename", "Size", "Extension",
                "Languages", "Created_At", "Modified_At", "Owner_Id");

        Specification<File> spec = FileSpecificationUtils.getFileSpecificationByFilterProps(fileDTO);

        Slice<File> slice = fileRepository.findAll(spec, PageRequest.of(0, 100));
        List<File> filesInBatch = slice.getContent();
        filesInBatch.forEach(file -> printRecordInCSV(csvPrinter, file, formatter));

        while(slice.hasNext()) {
            slice = fileRepository.findAll(spec, slice.nextPageable());
            slice.get().forEach(file -> printRecordInCSV(csvPrinter, file, formatter));
        }

        csvPrinter.flush();

        return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    @Override
    @Transactional
    public PagedAndFilteredFilesDTO getPaginatedAndFilteredFiles(PaginationFilterFileDTO fileDTO) {
        //Defining Pageable object
        Pageable pageable = PageRequest.of(fileDTO.getPage(), fileDTO.getSize());

        //Building Specification for filtered File query
        Specification<File> spec = FileSpecificationUtils.getFileSpecificationByFilterProps(fileDTO);

        //Result data set
        Page<File> page = fileRepository.findAll(spec, pageable);

        //Creating DTOs, reducing full File objects
        List<ReducedFileDTO> reducedFiles =
                page.getContent()
                    .stream()
                    .map(ReducedFileDTO::fromFile)
                    .toList();

        return new PagedAndFilteredFilesDTO(reducedFiles, page.getTotalPages(), page.getTotalElements());
    }

    private void printRecordInCSV(CSVPrinter csvPrinter, File file, DateTimeFormatter formatter) {
        try{
            csvPrinter.printRecord(
                    file.getId(),
                    file.getFileName(),
                    file.getSize(),
                    file.getExtension(),
                    file.getLanguages(),
                    file.getCreatedAt().format(formatter),
                    file.getModifiedAt().format(formatter),
                    file.getOwner().getId());
        }catch (IOException io) {
            log.error("Error occurred while printing CSV record for file: "
                    + file.getFileName() + "\n" + io.getMessage());
        }
    }
}
