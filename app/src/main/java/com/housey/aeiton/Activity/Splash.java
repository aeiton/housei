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
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.housey.aeiton.Adapters.CustomDialog;
import com.housey.aeiton.R;
import com.housey.aeiton.Utils.DataSingleton;
import com.housey.aeiton.Utils.HawkSingleton;
import com.housey.aeiton.Utils.NetworkSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.housey.aeiton.Utils.Constants.BASE_URL;
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

// TODO: 23-06-2017 change NammaTvMainActivity to respective NavigationDrawerActivity
public class Splash extends NammaTvMainActivity {

    ImageButton play;
    Snackbar snackbar;
    Intent intent;
    boolean success = false, isCard = false, isRegistered = false, fromPlay = false;
    ProgressBar pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_splash, constraintLayout);

        HawkSingleton.getInstance().init(getApplicationContext());

        setTitle("");
        play = (ImageButton) findViewById(R.id.actionButton);
        pro = (ProgressBar) findViewById(R.id.progress);
        pro.setVisibility(View.INVISIBLE);
        play.setVisibility(View.VISIBLE);
        pro.setIndeterminate(true);

        //Enable this to make this app OFFLINE compitable
        isCard = HawkSingleton.getInstance().hawkGet(ISCARD, false);
        Log.d("Iscard", isCard + " ");

        intent = new Intent(Splash.this, GameView.class);

        isRegistered = getSharedPreferences(REGISTRATION_KEY, MODE_PRIVATE).getBoolean(ISREGISTERED, false);

        userId = getSharedPreferences(REGISTRATION_KEY, MODE_PRIVATE).getInt(USER_ID, 0);
        Log.d("userid", " " + userId);

        if (!isRegistered) {
            startActivity(new Intent(Splash.this, Registration.class));
            Splash.this.finish();
        } else startTheEngine();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pro.getVisibility() == View.INVISIBLE) {
                    if (success) {
                        intent.putExtra("CARDRESPONSE", response);
//                        checkCardValidity(1);
                        startActivity(intent);
                        finish();
                    } else {
                        fromPlay = true;
                        startTheEngine();
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        pro.setVisibility(View.INVISIBLE);
        play.setVisibility(View.VISIBLE);
        fromPlay = false;
        // TODO: 23-06-2017 Change the index '0' to the position of "HOUSEY" in nav drawer
        navigationView.getMenu().getItem(7).setChecked(true);
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
        pro.setVisibility(View.VISIBLE);
        play.setVisibility(View.INVISIBLE);

        if (isconnected()) {
            Log.d("getcard", "startTheEngine");
            getCard();
        } else if (isCard) {
            valid = HawkSingleton.getInstance().hawkGet(VALIDITY);
            response = HawkSingleton.getInstance().hawkGet(RESPONSE);
            Log.d("checkCard", "starttheEngine");
            checkCardValidity(0);
        } else showSnackbar(getString(R.string.nic), true);
    }

    private void getCard() {
        pro.setVisibility(View.VISIBLE);
        play.setVisibility(View.INVISIBLE);
        Log.d("getcard", "called");
        StringRequest request = new StringRequest(StringRequest.Method.POST, BASE_URL + CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        success = true;
                        pro.setVisibility(View.INVISIBLE);
                        play.setVisibility(View.VISIBLE);
                        DataSingleton.response = response;
                        try {
                            JSONObject object = new JSONObject(response);
                            int status = object.getInt("status");
                            Log.d("resp", response);
                            if (status == 0) {
                                showSnackbar(object.getString("msg"), false);
                                isCard = false;
                                success = false;
                            } else if (status == 1) {
                                valid = object.getString("valid_till");
//                                showSnackbar(object.getString("msg"), false);
                                Log.d("checkCard", "status=1");
                                checkCardValidity(0);
                                //send response to GameView.java while opening it
                                intent.putExtra("CARDRESPONSE", response);
                                if (fromPlay) startActivity(intent);
                            } else if (status == 2) {
                                valid = object.getString("valid_till");
//                                showSnackbar(object.getString("msg"), false);
                                Log.d("checkCard", "status=2");
                                checkCardValidity(0);
                                //send response to GameView.java while opening it
                                intent.putExtra("CARDRESPONSE", response);
                                if (fromPlay) startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pro.setVisibility(View.INVISIBLE);
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
                Log.d("userid", " " + String.valueOf(userId));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


    }

    private void showSnackbar(String message, boolean doRetry) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        CustomDialog cd = CustomDialog.newInstance(getApplicationContext(), 0, message);
        cd.show(getSupportFragmentManager(), "customErrorDialog");
        cd.setStyle(0, R.style.DialogTheme);

        pro.setVisibility(View.INVISIBLE);
        play.setVisibility(View.VISIBLE);
    }

    private void checkCardValidity(int which) {
        try {
            //getActual date and time
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
            Date actualDate = sdf.parse(sdf.format(cal.getTime()));
            Date validDate = sdf.parse(valid);
            Log.d("actual", " " + actualDate);
            Log.d("valid ", "  " + validDate);

            if (validDate.compareTo(actualDate) < 0) {
                //validity has expired
                Log.d("CardExp", "yes");
                HawkSingleton.getInstance().hawkDeleteAll();
                isCard = false;
                getCard();
            } else {
                Log.d("cardExp", "No");
                isCard = true;
                HawkSingleton.getInstance().hawkPut(ISCARD, true);
                HawkSingleton.getInstance().hawkPut(VALIDITY, valid);
                HawkSingleton.getInstance().hawkPut(RESPONSE, response);
                success = true;
                pro.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
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