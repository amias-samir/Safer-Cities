package np.com.naxa.safercities.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import np.com.naxa.safercities.home.ISET;

public class NetworkUtils {
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ISET.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
