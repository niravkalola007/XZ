package com.dare;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dare.R;
import com.parse.ParseUser;

public class DrawerActivity extends ActionBarActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private DrawerAdapter drawerAdapter;
    private Toolbar toolbar;
    private ParseUser currentUser;
    private String[] leftSliderData = new String[]{"Our Store", "Share video on Facebook", "Rate Us","Logout"};
    private int[] drawerImages = new int[]{R.drawable.ic_store,R.drawable.ic_facebook_drawer,R.drawable.ic_ratting,R.drawable.ic_logout};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        initView();
        initDrawer();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        FragmentHome fragmentHome = FragmentHome.newInstance();
        if (manager.findFragmentByTag("FragmentHome") == null) {
            ft.replace(R.id.main_container, fragmentHome, "FragmentHome").commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve current user from Parse.com
        currentUser = ParseUser.getCurrentUser();
    }

    private void initView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawerAdapter=new DrawerAdapter(DrawerActivity.this,leftSliderData);
        leftDrawerList.setAdapter(drawerAdapter);
    }
    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public class DrawerAdapter extends BaseAdapter {

        Context context;
        String[] drawerTitleList;

        public DrawerAdapter(Context context, String[] drawerTitleList) {
            this.context = context;
            this.drawerTitleList = drawerTitleList;

        }

        public int getCount() {

            return drawerTitleList.length;
        }

        public Object getItem(int position) {
            return drawerTitleList[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            ImageView drawerIcon;
            TextView drawerText;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_drawer, parent,false);
                holder = new ViewHolder();
                holder.drawerIcon=(ImageView)convertView.findViewById(R.id.drawerIcon);
                holder.drawerText=(TextView)convertView.findViewById(R.id.drawerText);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.drawerIcon.setImageResource(drawerImages[position]);
            holder.drawerText.setText(drawerTitleList[position]);
            holder.drawerText.setTypeface(Typeface.createFromAsset(getAssets(), "regular.ttf"));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    drawerLayout.closeDrawers();

                    switch (position){



                        case 0:
                            try{
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Neoff+Studio")));
                            } catch (Exception e) {
                                Toast.makeText(DrawerActivity.this, " unable to find store ", Toast.LENGTH_LONG).show();
                            }
                            break;

                        case 1:
                            Intent intent=new Intent(DrawerActivity.this,ShareOnFbActivity.class);
                            startActivity(intent);

                            break;

                        case 2:
                            launchMarket();
                            break;
                        case 3:
                            currentUser.logOut();
                            Intent intentLogin=new Intent(DrawerActivity.this,LoginActivity.class);
                            startActivity(intentLogin);
                            finish();
                            break;
                    }
                }
            });
            return convertView;
        }
    }

    private void launchMarket() {

        try {
//            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Uri uri = Uri.parse("market://details?id=" +"com.google.android.gm");
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(myAppLinkToMarket);
        } catch (Exception e) {
            Toast.makeText(this, " unable to find market app ", Toast.LENGTH_LONG).show();
        }
    }
}
