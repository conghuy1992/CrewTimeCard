package timecard.dazone.com.dazonetimecard.utils;

public class Urls {
    public static final String URL_AUTO_LOGIN = "/UI/WebService/WebServiceCenter.asmx/AutoLogin";
    public static final String URL_GET_LOGIN = "/UI/WebService/WebServiceCenter.asmx/Login_v2";
    public static final String URL_GET_INSERT_TIME_CARDS = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/InsertTimeCards_v3";
    public static final String URL_GET_MY_LIST = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetMyList_v2";
    public static final String URL_GET_ALLOW_DEVICE = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetAllowDevice";
    public static final String URL_GET_ALL_EMPLOYEES_SORT = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetAllEmployerSortType_v2";
    public static final String URL_GET_ALL_EMPLOYEES_STATUS_NEW = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetCalculaterStatusForDays_v3";
    public static final String URL_GET_USER_LIST = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetEmployerSingler_v2";
    public static final String URL_CHECK_SESSION = "/UI/WebService/WebServiceCenter.asmx/CheckSessionUser_v2";
    public static final String URL_LOG_OUT = "/UI/WebService/WebServiceCenter.asmx/LogOutUser";
    public static final String URL_MESSAGE_STATUS_LOCAL = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetTextStatusLanguageCode";
    public static final String URL_SETTING_USER = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetSettingOffice";
    public static final String URL_SERVER_TIME = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetTimeServer_V3";
    public static final String URL_GET_STATUS = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetTypeStatusEnd_V2";
    public static final String URL_GET_USER_INFO = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetUserInfo";
    public static final String URL_REG_GCM_ID = "/UI/WebService/GCMWebService.asmx/Note_GCMUpdateRegID";
    public static final String URL_SAVE_SETTING_OFFICE = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/SaveSettingOffice";
    public static final String URL_DELETE_SETTING_OFFICE = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/DeleteSettingOffice";
    public static final String CheckUserPermissionType = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/CheckUserPermissionType";
    public static final String URL_GET_DEPARTMENT = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetDepartments";
    public static final String CheckAllowDevice = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/CheckAllowDevice";


    public static final String URL_HAS_APPLICATION = "/UI/WebService/WebServiceCenter.asmx/HasApplication_v2";
    public static final String URL_SIGN_UP = "http://www.crewcloud.net/UI/Center/MobileService.asmx/SendConfirmEmail";
    public static final String URL_GET_USER = "/UI/WebService/WebServiceCenter.asmx/GetUser";

    /**
     * NEW
     */
    public static final String URL_CHANGE_PASSWORD = "/UI/WebService/WebServiceCenter.asmx/UpdatePassword";

    /**
     * BEACON
     */
    public static final String URL_INSERT_TIME_CARD_FOR_BEACON = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/InsertTimeCardsForBeacon";
    public static final String URL_INSERT_BEACON = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/InsertBeaconPoint";
    public static final String URL_UPDATE_BEACON = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/UpdateBeaconPoint";
    public static final String URL_GET_BEACON_POINTS = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetBeaconPoints";
    public static final String URL_GET_BEACON_POINT = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetBeaconPoint";
    public static final String URL_GET_BEACON_POINT_BY_LOCATION = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/GetBeaconPointByLocation";
    public static final String URL_DELETE_BEACON_POINT = "/UI/MobileWorkingTime/WorkingTime_Service.asmx/DeleteBeaconPoint";
}