package com.login.howtosenddata;

import android.app.VoiceInteractor;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by omar on 4/2/18.
 */

public class Singleton {
    private static Singleton mInstence;
    private RequestQueue requestQueue;
    private static Context mContext;

    private Singleton(Context context){
        mContext=context;
        requestQueue=getRequestQueue();

    }

    public static synchronized Singleton getInstence(Context context){
        if(mInstence==null) mInstence=new Singleton(context);
        return mInstence;

    }



    public RequestQueue getRequestQueue(){
        if(requestQueue==null) requestQueue= Volley.newRequestQueue(mContext.getApplicationContext());
        return requestQueue;
    }

    public <T> void addToRequestQue(Request<T>request){
    requestQueue.add(request);
    }
}
