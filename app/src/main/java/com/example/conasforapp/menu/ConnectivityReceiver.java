package com.example.conasforapp.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class ConnectivityReceiver extends BroadcastReceiver {
    private ConnectivityReceiverListener listener;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ConnectivityReceiver(ConnectivityReceiverListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (listener != null) {
            listener.onNetworkConnectionChanged(isConnected);
            updateFirestoreConnection(isConnected);
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    private void updateFirestoreConnection(boolean isConnected) {
        if (isConnected) {
            firestore.enableNetwork().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Firestore", "Network enabled");
                }
            });
        } else {
            firestore.disableNetwork().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Firestore", "Network disabled");
                }
            });
        }
    }
}
