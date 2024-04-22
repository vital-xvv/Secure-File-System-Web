package ua.vital.securefilesystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Main entity in relation Many-to-One with {@link User}
 * @author Vitalii Huzii
 */
@Entity
@Table(name = "Files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq")
    @SequenceGenerator(name = "file_seq", allocationSize = 1, sequenceName = "file_seq")
    private Integer id;
    private String fileName;
    private int size;
    private String extension;
    private List<Language> languages;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public enum Language {
        ENGLISH, UKRAINIAN, RUSSIAN, GERMAN,
        FRENCH, SPANISH, DUTCH, POLISH, JAPANESE,
        CHINESE, KOREAN, ITALIAN, PORTUGUESE, PYTHON, JAVA;

        /**
         * Converts {@link ua.vital.securefilesystem.model.File.Language} object into pretty string <br/>
         * Example: JAVA -> Java, ENGLISH -> English
         * @return Pretty language string
         */
        public String getPrettyName() {
            String str = this.toString().toLowerCase();
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }

        /**
         * Converts a string with the specified format into list of language objects
         * @param array String of sequence of languages like "Japanese, Ukrainian, English"
         * @return Returns List<Language> {@link ua.vital.securefilesystem.model.File.Language}
         */
        public static List<ua.vital.securefilesystem.model.File.Language> deserializeLanguages(String array) {
            return Arrays.stream(array.split(" *, *"))
                    .filter(s -> !s.isBlank())
                    .map(el -> ua.vital.securefilesystem.model.File.Language.valueOf(el.toUpperCase()))
                    .toList();
        }
    }

}