package com.dare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dare.R;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class UploadVideoActivity extends ActionBarActivity {
    private static final List<String> PERMISSIONS = Arrays.asList("manage_pages");
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_VIDEO_FROM_GALLERY = 2;
    private Toolbar toolbar;
    private LinearLayout fbButtonLayoutUpload;
    private ImageView imgVideothumbnail;
    private TextView txtFacebookUpload;
    private EditText videoDescription;
    private LinearLayout videothumbnailLayout;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        videoDescription=(EditText) findViewById(R.id.videoDescription);
        videoDescription.setTypeface(Typeface.createFromAsset(getAssets(), "regular.ttf"));
        imgVideothumbnail=(ImageView) findViewById(R.id.imgVideothumbnail);


        fbButtonLayoutUpload= (LinearLayout) findViewById(R.id.fbButtonLayoutUpload);
        fbButtonLayoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadVideo();
//                postVideo();
                ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));
                Session session = ParseFacebookUtils.getSession();
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(UploadVideoActivity.this, Arrays.asList("publish_actions", "publish_stream"));
                session.requestNewPublishPermissions(newPermissionsRequest);

                Bundle params = new Bundle();
                params.putString("url", "https://camo.githubusercontent.com/2e5c9d7b6239afb43ade3e52e5156e33bf63dbc5/687474703a2f2f6934362e74696e797069632e636f6d2f32316b797769742e706e67");
/* make the API call */
                new Request(
                        session,
                        "/145634995501895/photos",
                        params,
                        HttpMethod.POST,
                        new Request.Callback() {
                            public void onCompleted(Response response) {
            /* handle the result */
                                Log.e("response",response+"");
                            }
                        }
                ).executeAsync();
//                File file=new File(Environment.getExternalStorageDirectory()+"/DCIM/100MEDIA/VIDEO0001.mp4");
            }
        });
        txtFacebookUpload=(TextView) findViewById(R.id.txtFacebookUpload);
        txtFacebookUpload.setTypeface(Typeface.createFromAsset(getAssets(), "regular.ttf"));
        videothumbnailLayout=(LinearLayout) findViewById(R.id.videothumbnailLayout);
        registerForContextMenu(videothumbnailLayout);
        videothumbnailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContextMenu(videothumbnailLayout);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_close);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0,1,Menu.NONE,"Video");
        menu.add(0,2,Menu.NONE,"Gallery");

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 1:
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
                break;
            case 2:
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                // intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Complete action using"), 2);
                break;
            case 3:

                break;
        }
        return super.onContextItemSelected(item);
    }

    public void postVideo() {
        progressDialog=new ProgressDialog(UploadVideoActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));
        File file=new File(Environment.getExternalStorageDirectory()+"/Download/testing.mp4");
//        File file=new File(Environment.getExternalStorageDirectory()+"/downloads/testing.mp4");
        Session session = ParseFacebookUtils.getSession();

        try {
            Request videoRequest = Request.newUploadVideoRequest(session, file, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    // TODO Auto-generated method stub

                    if(response.getError()==null)
                    {
                        progressDialog.dismiss();
                        Log.e("response",response.getError().getErrorMessage()+"");
                        Toast.makeText(UploadVideoActivity.this, "Video Shared Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        progressDialog.dismiss();
                        Log.e("response",response.getError().getErrorMessage()+"");
                        Toast.makeText(UploadVideoActivity.this, response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            videoRequest.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_VIDEO_CAPTURE:
                Uri selectedImageUri = data.getData();

                String selectedVideoPath = getPath(selectedImageUri);
                System.out.println("Video Path : " + selectedVideoPath);
                Bitmap bmThumbnail;

                // MICRO_KIND: 96 x 96 thumbnail
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                        selectedVideoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                imgVideothumbnail.setImageBitmap(bmThumbnail);
                break;

            case REQUEST_VIDEO_FROM_GALLERY:
                Uri selectedImageUriGallery = data.getData();

                String selectedVideoPathNew = getPath(selectedImageUriGallery);
                System.out.println("Video Path : " + selectedVideoPathNew);
                Bitmap bmThumbnailNew;

                // MICRO_KIND: 96 x 96 thumbnail
                bmThumbnailNew = ThumbnailUtils.createVideoThumbnail(
                        selectedVideoPathNew, MediaStore.Video.Thumbnails.MICRO_KIND);
                imgVideothumbnail.setImageBitmap(bmThumbnailNew);
                break;
        }

    }

    public String getPath(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}
