package com.utils;

import android.os.AsyncTask;
import android.widget.Toast;

import com.ordory.ordory.IConnectListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Nourdine on 28/11/2016.
 */
public class MyAsynctask extends AsyncTask<String, Void, JSONObject>  {

    private IConnectListner listner;

    @Override
    protected JSONObject doInBackground(String... urlApi) {
        InputStream in;
        URL url;
        String result=null;
        HttpURLConnection conn=null;
        JSONObject json = null;
        try{
            // get URL content
            System.out.println("Entree 1...."+urlApi[0]);
            url = new URL(urlApi[0]);
            conn = (HttpURLConnection) url.openConnection();
            System.out.println("Entree Try....");
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
            json = new JSONObject(result);
            System.out.println("Res JSON : "+result);
            br.close();
        }catch(MalformedURLException e) {
            result=null;
        } catch (IOException e) {
            result=null;
        } catch (Exception e) {
            result=null;
        }

        return json;
    }

    protected void onPostExecute(JSONObject result) {
        //Constant.mainObject = new JSONObject(result);
        //Constant.resultJsonConnect = Constant.mainObject.getJSONObject("result");
        //System.out.println("Code : "+Constant.mainObject.getString("code"));
        String msg=null;
        try {
            if(listner!=null && result!=null && result.getString("code").equals("0")){
                listner.onSuccess(result);
            }else{
                msg = result.getString("msg");
                listner.onFailed(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setListner(IConnectListner listner) {
        this.listner = listner;
    }

    public IConnectListner getListner() {
        return listner;
    }
}
