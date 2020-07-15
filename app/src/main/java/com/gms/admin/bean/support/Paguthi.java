package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Paguthi implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("paguthi_name")
    @Expose
    private String paguthi_name;
    
    /**
     * @return The id
     */
    public String getid() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setid(String id) {
        this.id = id;
    }

    /**
     * @return The paguthi_name
     */
    public String getpaguthi_name() {
        return paguthi_name;
    }

    /**
     * @param paguthi_name The paguthi_name
     */
    public void setpaguthi_name(String paguthi_name) {
        this.paguthi_name = paguthi_name;
    }

    public Paguthi(String id, String paguthi_name) {
        this.id = id;
        this.paguthi_name = paguthi_name;
    }

}