package timecard.dazone.com.dazonetimecard.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dazone on 3/13/2017.
 */

public class AllowDevices {

    @SerializedName("AllowDeviceNo")
    public int AllowDeviceNo;

    @SerializedName("DepartNo")
    public int DepartNo;

    @SerializedName("UserNo")
    public int UserNo;

    @SerializedName("ContentAllow")
    public String ContentAllow;

    @SerializedName("RegDate")
    public String RegDate;

    @SerializedName("ModDate")
    public String ModDate;

    public AllowDevices() {
    }

    public int getAllowDeviceNo() {
        return AllowDeviceNo;
    }

    public void setAllowDeviceNo(int allowDeviceNo) {
        AllowDeviceNo = allowDeviceNo;
    }

    public int getDepartNo() {
        return DepartNo;
    }

    public void setDepartNo(int departNo) {
        DepartNo = departNo;
    }

    public int getUserNo() {
        return UserNo;
    }

    public void setUserNo(int userNo) {
        UserNo = userNo;
    }

    public String getContentAllow() {
        return ContentAllow;
    }

    public void setContentAllow(String contentAllow) {
        ContentAllow = contentAllow;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getModDate() {
        return ModDate;
    }

    public void setModDate(String modDate) {
        ModDate = modDate;
    }
}
