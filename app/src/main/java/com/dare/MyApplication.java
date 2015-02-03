package com.dare;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;
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

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, LoginActivity.class);
    }
}
