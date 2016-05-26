package com.packex.manager;

import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.DotnetLoader;
import com.packex.loader.LanguageLoader;
import com.packex.model.pkgmgr.DotnetDownloadData;

public class DotnetManager extends LanguageManagerBase {
    
    public DotnetManager(BigQueryConnector connector, String company, String packageName, String category) {
        super(connector, company, packageName, category);
    }
    
    @Override
    protected LanguageLoader getLanguageLoader() {
        return new DotnetLoader(this.packageName);
    }

    @Override
    protected String getLanguageField() {
        return Constants.DOTNET_LANGUAGE;
    }

    @Override
    protected void putDownloadEntries(LanguageLoader loader, Map<String, Object> row) {
        DotnetDownloadData data = ((DotnetLoader)loader).getDownloadData();
        row.put(Constants.TOTAL_DOWNLOADS_FIELD, data.getDownloads());
    }
    
    public static void main(String[] args) {
        String datasetName = Util.getDatasetName();
        String tableName = Util.getTableName("google");
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.createDataset(datasetName);
        connector.createTable(datasetName, tableName);
        connector.begin(datasetName, tableName);
        
        DotnetManager manager = new DotnetManager(connector, "google", "Google.Apis.Datastore.v1beta1", "cloud");
        manager.saveData();
        
        connector.commit();
    }
}
