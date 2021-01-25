package pl.piterowsky.cars.file.parser.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.piterowsky.cars.exception.FileParsingException;
import pl.piterowsky.cars.file.ContentType;
import pl.piterowsky.cars.file.parser.FileParser;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@Log4j2

@Component(value = ExcelFileParser.BEAN_NAME)
public class ExcelFileParser implements FileParser {

    public static final String BEAN_NAME = "excelFileParser";
    public static final String FORMATTER_LOCALE = "PL";

    private final DataFormatter formatter;

    public ExcelFileParser() {
        formatter = new DataFormatter(Locale.forLanguageTag(FORMATTER_LOCALE));
    }

    @Override
    public List<Map<String, String>> parseMultipartFile(MultipartFile file) {
        var contentType = file.getContentType();
        try (
                var workbook = ContentType.XLS.equals(contentType)
                        ? new HSSFWorkbook(file.getInputStream())
                        : new XSSFWorkbook(file.getInputStream());
        ) {
            var sheet = workbook.getSheetAt(0);
            Row headers = sheet.getRow(0);
            return createListOfMappedValues(sheet, headers);
        } catch (IOException e) {
            log.error("Unexpected error while converting file from csv to entity", e);
            throw new FileParsingException("Unexpected error while converting file from csv to entity");
        }
    }

    private List<Map<String, String>> createListOfMappedValues(Sheet sheet, Row headers) {
        var listOfMaps = new ArrayList<Map<String, String>>();
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() != 0) {
                var mappedValues = createMapOfParsedValues(headers, currentRow);
                listOfMaps.add(mappedValues);
            }
        }
        return listOfMaps;
    }

    private Map<String, String> createMapOfParsedValues(Row headers, Row currentRow) {
        var mappedValues = new HashMap<String, String>();
        IntStream.range(0, headers.getLastCellNum())
                .forEachOrdered(i -> {
                    var header = headers.getCell(i).getStringCellValue();
                    var value = formatter.formatCellValue(currentRow.getCell(i));
                    mappedValues.put(header, value);
                });
        return mappedValues;
    }

    @Override
    public boolean hasValidFormat(MultipartFile file) {
        return ContentType.XLSX.equalsIgnoreCase(file.getContentType())
                || ContentType.XLS.equalsIgnoreCase(file.getContentType());
    }

}

