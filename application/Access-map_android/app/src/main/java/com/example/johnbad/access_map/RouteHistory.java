package com.example.johnbad.access_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RouteHistory extends AppCompatActivity implements View.OnClickListener {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_history);

        getSupportActionBar().setTitle("Route History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent myIntent = getIntent();
        if(myIntent.hasExtra("Username")) {
            Username = myIntent.getExtras().getString("Username");
            Log.v("username", "" + Username);
        }

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);

        try {
            receiveMessage();
        } catch (JSONException e) {
            System.out.println("Problem with the JSONObject creation" + e.getMessage());
        }
    }



    public void receiveMessage() throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/history";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/history";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", Username);
        jsonBody.put("historyArray", null);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            JSONArray return_msg_history = response.getJSONArray("historyArray");
                            String[] history = new String[5];

                            for(int i=0; i<return_msg_history.length(); i++) {
                                history[i] = return_msg_history.getString(i);
                            }

                            tv1.setText(history[0]);
                            tv2.setText(history[1]);
                            tv3.setText(history[2]);
                            tv4.setText(history[3]);
                            tv5.setText(history[4]);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        String temp = null;
        String[] parts;
        MapFragment.GeocoderTask mapGeo;

        switch (view.getId()) {

            case R.id.tv1:
                temp = tv1.getText().toString();
                if(!temp.equals("You haven't searched anything yet.")) {
                    parts = temp.split(",");
                    mapGeo = new MapFragment.GeocoderTask();
                    mapGeo.execute(parts[0] + " " + parts[1]);
                    finish();
                    break;
                } else {
                    break;
                }

            case R.id.tv2:
                temp = tv2.getText().toString();
                parts = temp.split(",");
                mapGeo = new MapFragment.GeocoderTask();
                mapGeo.execute(parts[0] + " " + parts[1]);
                finish();
                break;

            case R.id.tv3:
                temp = tv3.getText().toString();
                parts = temp.split(",");
                mapGeo = new MapFragment.GeocoderTask();
                mapGeo.execute(parts[0] + " " + parts[1]);
                finish();
                break;

            case R.id.tv4:
                temp = tv4.getText().toString();
                parts = temp.split(",");
                mapGeo = new MapFragment.GeocoderTask();
                mapGeo.execute(parts[0] + " " + parts[1]);
                finish();
                break;

            case R.id.tv5:
                temp = tv5.getText().toString();
                parts = temp.split(",");
                mapGeo = new MapFragment.GeocoderTask();
                mapGeo.execute(parts[0] + " " + parts[1]);
                finish();
                break;
        }
    }
}
