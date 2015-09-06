package com.example.recolouke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AnalyseActivity extends Activity {

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
		
		Global.IMG_SELECTED_CROPPED = Global.IMG_SELECTED;
		((ImageView) findViewById(R.id.imgToAnalyse)).setImageBitmap(Global.IMG_SELECTED_CROPPED);
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
