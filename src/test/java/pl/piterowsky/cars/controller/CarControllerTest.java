package pl.piterowsky.cars.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import pl.piterowsky.cars.file.ContentType;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class UploadFileTest {

        @Test
        void uploadFile_whenCsvFileGiven_responseWithNumberFilteredAndUploadedCars() throws Exception {
            var content = Files.readAllBytes(Paths.get("src/test/resources/cars.csv"));
            var request = multipart(CarController.UPLOAD_CARS_URL)
                    .file(new MockMultipartFile("file", "cars.csv", ContentType.TEXT_CSV, content));

            mockMvc
                    .perform(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.countOfSavedCars", is(5)));
        }

        @Test
        void uploadFile_whenNoColorGiven_responseWithAllCarsFromFile() throws Exception {
            var content = Files.readAllBytes(Paths.get("src/test/resources/cars.csv"));
            var request = multipart(CarController.UPLOAD_CARS_FILTERED_BY_COLOR_URL, "Biały")
                    .file(new MockMultipartFile("file", "cars.csv", ContentType.TEXT_CSV, content));

            mockMvc
                    .perform(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.countOfSavedCars", is(2)));
        }

        @Test
        void uploadFile_whenUnsupportedContentType_responseErrorMessage() throws Exception {
            var unsupportedContentType = "UNSUPPORTED_CONTENT_TYPE";

            var content = Files.readAllBytes(Paths.get("src/test/resources/cars.csv"));
            var request = multipart(CarController.UPLOAD_CARS_FILTERED_BY_COLOR_URL, "Biały")
                    .file(new MockMultipartFile("file", "cars.csv", unsupportedContentType, content));

            mockMvc
                    .perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("Unsupported file content-type")))
                    .andExpect(jsonPath("$.countOfSavedCars", is(0)));
        }

        @Test
        void uploadFile_whenCannotParseDate_responseErrorMessage() throws Exception {
            var content = Files.readAllBytes(Paths.get("src/test/resources/cars.csv"));
            var contentWithInvalidRow = concatenateByteArrays(content, "6, Test, 12/12/1996, Red".getBytes());
            var request = multipart(CarController.UPLOAD_CARS_FILTERED_BY_COLOR_URL, "Biały")
                    .file(new MockMultipartFile("file", "cars.csv", ContentType.TEXT_CSV, contentWithInvalidRow));

            mockMvc
                    .perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("Cannot parse date: 12/12/1996")))
                    .andExpect(jsonPath("$.countOfSavedCars", is(0)));
        }

        private byte[] concatenateByteArrays(byte[] arr1, byte[] arr2) {
            byte[] destination = new byte[arr1.length + arr2.length];
            System.arraycopy(arr1, 0, destination, 0, arr1.length);
            System.arraycopy(arr2, 0, destination, arr1.length, arr2.length);
            return destination;
        }
    }

}