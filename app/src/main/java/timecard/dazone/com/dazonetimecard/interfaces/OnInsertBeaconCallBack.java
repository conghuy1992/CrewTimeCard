package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

/**
 * Created by Dat on 7/27/2016.
 */
public interface OnInsertBeaconCallBack {
    void OnInsertBeaconSuccess(String response);
    void OnInsertBeaconFail(ErrorDto dto);
}
