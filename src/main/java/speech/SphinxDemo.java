package speech;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.DataBlocker;
import edu.cmu.sphinx.frontend.Signal;
import edu.cmu.sphinx.frontend.endpoint.SpeechClassifier;
import edu.cmu.sphinx.frontend.endpoint.SpeechEndSignal;
import edu.cmu.sphinx.frontend.endpoint.SpeechMarker;
import edu.cmu.sphinx.frontend.endpoint.SpeechStartSignal;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.result.WordResult;

import java.io.*;
import java.util.Calendar;

public class SphinxDemo {
    public static void transcribe(String fileToTranscribe) throws IOException {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        InputStream stream = new FileInputStream(new File(fileToTranscribe));

        recognizer.startRecognition(stream);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
            for (WordResult r : result.getWords()) {
                System.out.println(r);
            }
        }
        recognizer.stopRecognition();
    }

    public static void detectVoiceActivity(String audioFile) throws FileNotFoundException {
        InputStream stream = new FileInputStream(new File(audioFile));
        AudioFileDataSource audioFileDataSource = new AudioFileDataSource(32, null);
        audioFileDataSource.initialize();
        audioFileDataSource.setAudioFile(new File(audioFile), "exampleStream");

        DataBlocker b = new DataBlocker(10); // means 10ms
        SpeechClassifier s = new SpeechClassifier(10, 0.003, 10, 20);
        SpeechMarker speechMarker = new SpeechMarker(200, 200, 50);
        speechMarker.initialize();
        b.setPredecessor(audioFileDataSource);
        s.setPredecessor(b);
        speechMarker.setPredecessor(s);




        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        Data data;


        while((data = speechMarker.getData()) != null) {

            if(data instanceof SpeechStartSignal) {
                cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
                cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, (int) ((Signal) data).getTime());
                System.out.println("Speech START TIMESTAMP: " + cal.getTime().toString());
            }
            else if(data instanceof SpeechEndSignal) {
                cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
                cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, (int) ((Signal) data).getTime());
                System.out.println("Speech STOP TIMESTAMP: " + cal.getTime().toString());
            }
        }
        System.out.println("NOISY: " + s.getNoisy());
    }

}
