package com.gkt.browse.newdiary;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class PrivateDownloadActivity extends AppCompatActivity {

    Button button;
    private class ImageDownloader extends AsyncTask<String , String , String>
    {

        URL url;
        String result="";
        HttpURLConnection httpURLConnection = null;
        InputStream isr = null;

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                isr = httpURLConnection.getInputStream();
                int data = isr.read();

                while(data!=-1)
                {
                    char c = (char)data;
                    result+=c;
                    data = isr.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = jsonObject.getJSONArray(s);
                Log.i("jsonArray",jsonArray.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_download);

      //  String [] imgsrc;
     //   Pattern pattern = Pattern.compile("img class(.*?)src="\(.*?)"\");
       // Log.i("from main:",result);
        button = (Button)findViewById(R.id.download_button);
       final String url = "https://api.unsplash.com/search/photos?query=minimal";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDownloader imageDownloader = new ImageDownloader();
                try {
                    String res =  imageDownloader.execute(url).get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
