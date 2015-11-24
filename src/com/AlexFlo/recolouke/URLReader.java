package com.AlexFlo.recolouke;

import java.io.*;
import java.net.URL;

public class URLReader {
    public static String readURLData(String fullURL) throws Exception {
    	
        URL url = new URL(fullURL);
        BufferedReader in = new BufferedReader(
        		new InputStreamReader(
        				url.openStream()));

    	StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
        	response.append(inputLine);
        }
        in.close();
		return response.toString();
    }
}