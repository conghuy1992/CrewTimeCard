package timecard.dazone.com.dazonetimecard.dtos;

import com.google.gson.annotations.SerializedName;

public class ErrorDto {

    public boolean unAuthentication;
    public boolean serverError=false;
    @SerializedName("code")
    public int code = 1;

    @SerializedName("message")
    public String message = "";

}
