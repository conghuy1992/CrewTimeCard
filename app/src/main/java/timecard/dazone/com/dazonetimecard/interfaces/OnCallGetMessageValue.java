package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface OnCallGetMessageValue {
    void onResponseMessageSuccess(String message);
    void onResponseMessageError(ErrorDto errorDto);
}