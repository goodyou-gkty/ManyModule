package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.xml.sax.Parser;

import java.util.List;

public class ImageFeedActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_feed);

        setTitle("Image Feed");

        Intent intent = getIntent();

        String activeUSer = intent.getStringExtra("username");

        if(activeUSer!=null)
        {
            ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("Image");
            parseObjectParseQuery.whereEqualTo("username",activeUSer);
            parseObjectParseQuery.addDescendingOrder("updatedAt");

            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if(e==null && objects.size()>0)
                    {
                        Toast.makeText(getApplicationContext(),"Fetching",Toast.LENGTH_SHORT).show();

                        Log.i("ImageFetch","fetched");

                        for(ParseObject parseObject : objects)
                        {

                            ParseFile file = (ParseFile)parseObject.get("image");

                           file.getDataInBackground(new GetDataCallback() {
                               @Override
                               public void done(byte[] data, ParseException e) {

                                   Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                   ImageView imageView = new ImageView(getApplicationContext());

                                   imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                           ViewGroup.LayoutParams.MATCH_PARENT,
                                           ViewGroup.LayoutParams.WRAP_CONTENT
                                   ));

                                 //  imageView.setPadding(10,10,10,10);

                                   imageView.setImageBitmap(bitmap);
                                   linearLayout.addView(imageView);
                               }
                           });

                        }
                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(),"Unable-To-Fetch-Connect-Internet",Toast.LENGTH_LONG).show();
                        Log.i("ImageFetch","Not-Fetched");
                    }
                }
            });
        }


    }
}
