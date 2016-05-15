package com.packex.parser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.packex.Constants;
import com.packex.model.pkgmgr.PythonDownloadData;

public class PythonHtmlParser {
    private static final Logger logger = Logger.getLogger(PythonHtmlParser.class.getName());
    
    private String packageName;

    public PythonHtmlParser(String packageName) {
        this.packageName = packageName;
    }
    
    private boolean isDownloadsElement(Element element) {
        for (Node node : element.childNodes()) {
            if (node.toString().contains(Constants.PYTHON_DOWNLOADS_TEXT)) {
                return true;
            }
        }
        
        return false;
    }
    
    private PythonDownloadData populateDownloadData(Element downloadsElement) {
        Integer dayDownloads = 0;
        Integer weekDownloads = 0;
        Integer monthDownloads = 0;
        
        Elements liElements = downloadsElement.getElementsByTag("li");
        for (Element liElement : liElements) {
            Elements spanElements = liElement.getElementsByTag("span");
            if (spanElements != null && spanElements.size() == 1) {
                Element spanElement = spanElements.get(0);
                if (liElement.text().contains(Constants.PYTHON_DOWNLOADS_LAST_DAY)) {
                    dayDownloads = Integer.parseInt(spanElement.text());
                }
                
                if (liElement.text().contains(Constants.PYTHON_DOWNLOADS_LAST_WEEK)) {
                    weekDownloads = Integer.parseInt(spanElement.text());
                }
                
                if (liElement.text().contains(Constants.PYTHON_DOWNLOADS_LAST_MONTH)) {
                    monthDownloads = Integer.parseInt(spanElement.text());
                }
            }
        }
        
        return new PythonDownloadData(dayDownloads, weekDownloads, monthDownloads);
    }
    
    public PythonDownloadData parse(Document document) {
        Elements classElements = document.getElementsByClass(Constants.PYTHON_CLASS_SEARCH);
        
        // Find the element with downloads info
        Element downloadsElement = null;
        for (Element element : classElements) {
            if (this.isDownloadsElement(element)) {
                downloadsElement = element;
                break;
            }
        }
        
        if (downloadsElement == null) {
            logger.log(Level.SEVERE, String.format("Could not find the downloads <ul> for %s", this.packageName));
            return null;
        }
        
        // Fill out the downloads data
        return this.populateDownloadData(downloadsElement);
    }
    
    public static void main(String[] args) throws Exception {
        PythonHtmlParser parser = new PythonHtmlParser("aws");
        Document doc = Jsoup.connect("https://pypi.python.org/pypi/aws").get();
        PythonDownloadData data = parser.parse(doc);
        
        System.out.println(data.getDayDownloads());
        System.out.println(data.getWeekDownloads());
        System.out.println(data.getMonthDownloads());
    }
}
