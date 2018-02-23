package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface OnChangePasswordCallBack {
    void onSuccess(String response);
    void onFail(ErrorDto errorDto);
}