package pl.piterowsky.cars.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.FileConvertingException;
import pl.piterowsky.cars.exception.IncorrectFileFormatException;
import pl.piterowsky.cars.model.Car;
import pl.piterowsky.cars.response.CarUploadResponse;
import pl.piterowsky.cars.service.FileConversionService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CarFileController {

    private final FileConversionService csvFileConversionService;

    public CarFileController(FileConversionService csvFileConversionService) {
        this.csvFileConversionService = csvFileConversionService;
    }

    @RequestMapping("/upload/{color}")
    public ResponseEntity<CarUploadResponse<List<Car>>> uploadFile(@RequestParam("file") MultipartFile file,
                                                                   @PathVariable("color")String color) {
        var response = new CarUploadResponse<List<Car>>();

        if(color == null || color.isBlank()) {
            response.setMessage("Color is required");
            return ResponseEntity.badRequest().body(response);
        }

        if(file!= null && file.isEmpty()) {
            response.setMessage("File is required");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            var cars = csvFileConversionService.saveCarsFromFile(file, color);
            var message = cars.isEmpty() ? "Cars have been uploaded" : "No cars matching color: " + color;
            response.setObject(cars);
            response.setMessage(message);
            return ResponseEntity.ok(response);
        } catch (FileConvertingException | IncorrectFileFormatException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
