package com.example.johnbad.access_map;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.ads.mediation.MediationServerParameters;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapMenuActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String Username , emergencyCall;
    public static FragmentManager fragmentManager;
    private String return_msg_username,return_msg_pass,return_msg_permission;
    private Button bfinish;
    private ImageButton ibCall;
    private MapFragment fragment;
    private List<Double> gpsTracking;
    //private List<Double>[] gpsTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_menu);

        Intent myIntent = getIntent();
        if(myIntent.hasExtra("Username")) {
            Username = myIntent.getExtras().getString("Username");
            Log.v("username", "" + Username);
        }

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Menu");
        fragmentManager = getSupportFragmentManager();

        fragment = new MapFragment();
        fragment.setUsername(Username);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map, fragment).commit();

        ibCall = (ImageButton) findViewById(R.id.ibCall);
        ibCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    receiveCall();
                } catch(JSONException e) {
                    System.out.println("Problem with the JSONObject creation" + e.getMessage());
                }
            }
        });

        bfinish = (Button) findViewById(R.id.bFinish);
        bfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MapMenuActivity.this.getWindow().getContext());
                final EditText etEval = new EditText(MapMenuActivity.this);
                alert.setTitle("Evaluation");
                alert.setMessage("Submit your Evaluation for the route! (0-10)");
                alert.setView(etEval);
                alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gpsTracking = getGpsTracking();
                        String evalStr = etEval.getText().toString();
                        try {
                            sendEvaluation(evalStr);
                        } catch (JSONException e) {
                            System.out.println("Problem with the JSONObject creation" + e.getMessage());
                        }
                        fragment.afterFinish();
                    }
                });
                alert.create().show();
            }
        });

    }


    private void addDrawerItems() {

        final String[] osArray = {"Edit Account", "Delete Account", "Route History", "Report Problem", "Help",  "Logout"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (osArray[position].equals("Delete Account")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MapMenuActivity.this.getWindow().getContext());
                    alert.setTitle("Delete Account");
                    alert.setMessage("Are you sure you want to delete your account?");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                receiveMessage();
                            } catch (JSONException e) {
                                System.out.println("Problem with the JSONObject creation" + e.getMessage());
                            }
                        }
                    });
                    alert.create().show();
                } else if (osArray[position].equals("Report Problem")) {
                    Intent myIntent = new Intent(MapMenuActivity.this, Email.class);
                    startActivity(myIntent);
                } else if (osArray[position].equals("Logout")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MapMenuActivity.this.getWindow().getContext());
                    alert.setTitle("Logout");
                    alert.setMessage("Are you sure you want to logout?");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fragment.afterFinish();
                            Intent myIntent = new Intent(MapMenuActivity.this, LoginActivity.class);
                            startActivity(myIntent);
                            finish();
                        }
                    });
                    alert.create().show();
                } else if (osArray[position].equals("Edit Account")) {
                    Intent myIntent = new Intent(MapMenuActivity.this, EditAccountActivity.class);
                    myIntent.putExtra("Username", Username);
                    startActivity(myIntent);
                } else if (osArray[position].equals("Route History")) {
                    Intent myIntent = new Intent(MapMenuActivity.this, RouteHistory.class);
                    myIntent.putExtra("Username", Username);
                    startActivity(myIntent);
                } else if (osArray[position].equals("Help")) {
                    Intent myIntent = new Intent(MapMenuActivity.this, Help.class);
                    startActivity(myIntent);
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Options");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MapFragment.GeocoderTask mapGeo = new MapFragment.GeocoderTask();
                mapGeo.execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_us) {
            Intent myIntent = new Intent(MapMenuActivity.this, About.class);
            startActivity(myIntent);
            return true;
        }
        if(id == R.id.Clear_Map) {
            fragment.clearMap();
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void receiveMessage() throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/delete";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/delete";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", Username);
        jsonBody.put("password", "");
        jsonBody.put("permission", "");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            return_msg_username = response.getString("userName");
                            return_msg_pass = response.getString("password");
                            return_msg_permission = response.getString("permission");

                            if(return_msg_permission.equals("GRANTED")) {
                                Toast.makeText(MapMenuActivity.this,"Your account has been deleted.",Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(MapMenuActivity.this,LoginActivity.class);
                                startActivity(myIntent);
                                finish();
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

    public void receiveCall() throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/emergency/";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/emergency";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", Username);
        jsonBody.put("emergencyNumber", "");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            emergencyCall = response.getString("emergencyNumber");
                            makeCall(emergencyCall);

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

    public void makeCall(String eCall) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+eCall));
        startActivity(callIntent);
    }

    public List<Double> getGpsTracking() {
        List<Double> list = fragment.getGpsTracking();
        System.out.println("********************************");
        for(Double temp : list) {
            System.out.println(temp);
        }
        return list;
    }

    public void sendEvaluation(String eval) throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/receive";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/receive";


        if(eval.equals("")) {
            Toast.makeText(MapMenuActivity.this,"You must submit a number. Please try again!",Toast.LENGTH_SHORT).show();
            return;
        }

        int evaluation = Integer.parseInt(eval);
        if (evaluation < 0 || evaluation > 10) {
            Toast.makeText(MapMenuActivity.this,"Your evaluation must be from 0 to 10. Please try again!",Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", Username);
        jsonBody.put("evaluation", evaluation);
        jsonBody.put("route", new JSONArray(gpsTracking));

        System.out.println(jsonBody);

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
