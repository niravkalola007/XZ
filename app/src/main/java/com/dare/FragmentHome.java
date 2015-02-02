package com.dare;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentHome extends Fragment implements View.OnClickListener{

    private TextView profileName,txtTitle,txtQuest,txtAccept,txtDecline;
    private Typeface bold,semibold,regular;

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
        return convertView;
    }


    private void initView(View convertView) {
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
}
