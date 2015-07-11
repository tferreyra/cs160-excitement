package com.example.tatianaferreyra.excitement;

        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.util.Log;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.wearable.CapabilityApi;
        import com.google.android.gms.wearable.Node;
        import com.google.android.gms.wearable.Wearable;

        import java.util.Set;

/**
 * Created by tatianaferreyra on 7/10/15. (From section slides)
 */
public class camera extends Service {

    private static final int START_STICKY = 50;
    public GoogleApiClient mGoogleApiClient;
    public CapabilityApi.GetCapabilityResult result;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                // Do something
                final AsyncTask execute = new MyTask().execute();
            }

            @Override
            public void onConnectionSuspended(int i) {
                // Do something
            }
        })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        // Do something
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

        return START_STICKY;

    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            result = Wearable.CapabilityApi.getCapability(mGoogleApiClient, "do_stuff", CapabilityApi.FILTER_REACHABLE).await();

            Wearable.MessageApi.sendMessage(mGoogleApiClient, pickBestNodeId(result.getCapability().getNodes()), "do_stuff", new byte[3]);
            return null;
        }
    }

}
