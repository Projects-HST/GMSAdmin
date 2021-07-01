package com.gms.admin.utils;

/**
 * Created by Admin on 23-09-2017.
 */

public class GMSConstants {


    //URL'S
    //BASE URL
    private static final String BASE_URL = "https://happysanz.in/";

    //Development Mode
    //development
//    public static final String JOINT_URL = "development/";
    //uat
//    public static final String JOINT_URL = "uat/";
    //live
    public static final String JOINT_URL = "";
    //
//    //BUILD URL
    public static final String BUILD_URL = BASE_URL + JOINT_URL + "superadmingms/api/";
//    public static final String BUILD_URL = BASE_URL + "uat/apicustomer/";
//    public static final String BUILD_URL = BASE_URL + "apicustomer/";

    public static final String CHECK_CONSTITUENCY = "https://happysanz.in/gms/apiandroid/chk_constituency_code/";


    //CONSTITUENCY URL
    public static final String CONSTITUENCY = "list";

    //CONSTITUENCY URL
    public static final String SELECTED_CONSTITUENCY = "details";

    //MOBILE LOGIN URL
    public static final String MOBILE_LOGIN = "apiandroid/mobile_login";

    //MOBILE VERIFY URL
    public static final String MOBILE_VERIFY = "apiandroid/mobile_verify";

    //LOGIN URL
    public static final String USER_LOGIN = "apiandroid/login/";

    //GET OTP URL
    public static final String FORGOT_PASSWORD = "apiandroid/forgotPassword";

    //VERSION CHECK URL
    public static final String CHECK_VERSION = "apiandroid/version_check";

    //PAGUTHI LIST URL
    public static final String GET_PAGUTHI = "apiandroid/listPaguthi";

    //OFFICE LIST URL
    public static final String GET_OFFICE = "apiandroid/list_office";

    //DASHBOARD URL
    public static final String GET_DASHBOARD_DETAIL = "apiandroid/dashBoard";

    //MEMBER WIDGET URL
    public static final String GET_WIDGET_CONSTITUTENT = "apiandroid/widgets_members";

    //GRIEVANCE WIDGET URL
    public static final String GET_WIDGET_GRIEVANCE = "apiandroid/widgets_grievances";

    //FOOTFALL WIDGET URL
    public static final String GET_WIDGET_FOOTFALL = "apiandroid/widgets_footfall/";

    //MEETING WIDGET URL
    public static final String GET_WIDGET_MEETING = "apiandroid/widgets_meetings";

    //VOLUNTEER WIDGET URL
    public static final String GET_WIDGET_VOLUNTEER = "apiandroid/widgets_volunteer/";

    //GREETINGS WIDGET URL
    public static final String GET_WIDGET_GREETINGS = "apiandroid/widgets_greetings/";

    //VIDEO WIDGET URL
    public static final String GET_WIDGET_VIDEO = "apiandroid/widgets_videos/";

    //DASHBOARD SEARCH URL
    public static final String GET_SEARCH_RESULT = "apiandroid/dashBoard_searchnew";

    //CONSTITUENT SEARCH URL
    public static final String GET_SEARCH_RESULT_CONSTITUENT = "apiandroid/listConstituentsearch";

    //MEETING SEARCH URL
    public static final String GET_SEARCH_RESULT_MEETING = "apiandroid/meetingRequestsearch";

    //GRIEVANCE SEARCH URL
    public static final String GET_SEARCH_RESULT_GRIEVANCE = "apiandroid/listGrievancesearch";

    //CONSTITUENT LIST URL
    public static final String GET_CONSTITUENT_LIST = "apiandroid/listConstituentnew";

    //CONSTITUENT DETAILS URL
    public static final String GET_CONSTITUENT_DETAIL = "apiandroid/constituentDetails";

    //CONSTITUENT PLANTS URL
    public static final String GET_CONSTITUENT_PLANT = "apiandroid/constituentPlant";

    //CONSTITUENT MEETINGS URL
    public static final String GET_CONSTITUENT_MEETINGS = "apiandroid/constituentMeetings";

    //VERSION CHECK URL
    public static final String GET_CONSTITUENT_MEETING_DETAILS = "apiandroid/constituentMeetingdetails";

    //VERSION CHECK URL
    public static final String GET_CONSTITUENT_GRIEVANCE = "apiandroid/constituentGrievances";

    //VERSION CHECK URL
    public static final String GET_GRIEVANCE_MESSAGE = "apiandroid/grievanceMessage";

    //VERSION CHECK URL
    public static final String GET_CONSTITUENT_INTERACTION = "apiandroid/constituentInteraction";

    //VERSION CHECK URL
    public static final String GET_CONSTITUENT_DOC = "apiandroid/constituentDocuments";

    //VERSION CHECK URL
    public static final String GET_GRIEVANCE_DOC = "apiandroid/constituentgrvDocuments";

    //VERSION CHECK URL
    public static final String GET_MEETINGS = "apiandroid/meetingRequestnew";

    //VERSION CHECK URL
    public static final String GET_MEETINGS_DETAILS = "apiandroid/meetingDetails";

    //VERSION CHECK URL
    public static final String UPDATE_MEETING = "apiandroid/meetingUpdate";

    //VERSION CHECK URL
    public static final String GET_GRIEVANCE_LIST = "apiandroid/listGrievance";

    //VERSION CHECK URL
    public static final String GET_GRIEVANCE_LIST_NEW = "apiandroid/listGrievancenew";

    //VERSION CHECK URL
    public static final String GET_GRIEVANCE_DETAILS = "apiandroid/constituentGrievancedetails";

    //VERSION CHECK URL
    public static final String GET_STAFF_LIST= "apiandroid/listStaff";

    //VERSION CHECK URL
    public static final String GET_STAFF_DETAILS = "apiandroid/staffDetails";

    //VERSION CHECK URL
    public static final String GET_REPORT_STATUS = "apiandroid/reportStatus";

    //VERSION CHECK URL
    public static final String GET_REPORT_STATUS_SEARCH = "apiandroid/reportStatussearch";

    //VERSION CHECK URL
    public static final String GET_REPORT_GRIEVANCE = "apiandroid/reportGrievances/";

    //VERSION CHECK URL
    public static final String GET_REPORT_CONSTITUENT = "apiandroid/reportConstituent/";

    //VERSION CHECK URL
    public static final String GET_REPORT_VIDEO = "apiandroid/reportVideo/";

    //VERSION CHECK URL
    public static final String GET_FESTIVAL = "apiandroid/getFestivals/";

    //VERSION CHECK URL
    public static final String GET_REPORT_FESTIVAL = "apiandroid/reportFestival/";

    //VERSION CHECK URL
    public static final String GET_REPORT_CATEGORY_SEARCH = "apiandroid/reportCategorysearch/";

    //VERSION CHECK URL
    public static final String GET_SEEKER_LIST = "apiandroid/activeSeeker/";

    //VERSION CHECK URL
    public static final String GET_CATEGORY_LIST = "apiandroid/activeCategory/";

    //VERSION CHECK URL
    public static final String GET_SUB_CATEGORY_LIST = "apiandroid/activeSubcategory";

    //VERSION CHECK URL
    public static final String GET_GRIEVANCE_SUB_CATEGORY_LIST = "apiandroid/grivancesSubcategory";

    //VERSION CHECK URL
    public static final String GET_REPORT_SUB_CATEGORY = "apiandroid/reportsubCategorynew";

    //VERSION CHECK URL
    public static final String GET_REPORT_SUB_CATEGORY_SEARCH = "apiandroid/reportsubCategorysearch";

    //VERSION CHECK URL
    public static final String GET_REPORT_LOCATION = "apiandroid/reportLocationnew";

    //VERSION CHECK URL
    public static final String GET_REPORT_LOCATION_SEARCH = "apiandroid/reportLocationsearch";

    //VERSION CHECK URL
    public static final String GET_REPORT_MEETING = "apiandroid/reportMeetings";

    //VERSION CHECK URL
    public static final String GET_REPORT_MEETING_SEARCH = "apiandroid/reportMeetingssearch";

    //VERSION CHECK URL
    public static final String GET_REPORT_STAFF = "apiandroid/reportStaff/";

    //VERSION CHECK URL
    public static final String GET_REPORT_BIRTHDAY = "apiandroid/reportBirthday";

    //VERSION CHECK URL
    public static final String GET_REPORT_BIRTHDAY_SEARCH = "apiandroid/reportBirthdaysearch";

    //VERSION CHECK URL
    public static final String GET_PROFILE = "apiandroid/profileDetails";

    //VERSION CHECK URL
    public static final String UPDATE_PROFILE = "apiandroid/profileUpdate";

    //VERSION CHECK URL
    public static final String UPLOAD_PIC = "apiandroid/profilePictureUpload/";

    //VERSION CHECK URL
    public static final String CHANGE_PASSWORD = "apiandroid/changePassword/";



    //////    Service Params    ///////


    public static String PARAM_MESSAGE = "msg";
    public static String PARAM_MESSAGE_ENG = "msg_en";
    public static String PARAM_MESSAGE_TAMIL = "msg_ta";

    //     Shared preferences file name
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //    Shared FCM ID
    public static final String KEY_FCM_ID = "fcm_id";

    //    Shared IMEI No
    public static final String KEY_IMEI = "imei_code";

    //    Shared Phone No
    public static final String KEY_MOBILE_NUMBER = "mobile_no";
    public static final String KEY_ADMIN_MOBILE_NUMBER = "mobile_no";

    //    Shared Lang
    public static final String KEY_LANGUAGE = "language";

    //    USER DATA
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_ROLE = "user_role";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_OLD_PASSWORD = "old_password";
    public static final String KEY_NEW_PASSWORD = "new_password";
    public static final String KEY_CONSTITUENCY_ID = "constituency_id";
    public static final String KEY_CONSTITUENT_ID = "constituent_id";
    public static final String KEY_GRIEVANCE_ID = "grievance_id";
    public static final String KEY_CONSTITUENCY_NAME = "constituency_name";
    public static final String KEY_CONSTITUENT_NAME = "constituent_name";
    public static final String KEY_CLIENT_API_URL = "client_api_url";
    public static final String KEY_JUST_ID = "id";
    public static final String KEY_MEETING_ID = "meeting_id";
    public static final String KEY_STAFF_ID = "staff_id";
    public static final String KEY_PARTY_ID = "party_id";
    public static final String KEY_APP_BASE_COLOR = "base_colour";
    public static final String FULL_NAME = "full_name";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "PHONE";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_ADDRESS = "address";
    public static final String KEY_ADMIN_ADDRESS = "address";
    public static final String KEY_USER_PROFILE_PIC = "profile_pic";
    public static final String KEY_CONST_PROFILE_PIC = "const_profile_pic";
    public static final String KEY_USER_MAIL = "email";
    public static final String KEY_ADMIN_MAIL = "email";
    public static final String KEY_MAIL_ID = "email_id";
    public static final String KEY_USER_MAIL_STATUS = "email_verify_status";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_FROM_DATE = "from_date";
    public static final String KEY_TO_DATE = "to_date";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LAST_LOGIN = "last_login";
    public static final String KEY_LOGIN_COUNT = "login_count";
    public static final String KEY_GRIEVANCE_TYPE = "grievance_type";
    public static final String KEY_OFFSET = "offset";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_SEEKER = "seeker";
    public static final String KEY_SEEKER_ID = "seeker_type_id";
    public static final String KEY_FESTIVAL = "festival";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_GRIEVANCE_TYPE_ID = "grievance_type_id";
    public static final String KEY_SUB_CATEGORY = "sub_category";
    public static final String KEY_SUB_CATEGORY_ID = "sub_category_id";
    public static final String KEY_MONTH = "select_month";
    public static final String KEY_ROWCOUNT = "rowcount";
    public static final String KEY_PINCODE = "pin_code";
    public static final String KEY_WHATSAPP_NO = "whatsapp_no";
    public static final String KEY_FATHER_OR_HUSBAND = "father_husband_name";
    public static final String KEY_RELIGION = "religion_name";
    public static final String PAGUTHI_ID = "paguthi_id";
    public static final String OFFICE_ID = "office_id";
    public static final String OFFICE = "office";
    public static final String PAGUTHI = "paguthi";
    public static final String KEY_WARD = "ward_name";
    public static final String KEY_BOOTH = "booth_name";
    public static final String KEY_BOOTH_ADDRESS = "booth_address";
    public static final String KEY_SERIAL_NO = "serial_no";
    public static final String KEY_AADHAAR = "aadhaar_no";
    public static final String KEY_VOTER_ID = "voter_id_no";
    public static final String KEY_DOB = "dob";
    public static final String KEY_PIC_URL = "assets/constituent/";
    public static final String KEY_STAFFPIC_URL = "assets/users/";
    public static final String KEY_DOC_URL = "assets/constituent/doc/";

    // Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";
    public static final String ALERT_DIALOG_THEME = "alertDialogTheme";

    // Login Parameters
    public static final String KEY_CONSTITUENCY_CODE = "constituency_code";
    public static final String DYNAMIC_DATABASE = "dynamic_db";
    public static String KEY_OTP = "otp";
    public static String KEY_DEVICE_ID = "device_id";
    public static String DEVICE_TOKEN = "gcm_key";
    public static String MOBILE_TYPE = "mobile_type";
    public static String MOBILE_TYPE_VALUE = "1";

    public static String KEY_APP_VERSION = "version_code";
    public static String KEY_APP_VERSION_VALUE = "1";


    // Service Parameters
    public static String SEARCH_STATUS = "search_status";
    public static String SEARCH_TEXT = "keyword";
    public static String SEARCH_TEXT_TA = "search_txt_ta";
    public static String APP_COLOUR = "app_colour";


}
