package com.example.johnbad.access_map;


import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.UserDataHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment {

    private static MapView mapView;
    private static GoogleMap googleMap;

    private static Context myContext;
    private static LocationManager locationManager;
    private MyLocationListener myLocationListener;
    private static LatLng latLng;
    private static MarkerOptions markerOptions;
    private static Location location;
    private static boolean isMyLocationSet;
    private static LocationSource.OnLocationChangedListener mListener;
    private static String Username;
    private static List<LatLng> route = new ArrayList<LatLng>();
    private static List<Double> gpsTracking = new ArrayList<Double>();
    private static Double[] sendCurrLatLng = new Double[2];
    private static Double[] sendLatLng = new Double[2];
    private static Polyline line = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_map_fragment, container, false);

        mapView = (MapView) view.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);

        if(mapView!=null)
        {
            googleMap = mapView.getMap();

            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            googleMap.setMyLocationEnabled(true);

            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }

        myContext = getActivity().getApplicationContext();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(myContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(myContext,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, myLocationListener);
            }
        }

        setUpMapIfNeeded();

        return view;
    }

    public static void drawOnMap() {

        if(line!=null) {
            line.remove();
        }
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(route);
        lineOptions.width(12).color(Color.RED);
        line = googleMap.addPolyline(lineOptions);
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public static void getMyLocation() {

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location location) {

                if (isMyLocationSet)
                    return;
                isMyLocationSet = true;

                sendCurrLatLng[0] = location.getLatitude();
                sendCurrLatLng[1] = location.getLongitude();

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
            }
        });
    }

    public static void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) MapMenuActivity.fragmentManager.findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    private static void setUpMap() {
        // For showing a move to my loction button
        googleMap.setMyLocationEnabled(true);
        getMyLocation();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void clearMap() {
        if(line!=null) {
            line.remove();
            googleMap.clear();
        }
    }

    public List<Double> getGpsTracking() {
        return gpsTracking;
    }

    public void afterFinish() {
        gpsTracking.clear();
        getMyLocation();
        isMyLocationSet = false; // This to re-get the current location for the next search request
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (googleMap != null)
            setUpMap();

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) MapMenuActivity.fragmentManager.findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    @Override
    public void onResume()
    {
        mapView.onResume();

        super.onResume();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mapView.onDestroy();
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();

        mapView.onLowMemory();
    }



    private class MyLocationListener extends Activity implements LocationListener , LocationSource {

        @Override
        public void onLocationChanged(Location location) {

            LatLng changedLatLng;

            if( mListener != null )
            {
                mListener.onLocationChanged(location);

                //Move the camera to the user's location once it's available!



            }
            changedLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(changedLatLng, 17.02f));
            gpsTracking.add(location.getLatitude());
            gpsTracking.add(location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(myContext, "Gps is turned on! ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MapFragment.this.getActivity().getWindow().getContext());
            alert.setTitle("GPS");
            alert.setMessage("Your GPS is disabled. Would you like to enable it?");
            alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myContext.startActivity(intent);
                }
            });
            alert.create().show();
        }

        @Override
        public void activate(OnLocationChangedListener listener)
        {
            mListener = listener;

        }

        @Override
        public void deactivate()
        {
            mListener = null;
        }

    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    public static class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(myContext);
            List<Address> addresses = null;

            try {
                // Getting a maximum of 1 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(myContext, "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            googleMap.clear();

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                sendLatLng[0] = address.getLatitude();
                sendLatLng[1] = address.getLongitude();

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                try {
                    receiveMessage();
                } catch(JSONException e) {
                    System.out.println("Problem with the JSONObject creation" + e.getMessage());
                }

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                googleMap.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    public static void receiveMessage() throws JSONException {

        //String url = "http://access-map.ddns.net:8080/AccessMapServer/webresources/search";
        String url = "http://access-map-ce420.ddns.net:8080/AccessMapServer/webresources/search";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", Username);
        jsonBody.put("startingPoint", new JSONArray(sendCurrLatLng));
        jsonBody.put("destination", new JSONArray(sendLatLng));
        jsonBody.put("route", null);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            route.clear();
                            JSONArray return_msg_route = response.getJSONArray("route");
                            System.out.println("Length of route : " + return_msg_route.length());

                            for(int i=0; i< return_msg_route.length(); i++) {
                                String str =  return_msg_route.getString(i);
                                String[] parts = str.split("\\s+");
                                String part1 = parts[0];
                                String part2 = parts[1];
                                Double value1 = Double.parseDouble(part1);
                                Double value2 = Double.parseDouble(part2);
                                System.out.println(value1 + " * " + value2);
                                LatLng tempLatLng = new LatLng(value1,value2);
                                route.add(tempLatLng);
                            }
                            drawOnMap();
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
