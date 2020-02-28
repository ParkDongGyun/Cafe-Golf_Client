package com.sbsj.cafgolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.sbsj.cafgolf.Adapter.BookListAdapter;
import com.sbsj.cafgolf.Database.BookingInfoField;
import com.sbsj.cafgolf.Database.BookingInfoList;
import com.sbsj.cafgolf.Database.LessonTimeField;
import com.sbsj.cafgolf.Database.LessonTimeList;
import com.sbsj.cafgolf.Database.MasterInfoField;
import com.sbsj.cafgolf.Database.MemberInfoField;
import com.sbsj.cafgolf.Dialog.BookDialog;
import com.sbsj.cafgolf.OnItemClickListener;
import com.sbsj.cafgolf.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends BaseActivity {

    private final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "AAAA2Cx-nZU:APA91bGu9togdsBCtnW-L2TI0UWzm7jKjVWbEWmn5egCpUAD94NZoBpNRVCqVGShznaKg96IL3xEzKP79n2oTjJr3xocZvDe-tuVyQd5UwvFgtrOkNKsxBzMRaIr3Fw29TwRpVTCklfw";
    private final String TAG = "Book_Activity";
    //    private MaterialCalendarView materialCalendarView;
    private final Date today = new Date();
    private Toolbar toolbar;
    private CalendarView calendarView;
    private RecyclerView recyclerView;

    private ScrollView scrollView;

    int dWidth;
    int dHeight;

    //    private CalendarDay clickDate;
    private BookListAdapter bookListAdapter;

    private SimpleDateFormat format;

    private String book_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        toolbar = findViewById(R.id.tb_book);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        dWidth = displayMetrics.widthPixels;
        dHeight = displayMetrics.heightPixels;

        calendarView = findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        calendarView.setSelectionType(SelectionType.SINGLE);
        calendarView.setSelectionManager(new SingleSelectionManager(new OnDaySelectedListener() {
            @Override
            public void onDaySelected() {
                Date selectDate = calendarView.getSelectedDates().get(0).getTime();

                if (bookListAdapter.getBookinglist() != null) {
                    bookListAdapter.getBookinglist().clear();
                    bookListAdapter.notifyDataSetChanged();
                }
                getbooklist(selectDate);
            }
        }));

        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Month month) {

            }
        });

        recyclerView = findViewById(R.id.rcv_booklist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        format = new SimpleDateFormat(getString(R.string.dateformat));

        Call<LessonTimeList> call = getRetrofitQuery().get_lessontime();
        call.enqueue(new Callback<LessonTimeList>() {
            @Override
            public void onResponse(Call<LessonTimeList> call, Response<LessonTimeList> response) {
                if (response.message().equals("OK")) {
                    bookListAdapter = new BookListAdapter(getApplicationContext(), response.body().getlist(), new OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, boolean canbooking) {
                            if (canbooking) {
                                if (calendarView.getSelectedDates().size() > 0) {
                                    Date selectedDate = calendarView.getSelectedDates().get(0).getTime();
                                    book_date = format.format(itemDateTime(selectedDate, bookListAdapter.getlessonList().get(position)));
                                } else
                                    book_date = format.format(itemDateTime(new Date(), bookListAdapter.getlessonList().get(position)));

                                if (BaseActivity.memberinfo.getLesson_count() > 0) {
                                    setBookDialog();
                                } else
                                    Toast.makeText(BookActivity.this, "상담 문의전화: 010-6780-5875", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BookActivity.this, "이미 예약된 시간입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    recyclerView.setAdapter(bookListAdapter);
                    getbooklist(new Date());
                } else {
                    Log.i(TAG, "Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LessonTimeList> call, Throwable t) {
                Log.i(TAG, "Fail : " + t.getMessage());
            }
        });

        scrollView = findViewById(R.id.sv_book);
        scrollView.smoothScrollTo(scrollView.getScrollX(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getbooklist(Date date) {
        Call<BookingInfoList> call = getRetrofitQuery().get_bookinginfo_you(format.format(date));
        call.enqueue(new Callback<BookingInfoList>() {
            @Override
            public void onResponse(Call<BookingInfoList> call, Response<BookingInfoList> response) {
                if (response.message().equals("OK")) {
                    ArrayList<BookingInfoField> list = response.body().getlist();

                    if (list.get(0).getId() != -1) {
                        bookListAdapter.setBookinglist(list);
                    } else bookListAdapter.getBookinglist().clear();

                    bookListAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "Error_getbooklist : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BookingInfoList> call, Throwable t) {
                Log.i(TAG, "Fail_getbooklist : " + t.getMessage());
            }
        });
    }

    private Date itemDateTime(Date date, LessonTimeField field) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, field.getHour());
        cal.set(Calendar.MINUTE, field.getMinute());

        return cal.getTime();
    }

    private void setBookDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
        builder.setTitle("예약")
                .setMessage("예약시 취소 할 수 없습니다. 정말로 예약하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<String> call_member = getRetrofitQuery().update_memberinfo_lessoncount(b_id, -1);
                        call_member.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.i(TAG, "update_memberinfo_lessoncount Success : " + response.message());
                                Log.i(TAG, "update_memberinfo_lessoncount Success : " + response.body());


                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i(TAG, "update_memberinfo_lessoncount Success : " + t.getMessage());
                            }
                        });

                        Call<String> call_booking = getRetrofitQuery().insert_bookinginfo(b_id, book_date);
                        call_booking.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.i(TAG, "update_memberinfo_lessoncount Success : " + response.message());
                                Log.i(TAG, "update_memberinfo_lessoncount Success : " + response.body());

                                ArrayList<MasterInfoField> masterlist = BaseActivity.masterlist;
                                for (int i = 0; i < masterlist.size(); ++i) {
                                    new Thread(new MyThread(masterlist.get(i).getFb_token())).start();
                                }
                                try {
                                    Date tempdate = format.parse(book_date);
                                    getbooklist(tempdate);
                                } catch (ParseException e) {
                                    Log.i(TAG, "Error : " + e.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i(TAG, "update_memberinfo_lessoncount Fail : " + t.getMessage());
                            }
                        });
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    private void sendFCM(String master_token) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notification = new JSONObject();
            notification.put("body", "예약 승인 요청");
            notification.put("title", " 예약을 확인해주세요.");
            jsonObject.put("notification", notification);
            jsonObject.put("to", master_token);

            URL url = new URL(FCM_MESSAGE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
            Log.i(TAG, "Pass");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        }
    }

    class MyThread implements Runnable {
        private String master_token;

        public MyThread(String master_token) {
            this.master_token = master_token;
        }

        @Override
        public void run() {
            sendFCM(master_token);
        }

    }
}
