package cea.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cea.evaluation.model.CEABaseline;

import java.io.File;
import java.io.IOException;

public class JsonUtil {

    public static CEABaseline evaluationFromJson(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        File input = new File(path);
        return mapper.readValue(input, CEABaseline.class);
    }
}
