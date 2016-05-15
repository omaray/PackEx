package com.packex.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.NodeLoader;
import com.packex.model.pkgmgr.NodeDownloadData;

public class NodeManager implements LanguageManager {
    private String company;
    private String packageName;
    private String category;
    private String datasetName;
    private String tableName;

    public NodeManager(String company, String packageName, String category) {
        this.company = company;
        this.packageName = packageName;
        this.category = category;
        this.datasetName = Util.getDatasetName();
        this.tableName = Util.getTableName(this.company);
    }

    public void saveData() {
        NodeLoader loader = new NodeLoader(this.packageName);
        loader.load();
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.begin(this.datasetName, this.tableName);
        
        Map<String, Object> row = new HashMap<String, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = new Date();
        
        // Core fields
        row.put(Constants.DATE_FIELD, df.format(dateObj) + Constants.NEW_DAY_TIME);
        row.put(Constants.LANGUAGE_FIELD, Constants.NODE_LANGUAGE);
        row.put(Constants.PACKAGE_NAME_FIELD, this.packageName);
        row.put(Constants.VERSION_FIELD, Constants.ALL_VERSIONS);
        row.put(Constants.CATEGORY_FIELD, this.category);
        
        NodeDownloadData monthData = loader.getMonthDownloadData();
        NodeDownloadData weekData = loader.getWeekDownloadData();
        NodeDownloadData dayData = loader.getDayDownloadData();
        
        // Download fields
        row.put(Constants.MONTHLY_DOWNLOADS_FIELD, monthData.getDownloads());
        row.put(Constants.WEEKLY_DOWNLOADS_FIELD, weekData.getDownloads());
        row.put(Constants.DAILY_DOWNLOADS_FIELD, dayData.getDownloads());
        
        connector.addRow(row);
        connector.commit();
    }
    
    public static void main(String[] args) {
        NodeManager manager = new NodeManager("microsoft", "azure-storage", "cloud");
        manager.saveData();
    }
}
