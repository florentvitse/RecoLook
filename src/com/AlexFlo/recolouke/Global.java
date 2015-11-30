package com.AlexFlo.recolouke;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.*;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Global extends Application {

	final static String TAG = "[Global]";
	static Bitmap IMG_SELECTED = null;
	static String vocabFile = null;
	static List<Classe> classes = null;
	static Mat vocabulary = null;

	public static Mat parseVocabulary(String jsonFile) {
		if (vocabulary == null) {
			loadVocabulary(jsonFile);
		}
		return vocabulary;
	}

	public static void loadVocabulary(String ymlFile) {
		if (ymlFile != null) {
			try {
				int startArray = ymlFile.indexOf("data:") + 7;
				String tab = ymlFile.substring(startArray, (ymlFile.length()));
				List<Float> floatVocab = new LinkedList<Float>();
				floatVocab.addAll(tabStringtoFloat(tab));
				vocabulary = org.opencv.utils.Converters.vector_float_to_Mat(floatVocab);
			} catch (Exception e) {
				vocabulary = null;
				Log.e(TAG, "Erreur lors de la transcription du vocabulaire");
			}
		}
	}

	public static void unloadVocabulary() {
		vocabulary = null;
	}

	public static List<Classe> parseClasses(String jsonFile) {
		if (classes == null) {
			loadClasses(jsonFile);
		}
		return classes;
	}

	private static void loadClasses(String jsonFile) {
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

				classes = new LinkedList<Classe>();
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
					classes.add(new Classe(file, brand, url, imgs.toString()));
				}
				// Get the filename of vocabulary file
				vocabFile = jsonObject.getString(TAG_VOCAB);
			} catch (JSONException e) {
				classes = null;
				Log.e(TAG, "Le parsing du fichier JSON a échoué");
			}
		}
	}

	public static void unloadClassifiers() {
		classes = null;
	}

	private static List<Float> tabStringtoFloat(String tab) {
		List<Float> tabReturn = null;
		if (tab != null) {
			String[] tabString = tab.split(",");
			tabReturn = new LinkedList<Float>();
			for (String s : tabString) {
				try {
					tabReturn.add(Float.valueOf(s));
				} catch (NumberFormatException e) {
					Log.e(TAG, "Le parsing du fichier YML a échoué");
				}
			}
		}
		return tabReturn;
	}

	public static String[] getClassifiersFileNames() {
		if (classes == null) {
			return null;
		} else {
			String[] filenames = new String[classes.size()];
			int index = 0;
			for(Classe el : classes)
			{
				filenames[index] = new String();
				filenames[index] = el.getClassifierFile();
				index++;
			}
			return filenames;
		}
	}
}
