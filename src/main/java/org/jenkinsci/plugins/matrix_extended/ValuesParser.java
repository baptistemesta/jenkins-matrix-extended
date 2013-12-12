package org.jenkinsci.plugins.matrix_extended;

import java.util.HashMap;
import java.util.Map;

public class ValuesParser {

    public static String parseName(final String values) {
        int indexOf = values.indexOf('[');
        return values.substring(0, indexOf);
    }

    public static Map<String, String> parseValues(final String values) {
        int indexOf = values.indexOf('[');
        String[] split = values.substring(indexOf + 1, values.length() - 1).split("@@@");
        Map<String, String> map = new HashMap<String, String>(split.length);
        for (String entry : split) {
            String key = entry.substring(0, entry.indexOf("="));
            String value = entry.substring(entry.indexOf("=") + 1);
            map.put(key, value);
        }
        return map;
    }
}
