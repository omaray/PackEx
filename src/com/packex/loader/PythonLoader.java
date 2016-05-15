package com.packex.loader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.packex.Constants;
import com.packex.connector.HttpConnector;
import com.packex.model.pkgmgr.PythonDownloadData;
import com.packex.model.pkgmgr.PythonRawData;
import com.packex.model.pkgmgr.PythonRelease;
import com.packex.model.pkgmgr.PythonReleaseList;
import com.packex.parser.PythonHtmlParser;
import com.packex.parser.ReleaseDeserializer;
import com.packex.parser.ReleaseListDeserializer;

public class PythonLoader {
    private static final Logger logger = Logger.getLogger(PythonLoader.class.getName());
    
    private String packageName;
    private String urlJson;
    private String urlHtml;
    private PythonDownloadData data;
    
    public PythonLoader(String packageName) {
        this.packageName = packageName;
        this.urlJson = String.format(Constants.PYTHON_URL_TEMPLATE, this.packageName);
        this.urlHtml = String.format(Constants.PYTHON_HTML_URL_TEMPLATE, this.packageName);
    }
    
    public void loadFromHtml() {
        try {
            Document document = Jsoup.connect(this.urlHtml).get();
            
            PythonHtmlParser parser = new PythonHtmlParser(this.packageName);
            this.data = parser.parse(document);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Couldn't connect to %s using Jsoup", this.urlHtml), ex);
        }
    }
    
    // INCOMPLETE IMPLEMENTATION SINCE PYPI IS BROKEN
    public void load() {
        HttpConnector connector = HttpConnector.getInstance();
        String response = connector.get(this.urlJson);
        
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(PythonReleaseList.class, new ReleaseListDeserializer());
        builder.registerTypeAdapter(PythonRelease.class, new ReleaseDeserializer());
        Gson gson = builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        PythonRawData rawData = gson.fromJson(response, PythonRawData.class);
        
        PythonReleaseList releaseList = rawData.getReleases();
        for (PythonRelease release : releaseList.getReleaseList()) {
            System.out.println(release.getVersion());
        }
    }
    
    public PythonDownloadData getDownloadData() {
        return this.data;
    }
    
    public static void main(String[] args) {
        PythonLoader loader = new PythonLoader("boto3");
        loader.loadFromHtml();
        PythonDownloadData data = loader.getDownloadData();
        
        System.out.println(data.getDayDownloads());
        System.out.println(data.getWeekDownloads());
        System.out.println(data.getMonthDownloads());
    }
}
