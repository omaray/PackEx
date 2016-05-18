package com.packex.manager;

import java.util.Map;

import com.packex.Constants;
import com.packex.loader.NodeLoader;
import com.packex.loader.LanguageLoader;
import com.packex.model.pkgmgr.NodeDownloadData;

public class NodeManager extends LanguageManagerBase {
    
    public NodeManager(String company, String packageName, String category) {
        super(company, packageName, category);
    }

    @Override
    protected LanguageLoader getLanguageLoader() {
        return new NodeLoader(this.packageName);
    }

    @Override
    protected String getLanguageField() {
        return Constants.NODE_LANGUAGE;
    }

    @Override
    protected void putDownloadEntries(LanguageLoader loader, Map<String, Object> row) {
        NodeDownloadData monthData = ((NodeLoader)loader).getMonthDownloadData();
        NodeDownloadData weekData = ((NodeLoader)loader).getWeekDownloadData();
        NodeDownloadData dayData = ((NodeLoader)loader).getDayDownloadData();
        
        // Download fields
        row.put(Constants.MONTHLY_DOWNLOADS_FIELD, monthData.getDownloads());
        row.put(Constants.WEEKLY_DOWNLOADS_FIELD, weekData.getDownloads());
        row.put(Constants.DAILY_DOWNLOADS_FIELD, dayData.getDownloads());
    }
    
    public static void main(String[] args) {
        NodeManager manager = new NodeManager("microsoft", "azure-storage", "cloud");
        manager.saveData();
    }
}
