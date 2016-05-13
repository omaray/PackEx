package com.packex.loader;

import com.google.gson.Gson;
import com.packex.Constants;
import com.packex.connector.HttpConnector;
import com.packex.model.pkgmgr.PhpDownloadData;
import com.packex.model.pkgmgr.PhpRawData;

public class PhpLoader {
    private String packageName;
    private String url;
    private PhpDownloadData data;
    
    public PhpLoader(String packageName) {
        this.packageName = packageName;
        this.url = String.format(Constants.PHP_URL_TEMPLATE, this.packageName);
    }
    
    public void load() {
        HttpConnector connector = HttpConnector.getInstance();
        String response = connector.get(this.url);
        
        Gson gson = new Gson();
        PhpRawData rawData = gson.fromJson(response, PhpRawData.class);
        this.data = rawData.getPackageData().getDownloads();
    }
    
    public PhpDownloadData getDownloadData() {
        return this.data;
    }
    
    public static void main(String[] args) {
        PhpLoader loader = new PhpLoader("google/cloud");
        loader.load();
        PhpDownloadData data = loader.getDownloadData();
        
        System.out.println(data.getTotal());
        System.out.println(data.getMonthly());
        System.out.println(data.getDaily());
    }
}
