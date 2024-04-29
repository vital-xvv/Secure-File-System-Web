package ua.vital.securefilesystem.json;

import ua.vital.securefilesystem.dto.file_dto.FileParseResultDTO;

import java.io.InputStream;

public interface CustomJsonParser {
    FileParseResultDTO parseFileEfficient(InputStream inputStream);
}
