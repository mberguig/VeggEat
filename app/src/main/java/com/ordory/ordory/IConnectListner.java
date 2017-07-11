package com.ordory.ordory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nourdine on 28/11/2016.
 */
public interface IConnectListner {

    public void onSuccess(JSONObject obj);
    public void onFailed(String msg);

}
