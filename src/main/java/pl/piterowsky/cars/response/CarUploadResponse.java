package pl.piterowsky.cars.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarUploadResponse extends BaseResponse {

    private int countOfSavedCars;

}
