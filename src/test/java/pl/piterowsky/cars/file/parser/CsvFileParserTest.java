package pl.piterowsky.cars.file.parser;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.FileParsingException;
import pl.piterowsky.cars.file.ContentType;
import pl.piterowsky.cars.header.CarHeader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CsvFileParserTest {

    @Spy
    private CsvFileParser csvFileParser;

    @Test
    void parseMultipartFile_whenFileInputCorrect_returnMapWithCorrectValues() throws IOException {
        var content = Files.readAllBytes(Paths.get("src/test/resources/cars.csv"));
        var fileMock = new MockMultipartFile("cars", content);

        var mapList = csvFileParser.parseMultipartFile(fileMock);

        assertEquals(5, mapList.size());
        assertEquals("1", mapList.get(0).get(CarHeader.ID.getHeader()));
        assertEquals("Renault Clio", mapList.get(1).get(CarHeader.NAME.getHeader()));
        assertEquals("Renault Megane", mapList.get(2).get(CarHeader.NAME.getHeader()));
        assertEquals("13.05.2015", mapList.get(3).get(CarHeader.TRANSACTION_DATE.getHeader()));
        assertEquals("Żółty", mapList.get(4).get(CarHeader.COLOR.getHeader()));
    }

    @Test
    void parseMultipartFile_whenIoException_throwFileParsingException() throws IOException {
        var fileMock = mock(MultipartFile.class);

        doThrow(IOException.class).when(fileMock).getInputStream();

        assertThrows(FileParsingException.class, () -> csvFileParser.parseMultipartFile(fileMock));
    }

    @Test
    void hasValidFormat_whenFormatValid_returnTrue() throws IOException {
        var content = Files.readAllBytes(Paths.get("src/test/resources/cars.csv"));
        var fileMock = new MockMultipartFile("cars", "cars.csv", ContentType.TEXT_CSV, content);

        var hasValidFormat = csvFileParser.hasValidFormat(fileMock);

        assertTrue(hasValidFormat);
    }

    @Test
    void hasValidFormat_whenFormatInvalid_returnFalse() throws IOException {
        var content = Files.readAllBytes(Paths.get("src/test/resources/cars.csv"));
        var fileMock = Mockito.spy(new MockMultipartFile("cars", content));

        doReturn("invalid").when(fileMock).getContentType();

        var hasValidFormat = csvFileParser.hasValidFormat(fileMock);

        assertFalse(hasValidFormat);
    }

}