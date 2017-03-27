package com.example.johnbad.access_map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private EditText etUsername, etPassword;
    private Button bLogin;
    private TextView tvSignUp;
    private  String message_userName, message_password;
    private  String return_msg_permission, return_msg_userName, return_msg_password,return_msg_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        etUsername  = (EditText) findViewById(R.id.etUsername);
        etPassword  = (EditText) findViewById(R.id.etPassword);
        bLogin     = (Button) findViewById(R.id.bLogin);
        tvSignUp    = (TextView) findViewById(R.id.tvSignUp);

        bLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

    }

    public void recieveMessage() throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/login";
        //String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/login";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/login";


        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", message_userName );
        jsonBody.put("password", message_password);
        jsonBody.put("permission", "");
        jsonBody.put("emergencyNumber", "");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s", response.toString(4));

                            return_msg_permission = response.getString("permission");
                            return_msg_userName = response.getString("userName");
                            return_msg_password = response.getString("password");


                            System.out.println("After returnMessage : "+"Permission :"+return_msg_permission+" "+"userName :"+return_msg_userName+" "+"password :"+return_msg_password );

                            if(return_msg_permission.equals("GRANTED")) {
                                Intent myIntent = new Intent(LoginActivity.this,MapMenuActivity.class);
                                myIntent.putExtra("Username",message_userName);
                                Log.v("Username :", message_userName);
                                startActivity(myIntent);
                                Toast.makeText(context,"Welcome " + message_userName + "!\n\nBe noted that the suggested route follows the direction of cars.",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                Toast.makeText(context,"Invalid Username or Password!",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {

                            System.out.println("ERROR ON RESPONSE" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        ApplicationController.getInstance().addToRequestQueue(req);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bLogin:
                message_userName = etUsername.getText().toString();
                message_password = etPassword.getText().toString();

                System.out.println("My print's output : "+"userName :"+message_userName+" "+"password :"+message_password);

                if(message_userName.length() == 0 || message_password.length()==0) {

                    Toast.makeText(context, "Please fill in username and password!", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(message_userName.equals("admin") && message_password.equals("admin")) {
                    Intent myIntent = new Intent(LoginActivity.this,MapMenuActivity.class);
                    myIntent.putExtra("Username","admin");
                    startActivity(myIntent);
                    finish();
                }

                try{
                    recieveMessage();
                }catch (JSONException k) {
                    System.out.println("Problem with the JSONObject creation" + k.getMessage());
                }

                break;

            case R.id.tvSignUp:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    public void sendReminder(String Username) throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/login";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/login";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName",Username );

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s", response.toString(4));

                        } catch (JSONException e) {

                            System.out.println("ERROR ON RESPONSE" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        ApplicationController.getInstance().addToRequestQueue(req);
    }

}
