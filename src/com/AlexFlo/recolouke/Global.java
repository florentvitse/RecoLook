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
	static String vocabFile = null;
	static List<Classifier> classifiers = null;
	static Mat vocabulary = null;

	public static Mat parseVocabulary(String jsonFile) {
		if (vocabulary == null) {
			loadVocabulary(jsonFile);
		}
		return vocabulary;
	}

	public static void loadVocabulary(String jsonFile) {
		if (jsonFile != null) {
			try {
				JSONObject jsonObject = new JSONObject(jsonFile);
				
				// TODO LOAD THE MAT VOCABULARY
				
			} catch (JSONException e) {
				vocabulary = null;
				Log.e(TAG, "Le parsing du fichier JSON a échoué");
			}
		}
	}
	
	public static void unloadVocabulary() {
		vocabulary = null;
	}

	public static List<Classifier> parseClassifiers(String jsonFile) {
		if (classifiers == null) {
			loadClassifiers(jsonFile);
		}
		return classifiers;
	}

	private static void loadClassifiers(String jsonFile) {
		final String TAG_BRANDS = "brands";
		final String TAG_VOCAB = "vocabulary";
		final String TAG_FIELD_BRAND = "brandname";
		final String TAG_FIELD_BRANDURL = "url";
		final String TAG_FIELD_FILE = "classifier";
		final String TAG_FIELD_IMAGES = "images";

		// Parsing of file
		if (jsonFile != null) {
			try {
				JSONObject jsonObject = new JSONObject(jsonFile);
				// Get an array of brands
				JSONArray brands = jsonObject.getJSONArray(TAG_BRANDS);

				classifiers = new LinkedList<Classifier>();
				// For each brand, get settings
				for (int i = 0; i < brands.length(); i++) {
					JSONObject obj = brands.getJSONObject(i);
					String brand = obj.getString(TAG_FIELD_BRAND);
					String url = obj.getString(TAG_FIELD_BRANDURL);
					String file = obj.getString(TAG_FIELD_FILE);
					JSONArray images = obj.getJSONArray(TAG_FIELD_IMAGES);
					StringBuilder imgs = new StringBuilder();
					if (images != null) {
						for (int j = 0; j < images.length(); j++) {
							imgs.append(images.getString(j) + ';');
						}
					}
					// Add of an object classifier
					classifiers.add(new Classifier(file, brand, url, imgs.toString()));
				}
				// Get the filename of vocabulary file
				vocabFile = jsonObject.getString(TAG_VOCAB);
			} catch (JSONException e) {
				classifiers = null;
				Log.e(TAG, "Le parsing du fichier JSON a échoué");
			}
		}
	}

	public static void unloadClassifiers() {
		classifiers = null;
	}
}
