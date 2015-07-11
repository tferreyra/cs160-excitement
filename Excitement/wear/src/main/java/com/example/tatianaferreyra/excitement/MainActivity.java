package com.example.tatianaferreyra.excitement;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity implements SensorEventListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "jerKLm8jReAMNVauzEwcWkw3I";
    private static final String TWITTER_SECRET = "z2p0OeQYgpLLuRxRRKAqMvydbSHEtBl59siqufhIL4BaxRYOin";
    private static final String TAG = "SensorTag";
    private TextView mTextView;
    SensorManager mSensorManager;
    Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener((SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: check");
        super.onResume();
        mSensorManager.registerListener((SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "onSensorChanged: check");
        float a;
        float b;
        float c;
        a = Math.abs(sensorEvent.values[0]);
        b = Math.abs(sensorEvent.values[1]);
        c = Math.abs(sensorEvent.values[2]);

        if ((a > 10) || (b > 10) || (c > 10)) {
            presentExcitement();
            Log.d(TAG, "onSensorChanged: if stmt");
        }
    }

    public void presentExcitement() {
        Log.d(TAG, "excitement: check");
        Intent viewIntent = new Intent(this, camera.class);
        //Intent viewIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        Notification notification = new NotificationCompat.Builder(getApplication())
                .setSmallIcon(R.drawable.pic1)
                .setContentTitle("Excited?")
                .setContentText("Share it!")
                .extend(
                        new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                .addAction(R.drawable.camera, getString(R.string.picture), viewPendingIntent)
                .setContentIntent(viewPendingIntent)
                .build();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());

        int notificationId = 1;
        notificationManager.notify(notificationId, notification);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
