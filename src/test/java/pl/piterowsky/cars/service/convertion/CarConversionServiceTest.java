package pl.piterowsky.cars.service.convertion;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import pl.piterowsky.cars.exception.FileConvertingException;
import pl.piterowsky.cars.header.CarHeader;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CarConversionServiceTest {

    @Spy
    private CarConversionService carConversionService;

    @Test
    void getKeyValuesAsModel_whenDataAreCorrect_returnCorrectCarsList() {
        var testCarDataMaps = Arrays.asList(
                createTestCarDataMap("1", "Skoda Octavia", "12.12.1999", "black"),
                createTestCarDataMap("2", "Ford Fiesta", "31.12.2003", "red"),
                createTestCarDataMap("4", "", "12.12.1999", "green"),
                createTestCarDataMap("999", "ŻÓŁŁŚŁŹŻĆĄ", "23.12.2014", "red")
        );
        var cars = carConversionService.getKeyValuesAsModel(testCarDataMaps);
        assertEquals(4, cars.size());
    }

    @Test
    void convertToModel_whenIncorrectTransactionDateFormat_throwsFileConvertingException() {
        var carValuesMap = createTestCarDataMap("1", "Skoda Octavia", "incorrect12.12.1999", "black");
        assertThrows(FileConvertingException.class, () -> carConversionService.convertToModel(carValuesMap));
    }

    @Test
    void convertToModel_whenInputCorrect_returnValidModel() {
        var testCarDataMap = createTestCarDataMap("1", "Skoda Octavia", "11.12.1999", "black");
        var car = carConversionService.convertToModel(testCarDataMap);

        assertEquals(1, car.getId());
        assertEquals("Skoda Octavia", car.getName());
        assertEquals(Date.valueOf(LocalDate.of(1999, 12, 11)), car.getTransactionDate());
        assertEquals("black", car.getColor());
    }

    private Map<String, String> createTestCarDataMap(String id, String name, String date, String color) {
        var carValuesMap = new HashMap<String, String>();
        carValuesMap.put(CarHeader.ID.getHeader(), id);
        carValuesMap.put(CarHeader.NAME.getHeader(), name);
        carValuesMap.put(CarHeader.TRANSACTION_DATE.getHeader(), date);
        carValuesMap.put(CarHeader.COLOR.getHeader(), color);
        return carValuesMap;
    }

}