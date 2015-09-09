package com.example.recolouke;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageUtility extends Application {
	
	public static Bitmap cropCenterBitmap(Bitmap src)
	{	
		int offsetX, offsetY, destWidth, destHeight;
		// Largeur > Hauteur
		if(src.getWidth() > src.getHeight())
		{
			offsetX = (src.getWidth() / 2) - (src.getHeight() / 2 );
			offsetY = 0;
			destWidth = destHeight = src.getHeight();
			
		// Hauteur > Largeur
		} else {
			offsetY = (src.getHeight() / 2) - (src.getWidth() / 2 );
			offsetX = 0;
			destWidth = destHeight = src.getWidth();
		}		
		return Bitmap.createBitmap(src, offsetX, offsetY, destWidth, destHeight);
	}
	
	public static Bitmap cropCenterBitmap(Bitmap src, int squareEdge)
	{	
		//TODO Use of squareEdge for max Square Edge of the return Bitmap
		
		int offsetX, offsetY, destWidth, destHeight;
		// Largeur > Hauteur
		if(src.getWidth() > src.getHeight())
		{
			offsetX = (src.getWidth() / 2) - (src.getHeight() / 2 );
			offsetY = 0;
			destWidth = destHeight = src.getHeight();
			
		// Hauteur > Largeur
		} else {
			offsetY = (src.getHeight() / 2) - (src.getWidth() / 2 );
			offsetX = 0;
			destWidth = destHeight = src.getWidth();
		}		
		return Bitmap.createBitmap(src, offsetX, offsetY, destWidth, destHeight);
	}
	
	public static Bitmap RotateBitmap(Bitmap src, float angle)
	{
	      Matrix matrix = new Matrix();
	      matrix.postRotate(angle);
	      return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}
	
	public static Bitmap convertToGrayscale(Bitmap src)
	{
		Bitmap valR = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.RGB_565);
		// Creation of a Mat
		Mat mat_IMG_SELECTED = new Mat (src.getHeight(), src.getWidth(), CvType.CV_8UC3);
		// Conversion of the Bitmap to a Mat
		Utils.bitmapToMat(src, mat_IMG_SELECTED);
		// Convert the image mat in colors to grayscale
		Imgproc.cvtColor(mat_IMG_SELECTED, mat_IMG_SELECTED, Imgproc.COLOR_RGB2GRAY);
		// Recreate a grayscale bitmap from the mat 
		Utils.matToBitmap(mat_IMG_SELECTED, valR);
		return valR;
	}
	
	public static Mat convertToGrayscaleMat(Bitmap src)
	{
		// Creation of a Mat
		Mat mat_IMG_SELECTED = new Mat (src.getHeight(), src.getWidth(), CvType.CV_8UC3);
		// Conversion of the Bitmap to a Mat
		Utils.bitmapToMat(convertToGrayscale(src), mat_IMG_SELECTED);
		return mat_IMG_SELECTED;
	}
}
