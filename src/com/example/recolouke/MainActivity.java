package com.example.recolouke;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	final int RESULT_CAMERA = 200;
	final int RESULT_ACCESS_GALLERY = 300;
	final int RETURN_ANALYSIS = 400;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btnPhoto).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCaptureActivity();

			}
		});

		findViewById(R.id.btnGallery).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startGalleryActivity();
			}
		});

		findViewById(R.id.btnAnalyse).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startComparisonActivity();
			}
		});
	}

	private void startCaptureActivity() {
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), RESULT_CAMERA);
	}

	private void startGalleryActivity() {
		startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), RESULT_ACCESS_GALLERY);
	}

	private void startComparisonActivity() {
		startActivityForResult(new Intent(MainActivity.this, AnalyseActivity.class), RETURN_ANALYSIS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_CAMERA:
			if (resultCode == RESULT_OK) {
				Global.IMG_SELECTED = ((Bitmap) data.getExtras().get("data"));
				((ImageView) findViewById(R.id.imgView)).setImageBitmap(Global.IMG_SELECTED);
				findViewById(R.id.btnAnalyse).setVisibility(View.VISIBLE);
			} else {
				// Error
			}
			break;
		case RESULT_ACCESS_GALLERY:
			if (resultCode == RESULT_OK) {
				// Récupére le fichier image correspondant
				try {
					Global.IMG_SELECTED = ImageUtility.RotateBitmap(
							MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()),
							ImageUtility.getCameraPhotoOrientation(this, data.getData()));
					((ImageView) findViewById(R.id.imgView)).setImageBitmap(Global.IMG_SELECTED);
					findViewById(R.id.btnAnalyse).setVisibility(View.VISIBLE);
				} catch (IOException e) {
					// Error
				}
			} else {
				// Error
			}
			break;
		case RETURN_ANALYSIS:
			((ImageView) findViewById(R.id.imgView)).setImageBitmap(null);
			findViewById(R.id.btnAnalyse).setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
