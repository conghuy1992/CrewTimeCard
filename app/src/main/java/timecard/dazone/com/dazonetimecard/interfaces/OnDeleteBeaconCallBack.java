package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

/**
 * Created by Dat on 7/27/2016.
 */
public interface OnDeleteBeaconCallBack {
    void OnDeleteBeaconSuccess(String response);
    void OnDeleteBeaconFail(ErrorDto dto);
}
