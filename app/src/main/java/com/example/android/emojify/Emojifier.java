package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;


import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

// COMPLETED TODO (1.1): Create a Java class called Emojifier
// COMPLETED TODO (1.2): Create a static method in the Emojifier class called detectFaces() which detects and
    // logs the number of faces in a given bitmap.

class Emojifier {

    private static final String LOG_TAG = Emojifier.class.getSimpleName();

    // COMPLETED TODO (4.3): Create threshold constants for a person smiling, and and eye being open by
        // taking pictures of yourself and your friends and noting the logs.
    private static final double SMILING_PROB_THRESHOLD = .15;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

    static void detectFaces(Context context, Bitmap picture) {

        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(picture).build();

        SparseArray<Face> faces = detector.detect(frame);

        Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());

        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else { // COMPLETED TODO (3.2): Iterate through the faces, calling getClassifications() for each face.
            for (int i = 0; i < faces.size(); i++) {
                Face face = faces.valueAt(i);

                //log prob for each face
                whichEmoji(face);
                // TODO (4.6): Change the call to getClassifications to whichEmoji() to log the appropriate emoji for the facial expression.
            }
        }

        detector.release();
    }

    // COMPLETED TODO (3.1): Create a static method called getClassifications() which logs the
        // probability of each eye being open and that the person is smiling.
    // COMPLETED TODO (4.2): Change the name of the getClassifications() method to whichEmoji() (also change the log statements)
    private static void whichEmoji(Face face) {

        //Log all probabilities
        Log.d(LOG_TAG, "whichEmoji: simplingProb = " + face.getIsSmilingProbability());
        Log.d(LOG_TAG, "whichEmoji: leftEyeOpenProb = " + face.getIsLeftEyeOpenProbability());
        Log.d(LOG_TAG, "whichEmoji: rightEyeOpenProb = " + face.getIsRightEyeOpenProbability());

        // COMPLETED TODO (4.4): Create 3 boolean variables to track the state of the facial expression based
            // on the thresholds you set in the previous step: smiling, left eye closed, right eye closed.
        boolean smiling = face.getIsSmilingProbability() > SMILING_PROB_THRESHOLD;

        boolean leftEyeClosed = face.getIsLeftEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;
        boolean rightEyeClosed = face.getIsRightEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;


        // COMPLETED TODO (4.5): Create an if/else system that selects the appropriate emoji based
            // on the above booleans and log the result.

        Emoji emoji;
        if (smiling) {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK;
            } else if (rightEyeClosed && !leftEyeClosed) {
                emoji = Emoji.RIGHT_WINK;
            } else if (leftEyeClosed) {
                emoji = Emoji.CLOSED_EYE_SMILE;
            } else {
                emoji = Emoji.SMILE;
            }
        } else {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK_FROWN;
            } else if (rightEyeClosed && !leftEyeClosed) {
                emoji = Emoji.RIGHT_WINK_FROWN;
            } else if (leftEyeClosed) {
                emoji = Emoji.CLOSED_EYE_FROWN;
            } else {
                emoji = Emoji.FROWN;
            }
        }


        Log.d(LOG_TAG, "whichEmoji: " + emoji.name());

    }

    // COMPLETED TODO (4.1): Create an enum class called Emoji that contains all the possible emoji you can make
        // (smiling, frowning, left wink, right wink, left wink frowning, right wink frowning, closed eye smiling, close eye frowning).

    private enum Emoji {
        SMILE,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
    }






}
