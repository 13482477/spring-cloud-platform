package com.siebre.basic.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
    public static Map<String, String> getProperty(String filePath) {
        Map map = new HashMap();
        try {
            InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath);
            Throwable localThrowable2 = null;
            try {
                Properties p = new Properties();
                p.load(in);
                for (Map.Entry entry : p.entrySet()) {
                    String key = (String) entry.getKey();
                    map.put(key, (String) entry.getValue());
                }
            } catch (Throwable localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (in != null) if (localThrowable2 != null) try {
                    in.close();
                } catch (Throwable x2) {
                    localThrowable2.addSuppressed(x2);
                }
                else in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static Map<String, String> getProperty(File file) {
        Map map = new HashMap();
        try {
            InputStream in = new FileInputStream(file);
            Throwable localThrowable2 = null;
            try {
                Properties p = new Properties();
                p.load(in);
                for (Map.Entry entry : p.entrySet()) {
                    String key = (String) entry.getKey();
                    map.put(key, (String) entry.getValue());
                }
            } catch (Throwable localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (in != null) if (localThrowable2 != null) try {
                    in.close();
                } catch (Throwable x2) {
                    localThrowable2.addSuppressed(x2);
                }
                else in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}

