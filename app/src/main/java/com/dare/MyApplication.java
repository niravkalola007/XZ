package com.dare;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by nirav kalola on 2/1/2015.
 */
public class MyApplication extends Application{
    static final String TAG = "MyApp";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "SCNdLrP6aE04W0hq2bibq6txLYqvqGuS2ZrZIHeq", "p4It0D5qbioPvK5X83snfpvOiFOAlkPmA3UE319V");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
