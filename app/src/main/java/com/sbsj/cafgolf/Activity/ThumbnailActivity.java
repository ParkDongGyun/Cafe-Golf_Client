package com.sbsj.cafgolf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sbsj.cafgolf.Database.MemberInfoField;
import com.sbsj.cafgolf.Database.MemberInfoList;
import com.sbsj.cafgolf.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThumbnailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thumbnail);

		BaseActivity.getFirebaseInstanceID();
		if(b_id >= 0) {
			Call<MemberInfoField> call = getRetrofitQuery().get_memberinfo_id(b_id);
			call.enqueue(new Callback<MemberInfoField>() {
				@Override
				public void onResponse(Call<MemberInfoField> call, Response<MemberInfoField> response) {
					if(response.message().equals("OK")) {
						BaseActivity.memberinfo = response.body();
						mainScene();
					} else {
						Log.i("Thumbbnail_Activity", response.message());
					}
				}

				@Override
				public void onFailure(Call<MemberInfoField> call, Throwable t) {
					Log.i("Thumbbnail_Activity", t.getMessage());
				}
			});
		} else {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					loginScene();
				}
			}, 2000);
		}

	}
	public void mainScene() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void loginScene() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
}
