package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaguthiList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("paguthi_details")
    @Expose
    private ArrayList<Paguthi> paguthiArrayList = new ArrayList<>();

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
     * @return The constituencyArrayList
     */
    public ArrayList<Paguthi> getPaguthiArrayList() {
        return paguthiArrayList;
    }

    /**
     * @param paguthiArrayList The paguthiArrayList
     */
    public void setPaguthiArrayList(ArrayList<Paguthi> paguthiArrayList) {
        this.paguthiArrayList = paguthiArrayList;
    }
}