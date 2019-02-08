package com.gkt.browse.newdiary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InfoManual extends AppCompatActivity {

    private  LocationSetup locationSetup;
    LocationManager locationManager;
    LocationListener locationListener;
    ListView listView;
    TextView popup;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_of_duty);

        locationSetup = new LocationSetup(locationManager,locationListener,getApplicationContext(),InfoManual.this);

        locationSetup.initilizeLocation();

        ArrayList<String> list = getListItem();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,list);

        listView = (ListView)findViewById(R.id.duty_list);

        popup = (TextView)findViewById(R.id.popup);

        //clicking floating in back contentview context
        floatingActionButton = (FloatingActionButton) findViewById(R.id.back);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"please wait..",Toast.LENGTH_LONG).show();
                //fronImage.animate().alpha(0).setDuration(1000);
            }
        });

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                listView.animate().alpha(1).setDuration(1000);
            }
        });

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i)
                {
                    case 0:

                        Class<?> maps = MapsActivity.class;

                        callIdeaGame(maps);

                        //end of map
                        break;

                    case 1:

                        updateLocation(locationSetup.updateLocationInfo(locationSetup.getLocation()));



                        //end of location info

                        break;

                    case 2:
                        //initilizeLocation();
                        showText(locationSetup.updateAdressInfo(locationSetup.getLocation()));

                        break;

                    case 3:

                        Location locate = locationSetup.getLocation();
                        if(locate!=null)
                        {
                            showText(locationSetup.otherUpdate(locationSetup.getLocation()));
                        }
                        else
                        {
                            Log.i("Error","Null location");
                        }

                        //float bearingAccuracyDegrees = locate.getBearingAccuracyDegrees();

                        break;
                }

            }
        });
    }


    private ArrayList<String> getListItem() {

        ArrayList<String> list = new ArrayList<>();

        list.add("Locate Me on Map!");
        list.add("My GeoLocation!");
        list.add("My Address");
        list.add("Acurcy of Info");
        list.add("MyStudyRoutine");
        list.add("MyMarketingFantacy");
        list.add("MyFavouriteActors");
        list.add("MyFavouriteActresses");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");
        list.add("MyGirl/BoyFriends");

        return list;

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( grantResults.length>0 && grantResults[1]== PackageManager.PERMISSION_GRANTED)
        {
            locationSetup.startListening();
        }
    }

    //show message on list click
    private void showText(String msg)
    {

        popup.setText(msg);

        listView.animate().alpha(0.3f).setDuration(1000);

        popup.setVisibility(View.VISIBLE);

    }

    //Extracting location info from location
    private  void updateLocation(String info)
    {
        popup.setText(info);

        listView.animate().alpha(0.3f).setDuration(1000);

        popup.setVisibility(View.VISIBLE);

    }



    protected void callIdeaGame(Class<?> cls)
    {
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

}