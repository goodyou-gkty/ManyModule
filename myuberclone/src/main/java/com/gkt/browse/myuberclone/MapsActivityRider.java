package com.gkt.browse.myuberclone;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MapsActivityRider extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private double latitude;
    private double longitude;
    private String objectId;
    //private ParseObject object;
    private ParseGeoPoint driverGeopoint;
    private ParseGeoPoint riderGeopoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_rider);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("DriverLatitude",0.0);
        longitude = intent.getDoubleExtra("DriverLongitude",0.0);

        objectId = intent.getStringExtra("objectId");

        driverGeopoint = new ParseGeoPoint(latitude,longitude);

       // object = new ParseObject("Request");


       findRiderLocation();

       Log.i("RiderGeopoint",riderGeopoint.toString());
       Log.i("DriverGeopint",driverGeopoint.toString());



    }

    public void findRiderLocation()
    {
        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("Request");
        parseObjectParseQuery.whereEqualTo("objectId",objectId);
        parseObjectParseQuery.setLimit(1);

        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e==null && objects.size()>0)
                {
                    ParseObject parseObject = objects.get(0);
                    riderGeopoint = parseObject.getParseGeoPoint("RiderLocation");

                    if(riderGeopoint != null)
                        Log.i("RiderGeopoint","notNull");
                    else
                        Log.i("RiderGeopoint","null");

                }
                else
                {
                    if(objects.size()==0)
                        Log.i("userID","Not Found");
                    if(e!=null)
                        Log.i("ErrorInDriverSide",e.toString());
                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);

        LatLng sydney = new LatLng(riderGeopoint.getLatitude(),riderGeopoint.getLongitude());

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
