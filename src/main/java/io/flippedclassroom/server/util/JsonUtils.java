package io.flippedclassroom.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用于手动将对象序列化为 JSON，保持 controller 内外返回的数据一致
 * 原因是 controller 外不能使用注解 @ResponseBody
 */
public class JsonUtils {
    private JsonUtils() {
    }

    public static ObjectMapper getInstance() {
        return Inner.instance;
    }

    private static final class Inner {
        private static final ObjectMapper instance = new ObjectMapper();
    }
}
