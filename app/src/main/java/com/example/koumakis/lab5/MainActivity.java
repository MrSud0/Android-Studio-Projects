package com.example.koumakis.lab5;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        //Intent in = new Intent(getApplicationContext(), demog.class);
        //in.putExtra("name", "John");
        //startActivity(in);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://139.91.190.186/lesson/api.php/users/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        TextView tName = (TextView) findViewById(R.id.usernameText);
                        TextView tPass = (TextView) findViewById(R.id.passText);
                        Log.i("User typed", tName.getText().toString() +"\t"+tPass.getText().toString());
                        boolean flag = true;
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray usersRecords = json.getJSONObject("users").getJSONArray("records");
                            for (int i = 0; i < usersRecords.length(); i++) {
                                JSONArray curRecord = usersRecords.getJSONArray(i);
                                if( (curRecord.getString(1).compareTo(tName.getText().toString()) ==0)
                                        && (curRecord.getString(2).compareTo(tPass.getText().toString()) ==0)){
                                        Toast.makeText(getApplicationContext(), "Hello !", Toast.LENGTH_LONG).show();
                                            Intent in = new Intent(getApplicationContext(), demog.class);
                                            in.putExtra("username", tName.getText().toString());
                                            in.putExtra("id", curRecord.getString(0).toString());
                                            startActivity(in);
                                            flag = false;
                                            break;
                                }
                            }
                            if(flag)
                                Toast.makeText(getApplicationContext(), "Username or password error.\nPlease try again.", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error at REST API call.", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
