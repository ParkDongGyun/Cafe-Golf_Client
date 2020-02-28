package com.sbsj.cafgolf.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.cafgolf.Database.BookingInfoField;
import com.sbsj.cafgolf.Database.LessonTimeField;
import com.sbsj.cafgolf.OnItemClickListener;
import com.sbsj.cafgolf.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

	private final String TAG = "Book_Activity";

	private Context context;
	private ArrayList<LessonTimeField> lessonList;
	private ArrayList<BookingInfoField> bookinglist = new ArrayList<>();
	private OnItemClickListener listener;

	public BookListAdapter(Context context, ArrayList<LessonTimeField> lessonList, OnItemClickListener listener) {
		this.context = context;
		this.lessonList = lessonList;
		this.listener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View view = layoutInflater.inflate(R.layout.item_book, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.tv_book_id.setText(String.format(context.getString(R.string.idformat), position + 1));
		LessonTimeField lessonTime = lessonList.get(position);
		String startTime = attachTime(makeTime(lessonTime.getHour()), makeTime(lessonTime.getMinute()));
		String endTime = attachTime(makeTime(lessonTime.getHour() + 1), makeTime(lessonTime.getMinute()));

		holder.textView.setText(startTime + " ~ " + endTime);

		holder.textView.setPaintFlags(0);
		holder.button.setBackgroundColor(context.getResources().getColor(R.color.colorgreen));
		holder.button.setText("예약 가능");

		if (bookinglist.size() < 0) {
			return;
		}

		SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.dateformat));

		for (int i = 0; i < bookinglist.size(); ++i) {
			try {
				Date date = format.parse(bookinglist.get(i).getBook_date());

				if (lessonTime.getHour() == date.getHours()) {
					Log.i("Book_Activity", "booking date : " + bookinglist.get(i).getBook_date());
//					holder.textView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
					holder.button.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
					holder.button.setText("예약 불가");
					holder.canbook = false;
				} else {
				}
			} catch (ParseException e) {
				Log.i("Book_Activity", "ParseException : " + e.toString());
			}
		}
	}

	@Override
	public int getItemCount() {
		return lessonList.size();
	}


	public ArrayList<LessonTimeField> getlessonList() {
		return lessonList;
	}

	public ArrayList<BookingInfoField> getBookinglist() {
		return bookinglist;
	}

	public void setBookinglist(ArrayList<BookingInfoField> bookinglist) {
		this.bookinglist = bookinglist;
	}

	private String attachTime(String hour, String minute) {
		return hour + ":" + minute;
	}

	private String makeTime(int s) {
		String time = "";
		if (s == 0) {
			time = "00";
		} else if (s < 10) {
			time = "0" + Integer.toString(s);
		} else {
			time = Integer.toString(s);
		}

		return time;
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView tv_book_id;
		TextView textView;
		Button button;

		boolean canbook = true;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			tv_book_id = itemView.findViewById(R.id.tv_booking_id);
			textView = itemView.findViewById(R.id.tv_booktime);
			button = itemView.findViewById(R.id.btn_booktime);
			button.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			listener.onItemClick(view, getAdapterPosition(), canbook);
		}
	}
}
