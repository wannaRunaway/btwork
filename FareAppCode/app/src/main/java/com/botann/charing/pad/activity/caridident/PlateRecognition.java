package com.botann.charing.pad.activity.caridident;

import android.os.Environment;

import java.io.File;

public class PlateRecognition {
    public static long handle = 0;
    String assetPath = "pr";
    String sdcardPath = Environment.getExternalStorageDirectory()
            + File.separator + assetPath;
    String cascade_filename  =  sdcardPath
            + File.separator+"cascade.xml";
    String finemapping_prototxt  =  sdcardPath
            + File.separator+"HorizonalFinemapping.prototxt";
    String finemapping_caffemodel  =  sdcardPath
            + File.separator+"HorizonalFinemapping.caffemodel";
    String segmentation_prototxt =  sdcardPath
            + File.separator+"Segmentation.prototxt";
    String segmentation_caffemodel =  sdcardPath
            + File.separator+"Segmentation.caffemodel";
    String character_prototxt =  sdcardPath
            + File.separator+"CharacterRecognization.prototxt";
    String character_caffemodel=  sdcardPath
            + File.separator+"CharacterRecognization.caffemodel";
    String segmentationFree_prototxt = sdcardPath
            + File.separator+"SegmenationFree-Inception.prototxt";
    String segmentationFree_caffemodel = sdcardPath
            + File.separator+"SegmenationFree-Inception.caffemodel";
    public PlateRecognition(){
        handle  =  PlateRecognition.InitPlateRecognizer(
                cascade_filename,
                finemapping_prototxt, finemapping_caffemodel,
                segmentation_prototxt, segmentation_caffemodel,
                character_prototxt, character_caffemodel,
                segmentationFree_prototxt, segmentationFree_caffemodel
        );
    }
    static native long InitPlateRecognizer(String casacde_detection,
                                           String finemapping_prototxt, String finemapping_caffemodel,
                                           String segmentation_prototxt, String segmentation_caffemodel,
                                           String charRecognization_proto, String charRecognization_caffemodel,
                                           String segmentationFree_prototxt, String segmentationFree_caffemodel);

    static native void ReleasePlateRecognizer(long  object);
    static native String SimpleRecognization(long  inputMat, long object);

}
