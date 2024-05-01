package ua.vital.securefilesystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.vital.securefilesystem.model.User;
import ua.vital.securefilesystem.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.vital.securefilesystem.utils.UserTestsUtils.*;


@SpringBootTest(
        classes = SecureFileSystemWebApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAPIEndpointsTest {
    private final String contextPath = "/user";
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private final UserRepository repository;

    public UserAPIEndpointsTest(
                                ObjectMapper mapper,
                                MockMvc mvc,
                                UserRepository repository) {
        this.mapper = mapper;
        this.mvc = mvc;
        this.repository = repository;
    }


    /*
        GET - /api/user
    */
    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "1, John, Doe, johndoe@gmail.com, 123-456-7890, 30",
            "2, Jane, Smith, janesmith@yahoo.com, 987-654-3210, 25",
            "3, Michael, Johnson, michaeljohnson@outlook.com, 555-123-4567, 40",
            "4, Emily, Williams, emilywilliams@hotmail.com, 444-789-1230, 35",
            "5, William, Brown, williambrown@aol.com, 321-654-9870, 28",
            "6, Sophia, Jones, sophiajones@protonmail.com, 666-777-8888, 32",
            "7, Matthew, Davis, matthewdavis@icloud.com, 222-333-4444, 45",
            "8, Emma, Miller, emmamiller@live.com, 777-888-9999, 22",
            "9, Alexander, Anderson, alexanderanderson@mail.com, 111-222-3333, 38",
            "10, Olivia, Wilson, oliviawilson@inbox.com, 999-888-7777, 29",
            "11, Ethan, Martin, ethanmartin@me.com, 333-444-5555, 31",
            "12, Sophia, Thompson, sophiathompson@yandex.com, 444-555-6666, 27",
            "13, Liam, Garcia, liamgarcia@fastmail.com, 555-666-7777, 33",
            "14, Isabella, Martinez, isabellamartinez@zoho.com, 666-767-8888, 26",
            "15, Mason, Robinson, masonrobinson@tutanota.com, 777-889-9999, 34"
    })
    public void getListOfUsersTest(int id, String firstName, String lastName,
                                   String email, String phoneNumber, int age) throws Exception {
        //when
        MvcResult result = mvc.perform(get(contextPath))
                .andExpect(status().isOk())
                .andReturn();

        List<User> users = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>(){});

        //then
        assertUserMatches(users.get(id-1), id, firstName, lastName, email, phoneNumber, age);
    }


    /*
        POST - /api/user
        {...}
    */
    @Test
    @Order(2)
    public void createUserWithDuplicateEmailTest() throws Exception {
        //given
        String jsonBody =
                """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "johndoe@gmail.com",
                        "phoneNumber": "123-456-7890",
                        "age": 30
                    }
                """;

        //when
        mvc.perform(post(contextPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
        //then
                .andExpect(status().isInternalServerError()).andReturn();

    }


    /*
        POST - /api/user
        {...}
    */
    @Test
    @Order(3)
    public void createUserWithDuplicatePhoneNumberTest() throws Exception {
        //given
        String jsonBody =
                """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "unique_email_xxx@gmail.com",
                        "phoneNumber": "123-456-7890",
                        "age": 30
                    }
                """;

        //when
        mvc.perform(post(contextPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
        //then
                .andExpect(status().isInternalServerError()).andReturn();

    }


    /*
        POST - /api/user
        {...}
    */
    @Test
    @Order(4)
    public void createUserTest() throws Exception {
        //given
        String jsonBody =
                """
                    {
                        "firstName": "Kayle",
                        "lastName": "Storm",
                        "email": "kayle.storm@gmail.com",
                        "phoneNumber": "124-567-7865",
                        "age": 20
                    }
                """;

        //when
        MvcResult result = mvc.perform(post(contextPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser = mapper.readValue(result.getResponse().getContentAsString(), User.class);

        //then
        assertEquals(16, repository.count());
        assertUserMatches(createdUser, 18, "Kayle", "Storm",
                "kayle.storm@gmail.com", "124-567-7865", 20);
    }


    /*
        PUT - /api/user/{id}
        {...}
    */
    @Test
    @Order(5)
    public void updateUserByIdTest() throws Exception {
        //given
        int idToUpdate = 18;
        String jsonBody =
                """
                    {
                        "firstName": "Freddie",
                        "lastName": "Mercury",
                        "email": "fred.singer@gmail.com",
                        "phoneNumber": "6788799020",
                        "age": 35
                    }
                """;
        //when
        MvcResult result = mvc.perform(put(contextPath + "/{id}", idToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andReturn();

        User updatedUser = mapper.readValue(result.getResponse().getContentAsString(), User.class);

        //then
        assertEquals(16, repository.count());
        assertUserMatches(updatedUser, 18, "Freddie", "Mercury", "fred.singer@gmail.com", "6788799020", 35);
    }


    /*
        PUT - /api/user/{id}
        {...}
    */
    @Test
    @Order(6)
    public void userCreatedWhenUpdateByIdIfSuchIdDoesNotExistTest() throws Exception {
        //given
        int idToUpdate = 1000;
        String jsonBody =
                """
                    {
                        "firstName": "Andrea",
                        "lastName": "Dias",
                        "email": "dias_espanol@ex.com",
                        "phoneNumber": "1213141516",
                        "age": 45
                    }
                """;
        //when
        MvcResult result = mvc.perform(put(contextPath + "/{id}", idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andReturn();

        User updatedUser = mapper.readValue(result.getResponse().getContentAsString(), User.class);

        //then
        assertEquals(17, repository.count());
        assertUserMatches(updatedUser, 19, "Andrea", "Dias",
                "dias_espanol@ex.com", "1213141516", 45);
    }


    /*
        DELETE - /api/user/{id}
    */
    @Order(7)
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20})
    public void deleteUserByIdTest(int id) throws Exception {
        //when
        mvc.perform(delete(contextPath + "/" + id)).andExpect(status().isOk());
    }


    @Test
    @Order(8)
    public void dbIsEmptyTest(){
        assertEquals(0, repository.count());
    }
}
