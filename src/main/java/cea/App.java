package cea;

import cea.video.CEASlideTransitionDetectorManager;

import java.io.IOException;

/**
 * Created by Krzysiu on 15.10.2017.
 */
public class App {

    private static int test;

    public static void main(String[] args) throws IOException {
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
        CEASlideTransitionDetectorManager
                .evaluateAlgorithm("input\\old\\testInputPhysics1.json");

//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputChemistryFull.json");
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

//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputMechatronicsPartial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputPhysics2Partial.json");
//
//        CEASlideTransitionDetectorManager
//                .evaluateAlgorithm("input\\new\\testInputTourism1Partial.json");
    }
}
