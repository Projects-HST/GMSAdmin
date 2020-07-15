package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConstituencyList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("data")
    @Expose
    private ArrayList<Constituency> constituencyArrayList = new ArrayList<>();

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
    public ArrayList<Constituency> getConstituencyArrayList() {
        return constituencyArrayList;
    }

    /**
     * @param constituencyArrayList The constituencyArrayList
     */
    public void setConstituencyArrayList(ArrayList<Constituency> constituencyArrayList) {
        this.constituencyArrayList = constituencyArrayList;
    }
}