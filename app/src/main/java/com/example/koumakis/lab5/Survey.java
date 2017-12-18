package com.example.koumakis.lab5;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason on 12/17/2017.
 */

public class Survey  extends AppCompatActivity {
    final GlobalClass globalFlag = new GlobalClass();
    private RadioGroup radioGroup;
    private RadioButton radioOption1, radioOption2, radioOption3,radioOption4;
    private Button submit,logout;
    private TextView textView;
    String id,username;
    int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        id=i.getStringExtra("id");
        username= i.getStringExtra("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioOption1) {
                    score=0;
                   // Toast.makeText(getApplicationContext(), "choice: Option1",
                            //Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioOption2) {
                    score=1;
                    //Toast.makeText(getApplicationContext(), "choice: Option2",
                           // Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioOption3) {
                    score=2;
                    //Toast.makeText(getApplicationContext(), "choice: Option3",
                          // Toast.LENGTH_SHORT).show();
                }else {
                    score=3;
                    //Toast.makeText(getApplicationContext(), "choice: Option4",
                           // Toast.LENGTH_SHORT).show();
                }
            }

        });

        logout = (Button)findViewById(R.id.LogoutButton);
        submit = (Button)findViewById(R.id.submitButton);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout(v);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit(v);
            }
        });
    }
    public void submit (View view){
        //check ,works only for this case. Best solution that I know of should have been to GET then if we got 404 we will POST otherwise we will PUT
        String url = "http://139.91.190.186/lesson/api.php/mdcalcresults/" + id;
        int method = Request.Method.PUT;
        if (!globalFlag.getFlag()) {
            url = "http://139.91.190.186/lesson/api.php/mdcalcresults/" ;
            method = Request.Method.POST;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error at PUT mdcalcresults API call.",
                        Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //capture the timestamp and format it to the request pattern
                long currentTime=System.currentTimeMillis();
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:MM:SS");
                final Date resultdate = new Date(currentTime) ;

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                //display a toast message with the value of the radio button to check the submit button works
                TextView surveyName = (TextView) findViewById(R.id.surveyName) ;
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("userId", id);
                params.put("date",sdf.format(resultdate));
                params.put("calculatorName",surveyName.getText().toString());
                params.put("results", radioButton.getText().toString() );
                params.put("username", username);


                return params;
            }
        };
            // Add the request to the RequestQueue.
        globalFlag.setFlag(true);
        queue.add(stringRequest);
            //for the scoring to work properly we need the other 3 metrics of the suggested methodolog yhttps://www.mdcalc.com/bode-index-copd-survival#evidence

        switch(score){
            case 0:
            case 1:
            case 2:
                Toast.makeText(getApplicationContext(), "Your BODE Index Score is: "+ score +'\n'+"That gives you a chance of 80% for 4-year survival ", Toast.LENGTH_LONG).show();
                break;
            case 3:
            case 4:
                Toast.makeText(getApplicationContext(), "Your BODE Index Score is: "+ score +'\n'+"That gives you a chance of 67% for 4-year survival ", Toast.LENGTH_LONG).show();
                break;
            case 5:
            case 6:
                Toast.makeText(getApplicationContext(), "Your BODE Index Score is: "+ score +'\n'+"That gives you a chance of 57% for 4-year survival ", Toast.LENGTH_LONG).show();
                break;
            case 7:
            case 8:
            case 9:
            case 10:
                Toast.makeText(getApplicationContext(), "Your BODE Index Score is:"+ score +'\n'+"that gives you a chance of 18% for 4-year survival ", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "You have an invalid value of BODE Index score of "+score, Toast.LENGTH_LONG).show();

        }


    }


    public void logout(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}