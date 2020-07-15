package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("serial_no")
    @Expose
    private String serial_no;

    @SerializedName("profile_pic")
    @Expose
    private String profile_picture;

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
     * @return The serial_no
     */
    public String getSerial_no() {
        return serial_no;
    }

    /**
     * @param serial_no The serial_no
     */
    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    /**
     * @return The profile_picture
     */
    public String getprofile_picture() {
        return profile_picture;
    }

    /**
     * @param profile_picture The profile_picture
     */
    public void setprofile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

}