package com.packex.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.packex.Constants;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.RubyLoader;
import com.packex.model.pkgmgr.RubyDownloadData;

public class RubyManager {
    private String company;
    private String packageName;
    private String category;
    private String tableName;
    
    public RubyManager(String company, String packageName, String category) {
        this.company = company;
        this.packageName = packageName;
        this.category = category;
        this.tableName = String.format(Constants.BQ_TABLE_NAME, this.company);
    }
    
    public void saveData() {
        RubyLoader loader = new RubyLoader(this.packageName);
        loader.load();
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.begin(Constants.BQ_DATASET_NAME, this.tableName);
        
        Map<String, Object> row = new HashMap<String, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = new Date();
        
        // Core fields
        row.put(Constants.DATE_FIELD, df.format(dateObj) + Constants.NEW_DAY_TIME);
        row.put(Constants.LANGUAGE_FIELD, Constants.RUBY_LANGUAGE);
        row.put(Constants.PACKAGE_NAME_FIELD, this.packageName);
        row.put(Constants.VERSION_FIELD, Constants.ALL_VERSIONS);
        row.put(Constants.CATEGORY_FIELD, this.category);
        
        // Download fields
        RubyDownloadData data = loader.getDownloadData();
        row.put(Constants.TOTAL_DOWNLOADS_FIELD, data.getDownloads());
        
        connector.addRow(row);
        connector.commit();
    }
    
    public static void main(String[] args) {
        RubyManager rubyManager = new RubyManager("google", "gcloud", "cloud");
        rubyManager.saveData();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj));
    }
}
