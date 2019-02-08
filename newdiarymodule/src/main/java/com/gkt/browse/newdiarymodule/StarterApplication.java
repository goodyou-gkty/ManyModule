/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.gkt.browse.newdiarymodule;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("1a606d25edbd6b253b9d72394cfac280f85a566d")
            .clientKey("084af9d295a0b47ed6aaf42277be25b2c1255b29")
            .server("http://18.191.52.159:80/parse/")
            .build()
    );


   // ParseObject object = new ParseObject("ExampleObject");
    //object.put("myNumber", "123");
    //object.put("myString", "rob");

    ParseObject parseObject = new ParseObject("MyInfo");
    parseObject.put("name","gautam");
    parseObject.put("title","devraj");
    parseObject.put("lastname","yadav");

    parseObject.saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if(e==null)
                Log.i("Svaed:","saved succesfully");
            else
                Log.i("saveunsucces","unable to save");
        }
    });

   /* object.saveInBackground(new SaveCallback () {
      @Override
      public void done(ParseException ex) {
        if (ex == null) {
          Log.i("Parse Result", "Successful!");
        } else {
          Log.i("Parse Result", "Failed" + ex.toString());
        }
      }
    });*/

    ParseUser user = new ParseUser();

   // user.setEmail("goodyou.gkt@gmail.com");
    user.setUsername("gautamkumar");
    user.setPassword("Sub7@mca");
    user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
            if(e == null)
            Log.i("signup:","signup..done");

            else
                Log.i("signupfail","signup failed");
        }
    });


    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
