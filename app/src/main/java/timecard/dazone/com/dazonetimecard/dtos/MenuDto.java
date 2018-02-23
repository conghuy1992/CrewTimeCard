package timecard.dazone.com.dazonetimecard.dtos;

import timecard.dazone.com.dazonetimecard.utils.Util;

public class MenuDto {
    public String mTitle;
    public int mIcon;
    public boolean isHide = false;
    public MenuDto() {
    }
    public MenuDto(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }
    public MenuDto(int titleID, int icon) {
        mTitle = Util.getString(titleID);
        mIcon = icon;
    }
    public MenuDto(int titleID, int icon, boolean Hide) {
        mTitle = Util.getString(titleID);
        mIcon = icon;
        isHide = Hide;
    }
    public MenuDto(String title, int icon, boolean Hide) {
        mTitle = title;
        mIcon = icon;
        isHide = Hide;
    }
    public void setTitle(String title)
    {
        this.mTitle = title;
    }
    public void Hide()
    {
        isHide = true;
    }
    public void Show()
    {
        isHide = false;
    }
}
