package com.dare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dare.custom_components.CircularImageView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FragmentHome extends Fragment implements View.OnClickListener{

    private TextView profileName,txtTitle,txtQuest,txtAccept,txtDecline;
    private Typeface bold,semibold,regular;
    private CircularImageView circularImageView;
    private ParseUser parseUser;
    int randomInt;
    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();

        return fragment;
    }

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_home, container, false);
        initView(convertView);
        initFonts();
        setFonts();
        makeMeRequest();

        ParseQuery<ParseObject> queryDate = ParseQuery.getQuery("currentdate");
        queryDate.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Random random = new Random();
                    int randomIntValue = random.nextInt(50);
                    scoreList.get(0).put("date","nirav"+randomIntValue);
                    scoreList.get(0).saveInBackground();
                    Date serverDate=scoreList.get(0).getUpdatedAt();
                    String serverDateString =inputFormat.format(serverDate);
                    Date currentDate=new Date();
                    String currentDateString =inputFormat.format(currentDate);
                    Log.e("serverDate",serverDateString+"");
                    Log.e("currentDate",currentDateString+"");
                    if(serverDateString.equals(currentDateString)){

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quests");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> scoreList, ParseException e) {
                                if (e == null) {
                                    getRandomNumber(scoreList);
                                } else {

                                }
                            }
                        });
                    } else {
                        txtQuest.setText("wrong date");
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        return convertView;
    }


    private void getRandomNumber(List<ParseObject> scoreList){
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Random", Context.MODE_PRIVATE);
        int previousRandom=sharedpreferences.getInt("previous_random",0);

        randomInt=generateRandom(scoreList);

        if(previousRandom==randomInt){
            getRandomNumber(scoreList);
        } else{
            SharedPreferences.Editor editor=sharedpreferences.edit();
            editor.putInt("previous_random",randomInt);
            editor.commit();
            txtQuest.setText(scoreList.get(randomInt).get("Quest").toString());
        }
    }

    private int generateRandom(List<ParseObject> scoreList) {

        Random random = new Random();
        int randomInt = random.nextInt(scoreList.size());
        return randomInt;
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

    @Override
    public void onResume() {
        super.onResume();
        parseUser = ParseUser.getCurrentUser();

    }

    private void makeMeRequest() {
        ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(final GraphUser user, Response response) {
                        if (user != null)
                        {
                            ParseUser.getCurrentUser().put("fbName", user.getName());
                            ParseUser.getCurrentUser().saveInBackground();
                            profileName.setText("HELLO "+user.getName().toUpperCase());
                            AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>(){
                                protected Bitmap doInBackground(Void... p) {
                                    Bitmap bm = null;
                                    try {
                                        Log.e("facebook profile picture","http://graph.facebook.com/"+user.getId()+"/picture?type=large");
                                        URL aURL = new URL("https://graph.facebook.com/"+user.getId()+"/picture?type=large");
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
