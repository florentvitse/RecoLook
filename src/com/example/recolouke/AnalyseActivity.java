package com.example.recolouke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AnalyseActivity extends Activity {

	final static String TAG = "[Activity] - AnalyseActivity";
	
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
		
		findViewById(R.id.btnTestNext).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(AnalyseActivity.this, ShowAnalyse.class), 2);
			}
		});
		
		// For having the same caracterics on the gray image
		Global.IMG_SELECTED_GRAY = Bitmap.createBitmap(Global.IMG_SELECTED);
		// Creation of a Mat
		Mat mat_IMG_SELECTED = new Mat (Global.IMG_SELECTED.getHeight(), Global.IMG_SELECTED.getWidth(), CvType.CV_8UC3);
		// Conversion of the image to the Mat
		Utils.bitmapToMat(Global.IMG_SELECTED, mat_IMG_SELECTED);
		// Convert the image mat from color to gray
		Imgproc.cvtColor(mat_IMG_SELECTED, mat_IMG_SELECTED, Imgproc.COLOR_RGB2GRAY);
		// Recreate a bitmap from the mat 
		// (just for display if the result works here otherwise normally we display the original image)
		Utils.matToBitmap(mat_IMG_SELECTED, Global.IMG_SELECTED_GRAY);
		
		((ImageView) findViewById(R.id.imgToAnalyse)).setImageBitmap(Global.IMG_SELECTED_GRAY);	
		
		// Display a list of logo
		//TODO (gap between item to fix)
		
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
        ((ListView) findViewById(R.id.listview_widget)).setAdapter(adapter);
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
}
