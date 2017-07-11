package com.utils;

/**
 * Created by Nourdine on 11/11/2016.
 */

import com.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyRunnable implements Runnable {

    public static JSONObject resultRequest;
    public static JSONObject resultJsonConnect;
    public static JSONObject mainObject;

    public MyRunnable(String urlApi) {
        String result=null;
        InputStream in=null;
        URL url;
        HttpsURLConnection conn=null;

        try{
            // get URL content
            String test = "https://appspaces.fr/esgi/shopping_list/account/login.php?email=toto@gmail.com&password=azerty";
            url = new URL(test);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            in=conn.getInputStream();
            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line=br.readLine())!= null) {
                builder.append(line);
            }
            result=builder.toString();
            mainObject = new JSONObject(result);
            resultJsonConnect = mainObject.getJSONObject("result");
            System.out.println("Code : "+mainObject.getString("code"));
            System.out.print("Result : "+result);
            br.close();
        }catch(MalformedURLException e) {
            result=null;
        } catch (IOException e) {
            result=null;
        } catch (Exception e) {
            result=null;
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.disconnect();
        System.out.println(result);
    }

    public void run() {
        // code in the other thread, can reference "var" variable
    }

}
