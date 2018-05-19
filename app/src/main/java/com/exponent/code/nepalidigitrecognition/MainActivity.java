package com.exponent.code.nepalidigitrecognition;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.AssetManager;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class MainActivity extends AppCompatActivity {

    private CameraView cameraView;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textView;
    private Button button;
    private Classifier classifier;

    private static final int INPUT_SIZE = 36;
    private static final int IMAGE_MEAN = 36;
    private static final float IMAGE_STD = 1;
//    private static final String INPUT_NAME = "conv2d_1_input";
//    private static final String OUTPUT_NAME = "activation_8/Softmax";
    private static final String INPUT_NAME = "conv2d_3_input";
    private static final String OUTPUT_NAME = "activation_8/Softmax";

    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings.txt";
    private Vector<String> labels = new Vector<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (CameraView) findViewById(R.id.cameraView);

        textView = (TextView) findViewById(R.id.resultText);
        button = (Button) findViewById(R.id.resultButton);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                textView.setText(results.toString());
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });
        //textView.setText(MODEL_FILE);
        initTensorFlowAndLoadModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

}
