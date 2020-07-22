package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BirthdayList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("birthday_report")
    @Expose
    private ArrayList<Birthday> birthdayArrayList = new ArrayList<>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The birthdayArrayList
     */
    public ArrayList<Birthday> getBirthdayArrayList() {
        return birthdayArrayList;
    }

    /**
     * @param birthdayArrayList The birthdayArrayList
     */
    public void setBirthdayArrayList(ArrayList<Birthday> birthdayArrayList) {
        this.birthdayArrayList = birthdayArrayList;
    }
}