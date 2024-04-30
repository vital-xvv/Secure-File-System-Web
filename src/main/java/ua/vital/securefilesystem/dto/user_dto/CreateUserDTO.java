package ua.vital.securefilesystem.dto.user_dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;
import lombok.Data;
import ua.vital.securefilesystem.model.User;

@Data
public class CreateUserDTO {
    @NotBlank
    @JsonAlias("first_name")
    private String firstName;
    @NotBlank
    @JsonAlias("last_name")
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotBlank
    @JsonAlias("phone_number")
    @Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")
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
