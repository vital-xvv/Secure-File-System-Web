package ua.vital.securefilesystem.dto.file_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileParseResultDTO {
    private int success;
    private int failed;

    public void successIncrement(){
        success += 1;
    }

    public void failedIncrement(){
        failed += 1;
    }
}
