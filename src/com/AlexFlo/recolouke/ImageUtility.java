package com.AlexFlo.recolouke;

import java.io.File;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtility extends Application {
	
	final static String TAG = "ImageUtility";

	public static String getPath(Context ctx, Uri uri) {
		String valR = null;

		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			valR = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
		}
		cursor.close();
		return valR;
	}

	public static int getCameraPhotoOrientation(Context ctx, Uri imagePath) {
		int rotate = 0;
		try {
			File imageFile = new File(getPath(ctx, imagePath));

			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// ERROR
		}
		return rotate;
	}

	public static Bitmap RotateBitmap(Bitmap src, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}

	public static Uri getDrawableUri(Context ctx, int drawable) {
		Uri imageUri = Uri.parse(
				ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + ctx.getResources().getResourcePackageName(drawable)
						+ '/' + ctx.getResources().getResourceTypeName(drawable) + '/'
						+ ctx.getResources().getResourceEntryName(drawable));
		return imageUri;
	}

	public static Bitmap getDrawableBitmap(Context ctx, int logo) {
		Uri drawableUri = ImageUtility.getDrawableUri(ctx, logo);
		try {
			return MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), drawableUri);
		} catch (Exception e) {
			// Error
			return null;
		}
	}
}
