package timecard.dazone.com.dazonetimecard.interfaces;

import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;

/**
 * Created by maidinh on 12-Oct-17.
 */

public interface ChooseGroupNoCallBack {
    void onCompleted(TreeUserDTO treeUserDTO);
}
