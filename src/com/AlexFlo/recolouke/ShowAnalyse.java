package com.AlexFlo.recolouke;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

import com.example.recolouke.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ShowAnalyse extends Activity {

	final static String TAG = "[ShowAnalyse]";
	
	final static String homeURL = "http://www-rech.telecom-lille.fr/freeorb/";
	final static String indexFile = "index.json";
	static String vocabulary = "vocabulary.yml";
	final static String classifiersDirectory = "classifiers/";
	final static String testImagesDirectory = "test-images/";
	final static String trainImagesDirectory = "test-images/";
	final static String testImages = "test_images.txt";
	final static String trainImages = "test_images.txt";
	
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
		new DownloadHTTPFileIndex().execute(homeURL + indexFile);
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
	private Mat analyseScene(Mat srcGrayscale, int detector, int descriptorExtractor) {
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


		Mat _descriptors_scene = new Mat();
		// Extraction of the descriptors
		_descriptor.compute(srcGrayscale, _scenekeypoints, _descriptors_scene);
		return _descriptors_scene;
	}

	private String testClassifiers()
	{
		/*
		Part of the code to compute BoW with ORB:
		
		for( all your images )
		{
		    // Detect interesting points
		    orb(img, Mat(), keypoints, descriptors);
		    // Keep characteristics of images for further clustering.
		    characteristics.push_back(descriptors);
		}
		// Create the BOW object with K classes
		BOWKMeansTrainer bow(K);
		for( all your descriptors )
		{
		    // Convert characteristics vector to float
		    // This is require by BOWTrainer class
		    Mat descr;
		    characteristics[k].convertTo(descr, CV_32F);
		    // Add it to the BOW object
		    bow.add(descr);
		}
		// Perform the clustering of stored vectors
		Mat voc = bow.cluster();
		You need to convert features from CV_8U to CV_32F, because bag of words object expects float descriptors.
		It is not required for SIFT or SURF descriptors because they are in float
		*/
		
		//Extraction of descriptors of the image
		/*Mat scene_descriptors = analyseScene(ImageUtility.convertToGrayscaleMat(Global.IMG_SELECTED)
				, FeatureDetector.ORB
				, DescriptorExtractor.ORB);*/
		
		
		//TODO Chargement du vocabulaire
		/*vocabulary = Global.getVocabularyFileName(homeURL + indexFile);
		if(vocabulary != null)
		{
			Mat vocab = Global.getVocabulary(homeURL + vocabulary);*/
		
			//TODO Chargement des classifiers
			List<Classe> classifiers = new LinkedList<Classe>();
			classifiers.addAll(Global.parseClasses(homeURL + indexFile));
		
		//TODO POUR CHAQUE CLASSIFIER - CALCUL & ANALYSE DE l'HISTO
		
		//TODO Détermination du 'best match'
		
		//TODO return value = La 'classe' la plus proche
		//}
		return null;
	}
	
	// Implementation of AsyncTask used to download files asynchronous
	class DownloadHTTPFileIndex extends AsyncTask<String, Integer, Integer> 
	{
		String result = null;

		@Override
		protected Integer doInBackground(String... params) {
	        try {
	        	result = URLReader.readURLData(params[0]);
	        	return 0;
	        } catch (Exception e) {
	            Log.e(TAG, "Erreur lors de la lecture du fichier à l'adresse : " + params[0]);
	            return -1;
	        }
		}
		
	    protected void onPostExecute() {  
	    	Global.parseClasses(result);
	    }
	}
	
	// Second Implementation of AsyncTask used to download files asynchronous
	class DownloadHTTPFileVocab extends AsyncTask<String, Integer, Integer> 
	{
		String result = null;

		@Override
		protected Integer doInBackground(String... params) {
	        try {
	        	result = URLReader.readURLData(params[0]);
	        	return 0;
	        } catch (Exception e) {
	            Log.e(TAG, "Erreur lors de la lecture du fichier à l'adresse : " + params[0]);
	            return -1;
	        }
		}
		
	    protected void onPostExecute() {  
	    	Global.parseVocabulary(result);
	    }
	}
	
	// Third Implementation of AsyncTask used to download files asynchronous
	class DownloadHTTPFileClassifier extends AsyncTask<String, Integer, Integer> 
	{
		String result = null;

		@Override
		protected Integer doInBackground(String... params) {
	        try {
	        	result = URLReader.readURLData(params[0]);
	        	return 0;
	        } catch (Exception e) {
	            Log.e(TAG, "Erreur lors de la lecture du fichier à l'adresse : " + params[0]);
	            return -1;
	        }
		}
		
	    protected void onPostExecute() {  
	    	//Global.parseClassifiers(result);
	    }
	}
}
