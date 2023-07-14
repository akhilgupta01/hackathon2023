package com.hackathon.genbdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class Tester {
    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\n" +
                "  \"scenario-type\": \"data-transformation\",\n" +
                "  \"is-input-available\": true,\n" +
                "  \"input-data-source-type\": \"file\",\n" +
                "  \"input-data-record-type\": \"transaction\",\n" +
                "  \"input-data-record-attributes\": [\n" +
                "    \"transactionId\",\n" +
                "    \"customerId\",\n" +
                "    \"itemId\",\n" +
                "    \"quantity\",\n" +
                "    \"price\"\n" +
                "  ],\n" +
                "  \"output-data-record-type\": \"report\",\n" +
                "  \"output-data-record-attributes\": [\n" +
                "    \"customerId\",\n" +
                "    \"orderValue\"\n" +
                "  ]\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode properties = objectMapper.readTree(json);
        System.out.println(transformJsonToMap(properties, ""));

    }

    public static Map<String, String> transformJsonToMap(JsonNode node, String prefix){

        Map<String,String> jsonMap = new HashMap<>();

        if(node.isArray()) {
            jsonMap.put(prefix,node.toPrettyString());
        }else if(node.isObject()){
            Iterator<String> fieldNames = node.fieldNames();
            String curPrefixWithDot = (prefix==null || prefix.trim().length()==0) ? "" : prefix+".";
            //list all keys and values
            while(fieldNames.hasNext()){
                String fieldName = fieldNames.next();
                JsonNode fieldValue = node.get(fieldName);
                jsonMap.putAll(transformJsonToMap(fieldValue, curPrefixWithDot+fieldName));
            }
        }else {
            //basic type
            jsonMap.put(prefix,node.asText());
        }

        return jsonMap;
    }
}
