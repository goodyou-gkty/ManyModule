package com.gkt.browse.extras;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Firebase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String,String> mp = new HashMap<>();

        mp.put("hello","welcome");

        databaseReference.push().setValue(mp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null)
                {
                    Log.i("save","saved");
                }
                else
                {
                    Log.i("save","not saved");
                    Log.i("error",databaseError.toString());
                }
            }
        });
    }
}
