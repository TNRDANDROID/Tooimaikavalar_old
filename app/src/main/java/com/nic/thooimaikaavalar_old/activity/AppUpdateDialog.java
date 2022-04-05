package com.nic.thooimaikaavalar_old.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.databinding.AppUpdateDialogBinding;
import com.nic.thooimaikaavalar_old.support.MyCustomTextView;


public class AppUpdateDialog extends AppCompatActivity implements View.OnClickListener {


    private MyCustomTextView btnSave;
    private AppUpdateDialogBinding appUpdateDialogBinding;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUpdateDialogBinding = DataBindingUtil.setContentView(this,R.layout.app_update_dialog);
        appUpdateDialogBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intializeUI();

    }

    public void intializeUI() {
        appUpdateDialogBinding.btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                showGooglePlay();
                break;
        }
    }

    public void showGooglePlay() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drdpr.tn.gov.in/")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drdpr.tn.gov.in/")));
        }
    }
}
