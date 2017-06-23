package com.housey.aeiton.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by JAR on 19-03-2017.
 */

public class NetworkSingleton {
    public static NetworkSingleton mInstance;
    public static Context mContext;

    private RequestQueue mRequestQueue;

    private NetworkSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQ();
    }

    public static synchronized NetworkSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQ() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(Request request){
        getRequestQ().add(request);
    }
}
