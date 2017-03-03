package com.zy.gcode.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by admin5 on 17/2/16.
 */
public class MzUtils {
    public static String merge(Object... strs) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(strs).forEach(builder::append);
        return builder.toString();
    }

    public static String[] trimArray(String[] strs) {
        int len = strs.length;
        for (int i = 0; i < len; i++) {
            strs[i] = strs[i].trim();
        }
        return strs;
    }

    public static String inputStreamToString(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkEntry(Map map, String name) {
        return map.containsKey(name) && map.get(name) != null;
    }
}
