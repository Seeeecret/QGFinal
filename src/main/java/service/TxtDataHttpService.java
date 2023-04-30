package service;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * 用于发送txt数据到服务器
 *
 * @author Secret
 */
public class TxtDataHttpService {
    String url;
    CloseableHttpClient httpClient;

    public TxtDataHttpService(){
        this.url=("http://localhost:8080/QGFinal_war/txtData");
        httpClient = HttpClients.createDefault();
    }

    public TxtDataHttpService(String url){
        this.url = url;
//        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
//        connManager.setMaxTotal(100); // 设置连接池最大连接数
//        connManager.setDefaultMaxPerRoute(20); // 设置每个路由的最大连接数
//        httpClient = HttpClients.custom()
//                .setConnectionManager(connManager)
//                .build();
        httpClient = HttpClients.createDefault();
    }

    public void sendTxtData(String txtData) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        json.put("txtData", txtData);
        json.put("method", "txtData");
        json.put("printerId", 1);
        StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        response.close();
    }

    public void sendTxtData(String txtData, int id) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        json.put("txtData", txtData);
        json.put("method", "txtData");
        json.put("printerId", id);
        StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        response.close();
    }



    public void closeClient() throws IOException {
        httpClient.close();
    }

}
