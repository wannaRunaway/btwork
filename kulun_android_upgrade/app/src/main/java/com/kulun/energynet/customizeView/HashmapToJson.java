package com.kulun.energynet.customizeView;

import com.kulun.energynet.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class HashmapToJson {
    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Entry e = (Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "\"" + e.getValue() + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        Utils.log("","",string);
        return string;
    }

    public static String hashMapToJsonNoString(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Entry e = (Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string +=  e.getValue() + ",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        Utils.log("","",string);
        return string;
    }
}
