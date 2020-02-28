package com.sbsj.cafgolf.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.cafgolf.Database.BookingInfoField;
import com.sbsj.cafgolf.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BookingInfoField> dateList;

    public MainAdapter(Context context, ArrayList<BookingInfoField> dateList) {
        this.context = context;
        this.dateList = dateList;
    }

    public ArrayList<BookingInfoField> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<BookingInfoField> dateList) {
        this.dateList = dateList;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_main, parent, false);

        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {

        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.dateformat));
        Date date = new Date();
        try {
            date = format.parse(dateList.get(position).getBook_date());
        } catch (ParseException e) {
            Log.i("Main_Activity", "ParseException_Error : " + e.toString());
        }
        if(date == new Date())
            return;
        SimpleDateFormat format_date = new SimpleDateFormat(context.getString(R.string.dateformat_date));
        holder.tv_date.setText(format_date.format(date));
        SimpleDateFormat format_time = new SimpleDateFormat(context.getString(R.string.dateformat_time));
        holder.tv_time.setText(format_time.format(date));
        Log.i("Main_Activity", "success : " + dateList.get(position).getBook_date());
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date;
        TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_mainitem_date);
            tv_time = itemView.findViewById(R.id.tv_mainitem_time);
        }
    }
}