package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.AllowDevices;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface OnCheckAllowDevice {
    void onGetMyListSuccess(AllowDevices myListDtos);
    void onGetMyListFail(ErrorDto errorDto);
}
