package timecard.dazone.com.dazonetimecard.interfaces;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

/**
 * Created by Dat on 7/27/2016.
 */
public interface OnGetBeaconsCallBack {
    void OnGetBeaconsSuccess(ArrayList<BeaconDTO> beaconDTOs);

    void OnGetBeaconsFail(ErrorDto dto);
}
