package pl.piterowsky.cars.service.convertion;

import pl.piterowsky.cars.model.Car;

import java.util.List;
import java.util.Map;

public interface FileConversionService<T> {

    List<T> getKeyValuesAsModel(List<Map<String, String>> listKeyValuePairs);

    Car convertToModel(Map<String, String> keyValuePair);

}
