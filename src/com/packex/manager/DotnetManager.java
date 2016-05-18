package com.packex.manager;

import java.util.Map;

import com.packex.Constants;
import com.packex.loader.DotnetLoader;
import com.packex.loader.LanguageLoader;
import com.packex.model.pkgmgr.DotnetDownloadData;

public class DotnetManager extends LanguageManagerBase {
    
    public DotnetManager(String company, String packageName, String category) {
        super(company, packageName, category);
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
        DotnetManager manager = new DotnetManager("google", "Google.Apis.Datastore.v1beta1", "cloud");
        manager.saveData();
    }
}
