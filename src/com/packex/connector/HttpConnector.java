package com.packex.connector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpConnector {    
    private static final Logger logger = Logger.getLogger(HttpConnector.class.getName());
    
    private static HttpConnector instance;
    
    private HttpConnector() { }
    
    public static HttpConnector getInstance() {
        if (instance == null) {
            instance = new HttpConnector();
        }
        
        return instance;
    }
    
    private URLConnection getURLConnection(String baseUrl, String queryParameters) {
        URLConnection connection = null;
        try {
            String url = baseUrl;
            if (queryParameters != null)
                url = url + "?" + queryParameters;
            connection = new URL(url).openConnection();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error opening the connection", ex);
        }
        
        return connection;
    }
    
    private String getResponse(URLConnection urlConnection) {
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = null;
        
        try {
            InputStream inputStream = urlConnection.getInputStream();    
            rd = new BufferedReader(new InputStreamReader(inputStream));
             
            String line;
            while((line = rd.readLine()) != null) {
              sb.append(line);
              sb.append('\r');
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error reading from the stream", ex);
        } finally {
            try {
                rd.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return sb.toString();
    }
    
    public String get(String url) {
        URLConnection connection = getURLConnection(url, null);
        String response = getResponse(connection);
        return response;
    }
    
    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        String response = connector.get("https://api.npmjs.org/downloads/point/2016-02-01/gcloud");
        System.out.println(response);
    }
}
