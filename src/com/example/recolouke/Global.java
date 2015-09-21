package com.example.recolouke;

import android.app.Application;
import android.graphics.Bitmap;

public class Global extends Application {

	static Bitmap IMG_SELECTED = null;
	
	/* STATIC RESSOURCES FOR PROJECT DEVLOPMENT */
	
	// Array of strings storing brand names
    static String[] brands = new String[] {
        "AMD",
        "Auchan",
        "BMW",
        "Carrefour",
        "Starbucks Coffee",
        "Nike",
        "EA Sports",
        "EDF",
        "FedEx",
        "KFC"
    };
 
    // Array of references to images stored in /res/drawable-mdpi/
    static int[] logoIDs = new int[]{
        R.drawable.amd,
        R.drawable.auchan,
        R.drawable.bmw,
        R.drawable.carrefour,
        R.drawable.starbucks,
        R.drawable.nike,
        R.drawable.ea_sports,
        R.drawable.edf,
        R.drawable.fedex,
        R.drawable.kfc
    };
}
