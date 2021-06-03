package com.company.common.datatools;

import java.util.HashMap;
import java.util.Map;

public class DataStorage {
    private static Map<String,Object> dictionary = new HashMap<>();

    public static void Add(String key , Object value){
        dictionary.put(key,value);
    }

    public static void Delete(String key){
        dictionary.remove(key);
    }

    public static Object Get(String key){
        return dictionary.get(key);
    }

    public static boolean ExistKey(String key)
    {
        return dictionary.containsKey(key);
    }
}
