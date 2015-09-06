package com.example.recolouke;

import android.app.Application;
import android.graphics.Bitmap;
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
}
