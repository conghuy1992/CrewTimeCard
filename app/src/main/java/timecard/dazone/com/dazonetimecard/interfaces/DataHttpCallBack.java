package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.DataDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface DataHttpCallBack {
    void onDataHTTPSuccess(DataDto dto);
    void onDataHTTPFail(ErrorDto errorDto);
}