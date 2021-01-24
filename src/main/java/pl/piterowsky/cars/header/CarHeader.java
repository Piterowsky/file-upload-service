package pl.piterowsky.cars.header;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CarHeader {
    ID("Id"),
    NAME("Nazwa"),
    TRANSACTION_DATE("Data zakupu"),
    COLOR("Kolor");

    @Getter
    private final String header;
}
