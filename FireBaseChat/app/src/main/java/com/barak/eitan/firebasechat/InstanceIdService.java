package com.barak.eitan.firebasechat;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstanceIdService extends FirebaseInstanceIdService{

    SharedPreferences sp;
    public InstanceIdService(){

    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sp = getSharedPreferences("Firebase",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Token", FirebaseInstanceId.getInstance().getToken());
        editor.apply();
    }
}
