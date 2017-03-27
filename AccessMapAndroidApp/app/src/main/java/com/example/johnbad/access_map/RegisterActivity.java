package com.example.johnbad.access_map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    EditText etUsername, etPassword, etEmail, etName, etLastname, etAddress, etPhone, etEmergency, etConfirmPassword;
    Button bSignup;
    String message_addr , message_email, message_Ecall , message_Fname, message_Lname, message_pass, message_Pnumber, message_UserN, message_confirmPass;
    String return_msg_addr, return_msg_email, return_msg_Ecall, return_msg_Fname, return_msg_Lname, return_msg_pass, return_msg_Pnumber, return_msg_UserN, return_msg_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirm);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etName);
        etLastname = (EditText) findViewById(R.id.etLastname);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmergency = (EditText) findViewById(R.id.etEmergency);

        bSignup = (Button) findViewById(R.id.bSignup);

        bSignup.setOnClickListener(this);

    }

    public void receiveMessage() throws JSONException {

       // String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/signup";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/signup";


        JSONObject jsonBody = new JSONObject();
        jsonBody.put("address", message_addr );
        jsonBody.put("email", message_email);
        jsonBody.put("emergencyCall", message_Ecall );
        jsonBody.put("firstName", message_Fname);
        jsonBody.put("lastName", message_Lname );
        jsonBody.put("password", message_pass);
        jsonBody.put("permission", "");
        jsonBody.put("phoneNumber", message_Pnumber );
        jsonBody.put("userName", message_UserN);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            System.out.println("b4 returnMessg");

                            return_msg_addr = response.getString("address");
                            return_msg_email = response.getString("email");
                            return_msg_Ecall = response.getString("emergencyCall");
                            return_msg_Fname = response.getString("firstName");
                            return_msg_Lname = response.getString("lastName");
                            return_msg_pass = response.getString("password");
                            return_msg_Pnumber = response.getString("phoneNumber");
                            return_msg_UserN = response.getString("userName");
                            return_msg_permission = response.getString("permission");

                            System.out.println("After returnMessage "+"Permission is "+return_msg_permission+" "+"address :"+return_msg_addr+" "+"email :"+return_msg_email+" "+"emergencyCall :"+return_msg_Ecall
                                    +" "+"firstName :"+return_msg_Fname+" "+"lastName :"+return_msg_Lname+" "+"password :"+return_msg_pass+" "+"phoneNumber :"+return_msg_Pnumber+" "+"userName :"+return_msg_UserN);

                            if(return_msg_permission.equals("GRANTED")) {
                                Toast.makeText(RegisterActivity.this,"Your account has been created!",Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,"Username or Email already exists!",Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSignup:
                message_addr = etAddress.getText().toString();
                message_email = etEmail.getText().toString();
                message_Ecall = etEmergency.getText().toString();
                message_Fname = etName.getText().toString();
                message_Lname = etLastname.getText().toString();
                message_confirmPass = etConfirmPassword.getText().toString();
                message_pass = etPassword.getText().toString();
                message_Pnumber = etPhone.getText().toString();
                message_UserN = etUsername.getText().toString();


                if(message_UserN.length()==0 || message_pass.length()==0 || message_email.length()==0) {

                    Toast.makeText(context, "You must fill the mandatory fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(message_pass.length() < 6) {

                    Toast.makeText(context, "Password must be 6 or more characters long!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(message_confirmPass.equals(message_pass))) {

                    Toast.makeText(context, "These passwords do not match. Try again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println("My print's output : "+"address :"+message_addr+" "+"email :"+message_email+" "+"emergencyCall :"+message_Ecall
                        +" "+"firstName :"+message_Fname+" "+"lastName :"+message_Lname+" "+"password :"+message_pass+" "+"phoneNumber :"+message_Pnumber+" "+"userName :"+message_UserN);
                try{
                    receiveMessage();
                }catch (JSONException k) {
                    System.out.println("Problem with the JSONObject creation" + k.getMessage());
                }
                break;

        }
    }

}
