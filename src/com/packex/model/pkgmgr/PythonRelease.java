package com.packex.model.pkgmgr;

import java.util.List;

public class PythonRelease {
    List<PythonDownloadData> downloadDataList;
    String version;
    
    public PythonRelease(List<PythonDownloadData> downloadDataList) {
        this.downloadDataList = downloadDataList;
    }
    
    public List<PythonDownloadData> getDownloadDataList() {
        return this.downloadDataList;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getVersion() {
        return this.version;
    }
}
