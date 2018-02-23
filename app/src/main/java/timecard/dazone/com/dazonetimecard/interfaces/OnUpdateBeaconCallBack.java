package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

/**
 * Created by Dat on 7/27/2016.
 */
public interface OnUpdateBeaconCallBack {
    void OnUpdateBeaconSuccess(String response);
    void OnUpdateBeaconFail(ErrorDto dto);
}
