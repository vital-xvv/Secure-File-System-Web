package ua.vital.securefilesystem.dto.user_dto;

import lombok.Builder;
import lombok.Data;
import ua.vital.securefilesystem.model.User;

@Data
@Builder
public class ReducedUserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    public static ReducedUserDTO fromUser(User owner) {
        return ReducedUserDTO.builder()
                .id(owner.getId())
                .firstName(owner.getFirstName())
                .lastName(owner.getLastName())
                .email(owner.getEmail())
                .build();
    }
}
