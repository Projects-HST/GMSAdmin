package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Birthday implements Serializable {

    @SerializedName("const_id")
    @Expose
    private String id;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("birth_wish_status")
    @Expose
    private String birth_wish_status;

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
     * @return The full_name
     */
    public String getfull_name() {
        return full_name;
    }

    /**
     * @param full_name The full_name
     */
    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }

    /**
     * @return The mobile_no
     */
    public String getmobile_no() {
        return mobile_no;
    }

    /**
     * @param mobile_no The mobile_no
     */
    public void setmobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    /**
     * @return The dob
     */
    public String getdob() {
        return dob;
    }

    /**
     * @param dob The dob
     */
    public void setdob(String dob) {
        this.dob = dob;
    }

    /**
     * @return The birth_wish_status
     */
    public String getbirth_wish_status() {
        return birth_wish_status;
    }

    /**
     * @param birth_wish_status The birth_wish_status
     */
    public void setbirth_wish_status(String birth_wish_status) {
        this.birth_wish_status = birth_wish_status;
    }

}