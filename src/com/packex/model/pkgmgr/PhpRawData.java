package com.packex.model.pkgmgr;

import com.google.gson.annotations.SerializedName;

public class PhpRawData {
    
    @SerializedName("package")
    PhpPackageData packageData;
    
    public PhpRawData() { }
    
    public PhpPackageData getPackageData() {
        return this.packageData;
    }
}
