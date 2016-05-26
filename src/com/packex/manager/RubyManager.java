package com.packex.manager;

import java.util.Map;

import com.packex.Constants;
import com.packex.Util;
import com.packex.connector.BigQueryConnector;
import com.packex.loader.RubyLoader;
import com.packex.loader.LanguageLoader;
import com.packex.model.pkgmgr.RubyDownloadData;

public class RubyManager extends LanguageManagerBase {
    
    public RubyManager(BigQueryConnector connector, String company, String packageName, String category) {
        super(connector, company, packageName, category);
    }

    @Override
    protected LanguageLoader getLanguageLoader() {
        return new RubyLoader(this.packageName);
    }

    @Override
    protected String getLanguageField() {
        return Constants.RUBY_LANGUAGE;
    }

    @Override
    protected void putDownloadEntries(LanguageLoader loader, Map<String, Object> row) {
        RubyDownloadData data = ((RubyLoader)loader).getDownloadData();
        row.put(Constants.TOTAL_DOWNLOADS_FIELD, data.getDownloads());
    }
    
    public static void main(String[] args) {
        String datasetName = Util.getDatasetName();
        String tableName = Util.getTableName("google");
        
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.createDataset(datasetName);
        connector.createTable(datasetName, tableName);
        connector.begin(datasetName, tableName);
        
        RubyManager rubyManager = new RubyManager(connector, "google", "gcloud", "cloud");
        rubyManager.saveData();
        
        connector.commit();
    }
}
