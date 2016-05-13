package com.packex.model.pkgmgr;

public class RubyDownloadData {
    String name;
    int downloads;
    String version;
    int version_downloads;
    
    public RubyDownloadData() { }
    
    public String getName() {
        return this.name;
    }
    
    public int getDownloads() {
        return this.downloads;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public int getVersionDownloads() {
        return this.version_downloads;
    }
}