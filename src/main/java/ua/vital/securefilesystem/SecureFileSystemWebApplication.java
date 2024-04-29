package ua.vital.securefilesystem;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.model.Language;

import ua.vital.securefilesystem.model.User;
import ua.vital.securefilesystem.repository.FileRepository;
import ua.vital.securefilesystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;


@SpringBootApplication
public class SecureFileSystemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureFileSystemWebApplication.class, args);
    }

    //@Bean
    public CommandLineRunner commandLineRunner(FileRepository fileRepository, UserRepository userRepository) {
        return args -> {
            IntStream.range(1,11).forEach(i -> {
                Faker faker = new Faker();
                com.github.javafaker.Name fakeName = faker.name();
                String firstName = fakeName.firstName();
                String lastName = fakeName.lastName();
                User user = User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(faker.number().numberBetween(18,90))
                        .email(String.format("%s.%s@gmail.com", firstName, lastName))
                        .phoneNumber(faker.phoneNumber().phoneNumber())
                        .build();
                userRepository.save(user);

            });

            IntStream.range(1, 21).forEach(i -> {
                Faker faker = new Faker();
                com.github.javafaker.File fakeFile = faker.file();
                File file = File.builder()
                        .fileName(new java.io.File(fakeFile.fileName()).getName())
                        .size(faker.number().numberBetween(0, 10000))
                        .extension(fakeFile.extension())
                        .languages(List.of(Language.ENGLISH, Language.FRENCH))
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .owner(userRepository.findById(faker.number().numberBetween(1, 11)).get())
                        .build();
                fileRepository.save(file);
            });
        };
    }
}
