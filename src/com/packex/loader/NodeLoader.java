package com.packex.loader;

import com.google.gson.Gson;
import com.packex.Constants;
import com.packex.connector.HttpConnector;
import com.packex.model.pkgmgr.NodeDownloadData;

public class NodeLoader {
    private String packageName;
    private String dayURL;
    private String weekURL;
    private String monthURL;
    private NodeDownloadData dayData;
    private NodeDownloadData weekData;
    private NodeDownloadData monthData;
    
    public NodeLoader(String packageName) {
        this.packageName = packageName;
        this.dayURL = String.format(Constants.NODE_DAY_URL_TEMPLATE, this.packageName);
        this.weekURL = String.format(Constants.NODE_WEEK_URL_TEMPLATE, this.packageName);
        this.monthURL = String.format(Constants.NODE_MONTH_URL_TEMPLATE, this.packageName);
    }
    
    public void load() {
        Gson gson = new Gson();
        HttpConnector connector = HttpConnector.getInstance();
        
        String dayResponse = connector.get(this.dayURL);
        this.dayData = gson.fromJson(dayResponse, NodeDownloadData.class);
        
        String weekResponse = connector.get(this.weekURL);
        this.weekData = gson.fromJson(weekResponse, NodeDownloadData.class);
        
        String monthResponse = connector.get(this.monthURL);
        this.monthData = gson.fromJson(monthResponse, NodeDownloadData.class);
    }
    
    public NodeDownloadData getDayDownloadData() {
        return this.dayData;
    }
    
    public NodeDownloadData getWeekDownloadData() {
        return this.weekData;
    }
    
    public NodeDownloadData getMonthDownloadData() {
        return this.monthData;
    }
    
    public static void main(String[] args) {
        NodeLoader loader = new NodeLoader("gcloud");
        loader.load();
        
        NodeDownloadData dayData = loader.getDayDownloadData();
        System.out.println(dayData.getDownloads());
        
        NodeDownloadData weekData = loader.getWeekDownloadData();
        System.out.println(weekData.getDownloads());
        
        NodeDownloadData monthData = loader.getMonthDownloadData();
        System.out.println(monthData.getDownloads());
    }
}
