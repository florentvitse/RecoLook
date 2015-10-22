package com.AlexFlo.recolouke;

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

		// Get the position of the image clicked in the previous list
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			try {
				Uri uri = ImageUtility.getDrawableUri(this, extras.getInt("drawableSelected"));
				((ImageView) findViewById(R.id.imgReferenceSelected)).setImageURI(uri);
			} catch (Exception e) {
				// Error
			}
		}
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
}
