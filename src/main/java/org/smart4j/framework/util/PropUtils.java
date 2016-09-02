package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by stando on 2016/8/29.
 */
public class PropUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(PropUtils.class);

    /*
        加载属性文件
     */
    public static Properties loadProps(String fileName){
        Properties properties = null;
        InputStream is = null;

        try{
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + "file is not found");
            }
            properties = new Properties();
            properties.load(is);
        }catch (IOException e){
            LOGGER.error("load properties file failure", e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure", e);
                }
            }
        }
        return properties;
    }

    /*获取字符型属性*/
    public static String getString(Properties properties, String key){
        return getString(properties,key,"");
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.containsKey(key)) {
           value = properties.getProperty(key);
        }
        return value;
    }
    /*获取数字型属性*/
    public static int getInteger(Properties properties, String key) {
        return getInteger(properties,key,0);
    }

    private static int getInteger(Properties properties, String key, int defaultValue) {
        int value = defaultValue;
        if (properties.containsKey(key)) {
            value = CastUtil.castInt(properties.getProperty(key));
        }
        return value;
    }
    /*获取布尔型属性*/
    public  static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties,key,false);
    }

    private static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (properties.containsKey(key)) {
           value = CastUtil.castBoolean(properties.getProperty(key));
        }
        return value;
    }
}
