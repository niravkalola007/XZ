package com.dare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.internal.Utility;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

public class UploadVideoActivity extends ActionBarActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_VIDEO_FROM_GALLERY = 2;
    private Toolbar toolbar;
    private LinearLayout fbButtonLayoutUpload;
    private ImageView imgVideothumbnail;
    private TextView txtFacebookUpload;
    private EditText videoDescription;
    private LinearLayout videothumbnailLayout;
    private ProgressDialog progressDialog;
    private Uri selectedImageUri;
    byte[] inputData;
    String selectedVideoPath;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    AsyncFacebookRunner mAsyncRunner;
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

//                postVideo();

//                Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.login);
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
////                final Session sessionfb = Session.getActiveSession();
////                List<String> permissions = sessionfb.getPermissions();
////                if (!permissions.contains("publish_actions")) {
////
////                    Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
////                            UploadVideoActivity.this, Arrays.asList("publish_actions"))
////                            ;
////                    sessionfb.requestNewPublishPermissions(newPermissionsRequest);
////
////                }
//
//
//                Bundle parameters = new Bundle();
//
//                parameters.putString("message", "test message");
//                parameters.putByteArray("source", byteArray);
//
//
//                new Request(ParseFacebookUtils.getSession(), "me/photos", parameters, HttpMethod.POST,
//                        new Request.Callback() {
//                            public void onCompleted(Response response) {
////                                progressDialog.dismiss();
//                                Log.e("facebook post response",
//                                        response.toString());
//                            }
//                        }).executeAsync();

//                try {
//                    InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
//                    inputData = getBytes(iStream);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                ParseFacebookUtils.getSession().requestNewPublishPermissions(new Session.NewPermissionsRequest(UploadVideoActivity.this,
//                     Arrays.asList(ParseFacebookUtils.Permissions.Extended.PUBLISH_ACTIONS)));
//
//                progressDialog=new ProgressDialog(UploadVideoActivity.this);
//                progressDialog.setMessage("Loading");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                Bundle params = new Bundle();
//                params.putByteArray("source", inputData);
//                new Request(
//                        ParseFacebookUtils.getSession(),
//                        "1559395644274660/videos",
//                        params,
//                        HttpMethod.POST,
//                        new Request.Callback() {
//                            public void onCompleted(Response response) {
//                                Log.e("response",response+"");
//                                progressDialog.dismiss();
//                            }
//                        }
//                ).executeAsync();
//                uploadVideoSample();

                progressDialog=new ProgressDialog(UploadVideoActivity.this);
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test);
                try {
                    InputStream iStream = getContentResolver().openInputStream(uri);
                    byte[] inputData = getBytes(iStream);

                Bundle parameters = new Bundle();
                    parameters.putString("title", "title");
                    parameters.putString("description", "description");
                parameters.putByteArray("source", inputData);
                new Request(ParseFacebookUtils.getSession(), "me/videos", parameters, HttpMethod.POST,
                        new Request.Callback() {
                            public void onCompleted(Response response) {
                                progressDialog.dismiss();
                                Log.e("facebook post response",
                                        response.toString());
                                Toast.makeText(UploadVideoActivity.this,response+"", Toast.LENGTH_SHORT).show();
                            }
                        }).executeAsync();
                } catch (Exception e){
                    e.printStackTrace();
                }

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


    public void postVideo() {
        Toast.makeText(UploadVideoActivity.this, "click", Toast.LENGTH_SHORT).show();
        File file=new File(Environment.getExternalStorageDirectory()+"/Download/testing.mp4");
        try {

            Request videoRequest = Request.newUploadVideoRequest(ParseFacebookUtils.getSession(), file, new Request.Callback() {


                @Override
                public void onCompleted(Response response) {
                    // TODO Auto-generated method stub

                    if(response.getError()==null)
                    {
                        Log.e("response",response+"");
                        Toast.makeText(UploadVideoActivity.this, "Video Shared Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.e("response",response+"");
                        Toast.makeText(UploadVideoActivity.this, response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            Bundle params=new Bundle();
//            params.putString("message","message");
//            params.putString("title","title");
//            params.putString("description","description");
//            videoRequest.setParameters(params);
            videoRequest.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }






//    private void requestPublishPermissions(Session session) {
//        List<String> PERMISSIONS = Arrays.asList("publish_actions", "publish_stream");
//        if (session != null) {
//            pendingAnnounce = true;
//            Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
//
//            newPermissionsRequest.setRequestCode(REAUTH_ACTIVITY_CODE);
//            Session mSession = Session.openActiveSessionFromCache(this);
//            mSession.addCallback(callback);
//            mSession.requestNewPublishPermissions(newPermissionsRequest);
//        }
//    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_VIDEO_CAPTURE:
                selectedImageUri = data.getData();

                 selectedVideoPath = getPath(selectedImageUri);
                System.out.println("Video Path : " + selectedVideoPath);
                Bitmap bmThumbnail;

                // MICRO_KIND: 96 x 96 thumbnail
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                        selectedVideoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                imgVideothumbnail.setImageBitmap(bmThumbnail);
                break;

            case REQUEST_VIDEO_FROM_GALLERY:
                selectedImageUri = data.getData();

                selectedVideoPath = getPath(selectedImageUri);
                System.out.println("Video Path : " + selectedVideoPath);
                Bitmap bmThumbnailNew;

                // MICRO_KIND: 96 x 96 thumbnail
                bmThumbnailNew = ThumbnailUtils.createVideoThumbnail(
                        selectedVideoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                imgVideothumbnail.setImageBitmap(bmThumbnailNew);
                break;
        }

    }

    public String getPath(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}
