package pl.piterowsky.cars.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentType {

    public static final String CSV = "text/csv";
    public static final String XLS = "application/vnd.ms-excel";
    public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

}
