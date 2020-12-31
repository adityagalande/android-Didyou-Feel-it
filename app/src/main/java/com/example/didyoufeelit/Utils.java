package com.example.didyoufeelit;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Utils {
    private Utils(){

    }

    public static Event fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Event event = extractFromJson(jsonResponse);
        return event;
    }

    private static URL createUrl(String getUrl) {
        URL url = null;
        try {
            url = new URL(getUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection URLConnection = null;
        InputStream inputStream = null;

        try {
            URLConnection = (HttpURLConnection) url.openConnection();
            URLConnection.setReadTimeout(10000);
            URLConnection.setReadTimeout(15000);
            URLConnection.setRequestMethod("GET");
            URLConnection.connect();

            if(URLConnection.getResponseCode() == 200){
                inputStream = URLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e("Error TAG.","NOPE!!");
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            if(URLConnection != null){
                URLConnection.disconnect();
            }if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder stringBuilderOutput = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = br.readLine();
            while (line != null){
                stringBuilderOutput.append(line);
                line = br.readLine();
            }
        }
        return stringBuilderOutput.toString();
    }

    private static  Event extractFromJson(String earthquakeJson){
        if(TextUtils.isEmpty(earthquakeJson)){
            return null;
        }
        try{
            JSONObject baseJsonObject = new JSONObject(earthquakeJson);
            JSONArray featuures = baseJsonObject.getJSONArray("features");

            if(featuures.length() > 0){
                JSONObject firstFeature = featuures.getJSONObject(0);
                JSONObject properties = firstFeature.getJSONObject("properties");

                String title = properties.getString("title");
                String numOfPeoplr = properties.getString("felt");
                String perceivedStrength = properties.getString("cdi");

                return new Event(title, numOfPeoplr, perceivedStrength);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
