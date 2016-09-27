package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Mcfell on 16/9/27.
 */
public final class CodecUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);
    /**
     * 编码URL
     */
    public static String encodeURL(String source) {
        String target;
        try {
            target = URLEncoder.encode(source,"URF-8");
        } catch (Exception e) {
            LOGGER.error("encode url failre", e);
            throw new RuntimeException(e);
        }
        return target;
    }
    /**
     * URL解码
     */
    public static String decodeURL(String source) {
        String target;
        try {
            target = URLDecoder.decode(source,"UTF-8");
        } catch (Exception e) {
            LOGGER.error("decode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }
}
