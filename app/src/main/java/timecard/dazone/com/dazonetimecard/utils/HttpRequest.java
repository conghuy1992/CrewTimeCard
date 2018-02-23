package timecard.dazone.com.dazonetimecard.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.database.UserDBHelper;
import timecard.dazone.com.dazonetimecard.dtos.AllEmployeeDto;
import timecard.dazone.com.dazonetimecard.dtos.AllowDevices;
import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;
import timecard.dazone.com.dazonetimecard.dtos.DataDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.MessageDto;
import timecard.dazone.com.dazonetimecard.dtos.MyListDto;
import timecard.dazone.com.dazonetimecard.dtos.ProfileUserDTO;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;
import timecard.dazone.com.dazonetimecard.dtos.UserDto;
import timecard.dazone.com.dazonetimecard.dtos.UserInfoDto;
import timecard.dazone.com.dazonetimecard.dtos.UserSettingDto;
import timecard.dazone.com.dazonetimecard.interfaces.BaseHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.BaseHTTPCallBackWithString;
import timecard.dazone.com.dazonetimecard.interfaces.DataHttpCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.GetServerTimeCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.IGetListDepart;
import timecard.dazone.com.dazonetimecard.interfaces.OnAutoLoginCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnCallGetMessageValue;
import timecard.dazone.com.dazonetimecard.interfaces.OnChangePasswordCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnCheckAllowDevice;
import timecard.dazone.com.dazonetimecard.interfaces.OnDeleteBeaconCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetAllEmpHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetBeaconByLocationCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetBeaconsCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetMyListHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetStatusResponse;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetUserCallback;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetUserInfoCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetUserInfoResponse;
import timecard.dazone.com.dazonetimecard.interfaces.OnHasAppCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnInsertBeaconCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnInsertTimeCardForBeaconCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnUpdateBeaconCallBack;

public class HttpRequest {
    private static String TAG = "HttpRequest";
    private static HttpRequest mInstance;
    private static String root_link;

    public static HttpRequest getInstance() {
        if (null == mInstance) {
            mInstance = new HttpRequest();
        }

        root_link = DaZoneApplication.getInstance().getmPrefs().getServerSite();
        return mInstance;
    }

    private void loginSuccess(String response, String companyDomain, String password, BaseHTTPCallBack baseHTTPCallBack) {
        Gson gson = new Gson();
        UserDto userDto = gson.fromJson(response, UserDto.class);
        userDto.prefs.putAccessToken(userDto.session);
        userDto.prefs.putIsAdmin(userDto.PermissionType);
        userDto.prefs.putCompanyNo(userDto.CompanyNo);
        new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
        userDto.prefs.putUserNo(userDto.Id);
        userDto.prefs.putUserName(userDto.userID);
        userDto.prefs.putDomain(companyDomain);
        UserDBHelper.addUser(userDto);
        DaZoneApplication.getInstance().getmPrefs().putPassword(password);
        DaZoneApplication.getInstance().getmPrefs().putUserName(userDto.userID);
        DaZoneApplication.getInstance().getmPrefs().putBooleanValue(Statics.IS_CHECK_ALLOW, true);
        baseHTTPCallBack.onHTTPSuccess();
    }

    public void login(final BaseHTTPCallBack baseHTTPCallBack, String userID, final String password, final String companyDomain, final String server_link, final String server_site) {
        Log.d(TAG, "server_site:" + server_site);
        final String url = server_link + Urls.URL_GET_LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        params.put("companyDomain", companyDomain);
        params.put("password", password);
        params.put("userID", userID);
        params.put("mobileOSVersion", "Android " + android.os.Build.VERSION.RELEASE);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                DaZoneApplication.getInstance().getmPrefs().putServerSite(server_site);

                Log.d(TAG, "login:" + response);
                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(response, UserDto.class);
                userDto.prefs.putAccessToken(userDto.session);
//                userDto.prefs.putIsAdmin(userDto.PermissionType);
//                userDto.prefs.putCompanyNo(userDto.CompanyNo);
//                new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
//                userDto.prefs.putUserNo(userDto.Id);
//                userDto.prefs.putUserName(userDto.userID);
//                userDto.prefs.putDomain(companyDomain);
//                UserDBHelper.addUser(userDto);
//                DaZoneApplication.getInstance().getmPrefs().putPassword(password);
//                DaZoneApplication.getInstance().getmPrefs().putUserName(userDto.userID);
//                DaZoneApplication.getInstance().getmPrefs().putBooleanValue(Statics.IS_CHECK_ALLOW, true);
//                baseHTTPCallBack.onHTTPSuccess();
                HttpRequest.getInstance().CheckUserPermissionType(baseHTTPCallBack, companyDomain, password, response, "", "", null, userDto.PermissionType);

            }

            @Override
            public void onFailure(ErrorDto error) {
                baseHTTPCallBack.onHTTPFail(error);
            }
        });
    }

    public void signUp(final BaseHTTPCallBackWithString baseHTTPCallBack, final String email) {
        final String url = Urls.URL_SIGN_UP;
        Map<String, String> params = new HashMap<>();
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("mailAddress", "" + email);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                MessageDto messageDto = gson.fromJson(response, MessageDto.class);

                if (baseHTTPCallBack != null && messageDto != null) {
                    String message = messageDto.getMessage();
                    baseHTTPCallBack.onHTTPSuccess(message);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (baseHTTPCallBack != null) {
                    baseHTTPCallBack.onHTTPFail(error);
                }
            }
        });
    }

    public void checkLogin(final BaseHTTPCallBack baseHTTPCallBack) {
        final String url = root_link + Urls.URL_CHECK_SESSION;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {


                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(response, UserDto.class);
                HttpRequest.getInstance().CheckUserPermissionType(baseHTTPCallBack, "", "", "", response, "", null, userDto.PermissionType);
//                userDto.prefs.putAccessToken(userDto.session);
//                userDto.prefs.putIsAdmin(userDto.PermissionType);
//                userDto.prefs.putCompanyNo(userDto.CompanyNo);
//                new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
//                userDto.prefs.putUserNo(userDto.Id);
//                userDto.prefs.putUserName(userDto.userID);
//                UserDBHelper.addUser(userDto);
//                DaZoneApplication.getInstance().getmPrefs().putUserName(userDto.userID);
//                if (baseHTTPCallBack != null) {
//                    baseHTTPCallBack.onHTTPSuccess();
//                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (baseHTTPCallBack != null) {
                    baseHTTPCallBack.onHTTPFail(error);
                }
            }
        });
    }

    public void checkApplication(final OnHasAppCallBack callBack) {
        final String url = root_link + Urls.URL_HAS_APPLICATION;
        Map<String, String> params = new HashMap<>();
        String projectCode = "WorkingTime";

        params.put("projectCode", projectCode);
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (callBack != null) {
                        if (json.getBoolean("HasApplication")) {
                            callBack.hasApp();
                        } else {
                            ErrorDto errorDto = new ErrorDto();
                            errorDto.message = json.getString("Message");
                            callBack.noHas(errorDto);
                        }
                    }
                } catch (Exception e) {
                    callBack.noHas(new ErrorDto());
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                callBack.noHas(error);
            }
        });
    }

    public void getUpdateUserInfo(final OnGetUserInfoCallBack infoCallBack) {
        final String url = root_link + Urls.URL_CHECK_SESSION;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(response, UserDto.class);
                userDto.prefs.putAccessToken(userDto.session);
                userDto.prefs.putIsAdmin(userDto.PermissionType);
                userDto.prefs.putCompanyNo(userDto.CompanyNo);
                UserDBHelper.addUser(userDto);

                if (infoCallBack != null) {
                    infoCallBack.onSuccess();
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (infoCallBack != null) {
                    infoCallBack.onFail(error);
                }
            }
        });
    }

    public void logout(final BaseHTTPCallBack baseHTTPCallBack) {
        final String url = root_link + Urls.URL_LOG_OUT;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                DaZoneApplication.getInstance().getmPrefs().putServerSite("");
//                DaZoneApplication.getInstance().getmPrefs().putUserName("");
                baseHTTPCallBack.onHTTPSuccess();
            }

            @Override
            public void onFailure(ErrorDto error) {
                baseHTTPCallBack.onHTTPFail(error);
            }
        });
    }

    public void checkStatus(final OnGetStatusResponse onGetStatusResponse) {
        final String url = root_link + Urls.URL_GET_STATUS;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("TimeOffset", "" + Util.getTimeOffsetInHour());
        params.put("languagecode", Util.getPhoneLanguage());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int type = jsonObject.getInt("Type");
                    onGetStatusResponse.onGetStatusResponseSuccess(type);
                } catch (JSONException e) {
                    ErrorDto error = new ErrorDto();
                    error.message = "Data error";
                    onGetStatusResponse.onGetStatusResponseError(error);

                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                new Prefs().putStringValue("companyID", "");
                new Prefs().putStringValue("userID", "");
                onGetStatusResponse.onGetStatusResponseError(error);
            }
        });
    }

    public void insertTimeCards(final BaseHTTPCallBack baseHTTPCallBack, double lat, double lng, String remark, int type, String distance, double latcompany, double lngcompany, int locationNo) {
        Log.d(TAG, "distance:" + distance);

        String url = root_link + Urls.URL_GET_INSERT_TIME_CARDS;
        Map<String, String> params = new HashMap<>();

        params.put("lat", "" + lat);
        params.put("lng", "" + lng);
        params.put("remark", remark);
        params.put("type", "" + type);
        params.put("distance", "" + distance);
        params.put("latcompany", "" + latcompany);
        params.put("lngcompany", "" + lngcompany);
        params.put("locationNo", "" + locationNo);
        params.put("TimeOffset", "" + Util.getTimeZone());
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());

//        Log.d(TAG,"insertTimeCards:"+new Gson().toJson(params));

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                baseHTTPCallBack.onHTTPSuccess();
            }

            @Override
            public void onFailure(ErrorDto error) {
                baseHTTPCallBack.onHTTPFail(error);
            }
        });
    }

    public void checkAllowDevice(final OnCheckAllowDevice onCheckAllowDevice) {
        String url = root_link + Urls.URL_GET_ALLOW_DEVICE;
        Map<String, String> params = new HashMap<>();

        params.put("sessionId", DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Type listType = new TypeToken<AllowDevices>() {
                }.getType();
                AllowDevices allowDevices = new Gson().fromJson(response, listType);
                onCheckAllowDevice.onGetMyListSuccess(allowDevices);
            }

            @Override
            public void onFailure(ErrorDto error) {
                onCheckAllowDevice.onGetMyListFail(error);
            }
        });
    }

    public void getMyList(final OnGetMyListHTTPCallBack onGetMyListHTTPCallBack, long milis) {
        String url = root_link + Urls.URL_GET_MY_LIST;
        Map<String, String> params = new HashMap<>();
        params.put("LongMilliseconds", "" + (milis + Util.getTimeOffsetInMilis()));
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("TimeOffset", Util.getTimeZone());
        params.put("Lang", Util.getPhoneLanguage());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Type listType = new TypeToken<List<MyListDto>>() {
                }.getType();
                List<MyListDto> myListDtos = new Gson().fromJson(response, listType);
                onGetMyListHTTPCallBack.onGetMyListSuccess(myListDtos);
            }

            @Override
            public void onFailure(ErrorDto error) {
                onGetMyListHTTPCallBack.onGetMyListFail(error);
            }
        });
    }

    public void getUserList(final OnGetMyListHTTPCallBack onGetMyListHTTPCallBack, long time, String userNo) {
        String url = root_link + Urls.URL_GET_USER_LIST;
        Map<String, String> params = new HashMap<>();
        params.put("LongMilliseconds", "" + (time + Util.getTimeOffsetInMilis()));
        params.put("userNo", userNo);
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("TimeOffset", Util.getTimeZone());
        params.put("Lang", Util.getPhoneLanguage());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Type listType = new TypeToken<List<MyListDto>>() {
                }.getType();
                List<MyListDto> myListDtos = new Gson().fromJson(response, listType);

                onGetMyListHTTPCallBack.onGetMyListSuccess(myListDtos);
            }

            @Override
            public void onFailure(ErrorDto error) {
                onGetMyListHTTPCallBack.onGetMyListFail(error);
            }
        });
    }

    public void getAllEmployeesSort(final OnGetAllEmpHTTPCallBack onGetAllEmpHTTPCallBack, long time, int limit, String UserNoBegin, int SortType, int GroupNo) {
        String url = root_link + Urls.URL_GET_ALL_EMPLOYEES_SORT;
        Map<String, String> params = new HashMap<>();
        params.put("LongMilliseconds", "" + (time + Util.getTimeOffsetInMilis()));
        params.put("litmit", "" + limit);
        params.put("SortType", "" + SortType);
        params.put("UserNoBegin", UserNoBegin);
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("TimeOffset", Util.getTimeZone());
        params.put("Lang", Util.getPhoneLanguage());
        params.put("GroupNo", "" + GroupNo);

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Type listType = new TypeToken<List<AllEmployeeDto>>() {
                }.getType();
                List<AllEmployeeDto> myListDtos = new Gson().fromJson(response, listType);

                onGetAllEmpHTTPCallBack.onGetAllEmpSuccess(myListDtos);
            }

            @Override
            public void onFailure(ErrorDto error) {
                onGetAllEmpHTTPCallBack.onGetAllEmpFail(error);
            }
        });
    }

    public void getAllEmployeesStatusNew(final OnGetAllEmpHTTPCallBack onGetAllEmpHTTPCallBack, long time, int limit, String UserNoBegin, int type1, int type2, int type3, int type4, int GroupNo) {
        String url = root_link + Urls.URL_GET_ALL_EMPLOYEES_STATUS_NEW;
        Map<String, String> params = new HashMap<>();
        params.put("LongMilliseconds", "" + (time + Util.getTimeOffsetInMilis()));
        params.put("litmit", "" + limit);
        params.put("type1", "" + type1);
        params.put("type2", "" + type2);
        params.put("type3", "" + type3);
        params.put("type4", "" + type4);
        params.put("UserNoBegin", UserNoBegin);
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("TimeOffset", Util.getTimeZone());
        params.put("Lang", Util.getPhoneLanguage());
        params.put("GroupNo", "" + GroupNo);

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Type listType = new TypeToken<List<AllEmployeeDto>>() {
                }.getType();
                List<AllEmployeeDto> myListDtos = new Gson().fromJson(response, listType);

                onGetAllEmpHTTPCallBack.onGetAllEmpSuccess(myListDtos);
            }

            @Override
            public void onFailure(ErrorDto error) {
                onGetAllEmpHTTPCallBack.onGetAllEmpFail(error);
            }
        });
    }

    public void getStatusMessage(final OnCallGetMessageValue onCallGetMessageValue, int type) {
        String url = root_link + Urls.URL_MESSAGE_STATUS_LOCAL;
        Map<String, String> params = new HashMap<>();
        params.put("type", "" + type);
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languagecode", "" + Locale.getDefault().getLanguage().toUpperCase());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    onCallGetMessageValue.onResponseMessageSuccess(jsonObject.getString("Message"));
                } catch (JSONException e) {
                    ErrorDto errorDto = new ErrorDto();
                    errorDto.message = Util.getString(R.string.no_network_error);
                    onCallGetMessageValue.onResponseMessageError(errorDto);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                onCallGetMessageValue.onResponseMessageError(error);
            }
        });
    }

    public void getSettingUser(final OnGetUserInfoResponse onGetUserInforResponse) {
        final String url = root_link + Urls.URL_SETTING_USER;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                UserSettingDto userDto = gson.fromJson(response, UserSettingDto.class);
                onGetUserInforResponse.onGetUserInfoResponseSuccess(userDto);
            }

            @Override
            public void onFailure(ErrorDto error) {
                onGetUserInforResponse.onGetUserInfoResponseError(error);
            }
        });
    }

    public void getServerTime(final GetServerTimeCallBack callback) {
        final String url = root_link + Urls.URL_SERVER_TIME;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        WebServiceManager webServiceManager = new WebServiceManager();
        Log.d(TAG, "params:" + new Gson().toJson(params));
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "response:" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    long timeOffset = jsonObject.getLong("Offset");
                    long timeServer = jsonObject.getLong("LongTime");
//                    if (timeOffset == 99) {
//                        timeOffset = timeServer - new Date().getTime();
//                    }

//                    String TAG="getServerTime";
//                    Log.d(TAG,"timeOffset:"+timeOffset);
//                    Log.d(TAG,"timeServer:"+timeServer);
//                    Log.d(TAG, "putTimeOffset 1 :" + timeOffset);
                    DaZoneApplication.getInstance().getmPrefs().putTimeOffset(timeOffset);
                    DaZoneApplication.getInstance().getmPrefs().putServerTime(timeServer);

                    callback.onSuccess();
                } catch (JSONException e) {
                    callback.onFail();
//                    Log.d(TAG, "putTimeOffset 2 : 0");
//                    DaZoneApplication.getInstance().getmPrefs().putTimeOffset(0);
//                    DaZoneApplication.getInstance().getmPrefs().putServerTime(0);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                callback.onFail();
//                DaZoneApplication.getInstance().getmPrefs().putServerTime(0);
//                Log.d(TAG, "putTimeOffset 3 : 0");
//                DaZoneApplication.getInstance().getmPrefs().putTimeOffset(0);
            }
        });
    }

    public void GetUser(int userNo, final OnGetUserCallback callBack) {
        String url = root_link + Urls.URL_GET_USER;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        params.put("userNo", userNo + "");
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {

                Util.printLogs("Get user info = " + response);

                ProfileUserDTO profileUserDTO = new Gson().fromJson(response, ProfileUserDTO.class);
                if (callBack != null)
                    callBack.onHTTPSuccess(profileUserDTO);
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null)
                    callBack.onHTTPFail(error);
            }
        });
    }

    public void getUserInfo(final DataHttpCallBack dataHttpCallBack) {
        final String url = root_link + Urls.URL_GET_USER_INFO;
        Map<String, String> params = new HashMap<>();
        params.put("languagecode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {

                Util.printLogs("User info json =" + response);

                Gson gson = new Gson();
                DataDto dataDto = gson.fromJson(response, UserInfoDto.class);
                if (dataHttpCallBack != null) {
                    dataHttpCallBack.onDataHTTPSuccess(dataDto);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (dataHttpCallBack != null) {
                    dataHttpCallBack.onDataHTTPFail(error);
                }
            }
        });
    }

    public void saveSettingOffice(final BaseHTTPCallBack callBack, UserSettingDto.CompanyInfo companyInfo) {
        String url = root_link + Urls.URL_SAVE_SETTING_OFFICE;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("LocationNo", companyInfo.LocationNo + "");
        params.put("Name", companyInfo.nameoffice + "");
        params.put("Lat", companyInfo.lat + "");
        params.put("Lng", companyInfo.lng + "");
        params.put("ErrorRange", companyInfo.PermissibleRange + "");
        params.put("Desc", companyInfo.description + "");
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                if (callBack != null) {
                    callBack.onHTTPSuccess();
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.onHTTPFail(error);
                }
            }
        });
    }

    public void deleteSettingOffice(final BaseHTTPCallBack callBack, UserSettingDto.CompanyInfo companyInfo) {
        String url = root_link + Urls.URL_DELETE_SETTING_OFFICE;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("LocationNo", companyInfo.LocationNo + "");
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                if (callBack != null) {
                    callBack.onHTTPSuccess();
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.onHTTPFail(error);
                }
            }
        });
    }


    /**
     * BEACON
     */
    public void InsertBeacon(UserSettingDto.CompanyInfo companyInfo, String uuid, int Major, int Minor, final OnInsertBeaconCallBack callBack) {
        String url = root_link + Urls.URL_INSERT_BEACON;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        params.put("locationNo", companyInfo.LocationNo + "");
        params.put("beaconUUID", uuid + "");
        params.put("beaconMajor", Major + "");
        params.put("beaconMinor", Minor + "");

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                if (callBack != null) {
                    callBack.OnInsertBeaconSuccess(response);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.OnInsertBeaconFail(error);
                }
            }
        });
    }

    public void UpdateBeacon(BeaconDTO beaconDTO, final OnUpdateBeaconCallBack callBack) {
        String url = root_link + Urls.URL_UPDATE_BEACON;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        params.put("pointNo", beaconDTO.getPointNo() + "");
        params.put("locationNo", beaconDTO.getLocationNo() + "");
        params.put("beaconUUID", beaconDTO.getBeaconUUID() + "");
        params.put("beaconMajor", beaconDTO.getBeaconMajor() + "");
        params.put("beaconMinor", beaconDTO.getBeaconMinor() + "");

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                if (callBack != null) {
                    callBack.OnUpdateBeaconSuccess(response);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.OnUpdateBeaconFail(error);
                }
            }
        });
    }

    public void GetBeaconPointByLocation(int LocationNo, final OnGetBeaconByLocationCallBack callBack) {
        String url = root_link + Urls.URL_GET_BEACON_POINT_BY_LOCATION;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        params.put("locationNo", LocationNo + "");
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Type listType = new TypeToken<BeaconDTO>() {
                }.getType();
                BeaconDTO beaconDTO = new Gson().fromJson(response, listType);
                if (callBack != null) {
                    callBack.OnGetBeaconByLocationCallBackSuccess(beaconDTO);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.OnGetBeaconByLocationCallBackFail(error);
                }
            }
        });
    }

    public void DeleteBeacon(int pointNo, final OnDeleteBeaconCallBack callBack) {
        String url = root_link + Urls.URL_DELETE_BEACON_POINT;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        params.put("pointNo", pointNo + "");
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                if (callBack != null) {
                    callBack.OnDeleteBeaconSuccess(response);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.OnDeleteBeaconFail(error);
                }
            }
        });
    }

    public void GetBeacons(final OnGetBeaconsCallBack callBack) {
        String url = root_link + Urls.URL_GET_BEACON_POINTS;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Type listType = new TypeToken<ArrayList<BeaconDTO>>() {
                }.getType();
                ArrayList<BeaconDTO> beaconDTOs = new Gson().fromJson(response, listType);
                if (callBack != null) {
                    callBack.OnGetBeaconsSuccess(beaconDTOs);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.OnGetBeaconsFail(error);
                }
            }
        });
    }

    public void InsertTimeCardsForBeacon(double lat, double lng, String remark, int type, String distance, long checkTime, double latcompany, double lngcompany, String beaconInfo, final OnInsertTimeCardForBeaconCallBack callBack) {
        String url = root_link + Urls.URL_INSERT_TIME_CARD_FOR_BEACON;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("lat", "" + lat);
        params.put("lng", "" + lng);
        params.put("remark", remark);
        params.put("type", "" + type);
        params.put("distance", "" + distance);
        params.put("latcompany", "" + latcompany);
        params.put("lngcompany", "" + lngcompany);
        params.put("CheckTime", "" + checkTime);
        params.put("TimeOffset", "" + Util.getTimeZone());
        params.put("TimeOffset", "" + Util.getTimeZone());
        params.put("beaconInfo", "" + beaconInfo);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                callBack.OnInsertTimeCardForBeaconSuccess(response);
            }

            @Override
            public void onFailure(ErrorDto error) {
                callBack.OnInsertTimeCardForBeaconFail(error);
            }
        });
    }


    /**
     * AUTO LOGIN
     */
    public void AutoLogin(String companyDomain, String userID, String server_link, final OnAutoLoginCallBack callBack) {
        if (!server_link.startsWith("http://")) server_link = "http://" + server_link;

        final String url = server_link + Urls.URL_AUTO_LOGIN;
        Log.d(TAG, "AutoLogin url:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        params.put("companyDomain", companyDomain);
        params.put("userID", userID);
        params.put("mobileOSVersion", "Android " + android.os.Build.VERSION.RELEASE);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("AutoLogin", "response:" + response);

                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(response, UserDto.class);
                HttpRequest.getInstance().CheckUserPermissionType(null, "", "", "", "", response, callBack, userDto.PermissionType);
//                userDto.prefs.putAccessToken(userDto.session);
//                userDto.prefs.putIsAdmin(userDto.PermissionType);
//                userDto.prefs.putCompanyNo(userDto.CompanyNo);
//                new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
//                userDto.prefs.putUserNo(userDto.Id);
//                UserDBHelper.addUser(userDto);
//                callBack.OnAutoLoginSuccess(response);
            }

            @Override
            public void onFailure(ErrorDto error) {
                Log.d("AutoLogin", "error");
                callBack.OnAutoLoginFail(error);
            }
        });
    }

    /**
     * CHANGE PASSWORD
     */
    public void ChangePassword(String originalPassword, final String newPassword, final OnChangePasswordCallBack callBack) {
        String url = root_link + Urls.URL_CHANGE_PASSWORD;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        params.put("originalPassword", originalPassword);
        params.put("newPassword", newPassword);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String newSessionID = json.getString("newSessionID");
                    DaZoneApplication.getInstance().getmPrefs().putAccessToken(newSessionID);
                    if (callBack != null) {
                        callBack.onSuccess(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ErrorDto errorDto = new ErrorDto();
                    errorDto.message = e.toString();
                    callBack.onFail(errorDto);
                }

            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.onFail(error);
                }
            }
        });
    }

    public void handlerCheckAllowDevice(final BaseHTTPCallBack baseHTTPCallBack, final String companyDomain, final String password,
                                        final String strLogin, final String checkLogin, final String AutoLogin, final OnAutoLoginCallBack callBack, String response) {
        Log.d(TAG, "CheckUserPermissionType response:" + response);

        if (strLogin.trim().length() > 0) {
            Log.d(TAG, "strLogin");
            Gson gson = new Gson();
            UserDto userDto = gson.fromJson(strLogin, UserDto.class);
            userDto.prefs.putAccessToken(userDto.session);

            int PermissionType = 0;
            if (response.trim().length() > 0) {
                try {
                    PermissionType = Integer.parseInt(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            userDto.prefs.putIsAdmin(PermissionType);
//                userDto.prefs.putIsAdmin(userDto.PermissionType);
            userDto.prefs.putCompanyNo(userDto.CompanyNo);
            new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
            userDto.prefs.putUserNo(userDto.Id);
            userDto.prefs.putUserName(userDto.userID);
            userDto.prefs.putDomain(companyDomain);
            UserDBHelper.addUser(userDto);
            DaZoneApplication.getInstance().getmPrefs().putPassword(password);
            DaZoneApplication.getInstance().getmPrefs().putUserName(userDto.userID);
            DaZoneApplication.getInstance().getmPrefs().putBooleanValue(Statics.IS_CHECK_ALLOW, true);

            if (baseHTTPCallBack != null) {
                baseHTTPCallBack.onHTTPSuccess();
            }
        } else if (checkLogin.trim().length() > 0) {
            Log.d(TAG, "checkLogin");
            Gson gson = new Gson();
            UserDto userDto = gson.fromJson(checkLogin, UserDto.class);
            userDto.prefs.putAccessToken(userDto.session);

            int PermissionType = 0;
            if (response.trim().length() > 0) {
                try {
                    PermissionType = Integer.parseInt(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            userDto.prefs.putIsAdmin(PermissionType);
//                    userDto.prefs.putIsAdmin(userDto.PermissionType);
            userDto.prefs.putCompanyNo(userDto.CompanyNo);
            new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
            userDto.prefs.putUserNo(userDto.Id);
            userDto.prefs.putUserName(userDto.userID);
            UserDBHelper.addUser(userDto);
            DaZoneApplication.getInstance().getmPrefs().putUserName(userDto.userID);
            if (baseHTTPCallBack != null) {
                baseHTTPCallBack.onHTTPSuccess();
            }
        } else if (AutoLogin.trim().length() > 0) {
            Log.d(TAG, "AutoLogin");
            Gson gson = new Gson();
            UserDto userDto = gson.fromJson(AutoLogin, UserDto.class);
            userDto.prefs.putAccessToken(userDto.session);

            int PermissionType = 0;
            if (response.trim().length() > 0) {
                try {
                    PermissionType = Integer.parseInt(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            userDto.prefs.putIsAdmin(PermissionType);
//                    userDto.prefs.putIsAdmin(userDto.PermissionType);
            userDto.prefs.putCompanyNo(userDto.CompanyNo);
            new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
            userDto.prefs.putUserNo(userDto.Id);
            UserDBHelper.addUser(userDto);
            if (callBack != null) callBack.OnAutoLoginSuccess(response);
        } else {
            DaZoneApplication.getInstance().getmPrefs().putAccessToken("");
            if (AutoLogin.trim().length() > 0) {
                callBack.OnAutoLoginFail(new ErrorDto());
            } else {
                if (baseHTTPCallBack != null) baseHTTPCallBack.onHTTPFail(new ErrorDto());
            }
        }
    }

    public void CheckUserPermissionType(final BaseHTTPCallBack baseHTTPCallBack, final String companyDomain, final String password,
                                        final String strLogin, final String checkLogin, final String AutoLogin, final OnAutoLoginCallBack callBack, final int permissionTypeTemp) {
        final String url = root_link + Urls.CheckUserPermissionType;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        WebServiceManager webServiceManager = new WebServiceManager();

        Log.d(TAG, "url:" + url);
        Log.d(TAG, "CheckUserPermissionType params" + new Gson().toJson(params));

        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "CheckUserPermissionType response:" + response);
                CheckAllowDevice(baseHTTPCallBack, companyDomain, password,
                        strLogin, checkLogin, AutoLogin, callBack, response);
//                if (strLogin.trim().length() > 0) {
//                    Log.d(TAG, "strLogin");
//                    Gson gson = new Gson();
//                    UserDto userDto = gson.fromJson(strLogin, UserDto.class);
//                    userDto.prefs.putAccessToken(userDto.session);
//
//                    int PermissionType = 0;
//                    if (response.trim().length() > 0) {
//                        try {
//                            PermissionType = Integer.parseInt(response);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    userDto.prefs.putIsAdmin(PermissionType);
////                userDto.prefs.putIsAdmin(userDto.PermissionType);
//                    userDto.prefs.putCompanyNo(userDto.CompanyNo);
//                    new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
//                    userDto.prefs.putUserNo(userDto.Id);
//                    userDto.prefs.putUserName(userDto.userID);
//                    userDto.prefs.putDomain(companyDomain);
//                    UserDBHelper.addUser(userDto);
//                    DaZoneApplication.getInstance().getmPrefs().putPassword(password);
//                    DaZoneApplication.getInstance().getmPrefs().putUserName(userDto.userID);
//                    DaZoneApplication.getInstance().getmPrefs().putBooleanValue(Statics.IS_CHECK_ALLOW, true);
//
//                    if (baseHTTPCallBack != null) {
//                        baseHTTPCallBack.onHTTPSuccess();
//                    }
//                } else if (checkLogin.trim().length() > 0) {
//                    Log.d(TAG, "checkLogin");
//                    Gson gson = new Gson();
//                    UserDto userDto = gson.fromJson(checkLogin, UserDto.class);
//                    userDto.prefs.putAccessToken(userDto.session);
//
//                    int PermissionType = 0;
//                    if (response.trim().length() > 0) {
//                        try {
//                            PermissionType = Integer.parseInt(response);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    userDto.prefs.putIsAdmin(PermissionType);
////                    userDto.prefs.putIsAdmin(userDto.PermissionType);
//                    userDto.prefs.putCompanyNo(userDto.CompanyNo);
//                    new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
//                    userDto.prefs.putUserNo(userDto.Id);
//                    userDto.prefs.putUserName(userDto.userID);
//                    UserDBHelper.addUser(userDto);
//                    DaZoneApplication.getInstance().getmPrefs().putUserName(userDto.userID);
//                    if (baseHTTPCallBack != null) {
//                        baseHTTPCallBack.onHTTPSuccess();
//                    }
//                } else if (AutoLogin.trim().length() > 0) {
//                    Log.d(TAG, "AutoLogin");
//                    Gson gson = new Gson();
//                    UserDto userDto = gson.fromJson(AutoLogin, UserDto.class);
//                    userDto.prefs.putAccessToken(userDto.session);
//
//                    int PermissionType = 0;
//                    if (response.trim().length() > 0) {
//                        try {
//                            PermissionType = Integer.parseInt(response);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    userDto.prefs.putIsAdmin(PermissionType);
////                    userDto.prefs.putIsAdmin(userDto.PermissionType);
//                    userDto.prefs.putCompanyNo(userDto.CompanyNo);
//                    new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userDto.NameCompany);
//                    userDto.prefs.putUserNo(userDto.Id);
//                    UserDBHelper.addUser(userDto);
//                    if (callBack != null) callBack.OnAutoLoginSuccess(response);
//                } else {
//                    DaZoneApplication.getInstance().getmPrefs().putAccessToken("");
//                    if (AutoLogin.trim().length() > 0) {
//                        callBack.OnAutoLoginFail(new ErrorDto());
//                    } else {
//                        if (baseHTTPCallBack != null) baseHTTPCallBack.onHTTPFail(new ErrorDto());
//                    }
//                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                Log.d(TAG, "CheckUserPermissionType error:" + new Gson().toJson(error));
                if (error.serverError) {
                    // no api continue process
                    Log.d(TAG, "no API CheckUserPermissionType");
                    CheckAllowDevice(baseHTTPCallBack, companyDomain, password,
                            strLogin, checkLogin, AutoLogin, callBack, "" + permissionTypeTemp);
                } else {
                    DaZoneApplication.getInstance().getmPrefs().putAccessToken("");
                    if (AutoLogin.trim().length() > 0) {
                        callBack.OnAutoLoginFail(error);
                    } else {
                        if (baseHTTPCallBack != null) baseHTTPCallBack.onHTTPFail(error);
                    }
                }
            }
        });
    }

    public class ExportDepartmentList extends AsyncTask<String, String, ArrayList<TreeUserDTO>> {
        String response;
        IGetListDepart callBack;

        public ExportDepartmentList(String response, IGetListDepart callBack) {
            this.response = response;
            this.callBack = callBack;
        }

        @Override
        protected ArrayList<TreeUserDTO> doInBackground(String... params) {
            Type listType = new TypeToken<List<TreeUserDTO>>() {
            }.getType();
            ArrayList<TreeUserDTO> list = new Gson().fromJson(response, listType);
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<TreeUserDTO> list) {
            super.onPostExecute(list);
            if (callBack != null)
                callBack.onGetListDepartSuccess(list);
        }
    }

    public void GetListDepart(final IGetListDepart iGetListDepart) {

        String url = root_link + Urls.URL_GET_DEPARTMENT;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(final String response) {
//                Type listType = new TypeToken<List<TreeUserDTO>>() {
//                }.getType();
//                ArrayList<TreeUserDTO> list = new Gson().fromJson(response, listType);
//                if (iGetListDepart != null)
//                    iGetListDepart.onGetListDepartSuccess(list);
                new ExportDepartmentList(response, iGetListDepart).execute();
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (iGetListDepart != null)
                    iGetListDepart.onGetListDepartFail(error);
            }
        });
    }

    public void CheckAllowDevice(final BaseHTTPCallBack baseHTTPCallBack, final String companyDomain, final String password,
                                 final String strLogin, final String checkLogin, final String AutoLogin, final OnAutoLoginCallBack callBack, final String response) {
        String url = root_link + Urls.CheckAllowDevice;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + DaZoneApplication.getInstance().getmPrefs().getAccessToken());
        String deviceId = Constant.getIMEI(DaZoneApplication.getInstance());
        Log.d(TAG, "deviceId:" + deviceId);
        params.put("deviceId", deviceId);

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(final String s) {
                Log.d(TAG, "CheckAllowDevice:" + s);
                if (s.trim().equals("0")) {
                    // 0 : dont enable login
                    // 1 & 2 : enbale login
                    // 2 -> first login
                    // 1 -> count login n
                    // return
                    ErrorDto errorDto = new ErrorDto();
                    errorDto.message = "Your account can't login on this device.";
                    if (baseHTTPCallBack != null) baseHTTPCallBack.onHTTPFail(errorDto);
                } else {
                    // continue
                    handlerCheckAllowDevice(baseHTTPCallBack, companyDomain, password, strLogin, checkLogin, AutoLogin, callBack, response);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                Log.d(TAG, "CheckAllowDevice error");
                if (error.serverError) {
                    // no api -> continue process
                    Log.d(TAG,"CheckAllowDevice not exist");
                    handlerCheckAllowDevice(baseHTTPCallBack, companyDomain, password, strLogin, checkLogin, AutoLogin, callBack, response);
                } else {
                    if (baseHTTPCallBack != null) baseHTTPCallBack.onHTTPFail(new ErrorDto());
                }

            }
        });
    }
}