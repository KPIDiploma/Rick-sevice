package com.volontyr.api;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by volontyr on 09.05.17.
 */
public class PatientAPI {

    public static final String baseUrl = "http://52.29.160.135/api/v1/";

    public static int registerPatient(String jsonObject, String token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(baseUrl + "register/?token=" + token);

        StringEntity entity = new StringEntity(jsonObject);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();

        return response.getStatusLine().getStatusCode();
    }
}
