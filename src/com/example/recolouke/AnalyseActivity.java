package com.example.recolouke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

public class AnalyseActivity extends Activity {

	final static String TAG = "[AnalyseActivity]";
	
	static {
		if(!OpenCVLoader.initDebug()){
			// ERROR - Initialization Error
			Log.e(TAG, "OpenCV - Initialization Error");
		}
	}
	
	// Array of strings storing brand names
    String[] brands = new String[] {
        "AMD",
        "Auchan",
        "BMW",
        "Carrefour",
        "Disney",
        "E Leclerc",
        "EA Sports",
        "EDF",
        "FedEx",
        "KFC"
    };
 
    // Array of references to images stored in /res/drawable-mdpi/
    int[] logoIDs = new int[]{
        R.drawable.amd,
        R.drawable.auchan,
        R.drawable.bmw,
        R.drawable.carrefour,
        R.drawable.disney,
        R.drawable.e_leclerc,
        R.drawable.ea_sports,
        R.drawable.edf,
        R.drawable.fedex,
        R.drawable.kfc
    };
	
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
		
		/** Display a list of logo **/
		
		// For each row in the list which stores logo and brand
        List<HashMap<String,String>> adapterList = new ArrayList<HashMap<String,String>>();
 
        for(int i = 0 ; i < brands.length; i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("brand", brands[i]);
            hm.put("logo", String.valueOf(logoIDs[i]) );
            adapterList.add(hm);
        }
 
        // Keys used in Hashmap
        String[] from = { "brand", "logo" };
 
        // Ids of views in listview layout
        int[] to = { R.id.brand, R.id.logo };
 
        // Instantiating an adapter to store each items
        // R.layout.listview defines the layout of each item
        // We put one image and one string in each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), adapterList, R.layout.listview, from, to);
 
        // Getting a reference to listview (composant) to apply the adapter
        ListView lv = null;
        lv = ((ListView) findViewById(R.id.listview_widget));
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new OnItemClickListener()
        {
            @Override 
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            { 
                Toast.makeText(AnalyseActivity.this, "Item selected position : " + position, Toast.LENGTH_SHORT).show();
            }
        });
        
        // analyseScene(ImageUtility.convertToGrayscaleMat(Global.IMG_SELECTED), FeatureDetector.ORB, DescriptorExtractor.ORB);
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
	
	public void analyseScene(Mat srcGrayscale, int featureDetector, int descriptorExtractor)
	{
		// Creation of the detector
		// FeatureDetector.ORB to give to the method here (generic method also)
		FeatureDetector _detector = FeatureDetector.create(featureDetector);
		// Creation of the descriptor
		// DescriptorExtractor.ORB to give to the method here (generic method also)
		DescriptorExtractor _descriptor = DescriptorExtractor.create(descriptorExtractor);
		// Creation of the matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		// Vector of matches
		MatOfDMatch matches = new MatOfDMatch();
		
		// Object that will store the keypoint of the scene
		MatOfKeyPoint _scenekeypoints = new MatOfKeyPoint();
		// Detection of the keyPoints of the scene
		_detector.detect(srcGrayscale, _scenekeypoints);
		Mat _descriptors_scene = new Mat();
		// Extraction of the descriptors
        _descriptor.compute(srcGrayscale, _scenekeypoints, _descriptors_scene);
		
        // Object that will store the keypoint of the scene
		MatOfKeyPoint _objectkeypoints = new MatOfKeyPoint();
		// Detection of the keyPoints of the scene
		_detector.detect(srcGrayscale, _objectkeypoints);
		Mat _descriptors_object = new Mat();
		// Extraction of the descriptors
		
		// Get a Bitmap logo to compare
		Uri drawableUri = ImageUtility.getDrawableUri(this, logoIDs[0]);
		Mat objToCompare = null;
		try {
			objToCompare = ImageUtility.convertToGrayscaleMat(MediaStore.Images.Media.getBitmap(getContentResolver(), drawableUri));
		} catch (Exception e) {
			// Error
		}
		_detector.detect(objToCompare, _objectkeypoints);
        _descriptor.compute(objToCompare, _objectkeypoints, _descriptors_object);
        
        //TODO Comparison
        
        matcher.match(_descriptors_object, _descriptors_scene, matches);
        // Display in the logCat console (level ) the number of 'matches' the system thinks it have found
        Log.w(TAG, String.valueOf(matches.total()));
		
       
		
	}	
}
