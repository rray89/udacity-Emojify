package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;


import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import timber.log.Timber;

// COMPLETED TODO (1.1): Create a Java class called Emojifier
// COMPLETED TODO (1.2): Create a static method in the Emojifier class called detectFaces() which detects and
    // logs the number of faces in a given bitmap.

class Emojifier {

    //private static final String LOG_TAG = Emojifier.class.getSimpleName();

    // COMPLETED TODO (4.3): Create threshold constants for a person smiling, and and eye being open by
        // taking pictures of yourself and your friends and noting the logs.
    private static final double SMILING_PROB_THRESHOLD = .25;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

    private static final float  EMOJI_SCALE_FACTOR = .9f;

    static Bitmap detectFacesAndOverlayMoji(Context context, Bitmap picture) {

        // COMPLETED TODO (5.3) & (5.9): Change the name of the detectFaces() method to detectFacesAndOverlayEmoji()
            // and the return type from void to Bitmap

        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(picture).build();

        SparseArray<Face> faces = detector.detect(frame);

        //Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());
        Timber.d("detectFaces: number of faces = " + faces.size());

        // COMPLETED TODO (5.7): Create a variable called resultBitmap and initialize it to the original
            // picture bitmap passed into the detectFacesAndOverlayEmoji() method
        Bitmap resultBitmap = picture;

        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else { // COMPLETED TODO (3.2): Iterate through the faces, calling getClassifications() for each face.
            for (int i = 0; i < faces.size(); i++) {
                Face face = faces.valueAt(i);

                // COMPLETED TODO (5.4): Create a variable called emojiBitmap to hold the appropriate Emoji
                    // bitmap and remove the call to whichEmoji()

                //log prob for each face
                Bitmap emojiBitmap;

                //COMPLETED TODO (5.5): Create a switch statement on the result of the whichEmoji()
                    // call, and assign the proper emoji bitmap to the variable you created
                switch (whichEmoji(face)) {
                    case FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.frown);
                        break;
                    case SMILE:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.smile);
                        break;
                    case LEFT_WINK:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwink);
                        break;
                    case RIGHT_WINK:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwink);
                        break;
                    case LEFT_WINK_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwinkfrown);
                        break;
                    case CLOSED_EYE_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.closed_frown);
                        break;
                    case CLOSED_EYE_SMILE:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.closed_smile);
                        break;
                    case RIGHT_WINK_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwinkfrown);
                        break;
                    default:
                        emojiBitmap = null;
                        Toast.makeText(context, R.string.no_emoji, Toast.LENGTH_SHORT).show();
                }

                // COMPLETED TODO (5.8): Call addBitmapToFace(), passing in the resultBitmap, the emojiBitmap
                    // and the Face  object, and assigning the result to resultBitmap
                resultBitmap = addBitmapToFace(resultBitmap, emojiBitmap, face);
            }
        }

        detector.release();
        return resultBitmap;
    }

    // COMPLETED TODO (3.1): Create a static method called getClassifications() which logs the
        // probability of each eye being open and that the person is smiling.
    // COMPLETED TODO (4.2): Change the name of the getClassifications() method to whichEmoji() (also change the log statements)
    private static Emoji whichEmoji(Face face) {

        // COMPLETED TODO (5.1) & (5.2): Change the return type of the whichEmoji() method from void to Emoji.

        //Log all probabilities
        //Log.d(LOG_TAG, "whichEmoji: simplingProb = " + face.getIsSmilingProbability());
        //Log.d(LOG_TAG, "whichEmoji: leftEyeOpenProb = " + face.getIsLeftEyeOpenProbability());
        //Log.d(LOG_TAG, "whichEmoji: rightEyeOpenProb = " + face.getIsRightEyeOpenProbability());

        Timber.d("whichEmoji: smilingProb = " + face.getIsSmilingProbability());
        Timber.d("whichEmoji: leftEyeOpenProb = " + face.getIsLeftEyeOpenProbability());
        Timber.d("whichEmoji: rightEyeOpenProb = " + face.getIsRightEyeOpenProbability());

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

        //Log.d(LOG_TAG, "whichEmoji: " + emoji.name());
        Timber.d("whichEmoji: " + emoji.name());

        return emoji;

    }

    // COMPLETED TODO (5.6) Create a method called addBitmapToFace() which takes the background bitmap, the
        // Emoji bitmap, and a Face object as arguments and returns the combined bitmap with the Emoji over the face.

    private static Bitmap addBitmapToFace(Bitmap backgroundBitmap, Bitmap emojiBitmap, Face face) {

        //initialize the results bitmap to be a mutable copy of the original iamge
        Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight(), backgroundBitmap.getConfig());

        float scaleFactor = EMOJI_SCALE_FACTOR;

        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
        int newEmojiHeight = (int) (face.getHeight() * scaleFactor);

        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);

        float emojiPositionX =
                (face.getPosition().x + face.getWidth()/2) - emojiBitmap.getWidth()/2;
        float emojiPositionY =
                (face.getPosition().y + face.getHeight()/2) - emojiBitmap.getHeight()/3;

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY,null);

        return resultBitmap;
    }


    // COMPLETED TODO (4.1): Create an enum class called Emoji that contains all the possible emoji
        // you can make (smiling, frowning, left wink, right wink, left wink frowning, right wink
        // frowning, closed eye smiling, close eye frowning).
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
