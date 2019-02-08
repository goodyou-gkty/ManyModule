package com.gkt.browse.myuberclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LocationCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {

    private ListView driverLists;
    private ArrayList<Double> distance;
    private ArrayList<String> userObjectId;
    private ParseGeoPoint geoPoint;
    private  double latitude;
    private double longitude;
    private ParseObject object;

    private ArrayAdapter<Double> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        if(ParseUser.getCurrentUser()==null)
        {
            login();
        }

        final Intent intent = getIntent();

        latitude = intent.getDoubleExtra("latitude",0.0);
        longitude = intent.getDoubleExtra("longitude",0.0);

        geoPoint = new ParseGeoPoint(latitude,longitude);

        object = new ParseObject("Request");

       /* ParseGeoPoint.getCurrentLocationInBackground(10000, new LocationCallback() {
            @Override
            public void done(ParseGeoPoint geoPoint, ParseException e) {

            }
        });*/

        driverLists = (ListView)findViewById(R.id.driverList);

        distance = new ArrayList<>();
        userObjectId = new ArrayList<>();
        //distance.add(3.4);

        calculateDistance();

        driverLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();

                savingRequest();


                Intent intent1 = new Intent(getApplicationContext(),MapsActivityRider.class);

                intent1.putExtra("objectId",userObjectId.get(position));

                intent1.putExtra("DriverLatitude",latitude);
                intent1.putExtra("DriverLongitude",longitude);

                Log.i("info",String.valueOf(latitude)+" "+String.valueOf(longitude)+" "+userObjectId);
                startActivity(intent1);

            }
        });

    }

    public void calculateDistance()
    {

        ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("Request");

        parseObjectParseQuery.whereNear("RiderLocation",geoPoint);

        parseObjectParseQuery.addDescendingOrder("RiderLocation");

        parseObjectParseQuery.setLimit(20);

        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                if(e==null && objects.size()>0)
                {

                    for (ParseObject parseObject : objects)
                    {

                        if(parseObject.getParseGeoPoint("RiderLocation")!=null)
                        {

                            userObjectId.add(parseObject.getObjectId());
                            double dist = geoPoint.distanceInKilometersTo(parseObject.getParseGeoPoint("RiderLocation"));
                            dist = dist*100;
                            dist = Math.ceil(dist);
                            dist = dist/100;
                            distance.add(dist);
                        }


                    }

                    arrayAdapter = new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,distance);

                    driverLists.setAdapter(arrayAdapter);


                }

                else
                {
                    if(e == null)
                        Log.i("ErrorInDistance",e.toString());
                    else if(objects.size()==0)
                        Log.i("querrySize","zero");

                    arrayAdapter = new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,distance);

                    driverLists.setAdapter(arrayAdapter);
                }
            }
        });


    }

    public void login()
    {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if(e==null && user!=null)
                {
                    object.put("DriverID",user.getUsername());
                    Log.i("DriverID",user.getUsername());
                    object.put("RiderOrDriver","Driver");
                    Log.i("RiderOrDriver","Driver");

                    savingRequest();

                }

                else
                {
                    if(user == null)
                        Log.i("RiderUSerid","null");
                    else
                        Log.i("RiderIdError",e.toString());
                }
            }
        });
    }


    public void savingRequest()
    {
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null) {
                    Log.i("RiderId", "saved");
                }
                else
                    Log.i("sriderSavingError",e.toString());
            }
        });
    }

}
