package com.kulun.energynet.requestparams;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ResponseCode {
    void response(JsonObject json, JsonArray jsonArray, boolean isNull, int code, String message);
}
