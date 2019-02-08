package com.parse.starter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.xml.sax.Parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle("App User");
        setTitleColor(Color.argb(1,100,200,105));
        listView = (ListView)findViewById(R.id.userList);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_list_item_1,arrayList);

        ParseQuery<ParseUser> parseObjectParseQuery = ParseUser.getQuery();
        parseObjectParseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseObjectParseQuery.addAscendingOrder("username");

        parseObjectParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(e==null && objects.size()>0)
                {

                    for(ParseUser user : objects)
                    {
                        arrayList.add(user.getUsername());
                        Log.i("userList","userList available");
                        Log.i("user",user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"unable-to-fetch-newtwork-error",Toast.LENGTH_LONG).show();
                    Log.i("userList","userList not available");
                    arrayList.add("server unable to connect");
                    listView.setAdapter(arrayAdapter);
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String user  = arrayList.get(i);
                if(user!=null) {
                    Intent intent = new Intent(getApplicationContext(), ImageFeedActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"user-is-not-active",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu,menu);
        return true;
    }

    public void getPhoto()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.share) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {

                    getPhoto();

                }

            } else {

                getPhoto();

            }
        }

        return super.onOptionsItemSelected(item);

}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getPhoto();
                Log.i("permissiononDemand","permission Granted");
            }
            else
            {
                Log.i("permissiononDemand","permission not Granted");
            }
        }
        else
        {
            Log.i("PermissiononDemand","requestCodeError");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null)
        {
            Log.i("ImageData","image Selected");
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                Log.i("Photo","photoSelected");

                if(bitmap==null)
                {
                    Log.i("bitmap","null");
                    Toast.makeText(getApplicationContext(),"ImageSelected",Toast.LENGTH_LONG).show();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

                byte [] byteArray = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("image.png",byteArray);
                ParseObject parseObject = new ParseObject("Image");
                parseObject.put("image",parseFile);
                parseObject.put("username",ParseUser.getCurrentUser().getUsername());

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e==null)
                        {
                            Log.i("ImageSave","saved Sucessfully");
                            Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.i("ImageSave","Unabe to Save");
                            Toast.makeText(getApplicationContext(), "Uploading-failed-connect-to-internet", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } catch (IOException e) {
                Log.i("photo","photoNotSelected");
                Toast.makeText(this,"Image-Not-Selected",Toast.LENGTH_SHORT).show();
            }



        }
        else
        {
            Log.i("ImageData","ImageNotSelected");
            Toast.makeText(this,"Image-Not-Selected",Toast.LENGTH_SHORT).show();
        }
    }
}
