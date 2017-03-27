package com.example.johnbad.access_map;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener{

    Button bEdit;
    Context context;
    EditText etOldPassword, etNewPassword, etName, etLastname, etAddress, etPhone, etEmergency, etConfirmPassword;
    String Username;

    String message_addr , message_email, message_Ecall , message_Fname, message_Lname, message_Newpass, message_Pnumber, message_UserN, message_confirmPass, message_oldPass;
    String return_msg_addr, return_msg_email, return_msg_Ecall, return_msg_Fname, return_msg_Lname, return_msg_pass, return_msg_Pnumber, return_msg_UserN, return_msg_permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        getSupportActionBar().setTitle("Edit Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent myIntent = getIntent();
        if(myIntent.hasExtra("Username")) {
            Username = myIntent.getExtras().getString("Username");
            Log.v("username", "" + Username);
        }

        context = this;

        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etName = (EditText) findViewById(R.id.etFirstName);
        etLastname = (EditText) findViewById(R.id.etLastName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmergency = (EditText) findViewById(R.id.etEmergencyCall);

        bEdit = (Button) findViewById(R.id.bEdit);

        bEdit.setOnClickListener(this);

        try{
            receiveMessage();
        }catch (JSONException k) {
            System.out.println("Problem with the JSONObject creation" + k.getMessage());
        }

    }

    public void receiveMessage() throws JSONException {

        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/edit";
        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/edit";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", Username);
        jsonBody.put("password", null);
        jsonBody.put("email", null);
        jsonBody.put("firstName", null);
        jsonBody.put("lastName", null);
        jsonBody.put("address", null );
        jsonBody.put("phoneNumber", null);
        jsonBody.put("emergencyCall", null );

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            System.out.println("b4 returnMessg");

                            //return_msg_UserN = response.getString("userName");
                            return_msg_pass = response.getString("password");
                            return_msg_email = response.getString("email");
                            return_msg_Fname = response.getString("firstName");
                            return_msg_Lname = response.getString("lastName");
                            return_msg_addr = response.getString("address");
                            return_msg_Pnumber = response.getString("phoneNumber");
                            return_msg_Ecall = response.getString("emergencyCall");
                            //return_msg_permission = response.getString("permission");

                            System.out.println("After returnMessage " + "address :" + return_msg_addr + " " + "email :" + return_msg_email + " " + "emergencyCall :" + return_msg_Ecall
                                    + " " + "firstName :" + return_msg_Fname + " " + "lastName :" + return_msg_Lname + " " + "password :" + return_msg_pass + " " + "phoneNumber :" + return_msg_Pnumber);

                            etName.setText(return_msg_Fname);
                            etLastname.setText(return_msg_Lname);
                            etAddress.setText(return_msg_addr);
                            etPhone.setText(return_msg_Pnumber);
                            etEmergency.setText(return_msg_Ecall);

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

    public void receiveMessage2() throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/edit/confirm";
        String url ="http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/edit/confirm";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", Username);
        jsonBody.put("password", message_Newpass);
        jsonBody.put("email", message_email);
        jsonBody.put("firstName", message_Fname);
        jsonBody.put("lastName", message_Lname);
        jsonBody.put("address", message_addr);
        jsonBody.put("phoneNumber", message_Pnumber);
        jsonBody.put("emergencyCall", message_Ecall);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            //return_msg_UserN = response.getString("userName");
                            return_msg_pass = response.getString("password");
                            return_msg_email = response.getString("email");
                            return_msg_Fname = response.getString("firstName");
                            return_msg_Lname = response.getString("lastName");
                            return_msg_addr = response.getString("address");
                            return_msg_Pnumber = response.getString("phoneNumber");
                            return_msg_Ecall = response.getString("emergencyCall");

                            Toast.makeText(EditAccountActivity.this, "Your changes have been saved successfully!", Toast.LENGTH_SHORT).show();
                            finish();

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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bEdit:
                message_UserN = Username;
                message_oldPass = etOldPassword.getText().toString();
                message_Newpass = etNewPassword.getText().toString();
                message_confirmPass = etConfirmPassword.getText().toString();
                message_Fname = etName.getText().toString();
                message_Lname = etLastname.getText().toString();
                message_addr = etAddress.getText().toString();
                message_Pnumber = etPhone.getText().toString();
                message_Ecall = etEmergency.getText().toString();

                if(!message_Newpass.isEmpty()) {
                    if (!message_oldPass.equals(return_msg_pass)) {

                        Toast.makeText(context, "Wrong old password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (message_Newpass.length() < 6) {

                        Toast.makeText(context, "Password must be 6 or more characters long!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!message_Newpass.equals(message_confirmPass)) {

                        Toast.makeText(context, "Passwords don't match!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    message_Newpass = return_msg_pass;
                }

                try{
                    receiveMessage2();

                }catch (JSONException k) {
                    System.out.println("Problem with the JSONObject creation" + k.getMessage());
                }
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
