package de.frinshhd.LogicProxyTools.Core.mysql.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DatabaseTable(tableName = "Maintenance")
public class Maintenance {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String items;

    @DatabaseField
    private boolean active;

    @DatabaseField
    private String namesList;

    public Maintenance() {}

    public void create() {
        this.items = hashMapToString(new HashMap<>());
        this.active = false;
        this.namesList = listToString(new ArrayList<>());
    }

    public int getId() {
        return id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setNamesList(List<String> namesList) {
        this.namesList = listToString(namesList);
    }

    public List<String> getNamesList() {
        return stringToList(this.namesList);
    }

    public void putItems(String item, Object object) {
        HashMap<String, Object> items;
        if (this.items == null || this.items.isEmpty() || this.items.equals("{}")) {
            items = new HashMap<>();
        } else {
            items = (HashMap<String, Object>) stringToHashMap(this.items);
        }

        items.put(item, object);
        this.items = hashMapToString(items);
    }

    public HashMap<String, Object> getItems() {
        if (this.items == null || this.items.equals("{}") || this.items.isEmpty()) {
            return new HashMap<>();
        }

        return (HashMap<String, Object>) stringToHashMap(this.items);
    }

    public Map<String, Object> stringToHashMap(String jsonString) {
        Map<String, Object> resultMap = null;
        try {
            resultMap = objectMapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public String hashMapToString(Map<String, Object> map) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public List<String> stringToList(String jsonString) {
        List<String> resultList = new ArrayList<>();

        if (jsonString == null || jsonString.isEmpty() || jsonString.equals("[]")) {
            return resultList;
        }

        try {
            resultList = objectMapper.readValue(jsonString, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public String listToString(List<String> list) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}