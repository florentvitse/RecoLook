package com.AlexFlo.recolouke;

import java.io.*;
import java.net.URL;

public class URLReader {
    public static String readURLData(String fullURL) throws Exception {

    	String dataReturn = null;
    	
        URL url = new URL(fullURL);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
        	dataReturn += inputLine;
        in.close();
		return dataReturn;
    }
}
