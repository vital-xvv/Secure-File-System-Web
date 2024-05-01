package ua.vital.securefilesystem.utils;

import ua.vital.securefilesystem.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTestsUtils {
    public static void assertUserMatches(User user, int id, String firstName, String lastName,
                                         String email, String phoneNumber, int age){
        assertEquals(id, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(age, user.getAge());
    }
}
