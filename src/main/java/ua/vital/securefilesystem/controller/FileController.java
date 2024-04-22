package ua.vital.securefilesystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.vital.securefilesystem.dto.file_dto.UpdateFileDTO;
import ua.vital.securefilesystem.dto.file_dto.UploadFileDTO;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.model.User;
import ua.vital.securefilesystem.repository.FileRepository;

import java.time.LocalDateTime;

@Slf4j
@RequestMapping("/api/file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;

    @PostMapping
    public File uploadFile(@Valid @RequestBody UploadFileDTO file){
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

    @GetMapping("/{id}")
    public File getFileById(@PathVariable(name = "id") @Positive Integer id){
        return fileRepository.findById(id).orElse(File.builder().build());
    }

    @DeleteMapping("/{id}")
    public void deleteFileById(@PathVariable(name = "id")
                                   @Positive(message = "Id should be positive and greater than zero whole number.")
                                   Integer id){
        fileRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public File updateFileById(@PathVariable(name = "id") @Positive Integer id,
                               @Valid @RequestBody UpdateFileDTO fileDTO){
        return fileRepository.save(
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
                    .build()
        );
    }
}
