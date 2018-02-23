package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface OnGetStatusResponse {
    void onGetStatusResponseSuccess(int type);
    void onGetStatusResponseError(ErrorDto errorDto);
}
