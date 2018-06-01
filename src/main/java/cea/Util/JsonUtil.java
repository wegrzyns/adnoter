package cea.Util;

import cea.evaluation.model.CEABaseline;
import cea.output.AnnotationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonUtil {

    public static CEABaseline evaluationFromJson(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        File input = new File(path);
        return mapper.readValue(input, CEABaseline.class);
    }

    public static void annotationResultToJson(AnnotationResult annotationResult) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //TODO: output file name parsing/change, add date to make unique?
        File output = new File(annotationResult.getFileName().replace(".mp4", ".json"));
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, annotationResult);
    }
}
