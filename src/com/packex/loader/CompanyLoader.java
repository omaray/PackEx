package com.packex.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.packex.Constants;
import com.packex.Util;
import com.packex.model.company.CompanyPackages;
import com.packex.model.company.PackageInfo;


public class CompanyLoader {
    private LinkedList<CompanyPackages> companyData;
    
    public void load() {
        this.load(Constants.PACKAGES_FILE_PATH);
    }
    
    public void load(String filePath) {
        Gson gson = new Gson();
        String packagesInJson= Util.readFromFile(filePath);
        CompanyPackages[] companyPackages = gson.fromJson(packagesInJson, CompanyPackages[].class);
        
        this.companyData = new LinkedList<CompanyPackages>(Arrays.asList(companyPackages));
    }
    
    public LinkedList<CompanyPackages> getCompanyData() {
        return this.companyData;
    }
    
    public static void main(String[] args) {
        CompanyLoader companyLoader= new CompanyLoader();
        companyLoader.load();
        LinkedList<CompanyPackages> companyPackagesList = companyLoader.getCompanyData(); 
        
        for (CompanyPackages companyPackages : companyPackagesList) {
            System.out.println(companyPackages.getCompany());
            ArrayList<PackageInfo> packages = companyPackages.getPackages();
            for (PackageInfo pkg : packages) {
                System.out.print("  Language: " + pkg.getLanguage());
                System.out.print("  Name: " + pkg.getName());
                System.out.println("  Category: " + pkg.getCategory());
            }
        }
    }
}
