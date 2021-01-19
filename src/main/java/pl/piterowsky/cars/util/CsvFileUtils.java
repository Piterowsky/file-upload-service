package pl.piterowsky.cars.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.FileConvertingException;
import pl.piterowsky.cars.model.Car;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CsvFileUtils {

    public static final String FILE_TYPE = "text/csv";
    protected static final String[] HEADERS = {"Id", "Nazwa", "Data zakupu", "Kolor"};

    public boolean hasCSVFormat(MultipartFile file) {
        return FILE_TYPE.equals(file.getContentType());
    }

    public List<Car> csvFileToCar(MultipartFile file) {
        var csvFormat = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(',');
        try (
                InputStreamReader isr = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader fileReader = new BufferedReader(isr);
                CSVParser csvParser = new CSVParser(fileReader, csvFormat);
        ) {
            return csvParser.getRecords().stream().map(convertRecordToEntity()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileConvertingException("Unexpected error while converting file from csv to entity");
        }
    }

    private Function<CSVRecord, Car> convertRecordToEntity() {
        return record -> {
            var id = Long.parseLong(record.get(HEADERS[0]));
            var name = record.get(HEADERS[1]);
            var date = parseDate(record);
            var color = record.get(HEADERS[3]);
            return new Car(id, name, date, color);
        };
    }

    private static Date parseDate(CSVRecord record) {
        var date = record.get(HEADERS[2]);
        try {
            return new Date(new SimpleDateFormat("dd.MM.yyyy").parse(date).getTime());
        } catch (ParseException e) {
            throw new FileConvertingException(String.format("Cannot parse date: %s", date));
        }
    }

}
