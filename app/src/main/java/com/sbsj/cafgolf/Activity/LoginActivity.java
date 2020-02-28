package com.sbsj.cafgolf.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sbsj.cafgolf.Database.MemberInfoField;
import com.sbsj.cafgolf.Login.GoogleLogin;
import com.sbsj.cafgolf.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private final String URL = "http://dongkyun5654.cafe24.com/CafeGolf/insert_memberinfo.php";
    private final int RC_GOOGLE_LOGIN = 10;
    private final int RC_KAKAO_LOGIN = 11;
    private final int RC_NAVER_LOGIN = 12;

    private final String TAG = "Login_Activity";

    private Activity activity;

    GoogleLogin googleLogin;

    SharedPreferences sharedPreferences;
    private final String sns_id_code = "sns_id";
    private final String directory_code = "directory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;

        SignInButton btnGoogle = findViewById(R.id.Google_Login);
        googleLogin = new GoogleLogin(this, btnGoogle, RC_GOOGLE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //구글 로그인 시도
        if (requestCode == RC_GOOGLE_LOGIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleLogin.FirebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.i(TAG, "구글 로그인 시도 실패 APIException : " + e.toString());
            }
        } else {
            Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료")
                .setMessage("정말로 종료하시겠습니까?")
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.finishAffinity(activity);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    public void get_memberinfo_sns(String sns_id, String directory) {
        final String s = sns_id;
        final String d = directory;

        Call<MemberInfoField> call = getRetrofitQuery().get_memberinfo_sns(directory, sns_id);
        call.enqueue(new Callback<MemberInfoField>() {
            @Override
            public void onResponse(Call<MemberInfoField> call, Response<MemberInfoField> response) {

                if(response.message().equals(getString(R.string.connect_network))) {
                    if(response.body().getId() < 0) {
                        insert_memberinfo(s, d, BaseActivity.b_fb_token);
                    } else {
                        BaseActivity.memberinfo = response.body();
                        saveLogin(response.body().getId());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.i(TAG, "get_memberinfo_sns msg error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MemberInfoField> call, Throwable t) {
                Log.i(TAG, "get_memberinfo_sns msg fail : " + t.getMessage());
            }
        });
    }

    private void insert_memberinfo(final String sns_id, String directory, String fb_token) {
        final String s = sns_id;
        final String d = directory;

        Call<String> call = getRetrofitQuery().insert_memberinfo(directory, sns_id, fb_token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    Log.i(TAG, "insert_memberinfo success : "+ response.body());
                    get_memberinfo_sns(s, d);
                } else {
                    Log.i(TAG, "insert_memberinfo error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "insert_memberinfo fail : " + t.getMessage());
            }
        });
    }
}
