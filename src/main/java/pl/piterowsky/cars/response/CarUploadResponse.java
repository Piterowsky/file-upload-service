package pl.piterowsky.cars.response;

import lombok.Data;

@Data
public class CarUploadResponse<T> {

    private String message;
    private T object;

}
