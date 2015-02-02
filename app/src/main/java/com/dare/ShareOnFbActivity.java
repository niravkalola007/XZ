package com.dare;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dare.R;

public class ShareOnFbActivity extends ActionBarActivity implements View.OnClickListener {
    private Typeface bold,semibold,regular;
    private LinearLayout btnShareVideoOnFacebook;
    private TextView txtShareOnFbTitleQuestion,txtShareOnfbText,ShareOnfbSmallText,txtFacebookShare;
    private ImageView imgBackShareOnFb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_fb);
        initView();
        initFonts();
        setFonts();
    }

    private void initView() {
        txtShareOnFbTitleQuestion= (TextView) findViewById(R.id.txtShareOnFbTitleQuestion);
        txtShareOnfbText= (TextView) findViewById(R.id.txtShareOnfbText);
        ShareOnfbSmallText= (TextView) findViewById(R.id.ShareOnfbSmallText);
        txtFacebookShare= (TextView) findViewById(R.id.txtFacebookShare);
        imgBackShareOnFb= (ImageView) findViewById(R.id.imgBackShareOnFb);
        btnShareVideoOnFacebook= (LinearLayout) findViewById(R.id.btnShareVideoOnFacebook);

        btnShareVideoOnFacebook.setOnClickListener(this);
        imgBackShareOnFb.setOnClickListener(this);
    }

    private void initFonts() {
        bold = Typeface.createFromAsset(getAssets(), "bold.ttf");
        semibold = Typeface.createFromAsset(getAssets(), "semibold.ttf");
        regular = Typeface.createFromAsset(getAssets(), "regular.ttf");
    }

    private void setFonts() {
        txtShareOnFbTitleQuestion.setTypeface(bold);
        txtShareOnfbText.setTypeface(regular);
        ShareOnfbSmallText.setTypeface(regular);
        txtFacebookShare.setTypeface(regular);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnShareVideoOnFacebook:
                Intent intentUpload=new Intent(ShareOnFbActivity.this,UploadVideoActivity.class);
                startActivity(intentUpload);
                break;

            case R.id.imgBackShareOnFb:
                    finish();
                break;
        }
    }
}
