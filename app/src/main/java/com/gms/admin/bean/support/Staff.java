package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Staff implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("phone_number")
    @Expose
    private String mobile_no;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    @SerializedName("profile_pic")
    @Expose
    private String profile_picture;

    @SerializedName("status")
    @Expose
    private String status;

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
     * @return The email_id
     */
    public String getemail_id() {
        return email_id;
    }

    /**
     * @param email_id The email_id
     */
    public void setemail_id(String email_id) {
        this.email_id = email_id;
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

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
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

}