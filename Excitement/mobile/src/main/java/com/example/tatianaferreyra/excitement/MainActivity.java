package com.example.tatianaferreyra.excitement;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.content.Intent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.ByteArrayOutputStream;

import io.fabric.sdk.android.Fabric;



public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "ftsoW7N5sNUnoZGCiqoF1oit5";
    private static final String TWITTER_SECRET = "1O2m46RLzQATZL1KdgzzfSJ48ONp749D4t3bGymmR1xlOtIuJT";
    public static final int REQUEST_TWITTER_COMPOSER = 500;
    public static final int CAMERA_REQUEST = 10;

    private Uri fileUri;
    private TwitterLoginButton loginButton;
    private static final String TAG = "MainActivityMobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Fabric.with(this, new TweetComposer(), new Twitter(authConfig));
        setContentView(R.layout.activity_main);



        loginButton = (TwitterLoginButton) findViewById(R.id.twitterLog);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent viewIntent = new Intent(this, MainActivity.class);
                Intent viewIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //viewIntent.putExtra("send", "message");
                PendingIntent viewPendingIntent =
                        PendingIntent.getService(getApplicationContext(), 0, viewIntent, 0);

                Notification notification = new NotificationCompat.Builder(getApplication())
                        .setSmallIcon(R.drawable.pic1)
                        .setContentTitle("Excited?")
                        .setContentText("Share it!")
                                //.setLargeIcon(R.drawable.pic1)
                        .extend(
                                new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                        .addAction(R.drawable.camera, getString(R.string.picture), viewPendingIntent)
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());

                int notificationId = 1;
                notificationManager.notify(notificationId, notification);
            }
        });
    }

    public void createTweet(Uri image) {
         Intent x = new TweetComposer.Builder(this)
            .text("#cs160excitement").image(image).createIntent();
         startActivityForResult(x, REQUEST_TWITTER_COMPOSER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.pic1)
                .setContentTitle("Excited?")
                .setContentText("Record it!");
        //.setContentIntent(viewPendingIntent)
        //.addAction(R.drawable.pic1,
        //getString(R.string.picture), mapPendingIntent);
    }
    
    public void btnTakePhotoClick(View v) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(camera, CAMERA_REQUEST);
    }

    /* https://colinyeoh.wordpress.com/2012/05/18/android-getting-image-uri-from-bitmap/ */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Uri uri = Uri.parse(path);
        Log.d(TAG, uri.toString());
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        loginButton.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                // we are hearing back from the camera
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                //imgView.setImageBitmap(cameraImage);
                createTweet(getImageUri(getApplicationContext(), cameraImage));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
