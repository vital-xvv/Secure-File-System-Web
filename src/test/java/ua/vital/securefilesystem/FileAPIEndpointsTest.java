package ua.vital.securefilesystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import ua.vital.securefilesystem.dto.file_dto.PagedAndFilteredFilesDTO;
import ua.vital.securefilesystem.model.File;
import ua.vital.securefilesystem.repository.FileRepository;
import ua.vital.securefilesystem.service.FileService;

import java.io.FileInputStream;
import java.util.List;

import static ua.vital.securefilesystem.FileTestUtils.*;
import static ua.vital.securefilesystem.model.Language.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SecureFileSystemWebApplication.class
)
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileAPIEndpointsTest {

    private final MockMvc mvc;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final ObjectMapper mapper;

    private final String contextPath = "/file";

    public FileAPIEndpointsTest(
            MockMvc mvc,
            FileService fileService,
            FileRepository fileRepository,
            ObjectMapper mapper) {
        this.mvc = mvc;
        this.fileService = fileService;
        this.fileRepository = fileRepository;
        this.mapper = mapper;
    }


    /*
        POST - /api/file/upload
    */
    @Test
    @Order(1)
    public void fileUploadInBatchesTest() throws Exception {

        //given
        java.io.File testFile = new java.io.File("src/test/resources/dataset/test-files.json");

        MockMultipartFile dataSet = new MockMultipartFile("file", "test-upload.json",
                "application/json", new FileInputStream(testFile));

        MockMultipartHttpServletRequestBuilder builder = multipart(contextPath + "/upload");
        builder.with(request -> {
            request.setMethod("POST");
            return request;
        });


        //when
        mvc.perform(builder.file(dataSet))


        //then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(20))
        .andExpect(jsonPath("$.failed").value(0));

        assertEquals(20, fileRepository.count());

        File file = fileRepository.findById(20).orElse(null);

        assertFileMatches(file, "customer_survey_form.xlsx",
                29876, "xlsx", 5, ENGLISH, FRENCH);
    }


    /*
        POST - /api/file
        {...}
    */
    @Test
    @Order(2)
    public void creatingNewFileTest() throws Exception {
        //given
        String jsonBody =
                    """
                            {
                                "fileName": "my_file.json",
                                "size": 10000,
                                "extension": "json",
                                "languages": [
                                    "DUTCH"
                                ],
                                "ownerId": 10
                            }
                    """;

        //when
        MvcResult result = mvc.perform(post(contextPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody.getBytes()))
                .andExpect(status().isCreated())
                .andReturn();
        File createdFile = mapper.readValue(result.getResponse().getContentAsString(), File.class);

        //then
        assertEquals(21, fileRepository.count());
        assertFileMatches(createdFile, "my_file.json",
                10000, "json", 10, DUTCH);
    }


    /*
        GET - /api/file/{id}
    */
    @Test
    public void findFileByIdTest() throws Exception {
        //given
        int fileId = 1;

        //when
        MvcResult result = mvc.perform(get(contextPath + "/" + fileId))
                .andExpect(status().isOk())
                .andReturn();

        //then
        File file = mapper.readValue(result.getResponse().getContentAsString(), File.class);
        assertFileMatches(file, "user_feedback.docx",
                28765, "docx", 1, ENGLISH, FRENCH);
    }


    /*
        PUT - /api/file/{id}
        {...}
    */
    @Test
    public void updateFileByIdTest() throws Exception {
        //given
        int idToUpdate = 2;
        String jsonBody = """
                {
                    "fileName": "my_file_updated_with_put.json",
                    "size": 99999,
                    "extension": "json",
                    "languages": [
                        "UKRAINIAN", "DUTCH"
                    ],
                    "ownerId": 10
                }
                """;

        //when
        MvcResult result = mvc.perform(put(contextPath + "/" + idToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andReturn();
        File updatedFile = mapper.readValue(result.getResponse().getContentAsString(), File.class);

        //then
        assertFileMatches(updatedFile, "my_file_updated_with_put.json",
                99999, "json", 10, DUTCH, UKRAINIAN);
    }


    /*
        DELETE - /api/file/{id}
    */
    @Test
    public void deleteFileByIdTest() throws Exception {
        //given
        int idToDelete = 21;

        //when
        mvc.perform(delete(contextPath + "/" + idToDelete)).andExpect(status().isOk());
        File file = fileRepository.findById(idToDelete).orElse(null);

        //then
        assertEquals(20, fileRepository.count());
        assertNull(file);
    }


    /*
        POST - /api/file/_list
        {...}
    */
    @Test
    public void getFilteredPageOfFilesTest() throws Exception {
        //given
        String jsonBody = """
                {
                    "ownerId": 3,
                    "extension": "pdf",
                    "page": 0,
                    "size": 10
                }
                """;

        //when
        MvcResult result = mvc.perform(post(contextPath + "/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                        .andExpect(status().isOk())
                        .andReturn();

        PagedAndFilteredFilesDTO details = mapper.readValue(result.getResponse().getContentAsString(),
                PagedAndFilteredFilesDTO.class);

        //then
        assertEquals(1, details.totalElements());
        assertEquals(1, details.totalPages());
        assertEquals(1, details.list().size());
        assertFileMatches(details.list().get(0).toFile(), "marketing_campaign.pdf",
                29987, "pdf", 3, FRENCH, ITALIAN);
    }


    /*
        POST /api/file/_report
        {...}
    */
    @Test
    public void getCSVReportTest() throws Exception {
        //given
        String jsonBody =
                """
                {
                    "extension": "pdf"
                }
                """;

        //when
        MvcResult result = mvc.perform(post(contextPath + "/_report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andReturn();

        CSVParser parser = CSVParser.parse(result.getResponse().getContentAsString(), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        //then
        assertFileCsvRecordMatches(records.get(1), 3, "marketing_campaign.pdf",
                29987, "pdf", 3, ITALIAN, FRENCH);
        assertFileCsvRecordMatches(records.get(2), 7 , "project_proposal.pdf",
                39876, "pdf", 7, ENGLISH, FRENCH);
        assertFileCsvRecordMatches(records.get(3), 10, "product_catalog.pdf",
                35678, "pdf", 10, ITALIAN, FRENCH);
        assertFileCsvRecordMatches(records.get(4), 14, "financial_report.pdf",
                39987, "pdf", 14, SPANISH, PORTUGUESE);
    }

}
