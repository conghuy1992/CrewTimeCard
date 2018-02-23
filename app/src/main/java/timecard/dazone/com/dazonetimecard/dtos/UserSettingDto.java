package timecard.dazone.com.dazonetimecard.dtos;

import java.util.ArrayList;
import java.util.List;

public class UserSettingDto {
    public String CheckInTime = "";
    public String CheckOutTime = "";
    public String StartLunchTime = "";
    public String EndLunchTime = "";
    public int CheckLunch = 0;
    public List<CompanyInfo> offices = new ArrayList<>();
    public User user;

    public static class CompanyInfo {

        public int LocationNo = 0;
        public String nameoffice;
        public String description;
        public double lat;
        public double lng;
        public int PermissibleRange = 0;
    }
    public class User {
        public String Name = "";
        public String NameEN = "";
        public String PositionName = "";
        public String PositionNameEN = "";
    }
}
