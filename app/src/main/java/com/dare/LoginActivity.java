package com.dare;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView appTitle,appSubTitle,appDescription,txtPlayStore,txtFacebook;
    private Typeface bold,semibold,regular;
    private LinearLayout fbButtonLayout,storeButtonLayout;
    private static final int LOGIN_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initFonts();
        setFonts();


    }

    private void initView() {
        appTitle= (TextView) findViewById(R.id.appTitle);
        appSubTitle= (TextView) findViewById(R.id.appSubTitle);
        appDescription= (TextView) findViewById(R.id.appDescription);
        txtPlayStore= (TextView) findViewById(R.id.txtPlayStore);
        txtFacebook= (TextView) findViewById(R.id.txtFacebook);
        fbButtonLayout= (LinearLayout) findViewById(R.id.fbButtonLayout);
        storeButtonLayout= (LinearLayout) findViewById(R.id.storeButtonLayout);
        fbButtonLayout.setOnClickListener(this);
        storeButtonLayout.setOnClickListener(this);
    }

    private void initFonts() {
         bold = Typeface.createFromAsset(getAssets(), "bold.ttf");
         semibold = Typeface.createFromAsset(getAssets(), "semibold.ttf");
         regular = Typeface.createFromAsset(getAssets(), "regular.ttf");
    }

    private void setFonts() {
        appTitle.setTypeface(bold);
        appSubTitle.setTypeface(semibold);
        appDescription.setTypeface(regular);
        txtPlayStore.setTypeface(regular);
        txtFacebook.setTypeface(regular);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.storeButtonLayout:
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Neoff+Studio")));
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, " unable to find store ", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.fbButtonLayout:


                ParseFacebookUtils.logIn(this, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {


                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            finish();

                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                        } else {

                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("MyApp", "User logged in through Facebook!");
                        }
                    }
                });


                break;
        }
    }


//    https://play.google.com/store/apps/developer?id=Google+Inc.
}
