package com.dare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dare.custom_components.CircularImageView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FragmentHome extends Fragment implements View.OnClickListener{

    private TextView profileName,txtTitle,txtQuest,txtAccept,txtDecline;
    private Typeface bold,semibold,regular;
private CircularImageView circularImageView;
    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();

        return fragment;
    }

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_home, container, false);
        initView(convertView);
        initFonts();
        setFonts();
//        makeMeRequest();
        return convertView;
    }


    private void initView(View convertView) {
        circularImageView= (CircularImageView)convertView. findViewById(R.id.profileImage);
        profileName= (TextView)convertView. findViewById(R.id.profileName);
        txtTitle= (TextView) convertView.findViewById(R.id.txtTitle);
        txtQuest= (TextView)convertView. findViewById(R.id.txtQuest);
        txtAccept= (TextView)convertView. findViewById(R.id.txtAccept);
        txtDecline= (TextView)convertView. findViewById(R.id.txtDecline);
        txtAccept.setOnClickListener(this);
        txtDecline.setOnClickListener(this);
    }

    private void initFonts() {
        bold = Typeface.createFromAsset(getActivity().getAssets(), "bold.ttf");
        semibold = Typeface.createFromAsset(getActivity().getAssets(), "semibold.ttf");
        regular = Typeface.createFromAsset(getActivity().getAssets(), "regular.ttf");
    }

    private void setFonts() {
        txtTitle.setTypeface(bold);
        profileName.setTypeface(semibold);
        txtQuest.setTypeface(regular);
        txtAccept.setTypeface(regular);
        txtDecline.setTypeface(regular);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtAccept:

                Intent intentUpload=new Intent(getActivity(),UploadVideoActivity.class);
                startActivity(intentUpload);
                break;

            case R.id.txtDecline:
                Intent intent=new Intent(getActivity(),DeclineActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(final GraphUser user, Response response) {
                        if (user != null)
                        {
                            AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>(){
                                protected Bitmap doInBackground(Void... p) {
                                    Bitmap bm = null;
                                    try {
                                        URL aURL = new URL("http://graph.facebook.com/"+user.getId()+"/picture?type=large");
                                        URLConnection conn = aURL.openConnection();
                                        conn.setUseCaches(true);
                                        conn.connect();
                                        InputStream is = conn.getInputStream();
                                        BufferedInputStream bis = new BufferedInputStream(is);
                                        bm = BitmapFactory.decodeStream(bis);
                                        bis.close();
                                        is.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return bm;
                                }

                                protected void onPostExecute(Bitmap bm){



                                    circularImageView.setImageBitmap(bm);

                                }
                            };
                            t.execute();


                        } else if (response.getError() != null) {
                            // handle error
                        }
                    }
                });
        request.executeAsync();

    }
}
