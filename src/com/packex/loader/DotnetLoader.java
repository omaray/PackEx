package com.packex.loader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.packex.Constants;
import com.packex.model.pkgmgr.DotnetDownloadData;
import com.packex.parser.DotnetHtmlParser;

public class DotnetLoader implements LanguageLoader {
private static final Logger logger = Logger.getLogger(DotnetLoader.class.getName());
    
    private String packageName;
    private String urlHtml;
    private DotnetDownloadData data;
    
    public DotnetLoader(String packageName) {
        this.packageName = packageName;
        this.urlHtml = String.format(Constants.DOTNET_HTML_URL_TEMPLATE, this.packageName);
    }
    
    public void loadData() {
        try {
            Document document = Jsoup.connect(this.urlHtml).get();
            
            DotnetHtmlParser parser = new DotnetHtmlParser(this.packageName);
            this.data = parser.parse(document);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Couldn't connect or parse %s correctly", this.urlHtml), ex);
        }
    }
    
    public DotnetDownloadData getDownloadData() {
        return this.data;
    }
    
    public static void main(String[] args) {
        DotnetLoader loader = new DotnetLoader("Google.Apis.Datastore.v1beta1");
        loader.loadData();
        DotnetDownloadData data = loader.getDownloadData();
        
        System.out.println(data.getDownloads());
    }
}
