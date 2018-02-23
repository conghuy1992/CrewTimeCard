package timecard.dazone.com.dazonetimecard.dtos;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.Prefs;

public class UserDto {
    public String CellPhone = "";

    public String ExtensionNumber = "";

    public int Id;

    public int CompanyNo;

    public int PermissionType;//0 normal, 1 admin

    public String userID;

    public String FullName = "";

    public String MailAddress = "";

    public String session;

    public String avatar;

    public String NameCompany = "";

    public ArrayList<CompanyDto> informationcompany;

    public Prefs prefs = DaZoneApplication.getInstance().getmPrefs();

    public UserDto() {
        prefs = DaZoneApplication.getInstance().getmPrefs();
    }


}
