package com.barak.eitan.firebasechat;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by barak on 27/03/2017.
 */

public class VolleyHandler {
    private static VolleyHandler handler;
    private static Context context;
    private RequestQueue requestQueue;

    public VolleyHandler(Context context){
        VolleyHandler.context = context;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if(this.requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return this.requestQueue;
    }

    public static synchronized VolleyHandler getHandler(Context context){
        if(handler == null){
            handler = new VolleyHandler(context);
        }
        return handler;
    }

    public<T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
