package com.packex.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.PhpLoader;
import com.packex.model.pkgmgr.PhpDownloadData;

public class PhpManager implements LanguageManager{
    private String company;
    private String packageName;
    private String category;
    private String datasetName;
    private String tableName;

    public PhpManager(String company, String packageName, String category) {
        this.company = company;
        this.packageName = packageName;
        this.category = category;
        this.datasetName = Util.getDatasetName();
        this.tableName = Util.getTableName(this.company);
    }
    
    public void saveData() {
        PhpLoader loader = new PhpLoader(this.packageName);
        loader.loadData();
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.begin(this.datasetName, this.tableName);
        
        Map<String, Object> row = new HashMap<String, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = new Date();
        
        // Core fields
        row.put(Constants.DATE_FIELD, df.format(dateObj) + Constants.NEW_DAY_TIME);
        row.put(Constants.LANGUAGE_FIELD, Constants.PHP_LANGUAGE);
        row.put(Constants.PACKAGE_NAME_FIELD, this.packageName);
        row.put(Constants.VERSION_FIELD, Constants.ALL_VERSIONS);
        row.put(Constants.CATEGORY_FIELD, this.category);
        
        // Download fields
        PhpDownloadData data = loader.getDownloadData();
        row.put(Constants.TOTAL_DOWNLOADS_FIELD, data.getTotal());
        row.put(Constants.MONTHLY_DOWNLOADS_FIELD, data.getMonthly());
        row.put(Constants.DAILY_DOWNLOADS_FIELD, data.getDaily());
        
        connector.addRow(row);
        connector.commit();
    }
    
    public static void main(String[] args) {
        PhpManager manager = new PhpManager("google", "google/cloud", "cloud");
        manager.saveData();
    }
}
