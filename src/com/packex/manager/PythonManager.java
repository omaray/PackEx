package com.packex.manager;

import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.PythonLoader;
import com.packex.loader.LanguageLoader;
import com.packex.model.pkgmgr.PythonDownloadData;

public class PythonManager extends LanguageManagerBase {
    
    public PythonManager(BigQueryConnector connector, String company, String packageName, String category) {
        super(connector, company, packageName, category);
    }

    @Override
    protected LanguageLoader getLanguageLoader() {
        return new PythonLoader(this.packageName);
    }

    @Override
    protected String getLanguageField() {
        return Constants.PYTHON_LANGUAGE;
    }

    @Override
    protected void putDownloadEntries(LanguageLoader loader, Map<String, Object> row) {
        PythonDownloadData data = ((PythonLoader)loader).getDownloadData();
        row.put(Constants.MONTHLY_DOWNLOADS_FIELD, data.getMonthDownloads());
        row.put(Constants.WEEKLY_DOWNLOADS_FIELD, data.getWeekDownloads());
        row.put(Constants.DAILY_DOWNLOADS_FIELD, data.getDayDownloads());
    }
 
    public static void main(String[] args) {
        String datasetName = Util.getDatasetName();
        String tableName = Util.getTableName("amazon");
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.createDataset(datasetName);
        connector.createTable(datasetName, tableName);
        connector.begin(datasetName, tableName);
        
        PythonManager pythonManager = new PythonManager(connector, "amazon", "aws", "cloud");
        pythonManager.saveData();
        
        connector.commit();
    }
}
