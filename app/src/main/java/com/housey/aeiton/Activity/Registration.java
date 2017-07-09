package com.housey.aeiton.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.housey.aeiton.R;
import com.housey.aeiton.Utils.DataSingleton;
import com.housey.aeiton.Utils.NetworkSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.housey.aeiton.Utils.Constants.BASE_URL;
import static com.housey.aeiton.Utils.Constants.GET_UID;
import static com.housey.aeiton.Utils.Constants.ISREGISTERED;
import static com.housey.aeiton.Utils.Constants.REGISTRATION_KEY;
import static com.housey.aeiton.Utils.Constants.REQUEST_KEY;
import static com.housey.aeiton.Utils.Constants.USER_ID;
import static com.housey.aeiton.Utils.DataSingleton.userId;

// TODO: 23-06-2017 change NammaTvMainActivity to respective NavigationDrawerActivity
public class Registration extends NammaTvMainActivity {
    EditText name, email, phone;
    Button next;
    String emailString;
    ProgressDialog dialog;
    String TAG = "REGISTRATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_registration, constraintLayout);
        setTitle("");

        name = (EditText) findViewById(R.id.name_input);
        email = (EditText) findViewById(R.id.email_input);
        phone = (EditText) findViewById(R.id.phone_input);
        next = (Button) findViewById(R.id.button);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Please Wait");
        dialog.setIndeterminate(true);
        dialog.setTitle("Loading...");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailString = email.getText().toString();
                if (network()) {
                    if (name.getText().length() < 4) {
                        name.requestFocus();
                        name.setError("Please, Enter your full name");
                        return;
                    }
                    if (phone.getText().length() < 10 || !TextUtils.isDigitsOnly(phone.getText())) {
                        phone.requestFocus();
                        phone.setError("Please, Enter a valid phone number");
                        return;
                    }
                    emailString = emailString.trim();
                    if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
                        email.requestFocus();
                        email.setError("Please, Enter a valid Email ID");
                        return;
                    }
                    dialog.show();
                    sendToBE();
                } else {
                    Toast.makeText(Registration.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 23-06-2017 Change the index '0' to the position of "HOUSEY" in nav drawer
        navigationView.getMenu().getItem(7).setChecked(true);
    }

    private void sendToBE() {

        StringRequest request = new StringRequest(StringRequest.Method.POST, BASE_URL + GET_UID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject object = new JSONObject(response);
                            DataSingleton.userId = object.getInt("id");
                            Toast.makeText(Registration.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            saveData(object.getInt("id"));
                            dialog.cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(Registration.this, "Some Error Occurred\n" + error, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", REQUEST_KEY);
                params.put("name", name.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("email", email.getText().toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void saveData(int id) {
        getSharedPreferences(REGISTRATION_KEY, MODE_PRIVATE).edit().putBoolean(ISREGISTERED, true).apply();
        getSharedPreferences(REGISTRATION_KEY, MODE_PRIVATE).edit().putInt(USER_ID, id).apply();
        Log.d("userid", " " + userId + " " + id);
        startActivity(new Intent(Registration.this, Splash.class));
        finish();
    }

    private boolean network() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
