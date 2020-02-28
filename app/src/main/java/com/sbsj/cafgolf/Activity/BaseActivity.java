package com.sbsj.cafgolf.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sbsj.cafgolf.Database.MasterInfoField;
import com.sbsj.cafgolf.Database.MemberInfoField;
import com.sbsj.cafgolf.Database.RetrofitConnect;
import com.sbsj.cafgolf.Database.RetrofitQuery;
import com.sbsj.cafgolf.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    //임시데이터
    private final String preference_member = "memberinfo";
    protected int b_id = -1;
    static public  String b_fb_token;

    private RetrofitQuery retrofitQuery;

    static public MemberInfoField memberinfo;
    static public ArrayList<MasterInfoField> masterlist;

    public RetrofitQuery getRetrofitQuery() {
        return retrofitQuery;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofitQuery = RetrofitConnect.getClient().create(RetrofitQuery.class);
        loadShared();
    }

    public void saveLogin(int id) {
        SharedPreferences sharedPreferences = getSharedPreferences(preference_member, MODE_PRIVATE);
        SharedPreferences.Editor edior = sharedPreferences.edit();
        edior.putInt(getString(R.string.id), id);
        edior.apply();
    }

    /*쉐어드값 불러오기*/
    public void loadShared() {
        SharedPreferences pref = getSharedPreferences(preference_member, MODE_PRIVATE);
        b_id = pref.getInt(getString(R.string.id), -1);
    }

    public static void getFirebaseInstanceID() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("BaseActivity", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        Log.i("BaseActivity", "token : " + task.getResult().getToken());
                        BaseActivity.b_fb_token = task.getResult().getToken();

                    }
                });
    }
}
