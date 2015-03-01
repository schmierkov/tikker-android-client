package de.schmierkov.tikker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HttpClient {

    public static String getLoginToken(String url, List<NameValuePair> params) {
        String response_body = post(url, params);

        try {
            JSONObject json = new JSONObject(response_body.toString());
            return json.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String post(String url, List<NameValuePair> params) {
        StringBuffer stringBuffer = new StringBuffer("");
        String login_token = "";
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
