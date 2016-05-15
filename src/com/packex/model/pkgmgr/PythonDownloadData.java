package com.packex.model.pkgmgr;

public class PythonDownloadData {
    Integer downloads;
    Integer dayDownloads;
    Integer weekDownloads;
    Integer monthDownloads;
    
    public PythonDownloadData() { }
    
    public PythonDownloadData(Integer dayDownloads, Integer weekDownloads, Integer monthDownloads) {
        this.dayDownloads = dayDownloads;
        this.weekDownloads = weekDownloads;
        this.monthDownloads = monthDownloads;
    }
    
    public Integer getDownloads() {
        return this.downloads;
    }
    
    public Integer getDayDownloads() {
        return this.dayDownloads;
    }
    
    public Integer getWeekDownloads() {
        return this.weekDownloads;
    }
    
    public Integer getMonthDownloads() {
        return this.monthDownloads;
    }
}
