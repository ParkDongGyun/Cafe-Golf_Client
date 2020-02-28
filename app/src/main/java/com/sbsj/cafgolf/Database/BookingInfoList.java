package com.sbsj.cafgolf.Database;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BookingInfoList {
    @SerializedName("bookinginfo")
    private ArrayList<BookingInfoField> list = new ArrayList<>();

    public ArrayList<BookingInfoField> getlist() {
        return list;
    }
}
