package ru.aikozin.testvpk;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class API {

    private static final String API =
            "http://80.254.124.90/php/api.php?";

    public static JSONObject getJSON(String parameters) {
        try {
            URL url = new URL(API + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader reader = new BufferedReader(isr);

            StringBuffer json = new StringBuffer(1024);
            String tmp;
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");

            JSONObject data = new JSONObject(json.toString());

            if (data.getInt("status") != 101) {
                return null;
            }

            reader.close();
            is.close();

            return data;
        } catch (Exception e) {
            return null;
        }
    }
}