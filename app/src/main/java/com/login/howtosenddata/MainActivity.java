package com.login.howtosenddata;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String server_rul = "http://54.38.242.156/api/";
    double lat;
    double lng;

  List <Address> address;
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        final String full_name=bundle.getString("full_name");
        final Button button=findViewById(R.id.streaming);
        setContentView(R.layout.activity_main);
        //GetPosition
        final LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
       // criteria.setAltitudeRequired(true);

       // criteria.setBearingRequired(true);

        String best = lManager.getBestProvider(new Criteria(), true);
        Toast.makeText(this,best,Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this,"Issue with permission",Toast.LENGTH_LONG).show();
            return;
        }

        // location = lManager.getLastKnownLocation(best);
        if(location!=null){
         lat=(location.getLatitude());
        lng=(location.getLongitude());
            Toast.makeText(MainActivity.this, "location", Toast.LENGTH_SHORT).show();
        }
//        else{
//            lManager.requestLocationUpdates(best,0,0, (LocationListener) this);
//            location = lManager.getLastKnownLocation(best);
//
//p
//        }


  else  {

lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(MainActivity.this, "Update", Toast.LENGTH_SHORT).show();
        // lManager.removeUpdates(this);
       lat=location.getLatitude();
       lng=location.getLongitude();
       Toast.makeText(MainActivity.this, "latitude:" + lat + " longitude:" + lng, Toast.LENGTH_SHORT).show();
       Log.d("latitude",Double.toString(lat));

      //     Get Position Name

                final Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            address=  geocoder.getFromLocation(lat,lng,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_rul,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                if(lat!=0&&lng!=0){
                    params.put("lng", Double.toString(lng));
                    params.put("lat", Double.toString(lat));
                    params.put("place", full_name+" "+address.get(0).getLocality());
                }
                return params;
            }
        };
        Singleton.getInstence(MainActivity.this).addToRequestQue(stringRequest);
        lManager.removeUpdates(this);

//api/
        //lManager.removeUpdates(this);
     //   Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
});



       }


     //   Get Position Name
//
//                final Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
//        try {
//            address=  geocoder.getFromLocation(lat,lng,4);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//








      // Web Service

//                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_rul,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
//                    }
//                }
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                error.printStackTrace();
//
//            }
//
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String,String>();
//                if(lat!=0&&lng!=0){
//                params.put("lng", Double.toString(lng));
//                params.put("lat", Double.toString(lat));
//                params.put("place", "seif");
//                }
//                return params;
//            }
//        };
//        Singleton.getInstence(MainActivity.this).addToRequestQue(stringRequest);
//


    }
    public void Streaming( View view){
        Intent intent =new Intent(getApplicationContext(),Streaming.class);
        startActivity(intent);

    }
}
