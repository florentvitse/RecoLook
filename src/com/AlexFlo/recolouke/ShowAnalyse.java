package com.AlexFlo.recolouke;

import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

import com.example.recolouke.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ShowAnalyse extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_analyse);

		findViewById(R.id.btnReturnShowAnalyse).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK, null);
				finish();
			}
		});

		((ImageView) findViewById(R.id.imgAnalyzed)).setImageBitmap(Global.IMG_SELECTED);

		//TODO 
		// Working comparison
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_analyse, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Created Method
	
	/*
	 * analyseScene
	 * Calculate descriptor of one image converted into grayscale
	 */
	
	public void analyseScene(Mat srcGrayscale, int detector, int descriptorExtractor) {
		// Creation of the detector
		// FeatureDetector.ORB to give to the method here (generic method also)
		FeatureDetector _detector = FeatureDetector.create(detector);
		// Creation of the descriptor
		// DescriptorExtractor.ORB to give to the method here (generic method
		// also)
		DescriptorExtractor _descriptor = DescriptorExtractor.create(descriptorExtractor);

		// Object that will store the keypoint of the scene
		MatOfKeyPoint _scenekeypoints = new MatOfKeyPoint();
		// Detection of the keyPoints of the scene
		_detector.detect(srcGrayscale, _scenekeypoints);

		//Log.w(TAG, "* Number of keypoints (scene) *");
		//Log.w(TAG, String.valueOf(_scenekeypoints.size()));

		Mat _descriptors_scene = new Mat();
		// Extraction of the descriptors
		_descriptor.compute(srcGrayscale, _scenekeypoints, _descriptors_scene);
	}
	
}
