package com.AlexFlo.recolouke;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import org.json.*;
import org.opencv.core.Mat;

public class Global extends Application {

	final static String TAG = "[Global]";
	static Bitmap IMG_SELECTED = null;
	static List<Classifier> classifiers = null;

	public static String getVocabularyFileName(String fullUrl)
	{
		final String TAG_VOCAB = "vocabulary";
		
		try {
			String idx = URLReader.readURLData(fullUrl);
			if (idx != null) {
				try {
					JSONObject jsonObject = new JSONObject(idx);
					return jsonObject.getString(TAG_VOCAB);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			Log.e(TAG, "Erreur lors de la tentative de lecture du fichier à l'adresse : " + fullUrl);
		}
		return null;	
	}
	
	public static Mat getVocabulary(String fullUrl)
	{		
		try {
			String idx = URLReader.readURLData(fullUrl);
			if (idx != null) {
				//TODO LOAD THE MAT VOCABULARY
			}

		} catch (Exception e) {
			Log.e(TAG, "Erreur lors de la tentative de lecture du fichier à l'adresse : " + fullUrl);
		}	
		return null;
	}
		
	public static List<Classifier> getClassifiers(String fullUrl)
	{
		if(classifiers == null)
		{
			return loadClassifiers(fullUrl);
		} else {
			return classifiers;
		}
	}
	
	private static List<Classifier> loadClassifiers(String fullUrl) {
		final String TAG_BRANDS = "brands";
		final String TAG_FIELD_BRAND = "brandname";
		final String TAG_FIELD_BRANDURL = "url";
		final String TAG_FIELD_FILE = "classifier";
		final String TAG_FIELD_IMAGES = "images";

		List<Classifier> classifiers = null;

		// Chargement de l'index
		String idx;
		try {
			idx = URLReader.readURLData(fullUrl);
			if (idx != null) {
				try {
					JSONObject jsonObject = new JSONObject(idx);
					JSONArray brands = jsonObject.getJSONArray(TAG_BRANDS);

					classifiers = new LinkedList<Classifier>();
					for (int i = 0; i < brands.length(); i++) {
						JSONObject obj = brands.getJSONObject(i);
						String brand = obj.getString(TAG_FIELD_BRAND);
						String url = obj.getString(TAG_FIELD_BRANDURL);
						String file = obj.getString(TAG_FIELD_FILE);
						JSONArray images = obj.getJSONArray(TAG_FIELD_IMAGES);
						List<String> imgs = null;
						if (images != null) {
							imgs = new LinkedList<String>();
							for (int j = 0; j < images.length(); j++) {
								imgs.add(images.getString(j));
							}
						}
						//Ajout du classifier parsé
						classifiers.add(new Classifier(file, brand, url, (String[]) imgs.toArray()));						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Erreur lors de la tentative de lecture du fichier à l'adresse : " + fullUrl);
		}
		return classifiers;
	}
	
	public static void unloadClassifiers()
	{
		classifiers = null;
	}
}
