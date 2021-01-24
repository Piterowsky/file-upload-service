package pl.piterowsky.cars.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.FileConvertingException;
import pl.piterowsky.cars.exception.FileParsingException;
import pl.piterowsky.cars.exception.IncorrectFileFormatException;
import pl.piterowsky.cars.facade.CarFacade;
import pl.piterowsky.cars.response.CarUploadResponse;

@RestController
public class CarController {

    public static final String UPLOAD_CARS_URL = "/api/upload";
    public static final String UPLOAD_CARS_FILTERED_BY_COLOR_URL = "/api/upload/{color}";

    private final CarFacade carFacade;

    public CarController(CarFacade carFacade) {
        this.carFacade = carFacade;
    }

    @PostMapping({UPLOAD_CARS_URL, UPLOAD_CARS_FILTERED_BY_COLOR_URL})
    public ResponseEntity<CarUploadResponse> uploadCarsFile(@PathVariable(required = false) String color,
                                                            @RequestParam MultipartFile file) {
        var response = new CarUploadResponse();

        try {
            var numberOfUploadedCars = carFacade.uploadCarsFilteredByColor(file, color);
            var message = numberOfUploadedCars > 0 ? "Cars have been uploaded" : "No cars matching color: " + color;
            response.setCountOfSavedCars(numberOfUploadedCars);
            response.setMessage(message);
            return ResponseEntity.ok(response);
        } catch (FileConvertingException | IncorrectFileFormatException | FileParsingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
