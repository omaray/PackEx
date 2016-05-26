package com.packex.manager;

import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.PhpLoader;
import com.packex.loader.LanguageLoader;
import com.packex.model.pkgmgr.PhpDownloadData;

public class PhpManager extends LanguageManagerBase{

    public PhpManager(BigQueryConnector connector, String company, String packageName, String category) {
        super(connector, company, packageName, category);
    }

    @Override
    protected LanguageLoader getLanguageLoader() {
        return new PhpLoader(this.packageName);
    }

    @Override
    protected String getLanguageField() {
        return Constants.PHP_LANGUAGE;
    }

    @Override
    protected void putDownloadEntries(LanguageLoader loader, Map<String, Object> row) {
        PhpDownloadData data = ((PhpLoader)loader).getDownloadData();
        row.put(Constants.TOTAL_DOWNLOADS_FIELD, data.getTotal());
        row.put(Constants.MONTHLY_DOWNLOADS_FIELD, data.getMonthly());
        row.put(Constants.DAILY_DOWNLOADS_FIELD, data.getDaily());
    }
    
    public static void main(String[] args) {
        String datasetName = Util.getDatasetName();
        String tableName = Util.getTableName("google");
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.createDataset(datasetName);
        connector.createTable(datasetName, tableName);
        connector.begin(datasetName, tableName);
        
        PhpManager manager = new PhpManager(connector, "google", "google/cloud", "cloud");
        manager.saveData();
        
        connector.commit();
    }
}
