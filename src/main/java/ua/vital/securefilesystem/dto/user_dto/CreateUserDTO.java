package ua.vital.securefilesystem.dto.user_dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ua.vital.securefilesystem.model.User;

@Data
public class CreateUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotBlank
    private String phoneNumber;
    @Positive
    @Max(90)
    @Min(18)
    private int age;

    public User toUser() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .age(age)
                .build();
    }
}
