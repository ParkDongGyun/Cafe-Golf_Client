package com.sbsj.cafgolf.Database;

import com.google.gson.annotations.SerializedName;

public class BookingInfoField {
    @SerializedName("id")
    private int id;
    @SerializedName("member_id")
    private int member_id;
    @SerializedName("book_date")
    private String book_date;
    @SerializedName("date")
    private String date;

    public int getId() {
        return id;
    }

    public int getMember_id() {
        return member_id;
    }

    public String getBook_date() {
        return book_date;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setBook_date(String book_date) {
        this.book_date = book_date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
