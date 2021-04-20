package es.ucm.fdi.iw.g01.bayshop.model;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TemplateParams {
    private static Logger logger = LogManager.getLogger(TemplateParams.class);
    private Map<String, Object> attrs = new HashMap<String, Object>(); 

    // Recibe un objeto STRING JSON y lo parsea a Object para pasarselo al template
    public TemplateParams(String json){
        parse(json);
    }

    private void parse(String json){
        // Gson gson = new Gson();
        // JsonArray data = gson.fromJson(json, JsonArray.class);
        // iterar el jsonarray y meterlos en el Map con: attrs.put(key, value);

        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(json).getAsJsonObject();
        for(Map.Entry<String, JsonElement> entry : rootObj.entrySet()){
            this.attrs.put(entry.getKey(), entry.getValue());
        }
    }

    // llamar a este metodo desde los templates
    public Object getAttr(String key){
        return attrs.get(key);
    }

    @Override
    public String toString(){
        return this.attrs.toString() + " --> " + this.attrs.size();
    }
}
