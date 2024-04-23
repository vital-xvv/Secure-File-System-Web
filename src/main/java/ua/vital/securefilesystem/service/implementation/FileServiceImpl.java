package ua.vital.securefilesystem.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.vital.securefilesystem.dto.file_dto.PaginationFilterFileDTO;
import ua.vital.securefilesystem.dto.file_dto.ReducedFileDTO;
import ua.vital.securefilesystem.dto.file_dto.UploadFileDTO;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.model.User;
import ua.vital.securefilesystem.repository.FileRepository;
import ua.vital.securefilesystem.service.FileService;
import ua.vital.securefilesystem.specification.FileSpecification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

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
    public File updateFileByID(Integer id, UploadFileDTO fileDTO) {
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
                        .build());
    }

    @Override
    public Map<String, ?> getPaginatedAndFilteredFiles(PaginationFilterFileDTO fileDTO) {
        //Defining Pageable object
        Pageable pageable = PageRequest.of(fileDTO.getPage(), fileDTO.getSize());


        //Defining Specification for filtering
        List<Specification<File>> fileSpecifications = new ArrayList<>();
        if(fileDTO.getOwnerId() != null) {
            fileSpecifications.add(FileSpecification.hasOwnerId(fileDTO.getOwnerId()));
        }
        if(fileDTO.getExtension() != null && !fileDTO.getExtension().isBlank()) {
            fileSpecifications.add(FileSpecification.hasExtension(fileDTO.getExtension()));
        }
//        if(fileDTO.getLanguageList() != null && !fileDTO.getLanguageList().isEmpty()) {
//            fileDTO.getLanguageList().forEach(item ->
//                fileSpecifications.add(FileSpecification.hasLanguage(item))
//            );
//        }
        Specification<File> spec = Specification.allOf(fileSpecifications);

        //Response
        Page<File> page = fileRepository.findAll(spec, pageable);
        //Creating DTOs, reducing full File objects
        List<ReducedFileDTO> reducedFiles = page.getContent().stream().map(ReducedFileDTO::fromFile).toList();

        return Map.of("list", reducedFiles, "totalPages", page.getTotalPages(), "totalElements", page.getTotalElements());
    }
}
