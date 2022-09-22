package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

public class JsonConverter {
    public static Map<String,String> jsonStringToMap(String jsonString){
        return (Map<String,String>)jsonStringToObject(jsonString, Map.class);
    }

    public static String stringify(Object obj){
        return new Gson().toJson(obj);
    }

    public static Object jsonStringToObject(String jsonString, Class clazz){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }

}
