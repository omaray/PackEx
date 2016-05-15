package com.packex.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.packex.model.pkgmgr.PythonDownloadData;
import com.packex.model.pkgmgr.PythonRelease;

public class ReleaseDeserializer implements JsonDeserializer<PythonRelease> {

    @Override
    public PythonRelease deserialize(
            JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<PythonDownloadData> downloadDataList = new ArrayList<PythonDownloadData>();
        for (JsonElement entry : jsonArray) {
            PythonDownloadData data = context.deserialize(entry, PythonDownloadData.class);
            downloadDataList.add(data);
        }
        
        return new PythonRelease(downloadDataList);
    }
}