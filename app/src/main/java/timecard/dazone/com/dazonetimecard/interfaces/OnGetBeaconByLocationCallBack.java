package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

/**
 * Created by Dat on 7/27/2016.
 */
public interface OnGetBeaconByLocationCallBack {
    void OnGetBeaconByLocationCallBackSuccess(BeaconDTO beaconDTO);
    void OnGetBeaconByLocationCallBackFail(ErrorDto dto);
}
