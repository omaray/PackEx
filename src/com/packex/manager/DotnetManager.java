package com.packex.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.DotnetLoader;
import com.packex.model.pkgmgr.DotnetDownloadData;

public class DotnetManager implements LanguageManager {
    private String company;
    private String packageName;
    private String category;
    private String datasetName;
    private String tableName;
    
    public DotnetManager(String company, String packageName, String category) {
        this.company = company;
        this.packageName = packageName;
        this.category = category;
        this.datasetName = Util.getDatasetName();
        this.tableName = Util.getTableName(this.company);
    }
    
    public void saveData() {
        DotnetLoader loader = new DotnetLoader(this.packageName);
        loader.loadData();
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.begin(this.datasetName, this.tableName);
        
        Map<String, Object> row = new HashMap<String, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = new Date();
        
        // Core fields
        row.put(Constants.DATE_FIELD, df.format(dateObj) + Constants.NEW_DAY_TIME);
        row.put(Constants.LANGUAGE_FIELD, Constants.DOTNET_LANGUAGE);
        row.put(Constants.PACKAGE_NAME_FIELD, this.packageName);
        row.put(Constants.VERSION_FIELD, Constants.ALL_VERSIONS);
        row.put(Constants.CATEGORY_FIELD, this.category);
        
        // Download fields
        DotnetDownloadData data = loader.getDownloadData();
        row.put(Constants.TOTAL_DOWNLOADS_FIELD, data.getDownloads());
        
        connector.addRow(row);
        connector.commit();
    }
    
    public static void main(String[] args) {
        DotnetManager manager = new DotnetManager("google", "Google.Apis.Datastore.v1beta1", "cloud");
        manager.saveData();
    }
}
