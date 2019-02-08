package com.gkt.browse.newdiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class NewsReader extends AppCompatActivity {

    ListView listView;
     ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> url;
    ArrayList<String> content = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    SQLiteDatabase articlesDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reader);

        listView = (ListView) findViewById(R.id.newsTitle);

        articlesDb = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);

        articlesDb.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleId INTEGER, title VARCHAR, content VARCHAR)");

       // updateListView();

        titles.add("hello");
        arrayAdapter = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_list_item_1,titles);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),NewsArticle.class);

               // intent.putExtra("content", content.get(i));

                startActivity(intent);
            }
        });

        DownloadTask downloadTask = new DownloadTask();
        try {
            downloadTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

   public void updateListView() {
        Cursor c = articlesDb.rawQuery("SELECT * FROM articles", null);

        int contentIndex = c.getColumnIndex("content");
        int titleIndex = c.getColumnIndex("title");

        if (c.moveToFirst()) {
            titles.clear();
            content.clear();

            do {
                titles.add(c.getString(titleIndex));
                content.add(c.getString(contentIndex));
            } while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){

            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                in.close();
                reader.close();

                JSONArray jsonArray = new JSONArray(result);

                int numberOfItems = 20;

                if (jsonArray.length() < 20) {

                    numberOfItems = jsonArray.length();

                }

                articlesDb.execSQL("DELETE FROM articles");

                for (int i = 0; i < numberOfItems; i++) {

                    String articleId = jsonArray.getString(i);

                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty");

                    urlConnection = (HttpURLConnection) url.openConnection();

                    in = urlConnection.getInputStream();

                    reader = new InputStreamReader(in);

                    data = reader.read();

                    String articleInfo = "";

                    while (data != -1) {
                        char current = (char) data;

                        articleInfo += current;

                        data = reader.read();
                    }

                    in.close();
                    reader.close();
                    Log.i("contents:"+String.valueOf(i), articleInfo);
                    JSONObject jsonObject = new JSONObject(articleInfo);

                    if (!jsonObject.isNull("title") && !jsonObject.isNull("url")) {

                        String articleTitle = jsonObject.getString("title");

                        String articleURL = jsonObject.getString("url");

                        url = new URL(articleURL);

                        urlConnection = (HttpURLConnection) url.openConnection();

                        in = urlConnection.getInputStream();

                        reader = new InputStreamReader(in);

                        data = reader.read();

                        String articleContent = "";

                        while (data != -1) {
                            char current = (char) data;

                            articleInfo += current;

                            data = reader.read();
                        }
                        Log.i("articleContent:",articleContent);

                       String sql = "INSERT INTO articles (articleId, title, content) VALUES (?, ?, ?)";

                        SQLiteStatement statement = articlesDb.compileStatement(sql);

                        statement.bindString(1, articleId);
                        statement.bindString(2, articleTitle);
                        statement.bindString(3, articleContent);

                        statement.execute();

                    }
                    in.close();
                    reader.close();

                }
            }

            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            updateListView();

            Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
        }
    }
}

