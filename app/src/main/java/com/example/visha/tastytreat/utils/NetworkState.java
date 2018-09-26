package com.example.visha.tastytreat.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkState {

    /**
     * A method to get current status of connectivity.
     * @param connectivityManager giving access to NetworkInfo instance.
     * @return A boolean returning status of network (true = connected, false = disconnected).
     */
    public static boolean isConnected(ConnectivityManager connectivityManager){

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

}