package com.sbsj.cafgolf.Database;

import com.google.gson.annotations.SerializedName;

public class LessonTimeField {
    @SerializedName("id")
    private int id;
    @SerializedName("hour_before")
    private int hour;
    @SerializedName("minute_before")
    private int minute;

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
