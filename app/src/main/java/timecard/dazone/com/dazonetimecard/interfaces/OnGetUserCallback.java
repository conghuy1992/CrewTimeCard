package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.ProfileUserDTO;

public interface OnGetUserCallback {
    void onHTTPSuccess(ProfileUserDTO user);
    void onHTTPFail(ErrorDto errorDto);
}
