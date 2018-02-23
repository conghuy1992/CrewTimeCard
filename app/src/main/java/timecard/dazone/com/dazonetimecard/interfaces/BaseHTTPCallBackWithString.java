package timecard.dazone.com.dazonetimecard.interfaces;


import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface BaseHTTPCallBackWithString {
    void onHTTPSuccess(String message);
    void onHTTPFail(ErrorDto errorDto);
}
