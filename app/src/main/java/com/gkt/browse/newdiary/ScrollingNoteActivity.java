package com.gkt.browse.newdiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ScrollingNoteActivity extends AppCompatActivity {


    private ListView listView;
    static ArrayList<String> info = new ArrayList<>();
    SharedPreferences sharedPreferences;
    static ArrayAdapter<String> arrayAdapter;
    protected  void timer()
    {
        CountDownTimer countDownTimer = new CountDownTimer(1000*60*5,1000) {

            int i=0;

            @Override
            public void onTick(long l) {

                // Log.i("onTick value",String.valueOf(l));
                long time = i;

                long min = time/60;

                long sec = time - min*60;

                i++;

                String mins="00";

                String secs ="00";

                if(min!=0)
                    mins = "0"+String.valueOf(min);

                if(sec<10 && sec>0)
                    secs="0"+String.valueOf(sec);

                else if(sec>=10)
                    secs = String.valueOf(sec);

                String times = mins+":"+secs;
                textView.setText(times);
            }

            @Override
            public void onFinish() {
                textView.setText("00:00");
                Toast.makeText(getApplicationContext(),"saving..",Toast.LENGTH_LONG);
            }
        };
        countDownTimer.start();
    }

    protected  void  restartEdit(View view )
    {
    textView.setText("00:00");
    timer();

    }

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_scrolling_note);

        textView = (TextView)findViewById(R.id.counter);
        timer();

        listView = (ListView)findViewById(R.id.notes);

        HashSet<String> hashSet = new HashSet<>();

         final SharedPreferences sharedPreferences = this.getSharedPreferences("com.gkt.browse.newdiary",Context.MODE_PRIVATE);
         hashSet = (HashSet<String>) sharedPreferences.getStringSet("notes",null);

         if(hashSet==null)
         {
             info.add("hello");
         }
         else {

             info = new ArrayList<>(hashSet);
         }



        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,info);

        listView.setAdapter(arrayAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getApplicationContext(),MakeNoteActitvity.class);

                    intent.putExtra("note",i);

                    startActivity(intent);

                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                      final int itemToDelete = i;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingNoteActivity.this);
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Notes")
                            .setMessage("Do you want to delete this item")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    info.remove(itemToDelete);
                                    arrayAdapter.notifyDataSetChanged();

                                    SharedPreferences sharedPreferences1 = getSharedPreferences(" com.gkt.browse.newdiary",Context.MODE_PRIVATE);
                                    HashSet<String> hashSet1 = new HashSet<>(info);
                                    sharedPreferences.edit().putStringSet("notes",hashSet1).apply();
                                }
                            })
                            .setNegativeButton("No",null );

                    builder.show();
                    return  true;
                }
            });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_scrolling_note,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.makeNotes)
        {

            Toast.makeText(getApplicationContext(),"openinig...",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this,MakeNoteActitvity.class);

            startActivity(intent);
            return true;
        }

        return false;
    }
}
