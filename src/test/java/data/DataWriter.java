package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.file.Path;

public class DataWriter {
    private static final String RELATIVE_RESOURCES_PATH = "src/main/resources";

    private static final ObjectMapper objectMapper = JsonMapper.builder()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        .build();

    private DataWriter() {}

    public static <T> void writeData(T data, Path jsonPath) {
        try {
            objectMapper.writeValue(jsonPath.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("Failed writing JSON for " + jsonPath, e);
        }
    }

    public static String getRelativePathOf(String path) {
        return RELATIVE_RESOURCES_PATH + path;
    }
}
