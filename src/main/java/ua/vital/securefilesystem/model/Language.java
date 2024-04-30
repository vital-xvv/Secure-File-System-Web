package ua.vital.securefilesystem.model;

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
}
