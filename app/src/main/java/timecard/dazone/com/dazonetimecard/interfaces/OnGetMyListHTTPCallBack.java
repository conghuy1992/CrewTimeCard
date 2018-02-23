package timecard.dazone.com.dazonetimecard.interfaces;

import java.util.List;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.MyListDto;

public interface OnGetMyListHTTPCallBack {
    void onGetMyListSuccess(List<MyListDto> myListDtos);
    void onGetMyListFail(ErrorDto errorDto);
}
