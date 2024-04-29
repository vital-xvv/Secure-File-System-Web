package ua.vital.securefilesystem.json.implementation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ua.vital.securefilesystem.dto.file_dto.FileParseResultDTO;
import ua.vital.securefilesystem.dto.file_dto.UploadFileDTO;
import ua.vital.securefilesystem.json.CustomJsonParser;
import ua.vital.securefilesystem.model.Language;
import ua.vital.securefilesystem.service.FileService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomJsonParserImpl implements CustomJsonParser {
    private final FileService fileService;
    private final ObjectMapper mapper = new ObjectMapper();

    public CustomJsonParserImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @SuppressWarnings("deprecation")
    public FileParseResultDTO parseFileEfficient(InputStream inputStream) {
        JsonFactory factory = mapper.getFactory();
        FileParseResultDTO parseResultDTO = FileParseResultDTO.builder().build();
        try(JsonParser jsonParser = factory.createParser(inputStream)){
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                UploadFileDTO fileDTO = UploadFileDTO.builder().build();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    //get the current token
                    String fieldName = jsonParser.getCurrentName();
                    if ("file_name".equals(fieldName)) {
                        //move to next token
                        jsonParser.nextToken();
                        System.out.println(jsonParser.getText());
                        fileDTO.setFileName(jsonParser.getText());
                    }
                    if ("size".equals(fieldName)) {
                        //move to next token
                        jsonParser.nextToken();
                        fileDTO.setSize(jsonParser.getIntValue());
                    }
                    if("extension".equals(fieldName)){
                        //move to next token
                        jsonParser.nextToken();
                        fileDTO.setExtension(jsonParser.getText());
                    }
                    if("owner_id".equals(fieldName)){
                        //move to next token
                        jsonParser.nextToken();
                        fileDTO.setOwnerId(jsonParser.getIntValue());
                    }
                    if("languages".equals(fieldName)){
                        //move to next token
                        jsonParser.nextToken();
                        List<Language> languages = new ArrayList<>();
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            languages.add(Language.valueOf(jsonParser.getText()));
                        }
                        fileDTO.setLanguages(languages);
                    }
                }
                try {
                    fileService.createFile(fileDTO);
                    parseResultDTO.successIncrement();
                }catch (Exception e) {
                    e.printStackTrace();
                    parseResultDTO.failedIncrement();
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return parseResultDTO;
    }

    /**
     * Checks if a {@link java.io.File} is not a directory and has .json extension
     * @param file File obj to check
     * @return true if file corresponds to requirements
     */
    private static boolean isJsonFile(java.io.File file){
        return !file.isDirectory() && file.getName().endsWith(".json");
    }

}
