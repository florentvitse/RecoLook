package com.AlexFlo.recolouke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import com.example.recolouke.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@SuppressLint("UseSparseArrays")
public class AnalyseActivity extends Activity {

	final static String TAG = "AnalyseActivity";

	static {
		if (!OpenCVLoader.initDebug()) {
			// ERROR - Initialization Error
			Log.e(TAG, "OpenCV - Initialization Error");
		}
	}

	private Integer[] displayedDrawables = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analyse);

		findViewById(R.id.btnReturn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Global.IMG_SELECTED = null;
				setResult(RESULT_OK, null);
				finish();
			}
		});

		((ImageView) findViewById(R.id.imgToAnalyse)).setImageBitmap(Global.IMG_SELECTED);

		/** Construct the list of logo **/
		displayedDrawables = new Integer[Global.MAX_RESULT_DISPLAYED];
		HashMap<Integer, Integer> resultComparator = analyseScene(ImageUtility.convertToGrayscaleMat(Global.IMG_SELECTED),
				FeatureDetector.ORB,
				DescriptorExtractor.ORB);
		
		// Keys used in Hashmap
		String[] from = { "brand", "logo" };

		// Ids of views in listview layout
		int[] to = { R.id.brand, R.id.logo };
		
		// For each row in the list which stores logo and brand
		List<HashMap<String, String>> adapterList = new ArrayList<HashMap<String, String>>();
		
		int lastMax = 1000;		
		for (int i = 0; i < Global.MAX_RESULT_DISPLAYED; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			int oneEntry = getMax(resultComparator, lastMax);
			hm.put("brand", Global.brands[Global.getPositionLogo(oneEntry)]);
			hm.put("logo", String.valueOf(oneEntry));
			displayedDrawables[i] = oneEntry;
			lastMax = resultComparator.get(oneEntry);
		
			adapterList.add(hm);
		}

		// Instantiating an adapter to store each items
		// R.layout.listview defines the layout of each item
		// We put one string and one image in each item
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), adapterList, R.layout.listview, from, to);

		// Getting a reference to listview (composant) to apply the adapter
		ListView lv = ((ListView) findViewById(R.id.listview_widget));
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent _showAnalyse = new Intent(AnalyseActivity.this, ShowAnalyse.class);
				_showAnalyse.putExtra("drawableSelected", displayedDrawables[position]);
				startActivityForResult(_showAnalyse, 0);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.analyse, menu);
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

	private HashMap<Integer, Mat> calculateDescriptors(int detector, int descriptorExtractor) {
		FeatureDetector _detector = FeatureDetector.create(detector);
		DescriptorExtractor _descriptor = DescriptorExtractor.create(descriptorExtractor);

		HashMap<Integer, Mat> logoDescriptors = new HashMap<Integer, Mat>();
		
		for (int logo : Global.logoIDs) {
			// Object for the object image
			MatOfKeyPoint _objectkeypoints = new MatOfKeyPoint();

			// Get a Bitmap logo to compare
			Mat objToCompare = new Mat();
			try {
				objToCompare = ImageUtility.convertToGrayscaleMat(ImageUtility.getDrawableBitmap(this, logo));
			} catch (Exception e) {
				// Impossible error except if the memory become unreacheable
			}
			_detector.detect(objToCompare, _objectkeypoints);

			// Log.w(TAG, "* Number of keypoints (object) *");
			// Log.w(TAG, String.valueOf(_objectkeypoints.size()));

			Mat _descriptors_object = new Mat();
			_descriptor.compute(objToCompare, _objectkeypoints, _descriptors_object);

			logoDescriptors.put(logo, _descriptors_object);
		}
		return logoDescriptors;
	}

	private int descriptorsMatcher(Mat descriptorObject, Mat descriptorScene) {
		// Creation of the matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		// Vector of matches
		MatOfDMatch matches = new MatOfDMatch();

		// Comparison
		matcher.match(descriptorObject, descriptorScene, matches);

		// Log.w(TAG, "* Number of matches *");
		// Log.w(TAG, String.valueOf(matches.size()));

		List<DMatch> matchesList = matches.toList();

		// Les "bons" appariements (i.e. leur distance est < 60 )
		LinkedList<DMatch> goodMatchesArray = new LinkedList<DMatch>();

		for (DMatch el : matchesList) {

			if (el.distance < 60f) {
				goodMatchesArray.add(el);
			}
		}
		return goodMatchesArray.size();
	}

	public HashMap<Integer, Integer> analyseScene(Mat srcGrayscale, int detector, int descriptorExtractor) {
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

		//Log.w(TAG, "* Number of descriptor (scene) *");
		//Log.w(TAG, String.valueOf(_descriptors_scene.size()));

		HashMap<Integer, Integer> resultComparator = new HashMap<Integer, Integer>();

		HashMap<Integer, Mat> logoDescriptors = new HashMap<Integer, Mat>();
		
		logoDescriptors.putAll(calculateDescriptors(detector, descriptorExtractor));
		Iterator<Map.Entry<Integer, Mat>> it = logoDescriptors.entrySet().iterator();

		while (it.hasNext()) 
		{
		  Entry<Integer, Mat> entry = it.next();
		  resultComparator.put(entry.getKey(), descriptorsMatcher(entry.getValue(), _descriptors_scene));
		}
		
		return resultComparator;
	
		/*
		 * Mat featuredImg = new Mat(); Scalar kpColor = new
		 * Scalar(255,159,10);//this will be color of keypoints //featuredImg
		 * will be the output of first image
		 * Features2d.drawKeypoints(objToCompare, _objectkeypoints, featuredImg
		 * , kpColor, 0);
		 * 
		 * Bitmap a = null; try { a =
		 * MediaStore.Images.Media.getBitmap(getContentResolver(), drawableUri);
		 * } catch (FileNotFoundException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } Bitmap imageMatched
		 * = Bitmap.createBitmap(a.getWidth() , a.getHeight(),
		 * Bitmap.Config.RGB_565); Utils.matToBitmap(featuredImg,
		 * Global.IMG_SELECTED); ((ImageView)
		 * findViewById(R.id.imgToAnalyse)).setImageBitmap(Global.IMG_SELECTED);
		 */

	}
	
	private int getMax(HashMap<Integer, Integer> resultAnalyse, int lastMax)
	{
		int keyMax = 0;
		int tempMax = 0;
		
		Iterator<Entry<Integer, Integer>> it = resultAnalyse.entrySet().iterator();

		while (it.hasNext()) 
		{
		  Entry<Integer, Integer> entry = it.next();
		  if(entry.getValue() > tempMax && entry.getValue() < lastMax)
		  {
			  tempMax = entry.getValue();
			  keyMax = entry.getKey();
		  }
		}
		return keyMax;
	}
}