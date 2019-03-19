package com.example.attendance;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public class singleton {
    private static singleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private singleton(Context context)
    {
        mCtx = context;
        requestQueue =getRequestQueue();
    }


    private RequestQueue getRequestQueue()
    {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    public static synchronized singleton getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new singleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }

}
