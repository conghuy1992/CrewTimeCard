package timecard.dazone.com.dazonetimecard.interfaces;


import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface BaseHTTPCallBack {
    void onHTTPSuccess();
    void onHTTPFail(ErrorDto errorDto);
}
