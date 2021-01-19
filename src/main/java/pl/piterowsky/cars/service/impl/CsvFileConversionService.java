package pl.piterowsky.cars.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.CarRepository;
import pl.piterowsky.cars.exception.IncorrectFileFormatException;
import pl.piterowsky.cars.model.Car;
import pl.piterowsky.cars.service.FileConversionService;
import pl.piterowsky.cars.util.CsvFileUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvFileConversionService implements FileConversionService {

    private final CsvFileUtils csvFileUtils;
    private final CarRepository carRepository;

    public CsvFileConversionService(CsvFileUtils csvFileUtils, CarRepository carRepository) {
        this.csvFileUtils = csvFileUtils;
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> saveCarsFromFile(MultipartFile file, String color) {
        if (csvFileUtils.hasCSVFormat(file)) {
            var cars = csvFileUtils.csvFileToCar(file);
            var filteredCars = cars.stream()
                    .filter(car -> color.equalsIgnoreCase(car.getColor()))
                    .collect(Collectors.toList());
            return carRepository.saveAll(filteredCars);
        }
        throw new IncorrectFileFormatException("Given file is not a csv file");
    }

}
