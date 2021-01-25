package pl.piterowsky.cars.facade;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.IncorrectFileFormatException;
import pl.piterowsky.cars.file.ContentType;
import pl.piterowsky.cars.file.parser.impl.CsvFileParser;
import pl.piterowsky.cars.file.parser.FileParser;
import pl.piterowsky.cars.model.Car;
import pl.piterowsky.cars.repository.CarRepository;
import pl.piterowsky.cars.service.convertion.CarConversionService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CarFacadeTest {

    @Mock
    private CarConversionService carConversionService;

    @Mock
    private Map<String, FileParser> fileParsers;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarFacade carFacade;

    @Test
    void uploadCarsFilteredByColor_whenCorrectInput_shouldReturnNumberOfSavedCars() {
        var savedCarList = mock(List.class);
        var fileParser = mock(CsvFileParser.class);
        var multipartFile = mock(MultipartFile.class);
        var expectedNumberOfSavedCars = 5;

        doReturn(ContentType.CSV).when(multipartFile).getContentType();
        doReturn(Collections.emptyList()).when(carConversionService).getKeyValuesAsModel(Mockito.anyList());
        doReturn(fileParser).when(fileParsers).get(CsvFileParser.BEAN_NAME);
        doReturn(savedCarList).when(carRepository).saveAll(Mockito.anyList());
        doReturn(expectedNumberOfSavedCars).when(savedCarList).size();
        doReturn(Collections.emptyList()).when(fileParser).parseMultipartFile(multipartFile);

        var uploadedCars = carFacade.uploadCarsFilteredByColor(multipartFile, "green");
        assertEquals(expectedNumberOfSavedCars, uploadedCars);
    }

    @Test
    void getCarsToUpload_whenColorGiven_returnFilteredCars() {
        var car1 = mock(Car.class);
        var car2 = mock(Car.class);
        var car3 = mock(Car.class);
        var fileParser = mock(FileParser.class);

        doReturn("green").when(car1).getColor();
        doReturn("red").when(car2).getColor();
        doReturn("green").when(car3).getColor();
        doReturn(Collections.emptyList()).when(fileParser).parseMultipartFile(any());

        doReturn(List.of(car1, car2, car3)).when(carConversionService).getKeyValuesAsModel(Mockito.anyList());

        var greenCars = carFacade.getCarsToUpload(null, fileParser, "green");

        assertEquals(2, greenCars.size());
        assertArrayEquals(new Car[]{car1, car3}, greenCars.toArray());
    }

    @Test
    void getCarsToUpload_whenNoColorGiven_returnAll() {
        var car1 = mock(Car.class);
        var car2 = mock(Car.class);
        var car3 = mock(Car.class);
        var fileParser = mock(FileParser.class);

        doReturn("green").when(car1).getColor();
        doReturn("red").when(car2).getColor();
        doReturn("green").when(car3).getColor();
        doReturn(Collections.emptyList()).when(fileParser).parseMultipartFile(any());

        doReturn(List.of(car1, car2, car3)).when(carConversionService).getKeyValuesAsModel(Mockito.anyList());

        var greenCars = carFacade.getCarsToUpload(null, fileParser, null);

        assertEquals(3, greenCars.size());
        assertArrayEquals(new Car[]{car1, car2, car3}, greenCars.toArray());
    }

    @Test
    void getFileParserByContentType_whenCorrectContentType_shouldReturnCorrectFileParser() {
        var csvFileParser = mock(CsvFileParser.class);
        doReturn(csvFileParser).when(fileParsers).get(CsvFileParser.BEAN_NAME);

        var fileParser = carFacade.getFileParserByContentType(ContentType.CSV);

        assertTrue(fileParser instanceof CsvFileParser);
    }

    @Test
    void getFileParserByContentType_whenIncorrectContentType_shouldThrowException() {
        var exception = assertThrows(IncorrectFileFormatException.class,
                () -> carFacade.getFileParserByContentType("INCORRECT"));

        assertEquals("Unsupported file content-type", exception.getMessage());
    }

    @Test
    void getContentType_whenContentTypeNull_shouldThrowException() {
        var file = mock(MultipartFile.class);

        doReturn(null).when(file).getContentType();

        var exception = assertThrows(IncorrectFileFormatException.class, () -> carFacade.getContentType(file));

        assertEquals("No file content-type", exception.getMessage());
    }

    @Test
    void filterByColor_whenCorrectInput_shouldReturnTrue() {
        var car = mock(Car.class);
        doReturn("red").when(car).getColor();

        assertTrue(carFacade.isCarOfColor(car, "red"));
    }

}