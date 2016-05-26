package com.packex.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.LanguageLoader;

public abstract class LanguageManagerBase implements LanguageManager {
    protected String company;
    protected String packageName;
    protected String category;
    protected String datasetName;
    protected String tableName;
    protected BigQueryConnector connector;
    
    public LanguageManagerBase(BigQueryConnector connector, String company, String packageName, String category) {
        this.connector = connector;
        this.company = company;
        this.packageName = packageName;
        this.category = category;
        this.datasetName = Util.getDatasetName();
        this.tableName = Util.getTableName(this.company);
    }
    
    public void saveData() {
        LanguageLoader loader = this.getLanguageLoader();
        loader.loadData();
        
        Map<String, Object> row = new HashMap<String, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = new Date();
        
        // Core fields
        row.put(Constants.DATE_FIELD, df.format(dateObj) + Constants.NEW_DAY_TIME);
        row.put(Constants.LANGUAGE_FIELD, this.getLanguageField());
        row.put(Constants.PACKAGE_NAME_FIELD, this.packageName);
        row.put(Constants.VERSION_FIELD, Constants.ALL_VERSIONS);
        row.put(Constants.CATEGORY_FIELD, this.category);
        
        // Download Fields
        this.putDownloadEntries(loader, row);
        
        this.connector.addRow(row);
    }
    
    abstract protected LanguageLoader getLanguageLoader();
    abstract protected String getLanguageField();
    abstract protected void putDownloadEntries(LanguageLoader loader, Map<String, Object> row);
}
