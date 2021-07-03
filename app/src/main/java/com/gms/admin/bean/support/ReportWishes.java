package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportWishes implements Serializable {

    @SerializedName("send_on")
    @Expose
    private String send_on;

    @SerializedName("father_husband_name")
    @Expose
    private String father_husband_name;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("whatsapp_no")
    @Expose
    private String whatsapp_no;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("pin_code")
    @Expose
    private String pin_code;

    @SerializedName("door_no")
    @Expose
    private String door_no;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    public String getSend_on() {
        return send_on;
    }

    public void setSend_on(String send_on) {
        this.send_on = send_on;
    }

    public String getFather_husband_name() {
        return father_husband_name;
    }

    public void setFather_husband_name(String father_husband_name) {
        this.father_husband_name = father_husband_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getWhatsapp_no() {
        return whatsapp_no;
    }

    public void setWhatsapp_no(String whatsapp_no) {
        this.whatsapp_no = whatsapp_no;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getDoor_no() {
        return door_no;
    }

    public void setDoor_no(String door_no) {
        this.door_no = door_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }
}
