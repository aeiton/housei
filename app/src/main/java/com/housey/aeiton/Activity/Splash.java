package com.housey.aeiton.Activity;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.housey.aeiton.R;
import com.housey.aeiton.Utils.DataSingleton;
import com.housey.aeiton.Utils.HawkSingleton;
import com.housey.aeiton.Utils.NetworkSingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.housey.aeiton.Utils.Constants.BASE_URL;
import static com.housey.aeiton.Utils.Constants.CARD_KEY;
import static com.housey.aeiton.Utils.Constants.CARD_URL;
import static com.housey.aeiton.Utils.Constants.ISCARD;
import static com.housey.aeiton.Utils.Constants.ISREGISTERED;
import static com.housey.aeiton.Utils.Constants.REGISTRATION_KEY;
import static com.housey.aeiton.Utils.Constants.REQUEST_KEY;
import static com.housey.aeiton.Utils.Constants.RESPONSE;
import static com.housey.aeiton.Utils.Constants.USER_ID;
import static com.housey.aeiton.Utils.Constants.VALIDITY;
import static com.housey.aeiton.Utils.DataSingleton.response;
import static com.housey.aeiton.Utils.DataSingleton.userId;
import static com.housey.aeiton.Utils.DataSingleton.valid;
import static com.housey.aeiton.Utils.Constants.ISCARD;
import static com.housey.aeiton.Utils.Constants.ISREGISTERED;

// TODO: 23-06-2017 change NammaTvMainActivity to respective NavigationDrawerActivity
public class Splash extends NammaTvMainActivity {

    ImageButton play;
    Snackbar snackbar;
    Intent intent;
    boolean success = false, isCard = false, isRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_splash, constraintLayout);

        HawkSingleton.getInstance().init(getApplicationContext());

        setTitle("");

        play = (ImageButton) findViewById(R.id.actionButton);

        //Enable this to make this app OFFLINE compitable
        isCard = HawkSingleton.getInstance().hawkGet(ISCARD, false);

        intent = new Intent(Splash.this, GameView.class);

        isRegistered = getSharedPreferences(REGISTRATION_KEY, MODE_PRIVATE).getBoolean(ISREGISTERED, false);

        if (!isRegistered){
            startActivity(new Intent(Splash.this, Registration.class));
            Splash.this.finish();
        }

        startTheEngine();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (success) {
                    intent.putExtra("CARDRESPONSE", response);
                    startActivity(intent);
                    finish();
            } else {
                    showSnackbar("LOADING... PLEASE WAIT", true);
                    startTheEngine();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 23-06-2017 Change the index '0' to the position of "HOUSEY" in nav drawer
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void startTheEngine() {
        if (isCard) {
            valid =     HawkSingleton.getInstance().hawkGet(VALIDITY);
            response =  HawkSingleton.getInstance().hawkGet(RESPONSE);
            userId =    getSharedPreferences(REGISTRATION_KEY, MODE_PRIVATE).getInt(USER_ID, 0);
            Log.d("checkCard", "starttheEngine");
            checkCardValidity();
        } else if (isconnected()) {
            Log.d("getcard", "startTheEngine");
            getCard();
        } else showSnackbar(getString(R.string.nic), true);
    }

    private void getCard() {
        Log.d("getcard", "called");
        StringRequest request = new StringRequest(StringRequest.Method.POST, BASE_URL+CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        success = true;

                        DataSingleton.response = response;
                        try {
                            JSONObject object = new JSONObject(response);
                            int status = object.getInt("status");
                            Log.d("resp", response);
                            if (status == 0) {
                                showSnackbar(object.getString("msg"), false);
                                success = false;
                            } else if (status == 1) {
                                valid = object.getString("valid_till");
                                showSnackbar(object.getString("msg"), false);
                                Log.d("checkCard", "status=1");
                                checkCardValidity();
                                //send response to GameView.java while opening it
                                intent.putExtra("CARDRESPONSE", response);
                            } else if (status == 2) {
                                valid = object.getString("valid_till");
                                showSnackbar(object.getString("msg"), false);
                                Log.d("checkCard", "status=2");
                                checkCardValidity();
                                //send response to GameView.java while opening it
                                intent.putExtra("CARDRESPONSE", response);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        success = false;
                        showSnackbar(getString(R.string.err), true);
                        Log.d("Error", error + " ");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", REQUEST_KEY);
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


    }

    private void showSnackbar(String message, boolean doRetry) {
        snackbar = Snackbar.make(play, message, Snackbar.LENGTH_LONG);
        if (doRetry) {
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTheEngine();
                }
            });
        }
        snackbar.show();
    }

    private void checkCardValidity() {
        try {
            //getActual date and time
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            Date actualDate = sdf.parse(sdf.format(cal.getTime()));
            Date validDate = sdf.parse(valid);

            if (validDate.compareTo(actualDate) < 0) {
                //validity has expired
                HawkSingleton.getInstance().hawkDeleteAll();
                Log.d("getcard", "checkCardAvail");
                getCard();
            } else {
                        HawkSingleton.getInstance().hawkPut(ISCARD, true);
                        HawkSingleton.getInstance().hawkPut(VALIDITY, valid);
                        HawkSingleton.getInstance().hawkPut(RESPONSE, response);
                success = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private boolean isconnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}