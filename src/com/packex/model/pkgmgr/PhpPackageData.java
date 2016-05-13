package com.packex.model.pkgmgr;

public class PhpPackageData {
    String name;
    String description;
    String time;
    String type;
    String repository;
    PhpDownloadData downloads;
    int favers;
    
    public PhpPackageData() {}
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getTime() {
        return this.time;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getRepository() {
        return this.repository;
    }
    
    public PhpDownloadData getDownloads() {
        return this.downloads;
    }
    
    public int getFavers() {
        return this.favers;
    }
}
