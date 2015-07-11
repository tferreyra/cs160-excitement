package com.example.tatianaferreyra.excitement;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by tatianaferreyra on 7/11/15.
 */

public class MyReceiverService extends WearableListenerService {

    private static final String RECEIVER_SERVICE_PATH = "/receiver-service";
    private static final String TAG = "MainActivityMobile";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i(TAG, "Got a message");

    }

}
