package com.gkt.browse.extras;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Adverrise extends AppCompatActivity {

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverrise);
     //   AdView adView = new AdView(this);
       // adView.setAdSize(AdSize.BANNER);
      //  adView.setAdUnitId(String.valueOf(R.string.app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
