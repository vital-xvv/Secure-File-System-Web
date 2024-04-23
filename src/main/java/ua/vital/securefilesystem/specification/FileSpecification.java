package ua.vital.securefilesystem.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.vital.securefilesystem.model.File;

public class FileSpecification {
    public static Specification<File> hasOwnerId(int ownerId){
        return (
                Root<File> root,
                CriteriaQuery<?> query,
                CriteriaBuilder builder
        ) -> builder.equal(root.get("owner").get("id"), ownerId);

    }

//    public static Specification<File> hasLanguage(File.Language language){
//        return (
//                Root<File> root,
//                CriteriaQuery<?> query,
//                CriteriaBuilder builder
//        ) -> builder.isTrue(builder.literal(root.get("languages").s));
//    }



    public static Specification<File> hasExtension(String extension){
        return (
                Root<File> root,
                CriteriaQuery<?> query,
                CriteriaBuilder builder
        ) -> builder.equal(root.get("extension"), extension);

    }
}
