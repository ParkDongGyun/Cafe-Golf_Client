package com.sbsj.cafgolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sbsj.cafgolf.Adapter.GalleryAdapter;
import com.sbsj.cafgolf.Adapter.MainAdapter;
import com.sbsj.cafgolf.Database.BookingInfoField;
import com.sbsj.cafgolf.Database.BookingInfoList;
import com.sbsj.cafgolf.Database.MasterInfoList;
import com.sbsj.cafgolf.Database.MemberInfoField;
import com.sbsj.cafgolf.OnItemClickListener;
import com.sbsj.cafgolf.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements Button.OnClickListener {

    private final String TAG = "Main_Activity";
    final int CALL_PERMISSION_CODE = 100;
    private Activity activity = this;

    private Toolbar toolbar;

    private TextView tv_name;
    private TextView tv_lessoncount;

    private ImageView iv_calendar;
    private Button btn_book;
    private LinearLayout ll_address;
    private Button btn_address;

    private RecyclerView recyclerView;
    private MainAdapter adapter;

    private RelativeLayout relativeLayout;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        get_masterinfo();

        tv_lessoncount = findViewById(R.id.tv_lessoncount);

        iv_calendar = findViewById(R.id.iv_calendar);
        iv_calendar.setOnClickListener(this);

        btn_book = findViewById(R.id.btn_book);
        btn_book.setOnClickListener(this);

        ll_address = findViewById(R.id.ll_address);
        ll_address.setOnClickListener(this);
        btn_address = findViewById(R.id.btn_address);
        btn_address.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<BookingInfoField> list = new ArrayList<>();
        adapter = new MainAdapter(this, list);
        recyclerView.setAdapter(adapter);

        relativeLayout = findViewById(R.id.rl_nobooking);

        ArrayList<Drawable> drawablelist = new ArrayList<>();
        drawablelist.add(getResources().getDrawable(R.drawable.image_1));
        drawablelist.add(getResources().getDrawable(R.drawable.image_2));
        drawablelist.add(getResources().getDrawable(R.drawable.image_3));
        drawablelist.add(getResources().getDrawable(R.drawable.image_4));
        drawablelist.add(getResources().getDrawable(R.drawable.image_5));
        drawablelist.add(getResources().getDrawable(R.drawable.image_6));
        drawablelist.add(getResources().getDrawable(R.drawable.image_7));

        GalleryAdapter galleryAdapter = new GalleryAdapter(this, getLayoutInflater(), drawablelist);
        viewPager = findViewById(R.id.vp_gallery);
        viewPager.setAdapter(galleryAdapter);

        if (BaseActivity.memberinfo.getFb_token() == null || !BaseActivity.memberinfo.getFb_token().equals(BaseActivity.b_fb_token)) {
            update_memberinfo_fb_token(BaseActivity.memberinfo.getId(), BaseActivity.b_fb_token);
        }

        tv_name = findViewById(R.id.tv_membername);

        if(BaseActivity.memberinfo.getName() == null || BaseActivity.memberinfo.getName().isEmpty()) {
            setmemberName();
        } else {
            tv_name.setText(String.format(getString(R.string.membername), memberinfo.getName()));
        }

        Log.i(TAG, "token : " + b_fb_token);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getMemberInfo();
        getdate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem itemCall = menu.findItem(R.id.main_call);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_call:
                phoneCall();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_book:
                openActivity();
                break;
            case R.id.ll_address:
                copyAddress();
                break;
            case R.id.btn_address:
                copyAddress();
                break;
            case R.id.iv_calendar:
                openActivity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void copyAddress() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Address", getString(R.string.address));
        clipboardManager.setPrimaryClip(clipData);
    }

    private void openActivity() {
        Intent intent = new Intent(this, BookActivity.class);
        startActivity(intent);
    }

    private void phoneCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                Toast.makeText(this, "전화 권한 필요", Toast.LENGTH_LONG);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-6780-5875"));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                switch (requestCode) {
                    case CALL_PERMISSION_CODE:
                        Toast.makeText(this, "전화 관련 권한을 확인해주세요.", Toast.LENGTH_LONG);
                        break;
                }
            }
        }
    }

    private void getdate() {
        Call<BookingInfoList> call = getRetrofitQuery().get_bookinginfo_my(b_id);
        call.enqueue(new Callback<BookingInfoList>() {
            @Override
            public void onResponse(Call<BookingInfoList> call, Response<BookingInfoList> response) {
                if (response.message().equals("OK")) {
                    ArrayList<BookingInfoField> list = response.body().getlist();
                    updateAdapter(list);
                } else {
                    Log.i(TAG, "Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BookingInfoList> call, Throwable t) {
                Log.i(TAG, "Fail : " + t.getMessage());
            }
        });
    }

    private void updateAdapter(ArrayList<BookingInfoField> list) {
        if (list.get(0).getId() != -1) {
            adapter.setDateList(list);
            relativeLayout.setVisibility(View.INVISIBLE);
        } else {
            adapter.getDateList().clear();
            relativeLayout.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void getMemberInfo() {
        MemberInfoField memberinfo = BaseActivity.memberinfo;
        tv_lessoncount.setText(String.format(getString(R.string.lessoncount), memberinfo.getLesson_count()));
    }

    private void setmemberName() {
        final View dialogview = getLayoutInflater().inflate(R.layout.dialog_edittext, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("이름 등록")
                .setMessage("이름을 등록해주세요. 등록되지 않을 시 앱을 계속해서 이용 할 수 없습니다.")
                .setView(dialogview)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<String> call = getRetrofitQuery().update_memberinfo_name(b_id, ((EditText) dialogview).getText().toString());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.i(TAG, "update_memberinfo_name Success : " + response.message());
                                Log.i(TAG, "update_memberinfo_name Success : " + response.body());
                                tv_name.setText(String.format(getString(R.string.membername), ((EditText) dialogview).getText().toString()));
                                BaseActivity.memberinfo.setName(((EditText) dialogview).getText().toString());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i(TAG, "update_memberinfo_name fail : " + t.getMessage());
                            }
                        });
                    }
                }).setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.finishAffinity(activity);
            }
        }).create().show();
    }

    private void update_memberinfo_fb_token(int id, String fb_token) {
        Call<String> call = getRetrofitQuery().update_memberinfo_token(id, fb_token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    Log.i(TAG, "update_memberinfo_fbtoken success : "+response.body());
                } else {
                    Log.i(TAG, "update_memberinfo_fbtoken error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "update_memberinfo_fbtoken fail : " + t.getMessage());
            }
        });
    }

    private void get_masterinfo() {
        Call<MasterInfoList> call = getRetrofitQuery().get_masterinfo();
        call.enqueue(new Callback<MasterInfoList>() {
            @Override
            public void onResponse(Call<MasterInfoList> call, Response<MasterInfoList> response) {
                if(response.message().equals(getString(R.string.connect_network))) {
                    BaseActivity.masterlist = response.body().getMasterlist();
                } else {
                    Log.i(TAG, "getMasterinfo error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MasterInfoList> call, Throwable t) {
                Log.i(TAG, "getmasterinfo fail : " + t.getMessage());
            }
        });
    }
}
