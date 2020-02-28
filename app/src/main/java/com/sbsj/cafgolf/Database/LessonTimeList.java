package com.sbsj.cafgolf.Database;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LessonTimeList {
    @SerializedName("lessontimelist")
    private ArrayList<LessonTimeField> list = new ArrayList<>();

    public ArrayList<LessonTimeField> getlist() {
        return list;
    }
}
