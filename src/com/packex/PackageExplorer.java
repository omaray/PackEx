package com.packex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.packex.connector.BigQueryConnector;
import com.packex.loader.CompanyLoader;
import com.packex.manager.LanguageManager;
import com.packex.manager.LanguageManagerFactory;
import com.packex.model.company.CompanyPackages;
import com.packex.model.company.PackageInfo;

public class PackageExplorer {
    private static final Logger logger = Logger.getLogger(PackageExplorer.class.getName());
    
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
                
                try {
                    logger.log(Level.INFO, String.format("Saving the package info for %s in the %s language", 
                            pkg.getName().toUpperCase(), pkg.getLanguage().toUpperCase()));
                    
                    LanguageManager manager = 
                            factory.getLanguageManager(pkg.getLanguage(), companyName, pkg.getName(), pkg.getCategory());
                    manager.saveData();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, String.format("Hit an issues with the lang manager for: %s; %s; %s", 
                            pkg.getLanguage(), companyName, pkg.getName()), ex);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        PackageExplorer packex = new PackageExplorer();
        packex.execute();
    }
}
