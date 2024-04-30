package ua.vital.securefilesystem.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ua.vital.securefilesystem.dto.file_dto.FileFilterDTO;
import ua.vital.securefilesystem.dto.file_dto.PaginationFilterFileDTO;
import ua.vital.securefilesystem.model.File;

import java.util.ArrayList;
import java.util.List;

public class FileSpecificationUtils {

    public static Specification<File> hasOwnerId(int ownerId){
        return (
                Root<File> root,
                CriteriaQuery<?> query,
                CriteriaBuilder builder
        ) -> builder.equal(root.get("owner").get("id"), ownerId);

    }

    public static Specification<File> hasExtension(String extension){
        return (
                Root<File> root,
                CriteriaQuery<?> query,
                CriteriaBuilder builder
        ) -> builder.equal(root.get("extension"), extension);

    }

    public static Specification<File> getFileSpecificationByFilterProps(FileFilterDTO fileDTO){
        //Defining Specification for filtering
        List<Specification<File>> fileSpecifications = new ArrayList<>();
        if(fileDTO.getOwnerId() != null) {
            fileSpecifications.add(FileSpecificationUtils.hasOwnerId(fileDTO.getOwnerId()));
        }

        if(fileDTO.getExtension() != null && !fileDTO.getExtension().isBlank()) {
            fileSpecifications.add(FileSpecificationUtils.hasExtension(fileDTO.getExtension()));
        }

        return Specification.allOf(fileSpecifications);
    }

    public static Specification<File> getFileSpecificationByFilterProps(PaginationFilterFileDTO fileDTO){
        return getFileSpecificationByFilterProps(
                FileFilterDTO.builder()
                    .ownerId(fileDTO.getOwnerId())
                        .extension(fileDTO.getExtension())
                        .build());
    }
}
