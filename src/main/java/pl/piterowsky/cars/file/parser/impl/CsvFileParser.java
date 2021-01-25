package pl.piterowsky.cars.file.parser.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.FileParsingException;
import pl.piterowsky.cars.file.ContentType;
import pl.piterowsky.cars.file.parser.FileParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(value = CsvFileParser.BEAN_NAME)
public class CsvFileParser implements FileParser {

    public static final String BEAN_NAME = "csvFileParser";

    private static final CSVFormat CSV_FORMAT_CONFIG = CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase()
            .withTrim()
            .withDelimiter(',');

    @Override
    public List<Map<String, String>> parseMultipartFile(MultipartFile file) {
        try (
                InputStreamReader isr = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader fileReader = new BufferedReader(isr);
                CSVParser csvParser = new CSVParser(fileReader, CSV_FORMAT_CONFIG)
        ) {
            return csvParser.getRecords().stream().map(CSVRecord::toMap).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileParsingException("Unexpected error while converting file from csv to entity");
        }
    }

    @Override
    public boolean hasValidFormat(MultipartFile file) {
        return ContentType.CSV.equals(file.getContentType());
    }

}
