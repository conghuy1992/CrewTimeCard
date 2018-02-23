package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.UserSettingDto;

public interface OnGetUserInfoResponse {
    void onGetUserInfoResponseSuccess(UserSettingDto user);
    void onGetUserInfoResponseError(ErrorDto errorDto);
}