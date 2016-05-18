package com.packex.parser;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.packex.Constants;
import com.packex.model.pkgmgr.DotnetDownloadData;

public class DotnetHtmlParser {
private static final Logger logger = Logger.getLogger(PythonHtmlParser.class.getName());
    
    private String packageName;

    public DotnetHtmlParser(String packageName) {
        this.packageName = packageName;
    }
    
    private boolean isStatsDownloadsElement(Element element) {
        // Get the stat-label entry
        Elements statLabelElements = element.getElementsByClass(Constants.DOTNET_STAT_LABEL);
        if (statLabelElements == null || statLabelElements.size() != 1) {
            logger.log(Level.WARNING, String.format("A stat-label is missing it seems for %s", element.toString()));
        }
        
        String statLabel = statLabelElements.get(0).text();
        if (statLabel.equals(Constants.DOTNET_DOWNLOADS_TEXT)) {
            return true;
        }
        
        return false;
    }
    
    private DotnetDownloadData populateDownloadData(Element element) {
        Elements statNumberElements = element.getElementsByClass(Constants.DOTNET_STAT_NUMBER);
        if (statNumberElements == null || statNumberElements.size() != 1) {
            logger.log(Level.WARNING, String.format("A stat-label is missing it seems for %s", element.toString()));
        }
        
        Integer statValue = 0;
        String statNumber = statNumberElements.get(0).text();
        try {
            Number number = NumberFormat.getNumberInstance(java.util.Locale.US).parse(statNumber);
            statValue = number.intValue();
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, String.format("Couldn't parse the stat-number %s", statNumber));
            return null;
        }
        
        return new DotnetDownloadData(statValue);
    }

    public DotnetDownloadData parse(Document document) {        
        // Find the id element (which is unique) and then the elements under it
        Element idElement = document.getElementById(Constants.DOTNET_ID_SEARCH);
        Elements classElements = idElement.getElementsByClass(Constants.DOTNET_CLASS_SEARCH);
        
        // Find the element with downloads info
        Element downloadsElement = null;
        for (Element element : classElements) {
            if (this.isStatsDownloadsElement(element)) {
                downloadsElement = element;
                break;
            }
        }
        
        if (downloadsElement == null) {
            logger.log(Level.SEVERE, String.format("Could not find the Downloads stat-label for %s", this.packageName));
            return null;
        }
        
        // Fill out the downloads data
        return this.populateDownloadData(downloadsElement);
    }
    
    public static void main(String[] args) throws Exception {
        DotnetHtmlParser parser = new DotnetHtmlParser("Google.Apis.Datastore.v1beta1");
        Document doc = Jsoup.connect("https://www.nuget.org/packages/Google.Apis.Datastore.v1beta1/").get();
        DotnetDownloadData data = parser.parse(doc);
        
        System.out.println(data.getDownloads());
    }
}
