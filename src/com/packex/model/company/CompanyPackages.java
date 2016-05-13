package com.packex.model.company;

import java.util.ArrayList;

public class CompanyPackages {
    String company;
    ArrayList<PackageInfo> packages;
    
    public CompanyPackages() { }
    
    public String getCompany() {
        return this.company;
    }
    
    public ArrayList<PackageInfo> getPackages() {
        return this.packages;
    }
}
