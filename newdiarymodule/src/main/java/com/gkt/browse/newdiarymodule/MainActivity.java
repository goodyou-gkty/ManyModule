package com.gkt.browse.newdiarymodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("ExampleObject");

        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e==null && objects!=null) {

                    for (ParseObject object : objects) {

                        Log.i("findBackground:",object.toString());
                    }

                }
            }
        });

        parseObjectParseQuery.whereEqualTo("ExampleObject","new Value");
        parseObjectParseQuery.setLimit(1);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
