package com.example.doctorappui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public
class ConnectionReceiver extends BroadcastReceiver {

    public static ReceiverListener Listener;

    //public static ReceiverListener listener;

    @Override
    public
    void onReceive(Context context, Intent intent) {

        // initialize connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (Listener != null){

            // when connectivity receiver
            // listener  not null
            // get connection status
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

            //call listener method(abstract method)
            Listener.onNetworkChanged(isConnected);
        }

    }

    public interface ReceiverListener{
        void onNetworkChanged(boolean isConnected);
    }

}
