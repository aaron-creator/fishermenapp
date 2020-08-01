package com.example.myapplication;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final long INTERVAL = 1000 * 2;
    private static final long FASTEST_INTERVAL = 1000 * 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;
    static double distance = 0;
    double speed;
    double lat,longi;
    private final IBinder mBinder = new LocalBinder();


    String apilink= Constants.ApiMainLink+"samplecall.php";

    private Context activity;


    @Override
    public IBinder onBind(Intent intent) {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        return mBinder;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        distance = 0;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        TravelMode.locate.dismiss();
        mCurrentLocation = location;
        if (lStart == null) {
            lStart = mCurrentLocation;
            lEnd = mCurrentLocation;
        } else
            lEnd = mCurrentLocation;

        updateUI();

        speed = location.getSpeed() * 18 / 5;


        lat = mCurrentLocation.getLatitude();
        longi = mCurrentLocation.getLongitude();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }


    private void updateUI() {
        if (TravelMode.p == 0) {
            distance = distance + (lStart.distanceTo(lEnd) / 1000.00);
            TravelMode.endTime = System.currentTimeMillis();
            long diff = TravelMode.endTime - TravelMode.startTime;
            diff = TimeUnit.MILLISECONDS.toMinutes(diff);
            TravelMode.time.setText("Total Time Travelled : " + diff + " minutes");
            if (speed > 0.0)
            {
                TravelMode.speed.setText("Current speed : " + new DecimalFormat("#.##").format(speed) + " km/hr");

                callApi();

            }
            else
                TravelMode.speed.setText("Vehicle Stopped");
            TravelMode.dist.setText(new DecimalFormat("#.###").format(distance) + " Km's.");
            lStart = lEnd;
        }
    }

    private void callApi() {
        Toast.makeText(getApplicationContext(), "Api  called", Toast.LENGTH_LONG).show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, apilink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                try{
                    JSONObject obj = new JSONObject(response);
                    String rslt = obj.getString("status");


                    if (rslt.equals("success")) {
                        Toast.makeText(getApplicationContext(), "Succesfully Updated in server", Toast.LENGTH_LONG).show();


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }

                }
                catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("res",error.toString());

            }
        }){
            SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);

            // String x = preferences.getString("uid", null);
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params= new HashMap<>();
                params.put("userid",preferences.getString("uid", null));
                params.put("speed",String.valueOf(speed));
                params.put("lat",String.valueOf(lat));
                params.put("longi",String.valueOf(longi));



                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        lStart = null;
        lEnd = null;
        distance = 0;
        return super.onUnbind(intent);
    }
}