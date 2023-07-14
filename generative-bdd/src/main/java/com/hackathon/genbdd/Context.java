package com.hackathon.genbdd;

import java.util.Map;
import java.util.Properties;

public class Context extends Properties {

    public Context(){
    }

    public Context(Map<String, Object> initialValues){
        putAll(initialValues);
    }

    public void add(String key, Object value){
        put(key, value);
    }
}
