package com.example.recolouke;

import android.app.Application;
import android.graphics.Bitmap;

public class Global extends Application {

	static Bitmap IMG_SELECTED = null;
	
	/* STATIC RESSOURCES FOR PROJECT DEVELOPMENT */
	
	// Array of strings storing brand names
    static String[] brands = new String[] {
        "AMD",
        "Arsenal",
        "Burger King",
        "Starbucks Coffee",
        "Nike",
        "Facebook",
        "FedEx",
        "Hewlett Packard",
        "KFC"
    };
 
    // Array of references to images stored in /res/drawable-mdpi/
    static int[] logoIDs = new int[]{
        R.drawable.amd,
        R.drawable.arsenal,
        R.drawable.burger_king,
        R.drawable.starbucks,
        R.drawable.nike,
        R.drawable.facebook,
        R.drawable.fedex,
        R.drawable.hp,
        R.drawable.kfc
    };
    
    static int MAX_RESULT_DISPLAYED = 3;
    
	static int getPositionLogo(int id)
	{
		int valR = 0;
		for(int temp : logoIDs)
		{	
			if(temp == id) return valR;
			valR++;
		}
		return -1;
	}
}
