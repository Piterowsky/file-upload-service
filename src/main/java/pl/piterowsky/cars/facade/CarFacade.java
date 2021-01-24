package pl.piterowsky.cars.facade;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.IncorrectFileFormatException;
import pl.piterowsky.cars.file.ContentType;
import pl.piterowsky.cars.file.parser.CsvFileParser;
import pl.piterowsky.cars.file.parser.FileParser;
import pl.piterowsky.cars.model.Car;
import pl.piterowsky.cars.repository.CarRepository;
import pl.piterowsky.cars.service.convertion.CarConversionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarFacade {

    private final CarConversionService carConversionService;
    private final Map<String, FileParser> fileParsers;
    private final CarRepository carRepository;

    public CarFacade(CarConversionService carConversionService, Map<String, FileParser> fileParsers,
                     CarRepository carRepository) {
        this.carConversionService = carConversionService;
        this.fileParsers = fileParsers;
        this.carRepository = carRepository;
    }

    public int uploadCarsFilteredByColor(MultipartFile file, String color) {
        var contentType = getContentType(file);

        FileParser fileParser = getFileParserByContentType(contentType);

        List<Car> cars = getCarsToUpload(file, fileParser, color);
        var savedCars = carRepository.saveAll(cars);
        return savedCars.size();
    }

    protected List<Car> getCarsToUpload(MultipartFile file, FileParser fileParser, String color) {
        var cars = carConversionService.getKeyValuesAsModel(fileParser.parseMultipartFile(file));
        if (color != null) {
            cars = cars.stream().filter(car -> isCarOfColor(car, color)).collect(Collectors.toList());
        }
        return cars;
    }

    protected FileParser getFileParserByContentType(String contentType) {
        // Ready for future supported file types
        switch (contentType) {
            case ContentType.TEXT_CSV:
                return fileParsers.get(CsvFileParser.BEAN_NAME);
            default:
                throw new IncorrectFileFormatException("Unsupported file content-type");
        }
    }

    protected String getContentType(MultipartFile file) {
        return Optional.ofNullable(file.getContentType())
                .orElseThrow(() -> new IncorrectFileFormatException("No file content-type"));
    }

    protected boolean isCarOfColor(Car car, String color) {
        return color.equalsIgnoreCase(car.getColor());
    }

}
