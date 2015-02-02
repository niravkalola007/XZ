package com.dare;

import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dare.R;

public class DeclineActivity extends ActionBarActivity {

    private TextView txtDeclineMessage;
    private ImageView imgBackDecline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline);
        txtDeclineMessage= (TextView) findViewById(R.id.txtDeclineMessage);
        txtDeclineMessage.setTypeface(Typeface.createFromAsset(getAssets(), "bold.ttf"));
        imgBackDecline= (ImageView) findViewById(R.id.imgBackDecline);
        imgBackDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
