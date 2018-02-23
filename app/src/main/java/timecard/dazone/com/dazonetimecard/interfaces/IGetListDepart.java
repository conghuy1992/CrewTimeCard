package timecard.dazone.com.dazonetimecard.interfaces;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;

/**
 * Created by maidinh on 11-Oct-17.
 */

public interface IGetListDepart {
    void onGetListDepartSuccess(ArrayList<TreeUserDTO> treeUserDTOs);
    void onGetListDepartFail(ErrorDto dto);
}
