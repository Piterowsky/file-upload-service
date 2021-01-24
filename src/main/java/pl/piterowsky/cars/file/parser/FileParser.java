package pl.piterowsky.cars.file.parser;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileParser {

    List<Map<String, String>> parseMultipartFile(MultipartFile file);

    boolean hasValidFormat(MultipartFile file);

}
