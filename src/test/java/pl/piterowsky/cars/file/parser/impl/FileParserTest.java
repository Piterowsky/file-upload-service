package pl.piterowsky.cars.file.parser.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.FileParsingException;
import pl.piterowsky.cars.file.ContentType;
import pl.piterowsky.cars.file.parser.FileParser;
import pl.piterowsky.cars.header.CarHeader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FileParserTest {

    /**
     * Should contain all file parsers in application use and fileMocks of all types
     */
    private static Stream<Arguments> inputTestSource() throws IOException {
        var carsFilename = "cars";
        var csvFileMock = createFileMock(carsFilename, ContentType.CSV, "csv");
        var xlsFileMock = createFileMock(carsFilename, ContentType.XLS, "xls");
        var xlsxFileMock = createFileMock(carsFilename, ContentType.XLSX, "xlsx");
        return Stream.of(
                Arguments.of(new CsvFileParser(), csvFileMock),
                Arguments.of(new ExcelFileParser(), xlsFileMock),
                Arguments.of(new ExcelFileParser(), xlsxFileMock)
        );
    }

    private static MockMultipartFile createFileMock(String fileName, String contentType, String extension) throws IOException {
        var originalFilename = String.format("%s.%s", fileName, extension);
        var filePath = String.format("src/test/resources/%s.%s", fileName, extension);
        var content = Files.readAllBytes(Paths.get(filePath));
        return new MockMultipartFile(fileName, originalFilename, contentType, content);
    }

    @ParameterizedTest
    @MethodSource("inputTestSource")
    void parseMultipartFile_whenFileInputCorrect_returnMapWithCorrectValues(FileParser fileParser,
                                                                            MockMultipartFile fileMock) {
        var mapList = fileParser.parseMultipartFile(fileMock);

        assertEquals(5, mapList.size());
        assertEquals("1", mapList.get(0).get(CarHeader.ID.getHeader()));
        assertEquals("Renault Clio", mapList.get(1).get(CarHeader.NAME.getHeader()));
        assertEquals("Renault Megane", mapList.get(2).get(CarHeader.NAME.getHeader()));
        assertEquals("13.05.2015", mapList.get(3).get(CarHeader.TRANSACTION_DATE.getHeader()));
        assertEquals("Żółty", mapList.get(4).get(CarHeader.COLOR.getHeader()));
    }

    @ParameterizedTest
    @MethodSource("inputTestSource")
    void parseMultipartFile_whenIoException_throwFileParsingException(FileParser fileParser) throws IOException {
        var fileMock = mock(MultipartFile.class);
        doThrow(IOException.class).when(fileMock).getInputStream();

        assertThrows(FileParsingException.class, () -> fileParser.parseMultipartFile(fileMock));
    }

    @ParameterizedTest
    @MethodSource("inputTestSource")
    void hasValidFormat_whenFormatValid_returnTrue(FileParser fileParser, MockMultipartFile fileMock) {
        var hasValidFormat = fileParser.hasValidFormat(fileMock);

        assertTrue(hasValidFormat);
    }

    @ParameterizedTest
    @MethodSource("inputTestSource")
    void hasValidFormat_whenFormatInvalid_returnFalse(FileParser fileParser, MockMultipartFile fileMock) {
        var spiedFileMock = spy(fileMock);
        doReturn("invalid").when(spiedFileMock).getContentType();

        var hasValidFormat = fileParser.hasValidFormat(spiedFileMock);

        assertFalse(hasValidFormat);
    }

}