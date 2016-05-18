package com.packex.manager;

import java.util.Map;

import com.packex.Constants;
import com.packex.loader.RubyLoader;
import com.packex.loader.LanguageLoader;
import com.packex.model.pkgmgr.RubyDownloadData;

public class RubyManager extends LanguageManagerBase {
    
    public RubyManager(String company, String packageName, String category) {
        super(company, packageName, category);
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
        RubyManager rubyManager = new RubyManager("google", "gcloud", "cloud");
        rubyManager.saveData();
    }
}
