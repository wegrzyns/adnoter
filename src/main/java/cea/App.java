package cea;

import cea.Util.JsonUtil;
import cea.audio.CEADiarization;
import cea.audio.model.DiarizationResult;
import cea.evaluation.model.CEABaseline;
import cea.video.CEASlideTransitionDetectorManager;
import cea.video.model.Detection;
import cea.output.Output;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Krzysiu on 15.10.2017.
 */
public class App {

    private static final CountDownLatch resultLatch = new CountDownLatch (1);
    private static DiarizationResult diarizationResult;

    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException, InterruptedException {
//        Audio audio = new Audio("O150884019100-60384716fl.mp4");
//        audio.parseAudio();

//        evaluateAlgorithm("input\\old\\testInputPhysics1.json");
//        evaluateAlgorithm("input\\old\\testInputFood1.json");
//        evaluateComputedDiarization("input\\old\\testInputPhysics1.json", "input\\computed_results\\O150884019100-60384716fl.json");
//        evaluateComputedDiarization("input\\old\\testInputFood1.json", "input\\computed_results\\O150996520500-83143085fl.json");

//        evaluateAlgorithm("input\\old\\testInputEconomy1.json");
//        evaluateAlgorithm("input\\new\\testInputMath.json");
//        evaluateAlgorithm("input\\new\\testInputMechatronics.json");
//        evaluateAlgorithm("input\\new\\testInputChemistry.json");
//        evaluateAlgorithm("input\\inputOther.json");
    }

    public static void diarizationResultCallback(DiarizationResult result) {
        diarizationResult = result;
        resultLatch.countDown();
    }

    public static void evaluateAlgorithm(String pathToJsonInput) throws IOException, InterruptedException, IllegalAccessException, InvocationTargetException {
        CEABaseline baseline = JsonUtil.evaluationFromJson(pathToJsonInput);
        DiarizationResult diarizationResult = evaluateDiarization(baseline);
//        List<Detection> detections = evaluateSlideDetection(baseline);
        List<Detection> detections = new ArrayList<>();


        Output.createOutputAnnotation(diarizationResult, detections, baseline.getFilePath());
    }

    private static DiarizationResult evaluateDiarization(CEABaseline baseline) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        Instant start = Instant.now();

        new CEADiarization().launchDiarization(baseline);
        resultLatch.await();

        Instant end = Instant.now();

        CEADiarization.logResults(baseline, Duration.between(start, end), diarizationResult);
        return diarizationResult;
    }

    private static void evaluateComputedDiarization(String pathToJsonInput, String pathToComputedDiarization) throws IOException {
        CEABaseline baseline = JsonUtil.evaluationFromJson(pathToJsonInput);
        DiarizationResult diarizationResult = JsonUtil.diarizationResultFromJson(pathToComputedDiarization);

        CEADiarization.logResults(baseline, Duration.ZERO, diarizationResult);
    }

    private static List<Detection> evaluateSlideDetection(CEABaseline baseline) throws IOException {
        Instant start = Instant.now();

        List<Detection> detections = CEASlideTransitionDetectorManager.processVideo(baseline.getFilePath());

        Instant end = Instant.now();

        CEASlideTransitionDetectorManager.logSlideDetectionResult(detections, baseline, Duration.between(start, end));

        return detections;
    }


    private static void launchDetection() throws IOException, InterruptedException, IllegalAccessException, InvocationTargetException {
        //        Audio audio = new Audio("E:\\projekty\\mag\\H.264\\Rogue One - A Star Wars Story - Trailer.mp4");
//        Audio audio = new Audio("E:\\projekty\\mag\\8254-84205-0000.flac");
//        Audio audio = new Audio("E:\\projekty\\mag\\8254-115543-0008.flac");
//        try {
//            audio.parseAudio();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        OpenCV openCV = new OpenCV();
//        openCV.open("cut4.mp4");
//        openCV.open("cut1.mp4");

//        CEASlideTransitionDetectorManager.processVideo("medicalVideo.mp4");
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\old\\testInputPhysics1.json");
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputChemistry.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputEconomy2Partial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputEconomy3Partial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputEnglishPartial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputEnvirnomentPartial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputFood2Partial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputFood3Partial.json");

//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputMath1Partial.json");

        evaluateAlgorithm("input\\new\\testInputMechatronics.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputPhysics2Partial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputTourism1Partial.json");
    }
}
