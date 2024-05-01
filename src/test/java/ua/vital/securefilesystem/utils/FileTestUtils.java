package ua.vital.securefilesystem.utils;

import org.apache.commons.csv.CSVRecord;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.model.Language;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FileTestUtils {

    public static void assertFileMatches(File file, String fileName, int size, String extension,
                                          int ownerId, Language... languages) {
        assertNotNull(file);
        assertTrue(file.getId() >= 1);
        assertEquals(fileName, file.getFileName());
        assertEquals(ownerId, file.getOwner().getId());
        assertEquals(size, file.getSize());
        assertEquals(extension, file.getExtension());
        assertHasLanguages(file, languages);
    }


    public static void assertHasLanguages(File file, Language ... languages){
        Arrays.stream(languages).forEach(l ->
                assertTrue(file.getLanguages().contains(l))
        );
    }

    public static void assertFileCsvRecordMatches(CSVRecord record, int id, String fileName, int size, String extension,
                                                  int ownerId, Language... languages) {
        assertEquals(Integer.parseInt(record.get(0)), id);
        assertEquals(record.get(1), fileName);
        assertEquals(Integer.parseInt(record.get(2)), size);
        assertEquals(record.get(3), extension);
        Arrays.stream(languages).forEach(l ->
                assertTrue(record.get(4).contains(l.name())));
        assertEquals(Integer.parseInt(record.get(7)), ownerId);

    }

}
