package com.exponent.code.nepalidigitrecognition;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

/**
 * Created by TeasingExponent on 3/13/2018.
 */

/**
 * Generic interface for interacting with different recognition engines.
 */
public interface Classifier {
    /**
     * An immutable result returned by a Classifier describing what was recognized.
     */
    public class Recognition {
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        private final String id;

        /**
         * Display name for the recognition.
         */
        private final String title;

        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        private final Float confidence;

        public Recognition(
                final String id, final String title, final Float confidence) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title != null) {
                // Comment here
                // resultString += title + " ";
            }
            Log.i("Checking Value", "Title: " + titsle);
            if (confidence != null) {
                // Comment Here
                // resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }
            Log.i("Checking Value", "Confidence: " + title);

            return resultString.trim();
        }
    }

    List<Recognition> recognizeImage(Bitmap bitmap);

    void enableStatLogging(final boolean debug);

    String getStatString();

    void close();
}

