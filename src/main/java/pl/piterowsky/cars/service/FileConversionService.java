package pl.piterowsky.cars.service;

import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.model.Car;

import java.util.List;

public interface FileConversionService {

    List<Car> saveCarsFromFile(MultipartFile file, String color);

}
