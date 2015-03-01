package de.schmierkov.tikker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HttpClient {
    public static String getLoginToken(String url, List<NameValuePair> params) {
        try {
            return new JSONObject(post(url, params)).getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static JSONArray getMessages(String token, String url) {
        try {
            return new JSONArray(get(token, url));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONArray sendMessage(String token, String url, List<NameValuePair> params) {
        try {
            return new JSONArray(post(url + "?token=" + token, params));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private static String get(String token, String url) {
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;
        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(url + "?token=" + token);

        try {
            HttpResponse response = httpclient.execute(request);
            bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }

    private static String post(String url, List<NameValuePair> params) {
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;
        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            request.setEntity(entity);
            HttpResponse response = httpclient.execute(request);
            bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }
}
