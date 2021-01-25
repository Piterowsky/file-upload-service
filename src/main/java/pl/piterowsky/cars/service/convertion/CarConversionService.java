package pl.piterowsky.cars.service.convertion;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.piterowsky.cars.exception.FileConvertingException;
import pl.piterowsky.cars.header.CarHeader;
import pl.piterowsky.cars.model.Car;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2

@Service
public class CarConversionService implements FileConversionService<Car> {

    @Override
    public List<Car> getKeyValuesAsModel(List<Map<String, String>> listKeyValuePairs) {
        return listKeyValuePairs.stream().map(this::convertToModel).collect(Collectors.toList());
    }

    @Override
    public Car convertToModel(Map<String, String> keyValuePair) {
        var id = Long.parseLong(keyValuePair.get(CarHeader.ID.getHeader()));
        var name = keyValuePair.get(CarHeader.NAME.getHeader());
        var transactionDate = parseCarDate(keyValuePair.get(CarHeader.TRANSACTION_DATE.getHeader()));
        var color = keyValuePair.get(CarHeader.COLOR.getHeader());
        return new Car(id, name, transactionDate, color);
    }

    private static Date parseCarDate(String date) {
        try {
            var carDateFormat = "dd.MM.yyyy";
            return new Date(new SimpleDateFormat(carDateFormat).parse(date).getTime());
        } catch (ParseException e) {
            var message = String.format("Cannot parse date: %s", date);
            log.warn("Cannot convert date: {}", date);
            throw new FileConvertingException(message);
        }
    }

}
