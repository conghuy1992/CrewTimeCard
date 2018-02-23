package timecard.dazone.com.dazonetimecard.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.database.UserDBHelper;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.ProfileUserDTO;
import timecard.dazone.com.dazonetimecard.filechooser.FileChooser;
import timecard.dazone.com.dazonetimecard.interfaces.OnDialogWithListChoiceCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetUserCallback;
import timecard.dazone.com.dazonetimecard.utils.Constant;
import timecard.dazone.com.dazonetimecard.utils.DialogUtil;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class ProfileUserActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "ProfileActivity";
    private ImageView img_bg;
    private TextView tv_name, tv_personal, tv_email, tv_company, tv_phone, tvExtensionNumber, tvCellPhone;
    private LinearLayout layoutExtensionNumber, layoutCellPhone;
    private JSONObject object;
    private String CellPhone = "";
    private String MailAddress = "";
    private ImageView avatar_imv;
    private ImageView image_profile;
    public Prefs prefs;
    private RelativeLayout lay_image_profile;

    /**
     * VIEW
     */
    private TextView btnChangePassword;
    private ImageView btnChangePhoto;

    /**
     * PARAM
     */
    private Uri uriCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.nav_back_ic);
        toolbar.setTitle(getString(R.string.profile));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        image_profile = (ImageView) findViewById(R.id.image_profile);
        lay_image_profile = (RelativeLayout) findViewById(R.id.lay_image_profile);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_personal = (TextView) findViewById(R.id.tv_personal);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_phone = (TextView) findViewById(R.id.tv_phone);

        tvExtensionNumber = (TextView) findViewById(R.id.tvExtensionNumber);
        tvCellPhone = (TextView) findViewById(R.id.tvCellPhone);


        layoutExtensionNumber = (LinearLayout) findViewById(R.id.layoutExtensionNumber);
        layoutCellPhone = (LinearLayout) findViewById(R.id.layoutCellPhone);

        avatar_imv = (ImageView) findViewById(R.id.avatar_imv);

        btnChangePassword = (TextView) findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(this);

        btnChangePhoto = (ImageView) findViewById(R.id.btn_change_photo);
        btnChangePhoto.setOnClickListener(this);

        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpRequest.getInstance().GetUser(UserDBHelper.getUser().Id, new OnGetUserCallback() {
            @Override
            public void onHTTPSuccess(ProfileUserDTO user) {
                fillData(user);
            }

            @Override
            public void onHTTPFail(ErrorDto errorDto) {

            }
        });
    }

    private void fillData(ProfileUserDTO profile) {
//        Log.d(TAG,new Gson().toJson(profile));
        final String url = new Prefs().getServerSite() + profile.getAvatarUrl();
        ImageLoader.getInstance().displayImage(url, avatar_imv);

        avatar_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileUserActivity.this, ImageViewActivity.class);
                intent.putExtra(Statics.KEY_URL, url);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        tv_name.setText(profile.getName());
        tv_personal.setText(profile.getUserID());
//        tvName.setText(profile.getName());
        tv_email.setText(profile.getMailAddress());
        //tvSex.setText(profile.getSex() == 0 ? "Female" : "Male");


        String company = new Prefs().getStringValue(Statics.PREFS_KEY_COMPANY_NAME, "");
        /*
        ArrayList<BelongDepartmentDTO> belongs = profile.getBelongs();
        if (belongs != null) {
            for (int i = 0; i < belongs.size(); i++) {
                if (i == 0) {
                    company += belongs.get(i).getDepartName();
                } else {
                    company += "," + belongs.get(i).getDepartName();
                }
            }
        }*/

        tv_company.setText(company);

        String CellPhone = "";
        String ExtensionNumber = "";
        try {
            CellPhone = profile.getCellPhone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ExtensionNumber = profile.getExtensionNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvCellPhone.setText(CellPhone);
        tvExtensionNumber.setText(ExtensionNumber);
        if (CellPhone.length() > 0) layoutCellPhone.setVisibility(View.VISIBLE);
        else layoutCellPhone.setVisibility(View.GONE);
        if (ExtensionNumber.length() > 0) layoutExtensionNumber.setVisibility(View.VISIBLE);
        else layoutExtensionNumber.setVisibility(View.GONE);


        //tvPhoneNumber.setText(profile.getCellPhone());
        //tvCompanyNumber.setText(profile.getCompanyPhone());
        //tvExtensionNumber.setText(profile.getExtensionNumber());
        //tvEntranceDate.setText(profile.displayTimeWithoutOffset(profileUserDTO.getEntranceDate()));
        //tvBirthday.setText(TimeUtils.displayTimeWithoutOffset(profileUserDTO.getBirthDate()));
        //tvBelongToDepartment.setText(Html.fromHtml(belongToDepartment));

        String phoneNumber = !TextUtils.isEmpty(profile.getCellPhone().trim()) ?
                profile.getCellPhone() :
                !TextUtils.isEmpty(profile.getCompanyPhone().trim()) ?
                        profile.getCompanyPhone() :
                        "";

        tv_phone.setText(phoneNumber);
    }

    @Override
    public void onBackPressed() {
        if (lay_image_profile.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            lay_image_profile.setVisibility(View.GONE);
        }
    }

    private void showDialogListPhoto() {
        /** DISPLAY LIST CHOICE */
        final ArrayList<String> itemList = new ArrayList<>();
        itemList.add(Util.getString(R.string.dialog_list_choice_photo_camera));
        itemList.add(Util.getString(R.string.dialog_list_choice_photo_attachment));

        DialogUtil.displayDialogWithListChoice(this, itemList, new OnDialogWithListChoiceCallBack() {
            @Override
            public void onClickOK(final int position) {
                String choice = itemList.get(position);
                if (choice.equals(Util.getString(R.string.dialog_list_choice_photo_camera))) {
                    choiceCamera();
                } else if (choice.equals(Util.getString(R.string.dialog_list_choice_photo_attachment))) {
                    choiceAttachment();
                }
            }
        });
    }

    private void choiceCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uriCamera = getOutputMediaFileUri(Constant.MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCamera);
        startActivityForResult(intent, Constant.REQUEST_CODE_CAPTURE_IMAGE);
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(Util.getOutputMediaFile(type));
    }

    private void choiceAttachment() {
        Intent i = new Intent(this, FileChooser.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, Constant.REQUEST_CODE_SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String realPath = "";
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAPTURE_IMAGE:
                    realPath = uriCamera.getPath();
                    Util.showShortMessage(realPath);
                    break;
                case Constant.REQUEST_CODE_SELECT_FILE:
                    realPath = data.getData().getPath();
                    Util.showShortMessage(realPath);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                callActivity(ChangePasswordActivity.class);
                break;
            case R.id.btn_change_photo:
                showDialogListPhoto();
                break;
        }
    }
}