package com.packex.model.pkgmgr;

import java.util.List;

public class PythonReleaseList {
    List<PythonRelease> releaseList;
    
    public PythonReleaseList(List<PythonRelease> releaseList) {
        this.releaseList = releaseList;
    }
    
    public List<PythonRelease> getReleaseList() {
        return this.releaseList;
    }
}
