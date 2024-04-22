package ua.vital.securefilesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vital.securefilesystem.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
