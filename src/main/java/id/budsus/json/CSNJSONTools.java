package id.budsus.json;

import com.google.gson.Gson;

// CodeSearchNet dataset JSON to Object POJO CodeSearchNet
//
public class CSNJSONTools {
    public static CodeSearchNet getCSNObjectFromJSON(String json) {
        CodeSearchNet csn = new CodeSearchNet();
        Gson gson = new Gson();
        csn = gson.fromJson(json, CodeSearchNet.class);
        return csn;
    }
}
