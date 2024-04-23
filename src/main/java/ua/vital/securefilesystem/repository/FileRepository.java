package ua.vital.securefilesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.vital.securefilesystem.model.File;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer>, JpaSpecificationExecutor<File> {
    Optional<File> findById(int id);
}
