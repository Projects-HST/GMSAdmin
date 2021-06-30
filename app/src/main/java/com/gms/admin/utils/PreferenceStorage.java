package com.gms.admin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

/**
 * Created by Admin on 11-10-2017.
 */

public class PreferenceStorage {

    /*To check welcome screen to launch*/
    public static void setFirstTimeLaunch(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GMSConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(GMSConstants.IS_FIRST_TIME_LAUNCH, true);
    }
    /*End*/

    /*To store dynamic database */
    public static void setDynamicDb(Context context, String dbDynamic) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.DYNAMIC_DATABASE, dbDynamic);
        editor.apply();
    }

    public static String getDynamicDb(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(GMSConstants.DYNAMIC_DATABASE, "");
    }
    /*End*/

    /*To save mobile IMEI number */
    public static void saveIMEI(Context context, String imei) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_IMEI, imei);
        editor.apply();
    }

    public static String getIMEI(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(GMSConstants.KEY_IMEI, "");
    }
    /*End*/

    // UserId
    public static void saveUserId(Context context, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_ID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_ID, "");
        return userId;
    }
    /*End*/

    // UserId
    public static void saveUserRole(Context context, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_ROLE, userId);
        editor.apply();
    }

    public static String getUserRole(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_ROLE, "");
        return userId;
    }
    /*End*/

    // AppBase Color
    public static void saveAppBaseColor(Context context, String colour) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_APP_BASE_COLOR, colour);
        editor.apply();
    }

    public static String getAppBaseColor(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String colour;
        colour = sharedPreferences.getString(GMSConstants.KEY_APP_BASE_COLOR, "");
        return colour;
    }
    /*End*/


    // User Type
    public static void saveStatus(Context context, String userType) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_STATUS, userType);
        editor.apply();
    }

    public static String getStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userType;
        userType = sharedPreferences.getString(GMSConstants.KEY_STATUS, "");
        return userType;
    }
    /*End*/

    // User Type
    public static void saveLogin(Context context, String userType) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_LAST_LOGIN, userType);
        editor.apply();
    }

    public static String getLogin(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userType;
        userType = sharedPreferences.getString(GMSConstants.KEY_LAST_LOGIN, "");
        return userType;
    }
    /*End*/

    // User Type
    public static void saveLoginCount(Context context, String userType) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_LOGIN_COUNT, userType);
        editor.apply();
    }

    public static String getLoginCount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userType;
        userType = sharedPreferences.getString(GMSConstants.KEY_LOGIN_COUNT, "");
        return userType;
    }
    /*End*/

    // UserName
    public static void saveAdminName(Context context, String userName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.FULL_NAME, userName);
        editor.apply();
    }

    public static String getAdminName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.FULL_NAME, "");
        return userId;
    }
    /*End*/

    // UserName
    public static void saveConstituentName(Context context, String userName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CONSTITUENT_NAME, userName);
        editor.apply();
    }

    public static String getConstituentName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_CONSTITUENT_NAME, "");
        return userId;
    }
    /*End*/

    // UserGender
    public static void saveGender(Context context, String userGender) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_GENDER, userGender);
        editor.apply();
    }

    public static String getGender(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_GENDER, "");
        return userId;
    }
    /*End*/

    // UserGender
    public static void saveAdminGender(Context context, String userGender) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_GENDER, userGender);
        editor.apply();
    }

    public static String getAdminGender(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_GENDER, "");
        return userId;
    }
    /*End*/

    // UserAddress
    public static void saveAddress(Context context, String userAddress) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_ADDRESS, userAddress);
        editor.apply();
    }

    public static String getAddress(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_ADDRESS, "");
        return userId;
    }
    /*End*/

    // UserAddress
    public static void saveAdminAddress(Context context, String userAddress) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_ADMIN_ADDRESS, userAddress);
        editor.apply();
    }

    public static String getAdminAddress(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_ADMIN_ADDRESS, "");
        return userId;
    }
    /*End*/

    // UserAddress
    public static void saveConstituentAddress(Context context, String userAddress) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_ADDRESS, userAddress);
        editor.apply();
    }

    public static String getConstituentAddress(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_ADDRESS, "");
        return userId;
    }
    /*End*/

    // UserEmail
    public static void saveEmail(Context context, String userEmail) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_MAIL, userEmail);
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_MAIL, "");
        return userId;
    }
    /*End*/

    // UserEmail
    public static void saveAdminEmail(Context context, String userEmail) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_ADMIN_MAIL, userEmail);
        editor.apply();
    }

    public static String getAdminEmail(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_ADMIN_MAIL, "");
        return userId;
    }
    /*End*/

    // UserPic
    public static void saveProfilePic(Context context, String userPic) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_USER_PROFILE_PIC, userPic);
        editor.apply();
    }

    public static String getProfilePic(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_USER_PROFILE_PIC, "");
        return userId;
    }
    /*End*/

    // UserPic
    public static void saveCOnstituentProfilePic(Context context, String userPic) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CONST_PROFILE_PIC, userPic);
        editor.apply();
    }

    public static String getCOnstituentProfilePic(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_CONST_PROFILE_PIC, "");
        return userId;
    }
    /*End*/

    // UserEmailVerify
    public static void savefatherORhusband(Context context, String userEmailVerify) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_FATHER_OR_HUSBAND, userEmailVerify);
        editor.apply();
    }

    public static String getfatherORhusband(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(GMSConstants.KEY_FATHER_OR_HUSBAND, "");
        return userId;
    }
    /*End*/

    /*To save FCM key locally*/
    public static void saveGCM(Context context, String gcmId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_FCM_ID, gcmId);
        editor.apply();
    }

    public static String getGCM(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(GMSConstants.KEY_FCM_ID, "");
    }
    /*End*/


    // User Type
    public static void savePincode(Context context, String userType) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_PINCODE, userType);
        editor.apply();
    }

    public static String getPincode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userType;
        userType = sharedPreferences.getString(GMSConstants.KEY_PINCODE, "");
        return userType;
    }
    /*End*/

    /*To store mobile number*/
    public static void savePaguthiName(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.PAGUTHI, type);
        editor.apply();
    }

    public static String getPaguthiName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.PAGUTHI, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveMobileNo(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_MOBILE_NUMBER, type);
        editor.apply();
    }

    public static String getMobileNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_MOBILE_NUMBER, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveAdminMobileNo(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_ADMIN_MOBILE_NUMBER, type);
        editor.apply();
    }

    public static String getAdminMobileNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_ADMIN_MOBILE_NUMBER, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveWhatsappNo(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_WHATSAPP_NO, type);
        editor.apply();
    }

    public static String getWhatsappNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_WHATSAPP_NO, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveReligionName(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_RELIGION, type);
        editor.apply();
    }

    public static String getReligionName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_RELIGION, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void savePaguthiID(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.PAGUTHI_ID, type);
        editor.apply();
    }

    public static String getPaguthiID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.PAGUTHI_ID, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveOfficeID(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.OFFICE_ID, type);
        editor.apply();
    }

    public static String getOfficeID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.OFFICE_ID, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveWard(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_WARD, type);
        editor.apply();
    }

    public static String getWard(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_WARD, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveBooth(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_BOOTH, type);
        editor.apply();
    }

    public static String getBooth(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_BOOTH, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveBoothAddress(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_BOOTH_ADDRESS, type);
        editor.apply();
    }

    public static String getBoothAddress(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_BOOTH_ADDRESS, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveSerialNo(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_SERIAL_NO, type);
        editor.apply();
    }

    public static String getSerialNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_SERIAL_NO, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveAadhaarNo(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_AADHAAR, type);
        editor.apply();
    }

    public static String getAadhaarNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_AADHAAR, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveVoterId(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_VOTER_ID, type);
        editor.apply();
    }

    public static String getVoterId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_VOTER_ID, "");
        return mobileNo;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveDob(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_DOB, type);
        editor.apply();
    }

    public static String getDob(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_DOB, "");
        return mobileNo;
    }
    /*End*/

    /*To store Constituency ID*/
    public static void saveConstituencyID(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CONSTITUENCY_ID, type);
        editor.apply();
    }

    public static String getConstituencyID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_CONSTITUENCY_ID, "");
        return mobileNo;
    }
    /*End*/

    /*To store Constituent ID*/
    public static void saveConstituentID(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CONSTITUENT_ID, type);
        editor.apply();
    }

    public static String getConstituentID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_CONSTITUENT_ID, "");
        return mobileNo;
    }
    /*End*/

    /*To store Constituency ID*/
    public static void saveUserConstituencyName(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CONSTITUENCY_NAME, type);
        editor.apply();
    }

    public static String getUserConstituencyName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_CONSTITUENCY_NAME, "");
        return mobileNo;
    }
    /*End*/

    /*To store Constituency ID*/
    public static void saveConstituencyName(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CONSTITUENCY_NAME, type);
        editor.apply();
    }

    public static String getConstituencyName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_CONSTITUENCY_NAME, "");
        return mobileNo;
    }
    /*End*/

    /*To store Constituency ID*/
    public static void saveClientUrl(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CLIENT_API_URL, type);
        editor.apply();
    }

    public static String getClientUrl(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(GMSConstants.KEY_CLIENT_API_URL, "");
        return mobileNo;
    }
    /*End*/

    /*To search*/
    public static void setSearchFor(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.SEARCH_STATUS, ser);
        editor.apply();
    }

    public static String getSearchFor(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.SEARCH_STATUS, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveGrievanceType(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_GRIEVANCE_TYPE, ser);
        editor.apply();
    }

    public static String getGrievanceType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_GRIEVANCE_TYPE, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveFromDate(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_FROM_DATE, ser);
        editor.apply();
    }

    public static String getFromDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_FROM_DATE, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveToDate(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_TO_DATE, ser);
        editor.apply();
    }

    public static String getToDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_TO_DATE, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveReportStatus(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_STATUS, ser);
        editor.apply();
    }

    public static String getReportStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_STATUS, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveReportSeeker(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_SEEKER, ser);
        editor.apply();
    }

    public static String getReportSeeker(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_SEEKER, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveReportCategory(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_CATEGORY, ser);
        editor.apply();
    }

    public static String getReportCategory(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_CATEGORY, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveReportSubCategory(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_SUB_CATEGORY, ser);
        editor.apply();
    }

    public static String getReportSubCategory(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_SUB_CATEGORY, "");
        return ser;
    }
    /*End*/

    /*To search*/
    public static void saveMonth(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.KEY_MONTH, ser);
        editor.apply();
    }

    public static String getMonth(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.KEY_MONTH, "");
        return ser;
    }
    /*End*/


    /*To search*/
    public static void saveColour(Context context, String ser) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GMSConstants.APP_COLOUR, ser);
        editor.apply();
    }

    public static String getColour(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ser;
        ser = sharedPreferences.getString(GMSConstants.APP_COLOUR, "");
        return ser;
    }
    /*End*/
}
