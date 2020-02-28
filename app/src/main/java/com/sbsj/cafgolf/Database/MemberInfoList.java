package com.sbsj.cafgolf.Database;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MemberInfoList {
    @SerializedName("memberlist")
    private ArrayList<MemberInfoField> list = new ArrayList<>();

    public ArrayList<MemberInfoField> getlist() {
        return list;
    }
}
