package org.smart4j.framework.helper;

import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.PropUtils;

import java.util.Properties;

/**
 * Created by Mcfell on 2016/9/2.
 */
public class ConfigHelper {
    private static final Properties CONFIG_PROPS = PropUtils.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver() {
        return PropUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl() {
        return PropUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }

    public static String getJdbcPassWord() {
        return PropUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    public static String getJdbcUserName() {
        return PropUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    public static String getAppBasePackage() {
        return PropUtils.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }

    public static String getAppJspPath() {
        return PropUtils.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH,"/WEB-INF/jsp");
    }

    public static String getAppAssetPath() {
        return PropUtils.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH,"/asset/");
    }
}
