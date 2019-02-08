package com.gkt.browse.newdiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

public class MakeNoteActitvity extends AppCompatActivity {

    private EditText editText;
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note_actitvity);


        editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        noteId = intent.getIntExtra("note",-1);

        if(noteId!=-1)
        {

            editText.setText(ScrollingNoteActivity.info.get(noteId));


        }
        else
        {

            ScrollingNoteActivity.info.add(" ");
            noteId = ScrollingNoteActivity.info.size()-1;
            ScrollingNoteActivity.arrayAdapter.notifyDataSetChanged();
        }



        editText.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged (CharSequence charSequence,int i, int i1, int i2){

                }

                    @Override
                    public void onTextChanged (CharSequence charSequence,int i, int i1, int i2){

                        ScrollingNoteActivity.info.set(noteId,String.valueOf(charSequence));

                        ScrollingNoteActivity.arrayAdapter.notifyDataSetChanged();

                        SharedPreferences sharedPreferences = getSharedPreferences("com.gkt.browse.newdiary", Context.MODE_PRIVATE);

                        HashSet<String> hashSet = new HashSet<>(ScrollingNoteActivity.info);

                        sharedPreferences.edit().putStringSet("notes",hashSet).apply();
                }

                    @Override
                    public void afterTextChanged (Editable editable){

                }

            });

    }




}
