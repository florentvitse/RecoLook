package com.AlexFlo.recolouke;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BOWImgDescriptorExtractor;
import org.bytedeco.javacpp.opencv_features2d.FlannBasedMatcher;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_ml.CvSVM;
import org.bytedeco.javacpp.opencv_nonfree.SIFT;

import static org.bytedeco.javacpp.opencv_highgui.imread;

import com.example.recolouke.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ShowAnalyse extends Activity {

	final static String TAG = "[ShowAnalyse]";

	final static String homeURL = "http://www-rech.telecom-lille.fr/nonfreesift/";
	final static String indexFile = "index.json";
	// final static String vocabulary = "vocabulary.yml";
	final static String classifiersDirectory = "classifiers/";
	final static String testImagesDirectory = "test-images/";
	final static String trainImagesDirectory = "test-images/";
	final static String testImages = "test_images.txt";
	final static String trainImages = "test_images.txt";

	Mat response_hist;
	CvSVM[] classifiers;

	ProgressDialog progressDialog;
	
	//create SIFT feature point extracter 
    final org.bytedeco.javacpp.opencv_nonfree.SIFT SIFTdetector = new SIFT(0, 3, 0.04, 10, 1.6);
  	// Create a matcher with FlannBased Euclidien distance (possible also with BruteForce-Hamming)
  	final FlannBasedMatcher matcher = new FlannBasedMatcher(); 		        
  	// Create BoW descriptor extractor
  	final BOWImgDescriptorExtractor bowide = new BOWImgDescriptorExtractor(SIFTdetector.asDescriptorExtractor(), matcher);
	

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
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Comparaison en cours...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		// progressDialog.setIcon();
		progressDialog.setCancelable(true);
		progressDialog.show();

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
	
	// Implementation of AsyncTask used to download files asynchronous
	class DownloadHTTPFileIndex extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			
			try {
				publishProgress(0);

				// (DEV ONLY)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// END REGION

				String result = URLReader.readURLData(params[0]);
				publishProgress(100);

				// (DEV ONLY)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// END REGION

				return result;
			} catch (Exception e) {
				Log.e(TAG, "Erreur lors de la lecture du fichier à l'adresse : " + params[0]);
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressDialog.setMessage("Téléchargement du fichier d'index...\t " + String.valueOf(values[0]) + '%');
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(String result) {

			Global.parseClasses(result);
			classifiers = new CvSVM[Global.classes.size()];
			
			// (DEV ONLY)
			/*
			 * Toast.makeText(ShowAnalyse.this, clasFileNames[0],
			 * Toast.LENGTH_LONG).show();
			 */
			// END REGION

			List<String> files = new ArrayList<String>();
			files.add(Global.vocabFile);
			for(Classe c : Global.classes)
			{
				files.add(c.getClassifierFile());
			}
			new DownloadHTTPFiles().execute(files);
		}
	}

	// Second Implementation of AsyncTask used to download files asynchronous
	class DownloadHTTPFiles extends AsyncTask<List<String>, Integer, String> {

		boolean inDLClassifiers = false;
		int nbClassifierToDL = 0;

		@Override
		protected String doInBackground(List<String>... params) {
			
			nbClassifierToDL = params[0].size() - 1;
			
			for(String p : params[0])
			{
				Log.e(TAG, p);
			}
			
			try {
				publishProgress(0);

				// (DEV ONLY)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// END REGION

				String result = URLReader.readURLData(homeURL + params[0].get(0));
				publishProgress(100);

				// (DEV ONLY)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// END REGION
				
				inDLClassifiers = true;
				
				for(int numClasInProgress = 0; numClasInProgress < nbClassifierToDL; numClasInProgress++)
				{
					publishProgress(numClasInProgress + 1);
					
					parseClassifier(numClasInProgress, params[0].get(numClasInProgress + 1) );
					
					// (DEV ONLY)
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// END REGION
				}			

				return result;
			} catch (Exception e) {
				Log.e(TAG, "Erreur lors de la lecture du fichier à l'adresse : " + params[0]);
				return null;
			}
		}
		
		private void parseClassifier(int index, String filename) throws Exception
		{
			//If file exists, no need to download again and parsing too
			if(!existingFiles(filename))
			{
				String xmlFile = URLReader.readURLData(homeURL + classifiersDirectory + filename);
				if (xmlFile != null) {
					try {		
					
						FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
						fos.write(xmlFile.getBytes());
						fos.close();
						
						classifiers[index] = new CvSVM();
						// Filename of the file is enough, no need of the full path
						classifiers[index].load( filename );
					} catch (Exception e) {
						Log.e(TAG, "Erreur lors du parsing du classifier : " + filename);
					}
				}
			}
		}
		
		private boolean existingFiles(String filename)
		{
			File file = getFileStreamPath(filename);
		    if(file == null || !file.exists()) {
		        return false;
		    }
		    return true;
		}

		@Override
		protected void onPostExecute(String result) {
			
			try {
				FileOutputStream fos = openFileOutput(Global.vocabFile, Context.MODE_PRIVATE);
				fos.write(result.getBytes());
				fos.close();
			} catch (Exception e) {
				Log.e(TAG, "Erreur lors de l'enregistrement du fichier : " + Global.vocabFile);
			}
			
			Global.parseVocabulary(Global.vocabFile);
			
			progressDialog.dismiss();
			
			//TODO
			//compare(imread("caca"));
			
			Toast.makeText(ShowAnalyse.this, "Comparaison terminée", Toast.LENGTH_LONG).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if(!inDLClassifiers)
			{
				progressDialog.setMessage("Téléchargement du fichier vocabulaire...\t " + String.valueOf(values[0]) + '%');
			} else {
				progressDialog.setMessage("Téléchargement des fichiers classifiers...\t " + values[0] + '/' + nbClassifierToDL);
			}
		}
		
		private void analyseScene(Mat src) {
			KeyPoint _scenekeypoints = new KeyPoint();
			Mat _scenedescriptors = new Mat();
			
			// Extraction of the descriptors
			SIFTdetector.detectAndCompute(src, Mat.EMPTY,_scenekeypoints, _scenedescriptors);
			bowide.compute(src, _scenekeypoints, response_hist);			
		}
		
		private String compare(Mat src) {		
			
			analyseScene(src);
			
	      	bowide.setVocabulary(Global.vocabulary);
	      	
	      	float minf = Float.MAX_VALUE;
	      	String bestMatch = null;
	      	for (int i = 0; i < Global.classes.size(); i++)
	        {
         		// classifier prediction based on reconstructed histogram
         		float res = classifiers[i].predict(response_hist, true);
         		//System.out.println(class_names[i] + " is " + res);
         		if (res < minf) {
         			minf = res;
         			bestMatch = Global.classes.get(i).getBrand();
         		}
         	}
	      	return bestMatch;
		}
	}
}
