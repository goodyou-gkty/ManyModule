package com.gkt.browse.newdiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class AccountInfo extends AppCompatActivity {

    private EditText names;
    private EditText passwords;
    private EditText phones;
    private EditText emails;
    private EditText editText;
    private EditText postals;
    private TextView textView;
    private Spinner spinners;

    private ToggleButton toggleButtons;

    private Button buttons;

    String name;
    String email;
    String phone;
    String pass;
    String postal;
    String sex;
    ArrayList<String> question;
    SQLiteDatabase sqLiteDatabase;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        names = (EditText) findViewById(R.id.editText2);
        emails =(EditText)findViewById(R.id.editText3);
        phones = (EditText)findViewById(R.id.editText4);
        passwords=(EditText)findViewById(R.id.editText5);
        postals = (EditText)findViewById(R.id.editText6);
        spinners =(Spinner)findViewById(R.id.spinner);
        toggleButtons =(ToggleButton)findViewById(R.id.toggleButton);
        buttons = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView3);
        question = new ArrayList<>();
        editText = (EditText)findViewById(R.id.editText7);
        editText.animate().alpha(0).setDuration(1000);
        question.add("your favourite leader");
        question.add("your favourite actor");
        question.add("your favourite actoress");
        question.add("your favourite scientist");
        question.add("your favourite school");
        question.add("your favourite college");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,question);

        spinners.setAdapter(arrayAdapter);

        sharedPreferences = getSharedPreferences("com.gkt.browse.newdiary", Context.MODE_PRIVATE);

       spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

              textView.setText(question.get(i));
              editText.animate().alpha(1).setDuration(1000);
              if(editText.getText()!=null)
              {
                  Toast.makeText(getApplicationContext(),"Answer saved",Toast.LENGTH_SHORT).show();
              }
              sharedPreferences.edit().putString("answer",editText.getText().toString()).apply();

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

              Toast.makeText(getApplicationContext(),"please select a choice",Toast.LENGTH_SHORT).show();
           }
       });


        try {
            sqLiteDatabase = this.openOrCreateDatabase("User", MODE_PRIVATE, null);

            String str = "CREATE TABLE user(name VARCHAR,email VARCHAR,phone VARCHAR,pass VARCHAR,postal VARCHAR ,sex VARCHAR)";

            sqLiteDatabase.execSQL(str);

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user",null);

            if(cursor!=null)
            {
                cursor.moveToFirst();

                int nameIndex = cursor.getColumnIndex("name");
                int emailIndex = cursor.getColumnIndex("email");
                int phoneIndex = cursor.getColumnIndex("phone");
                int passIndex = cursor.getColumnIndex("pass");
                int postalIndex = cursor.getColumnIndex("postal");
                int sexIndex = cursor.getColumnIndex("sex");

                name = cursor.getString(nameIndex);
                email = cursor.getString(emailIndex);
                phone = cursor.getString(phoneIndex);
                pass = cursor.getString(passIndex);
                postal = cursor.getString(postalIndex);
                sex = cursor.getString(sexIndex);

                names.setText(name);
                emails.setText(email);
                phones.setText(phone);
                passwords.setText(pass);
                postals.setText(postal);
                toggleButtons.setText(sex);


            }
        }
        catch (Exception ex)
        {


        }
        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Editable editable=names.getText();
                name = editable.toString();
                email = emails.getText().toString();
                phone = phones.getText().toString();
                pass = passwords.getText().toString();
                postal = postals.getText().toString();
                sex = toggleButtons.getText().toString();
                //Toast.makeText(getApplicationContext(),name+email,Toast.LENGTH_LONG).show();
                String str = "INSERT INTO user(name,email,phone,pass,postal,sex) VALUES(?,?,?,?,?,?)";

                SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(str);

                sqLiteStatement.bindString(1,name);
                sqLiteStatement.bindString(2,email);
                sqLiteStatement.bindString(3,phone);
                sqLiteStatement.bindString(4,pass);
                sqLiteStatement.bindString(5,postal);
                sqLiteStatement.bindString(6,sex);

               sqLiteStatement.execute();
             Toast.makeText(AccountInfo.this,"Saving..",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean ValidateName(String name)
    {
        if(name==null || name.matches("[0-9]"))
            return false;
        return true;


    }

}
