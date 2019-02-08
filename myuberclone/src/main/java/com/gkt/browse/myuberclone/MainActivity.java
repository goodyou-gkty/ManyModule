package com.gkt.browse.myuberclone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    private Switch userSwitch;
    private  boolean switchUSer = false;
    private Button button;
    private   LocationManager locationManager;
    private  LocationListener locationListener;
    private boolean locations = false;

    private ParseGeoPoint geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSwitch = (Switch)findViewById(R.id.userSwitcher);

        button = findViewById(R.id.button);

        userSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                switchUSer = isChecked;

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchUsers();
            }
        });

    }

    public void switchUsers()
    {

        if(!switchUSer)
        {

            Intent intent = new Intent(this.getApplicationContext(),MapsActivity.class);
       Log.i("RiderId","saved");
       startActivity(intent);

        }
        else
        {

            final ParseObject request = new ParseObject("Request");

            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    if(location!=null)
                    {

                        if(geoPoint==null) {
                            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                            request.addUnique("DriverLocation", parseGeoPoint);
                            Log.i("location1", String.valueOf(location));
                            Log.i("geopoints1", String.valueOf(parseGeoPoint));
                            locations = true;
                            geoPoint = parseGeoPoint;
                        }
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };


            if (Build.VERSION.SDK_INT < 21)
            {

                startListening();

            }
            else
                {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else
                    {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null && geoPoint==null)
                    {

                        Log.i("location",String.valueOf(location));

                        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(),location.getLongitude());

                        request.addUnique("DriverLocation",parseGeoPoint);
                        geoPoint = parseGeoPoint;

                        Log.i("geopoints2",String.valueOf(parseGeoPoint));
                        locations=true;

                    }

                }

            }

            if(locations)
            {
                request.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {


                        if(e == null)
                        {

                            Log.i("DriverLocation","saved to server");
                            Log.i("DriverGeopoint",String.valueOf(geoPoint));

                            Intent intent = new Intent(getApplicationContext(),DriverActivity.class);

                            intent.putExtra("latitude",geoPoint.getLatitude());
                            intent.putExtra("longitude",geoPoint.getLongitude());
                            startActivity(intent);

                        }
                        else
                        {
                            Log.i("DriverLocation","notSaved");
                            Log.i("error",e.toString());
                        }
                    }
                });

            }

            else
            {
                Log.i("DriverLocation","unable to save");
            }

        }


    }

    public void startListening() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();

        }

    }

}

