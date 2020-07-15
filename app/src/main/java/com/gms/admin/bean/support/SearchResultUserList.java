package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResultUserList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("search_result")
    @Expose
    private ArrayList<User> userArrayList = new ArrayList<>();

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
     * @return The userArrayList
     */
    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    /**
     * @param userArrayList The userArrayList
     */
    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }
}