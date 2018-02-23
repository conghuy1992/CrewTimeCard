package timecard.dazone.com.dazonetimecard.interfaces;

import java.util.List;

import timecard.dazone.com.dazonetimecard.dtos.AllEmployeeDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface OnGetAllEmpHTTPCallBack {
    void onGetAllEmpSuccess(List<AllEmployeeDto> myListDtos);
    void onGetAllEmpFail(ErrorDto errorDto);
}
