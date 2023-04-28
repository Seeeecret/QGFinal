package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 我的io工具类
 *
 * @author Secret
 * @date 2023/04/19
 */
public class MyIOUtil {
    /**
     * 将输入流的内容变为字符串
     *
     * @param inputStream 输入流
     * @param encoding    编码
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String toString(InputStream inputStream, String encoding) throws IOException {
        if (inputStream == null) {
            return "";
        }
        // 创建一个字节数组输出流，用来存储输入流中的数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 创建一个缓冲区，用来读取输入流中的数据
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        String result = outputStream.toString(encoding);
        outputStream.close();
        return result;
    }
    public static String URLtoJson(String paramIn) {
        paramIn = paramIn.replaceAll("=","\":\"");
        paramIn = paramIn.replaceAll("&","\",\"");
        return"{\"" + paramIn +"\"}";
    }

}
