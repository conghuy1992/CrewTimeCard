package timecard.dazone.com.dazonetimecard.interfaces;


import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public interface OnHasAppCallBack {
    void hasApp();
    void noHas(ErrorDto dto);
}
