package timecard.dazone.com.dazonetimecard.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import timecard.dazone.com.dazonetimecard.BuildConfig;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;

/**
 * Created by LongTran on 12/26/2016.
 */

public class Constant {
    public static String TAG = "Constant";
    public static final String IMAGE_DIRECTORY_NAME = "CrewTimeCard";

    public static final String DATE_FORMAT_PICTURE = "yyyyMMdd_HHmmss";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * REQUEST CODE
     */
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    public static final int REQUEST_CODE_CAPTURE_VIDEO = 2;
    public static final int REQUEST_CODE_SELECT_FILE = 3;

    /**
     * FILE TYPE
     */
    public static final String IMAGE_JPG = ".jpg";
    public static final String IMAGE_JPEG = ".jpeg";
    public static final String IMAGE_PNG = ".png";
    public static final String IMAGE_GIF = ".gif";
    public static final String AUDIO_MP3 = ".mp3";
    public static final String AUDIO_WMA = ".wma";
    public static final String AUDIO_AMR = ".amr";
    public static final String VIDEO_MP4 = ".mp4";
    public static final String FILE_PDF = ".pdf";
    public static final String FILE_DOCX = ".docx";
    public static final String FILE_DOC = ".doc";
    public static final String FILE_XLS = ".xls";
    public static final String FILE_XLSX = ".xlsx";
    public static final String FILE_PPTX = ".pptx";
    public static final String FILE_PPT = ".ppt";
    public static final String FILE = "drawable://";

//    public static boolean isMockSettingsON(Context context) {
//        // returns true if mock location enabled, false if not enabled.
//        try {
//            if (Settings.Secure.getString(context.getContentResolver(),
//                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
//                return false;
//            else
//                return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public static TreeUserDTO buildTree(List<TreeUserDTO> dtos) throws Exception {
        //boolean temp = true;
        TreeUserDTO root = new TreeUserDTO(0, 0, "Dazone");
        HashMap<Integer, ArrayList<TreeUserDTO>> jsonDataMap = new HashMap<>();
        //HashMap<Integer, ArrayList<TreeUserDTO>> jsonDataMap2 = new HashMap<>();
        for (TreeUserDTO dto : dtos) {
            if (jsonDataMap.containsKey(dto.getParent())) {
                jsonDataMap.get(dto.getParent()).add(dto);
            } else {
                ArrayList<TreeUserDTO> subordinates = new ArrayList<>();
                subordinates.add(dto);
                jsonDataMap.put(dto.getParent(), subordinates);
            }
        }

        if (jsonDataMap.get(root.getId()) != null) {
            for (TreeUserDTO subordinate : jsonDataMap.get(root.getId())) {
                root.addSubordinate(subordinate);
                subordinate.setParent(root.getId());
                // Sort by name
                buildSubTree(subordinate, jsonDataMap);
            }
        }

        return root;
    }

    public static void buildSubTree(TreeUserDTO parent, HashMap<Integer, ArrayList<TreeUserDTO>> jsonDataMap) {
        List<TreeUserDTO> subordinates = jsonDataMap.get(parent.getId());
        if (subordinates != null) {
            for (TreeUserDTO subordinate : subordinates) {
                subordinate.setParent(parent.getParent());

                parent.addSubordinate(subordinate);
                if (subordinate.getType() == 0) {
                    buildSubTree(subordinate, jsonDataMap);
                }
            }
        }
    }

    public static String get_department_name(TreeUserDTO index, List<TreeUserDTO> listTemp_3) {
        int n = listTemp_3.size();
        if (n == 0) return "";
        for (int i = n - 1; i >= 0; i--) {
            TreeUserDTO obj = listTemp_3.get(i);
            if (obj != null) {
                if (obj.getLevel() < index.getLevel())
                    return obj.getName();
            } else {
                return "";
            }
        }
        return "";
    }

    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isAllowMockLocationsOn(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            if (Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
                return false;
            else {
                return true;
            }
        } else {
            return false;
        }
    }


    public static boolean checkForAllowMockLocationsApps(Context context) {

        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Got exception " + e.getMessage());
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLocationFromMockProvider(Context context, Location location) {
        boolean isMock = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                isMock = (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(), BuildConfig.APPLICATION_ID) == AppOpsManager.MODE_ALLOWED);
            } else if (android.os.Build.VERSION.SDK_INT >= 18) {
                isMock = location.isFromMockProvider();
            } else {
                //isMock = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
                if (Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
                    return false;
                else {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return isMock;
    }

}
