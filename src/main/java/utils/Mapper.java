package utils;

import com.alibaba.fastjson.JSON;

import java.io.Writer;

public class Mapper {

    public static void writeValue(Writer out, Object obj) {
        String jsonString = JSON.toJSONString(obj);
        try {
            out.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
    }

    }
    private Mapper() {
    }
}
