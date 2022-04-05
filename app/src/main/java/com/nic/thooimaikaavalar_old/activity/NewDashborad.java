package com.nic.thooimaikaavalar_old.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.databinding.ActivityNewDashboradBinding;

import java.text.ParseException;
import java.util.Date;

public class NewDashborad extends AppCompatActivity {

    ActivityNewDashboradBinding activityNewDashboradBinding;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activityNewDashboradBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_dashborad);
        activityNewDashboradBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        activityNewDashboradBinding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
        activityNewDashboradBinding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoeditThooimaiKavalarActivity();
            }
        });
        activityNewDashboradBinding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoEditViewEditComponentsActivity();
            }
        });
        activityNewDashboradBinding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoViewWasteCollectedDetailsActivity();
            }
        });

        activityNewDashboradBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initializeUi();
    }

    public void initializeUi(){
        String date_of_commencement= dateFormate(getIntent().getStringExtra("date_of_commencement"),"yes");
        activityNewDashboradBinding.villageName.setText("Village : "+getIntent().getStringExtra("village_name"));
        activityNewDashboradBinding.mccName.setText("MCC Name : "+getIntent().getStringExtra("mcc_name"));
        activityNewDashboradBinding.dateOfCommencement.setText("Date of Commencement : "+date_of_commencement);
    }

    public  void showAlert(){
        try {
            final Dialog dialog = new Dialog(NewDashborad.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.no_of_count_dialog);



            ImageView close_icon = (ImageView) dialog.findViewById(R.id.close_icon);
            TextView add_text = (TextView) dialog.findViewById(R.id.add);
            TextView minus_text = (TextView) dialog.findViewById(R.id.minus);
            TextView count_text = (TextView) dialog.findViewById(R.id.number);
            Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
            Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);

            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            add_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(count_text.getText().toString());
                    if(count<5){
                        count =count+1;
                        count_text.setText(""+count);
                    }
                }
            });
            minus_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(count_text.getText().toString());
                    if(count>1){
                        count =count-1;
                        count_text.setText(""+count);
                    }
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(NewDashborad.this,AddThooimaiKaavalarDetails.class);
                    intent.putExtra("count",Integer.parseInt(count_text.getText().toString()));
                    intent.putExtra("mcc_id",getIntent().getStringExtra("mcc_id"));

                    startActivity(intent);
                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoeditThooimaiKavalarActivity(){
        Intent intent =new Intent(NewDashborad.this,NewThooimaiKavalarEdit.class);
        intent.putExtra("mcc_id",getIntent().getStringExtra("mcc_id"));
        startActivity(intent);
    }
    public void gotoEditViewEditComponentsActivity(){
        Intent intent =new Intent(NewDashborad.this,ViewTakeEditComponentsPhots.class);
        intent.putExtra("mcc_id",getIntent().getStringExtra("mcc_id"));
        intent.putExtra("village_name",getIntent().getStringExtra("village_name"));
        intent.putExtra("mcc_name",getIntent().getStringExtra("mcc_name"));
        intent.putExtra("date_of_commencement",getIntent().getStringExtra("date_of_commencement"));
        startActivity(intent);
    }
    public void gotoViewWasteCollectedDetailsActivity(){
        Intent intent =new Intent(NewDashborad.this,ViewWasteCollectedDetails.class);
        intent.putExtra("mcc_id",getIntent().getStringExtra("mcc_id"));
        intent.putExtra("village_name",getIntent().getStringExtra("village_name"));
        intent.putExtra("pvcode",getIntent().getStringExtra("pvcode"));
        intent.putExtra("mcc_name",getIntent().getStringExtra("mcc_name"));
        intent.putExtra("date_of_commencement",getIntent().getStringExtra("date_of_commencement"));
        startActivity(intent);
    }
    public  String dateFormate( String strDate,String type ){
        try {
            SimpleDateFormat sdfSource =null;
            if(type.equals("")) {
                // create SimpleDateFormat object with source string date format
                sdfSource = new SimpleDateFormat(
                        "dd-M-yyyy");
            }
            else {
                sdfSource = new SimpleDateFormat(
                        "yyyy-mm-dd");
            }

            // parse the string into Date object
            Date date = sdfSource.parse(strDate);

            // create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat(
                    "dd-MM-yyyy");

            // parse the date into another format
            strDate = sdfDestination.format(date);

           /* System.out
                    .println("Date is converted from yyyy-MM-dd'T'hh:mm:ss'.000Z' format to dd/MM/yyyy, ha");
            System.out.println("Converted date is : " + strDate.toLowerCase());
*/
        } catch (ParseException pe) {
            System.out.println("Parse Exception : " + pe);
        }
        return strDate;
    }

}
