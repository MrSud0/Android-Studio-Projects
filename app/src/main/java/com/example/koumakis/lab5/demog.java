package com.example.koumakis.lab5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class demog extends AppCompatActivity {

    boolean demogDataExists = false;
    String id = "",username= "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demog);

        Intent i = getIntent();
        Log.i("Name:", i.getStringExtra("username"));
        TextView tv = (TextView) findViewById(R.id.nameTextView);
        tv.setText(i.getStringExtra("username"));
        username=i.getStringExtra("username");
        id = i.getStringExtra("id");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.91.190.186/lesson/api.php/demographics/" + id;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        try {
                            JSONObject json = new JSONObject(response);
                            TextView nameTextV = (TextView) findViewById(R.id.nameText);
                            TextView lastnameTextV = (TextView) findViewById(R.id.lastnameText);
                            TextView genderTextV = (TextView) findViewById(R.id.genderText);

                            nameTextV.setText(json.get("name").toString());
                            lastnameTextV.setText(json.get("lastname").toString());
                            genderTextV.setText(json.get("gender").toString());
                            demogDataExists = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please provide your demographics data.", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void logout(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    public void take_survey(View view){
        Intent i = new Intent(getApplicationContext(), Survey.class);
        i.putExtra("username", username.toString());
        i.putExtra("id",id.toString());
        startActivity(i);
    }

    public void save(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.91.190.186/lesson/api.php/demographics/" + id;
        int method = Request.Method.PUT;
        if (!demogDataExists) {
            url = "http://139.91.190.186/lesson/api.php/demographics/" ;
            method = Request.Method.POST;
        }

        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Demographics data updated.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String responseBody = error.networkResponse.data.toString();
                Toast.makeText(getApplicationContext(), "Error saving your data to the DB.\nPlease try again.\n"+responseBody, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                TextView nameTextV = (TextView) findViewById(R.id.nameText);
                TextView lastnameTextV = (TextView) findViewById(R.id.lastnameText);
                TextView genderTextV = (TextView) findViewById(R.id.genderText);
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", id);
                params.put("name", nameTextV.getText().toString());
                params.put("lastname", lastnameTextV.getText().toString());
                params.put("gender", genderTextV.getText().toString());
                params.put("birthday", "");
                params.put("city", "");
                params.put("ethnicity", "");
                return params;
            }

        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
