package com.dare;

import android.app.ProgressDialog;
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
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener{
    private ProgressDialog dialog;
    private TextView appTitle,appSubTitle,appDescription,txtPlayStore,txtFacebook;
    private Typeface bold,semibold,regular;
    private LinearLayout fbButtonLayout,storeButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initFonts();
        setFonts();

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to Welcome.class
                Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                startActivity(intent);
                finish();
            } else {
            }
        }
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
                dialog=new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Loading");
                dialog.setCancelable(false);
                dialog.show();
                ParseFacebookUtils.logIn(this, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            dialog.dismiss();
                        } else if (user.isNew()) {
                            dialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
