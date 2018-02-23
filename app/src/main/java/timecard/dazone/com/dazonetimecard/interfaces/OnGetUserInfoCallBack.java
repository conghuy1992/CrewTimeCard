package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface OnGetUserInfoCallBack {
    void onSuccess();
    void onFail(ErrorDto errorDto);
}