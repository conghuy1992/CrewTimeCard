package timecard.dazone.com.dazonetimecard.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dazone on 3/13/2017.
 */

public class ContentAllow {

    @SerializedName("Device")
    private String device;

    @SerializedName("Allow")
    private boolean allow;

    public ContentAllow() {
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
