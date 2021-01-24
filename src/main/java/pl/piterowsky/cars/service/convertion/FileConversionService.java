package pl.piterowsky.cars.service.convertion;

import java.util.List;
import java.util.Map;

public interface FileConversionService<T> {

    List<T> getKeyValuesAsModel(List<Map<String, String>> listKeyValuePairs);

    T convertToModel(Map<String, String> keyValuePair);

}
