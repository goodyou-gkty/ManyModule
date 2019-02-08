package com.gkt.browse.newdiary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationSetup {

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Location location;

    private  Context context;

    private Activity activity;

    LocationSetup(LocationManager locationManager,LocationListener locationListener,Context context,Activity activity)
    {

        this.locationListener = locationListener;

        this.locationManager = locationManager;

        this.context = context;

        this.activity = activity;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
    public void startListening()
    {


        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {


            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }
    }


    public void initilizeLocation()
    {

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //  Log.i("Locatrion:",location.getAltitude()+" "+location.getLatitude()+" "+location.getLongitude());
                // PopupMenu popupMenu = new PopupMenu(getParent(),MainActivity.this);
                //   location1 = location;
                setLocation(location);
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
        };


        if(Build.VERSION.SDK_INT<23)
        {
            startListening();
        }

        else
        {

            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

            else
            {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                setLocation(location);

                //  updateLocationInfo(getLocation());
            }
        }

    }


    public String otherUpdate(Location locate) {

        float speed = locate.getSpeed();

        long time = locate.getTime();

        String provider = locate.getProvider();

        float bearing = locate.getBearing();

        float accuracy = locate.getAccuracy();
        // Log.i("info 79809876789:",speed+" "+time+" "+provider+" "+bearing+"  "+accuracy);

        String msg = "Some Essential Info\n"
                + "\nspeed:" + speed + "\n"
                + "\ntime:" + time + "\n"
                + "\nprovider:" + provider + "\n"
                + "\nbearing:" + bearing + "\n"
                + "accuracy:" + accuracy;

        return  msg;
    }

    public  String updateAdressInfo(Location location) {
        String adress = "";

        if (location != null) {

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            try {

                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                //Toast.makeText(getApplicationContext(),addressList.get(0).toString(),Toast.LENGTH_LONG).show();

                if (addressList != null && addressList.size() > 0) {


                    if (addressList.get(0).getCountryCode() != null) {
                        adress += "CountryCode:" + addressList.get(0).getCountryCode() + "\n";
                    }

                    if (addressList.get(0).getCountryName() != null) {
                        adress += "CountryName:" + addressList.get(0).getCountryName() + "\n";
                    }

                    if (addressList.get(0).getThoroughfare() != null) {
                        adress += "ThoroughFare:" + addressList.get(0).getThoroughfare() + "\n";
                    }

                    if (addressList.get(0).getSubThoroughfare() != null) {
                        adress += "SubThroughFare:" + addressList.get(0).getSubThoroughfare() + "\n";
                    }

                    if (addressList.get(0).getLocality() != null) {
                        adress += "Locality:" + addressList.get(0).getLocality() + "\n";
                    }

                    if (addressList.get(0).getSubLocality() != null) {

                        adress += "SubLocality:" + addressList.get(0).getSubLocality() + "\n";
                    }

                } else
                    adress = "Adress Not Available";
            } catch (IOException e) {
                adress = "Unable To Fetch Adress";

            }
        } else {

            adress = "Unable to Locate You";
        }
    return adress;
    }


    public  String updateLocationInfo(Location location)
    {
        String info="unable To Fetch Information\n sorry\nnplease turn on your gps...";
        if(location!=null) {

            double latitude = location.getLatitude();

            double longitude = location.getLongitude();

            double altitude = location.getAltitude();

            double accuracy = location.getAccuracy();


            info = "Your GeoLocation Info:\n"
                    + "\nLatitude: " + String.valueOf(latitude) + "\n"
                    + "\nLongitude: " + String.valueOf(longitude) + "\n"
                    + "\n Altitude: " + String.valueOf(altitude) + "\n"
                    + "\nAccuracy: " + String.valueOf(accuracy) + "\n"
                    + "\nClick it to get out";
        }
        return info;

        }
        }
