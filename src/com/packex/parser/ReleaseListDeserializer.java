package com.packex.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.packex.model.pkgmgr.PythonRelease;
import com.packex.model.pkgmgr.PythonReleaseList;

public class ReleaseListDeserializer implements JsonDeserializer<PythonReleaseList> {

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
