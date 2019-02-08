/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnKeyListener,View.OnClickListener {

  private EditText userName;
  private EditText passWord;
  private EditText email;
  private Button submit;
  private Switch aSwitch;
  private ConstraintLayout parent;
  private ImageView logo;

  private boolean signupLogin = false;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(ParseUser.getCurrentUser().getUsername()!=null) {
        automaticLogin();
    }

    setContentView(R.layout.activity_main);

    setTitleColor(Color.blue(255));

    setTitle(ParseUser.getCurrentUser().getUsername());

      userName = (EditText) findViewById(R.id.username);
      passWord = (EditText)findViewById(R.id.pass);
      email = (EditText) findViewById(R.id.email);
      submit = (Button) findViewById(R.id.submit);
      aSwitch = (Switch)findViewById(R.id.switch2);
      logo = (ImageView )findViewById(R.id.imageView3);
      parent = (ConstraintLayout)findViewById(R.id.parentLayout);

      submit.setOnClickListener(this);
      logo.setOnClickListener(this);
      parent.setOnClickListener(this);
      aSwitch.setOnCheckedChangeListener(this);



    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


public void  signingUP()
{
    ParseUser user = new ParseUser();
    user.setUsername(userName.getText().toString());
    user.setPassword(passWord.getText().toString());
    user.setEmail(email.getText().toString());

    user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
            if(e==null)
            {
                Toast.makeText(getApplicationContext(),"signedUP succesfully..",Toast.LENGTH_LONG).show();
                showUserList();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"unable to reach server..",Toast.LENGTH_LONG).show();
            }
        }
    });

}

public void  automaticLogin()
{
    ParseUser user = ParseUser.getCurrentUser();

    if(user!=null)
    {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if(user!=null && e==null)
                {
                    showUserList();
                    Log.i("Logged","logged Succesfully");
                }
                else
                {
                    Log.i("Logged","logged Unsuccesfull");
                }
            }
        });
    }
}

public void logIn()
{
    ParseUser.logInInBackground(userName.getText().toString(),passWord.getText().toString() , new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {

            if(user!=null && e==null)
            {
                Toast.makeText(getApplicationContext(),"loggedInSuccesfully",Toast.LENGTH_LONG).show();
                Log.i("Logged","logged Succesfully");
                showUserList();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"username or password is wrong ",Toast.LENGTH_LONG).show();
                Log.i("Logged","logged Unsuccesfull");
            }
        }
    });

}

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

      signupLogin=b;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

      if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction()== KeyEvent.ACTION_DOWN)
      {
          if(signupLogin)
              logIn();
          else
              signingUP();
      }

        return false;
    }

    @Override
    public void onClick(View view) {

      if(view.getId() == R.id.submit)
      {

          if(userName.getText().toString().matches("")|| passWord.getText().toString().matches("")||email.getText().toString().matches(""))
          {

              Toast.makeText(this,"All fields are Required..",Toast.LENGTH_LONG).show();
          }
          else
          {

              if(signupLogin)
              {

                  logIn();
              }
              else
              {
                  signingUP();
              }

          }

      }

      else if(view.getId()==R.id.imageView3 || view.getId() == R.id.parentLayout)
      {

          InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
          inputMethodManager.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),0);
          inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
      }
    }

    public void showUserList()
    {
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }
}
