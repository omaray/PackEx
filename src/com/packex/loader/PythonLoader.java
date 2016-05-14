package com.packex.loader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.FieldNamingPolicy;
import com.packex.Constants;
import com.packex.connector.HttpConnector;
import com.packex.model.pkgmgr.PythonDownloadData;
import com.packex.model.pkgmgr.PythonRawData;
import com.packex.model.pkgmgr.PythonRelease;
import com.packex.model.pkgmgr.PythonReleaseList;

public class PythonLoader {
    private String packageName;
    private String url;
    
    public PythonLoader(String packageName) {
        this.packageName = packageName;
        this.url = String.format(Constants.PYTHON_URL_TEMPLATE, this.packageName);
    }
    
    public void load() {
        HttpConnector connector = HttpConnector.getInstance();
        String response = connector.get(this.url);
        
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(PythonReleaseList.class, new ReleaseListDeserializer());
        builder.registerTypeAdapter(PythonRelease.class, new ReleaseDeserializer());
        Gson gson = builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        PythonRawData rawData = gson.fromJson(response, PythonRawData.class);
        
        PythonReleaseList releaseList = rawData.getReleases();
        for (PythonRelease release : releaseList.getReleaseList()) {
            System.out.println(release.getVersion());
        }
    }
    
    public static class ReleaseListDeserializer implements JsonDeserializer<PythonReleaseList> {

        @Override
        public PythonReleaseList deserialize(
                JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            List<PythonRelease> releases = new ArrayList<PythonRelease>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                PythonRelease pythonRelease = context.deserialize(entry.getValue(), PythonRelease.class);
                System.out.println(entry.getKey().toString());
                pythonRelease.setVersion(entry.getKey().toString());
                releases.add(pythonRelease);
            }
            
            return new PythonReleaseList(releases);
        }
    }
    
    public static class ReleaseDeserializer implements JsonDeserializer<PythonRelease> {

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
    
    public static void main(String[] args) {
        PythonLoader loader = new PythonLoader("gcloud");
        loader.load();
    }
}
