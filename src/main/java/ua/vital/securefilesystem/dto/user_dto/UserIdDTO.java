package ua.vital.securefilesystem.dto.user_dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserIdDTO {
    @NotNull
    @Positive
    private Integer id;
}
