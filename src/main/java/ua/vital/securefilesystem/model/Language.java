package ua.vital.securefilesystem.model;

import java.util.Arrays;
import java.util.List;

public enum Language {
    ENGLISH, UKRAINIAN, RUSSIAN, GERMAN,
    FRENCH, SPANISH, DUTCH, POLISH, JAPANESE,
    CHINESE, KOREAN, ITALIAN, PORTUGUESE, PYTHON, JAVA;

    /**
     * Converts {@link ua.vital.securefilesystem.model.Language} object into pretty string <br/>
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
     * @return Returns List<Language> {@link ua.vital.securefilesystem.model.Language}
     */
    public static List<Language> deserializeLanguages(String array) {
        return Arrays.stream(array.split(" *, *"))
                .filter(s -> !s.isBlank())
                .map(el -> ua.vital.securefilesystem.model.Language.valueOf(el.toUpperCase()))
                .toList();
    }
}
