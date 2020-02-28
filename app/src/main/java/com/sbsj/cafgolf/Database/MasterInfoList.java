package com.sbsj.cafgolf.Database;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MasterInfoList {
    @SerializedName("masterlist")
    private ArrayList<MasterInfoField> masterlist = new ArrayList<>();

    public ArrayList<MasterInfoField> getMasterlist() {
        return masterlist;
    }
}
