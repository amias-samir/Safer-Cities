package np.com.naxa.safercities.utils;

import android.util.Log;

import com.google.gson.Gson;

import np.com.naxa.safercities.mycircle.registeruser.UserModel;

public class JsonGsonConverterUtils {
    private static final String TAG = "JsonGsonConverterUtils";

    public static String getJsonFromGson (Object object){
        String string = null;
        Gson gson = new Gson();
        string = gson.toJson(object);
        Log.d(TAG, "getJsonFromGson: "+string);
        return string;
    }


    public static UserModel getUserFromJson(String json){
        Gson gson = new Gson();
        UserModel userModel = gson.fromJson(json, UserModel.class);
        return userModel;
    }


}
