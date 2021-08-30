package com.kulun.kulunenergy.requestparams;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public interface Response {
    void response(JsonObject json, JsonArray jsonArray);
}
