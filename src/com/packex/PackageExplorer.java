package com.packex;

import java.util.ArrayList;
import java.util.LinkedList;

import com.packex.connector.BigQueryConnector;
import com.packex.loader.CompanyLoader;
import com.packex.manager.LanguageManager;
import com.packex.manager.LanguageManagerFactory;
import com.packex.model.company.CompanyPackages;
import com.packex.model.company.PackageInfo;

public class PackageExplorer {
    public void execute() {
        BigQueryConnector connector = BigQueryConnector.getInstance();
        connector.createDataset(Util.getDatasetName());
        
        CompanyLoader companyLoader = new CompanyLoader();
        companyLoader.load();
        LinkedList<CompanyPackages> companyPackagesList = companyLoader.getCompanyData(); 
        
        LanguageManagerFactory factory = LanguageManagerFactory.getInstance();
        for (CompanyPackages companyPackages : companyPackagesList) {
            String companyName = companyPackages.getCompany();
            connector.createTable(Util.getDatasetName(), Util.getTableName(companyName));
            ArrayList<PackageInfo> packages = companyPackages.getPackages();
            for (PackageInfo pkg : packages) {
                LanguageManager manager = 
                        factory.getLanguageManager(pkg.getLanguage(), companyName, pkg.getName(), pkg.getCategory());
                manager.saveData();
            }
        }
    }
    
    public static void main(String[] args) {
        PackageExplorer packex = new PackageExplorer();
        packex.execute();
    }
}
