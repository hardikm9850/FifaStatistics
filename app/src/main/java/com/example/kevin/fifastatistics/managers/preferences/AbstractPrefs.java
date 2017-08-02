package com.example.kevin.fifastatistics.managers.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractPrefs {

    protected SharedPreferences mPrefs;

    public AbstractPrefs(Context context) {
        mPrefs = context.getSharedPreferences(name(), Context.MODE_PRIVATE);
    }

    abstract String name();

    protected <T> void storeAsString(T t, String key) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, SerializationUtils.toJson(t));
        editor.apply();
    }

    protected <T> List<T> getList(String key) {
        TypeReference<List<T>> ref = new TypeReference<List<T>>() {};
        return getObject(ref, key);
    }

    protected <K, V> Map<K, V> getMap(String key) {
        TypeReference<Map<K, V>> ref = new TypeReference<Map<K, V>>() {};
        return getObject(ref, key);
    }

    protected <T> T getObject(TypeReference<T> typeReference, String key) {
        ObjectMapper mapper = new ObjectMapper();
        String object = mPrefs.getString(key, null);
        try {
            return object == null ? null : mapper.readValue(object, typeReference);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
    }

    protected <T> T getObject(Class<T> clazz, String key) {
        ObjectMapper mapper = new ObjectMapper();
        String object = mPrefs.getString(key, null);
        try {
            return object == null ? null : mapper.readValue(object, clazz);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
    }
}
