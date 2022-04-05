package com.nic.thooimaikaavalar_old.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityWasteCollectedFormBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class WasteCollectedForm extends AppCompatActivity {

    public ActivityWasteCollectedFormBinding activityWasteCollectedFormBinding;
    public com.nic.thooimaikaavalar_old.dataBase.dbData dbData = new dbData(this);
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    String pvcode="";
    String pvname="";
    String mcc_id="";
    String mcc_name="";
    String house_hold_text="";
    String shops_text="";
    String market_places_text="";
    String hotels_text="";
    String others_text="";
    String total_quantity_waste_text="";
    String shredded_text="";
    String compost_produced_text="";
    String compost_sold_text="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWasteCollectedFormBinding = DataBindingUtil.setContentView(this, R.layout.activity_waste_collected_form);
        activityWasteCollectedFormBinding.setActivity(this);

        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pvcode = getIntent().getStringExtra("pvcode");
        pvname= getIntent().getStringExtra("pv_name");
        mcc_id = getIntent().getStringExtra("mcc_id");
        mcc_name = getIntent().getStringExtra("mcc_name");

        activityWasteCollectedFormBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });

        activityWasteCollectedFormBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void fieldValidation(){
      /*
        if(!house_hold_text.equals("")){
            house_hold_text=activityWasteCollectedFormBinding.houseHoldText.getText().toString();
        }
        else {
            house_hold_text="";
        }
        if(!shops_text.equals("")){
            shops_text=activityWasteCollectedFormBinding.shopsText.getText().toString();
        }
        else {
            shops_text="";
        }
        if(!market_places_text.equals("")){
            market_places_text=activityWasteCollectedFormBinding.marketPlacesText.getText().toString();
        }
        else {
            market_places_text="";
        }
        if(!hotels_text.equals("")){
            hotels_text=activityWasteCollectedFormBinding.hotelsText.getText().toString();
        }
        else {
            hotels_text="";
        }
        if(!others_text.equals("")){
            others_text=activityWasteCollectedFormBinding.othersText.getText().toString();
        }
        else {
            others_text="";
        }
        if(!total_quantity_waste_text.equals("")){
            total_quantity_waste_text=activityWasteCollectedFormBinding.totalQuantityWasteText.getText().toString();
        }
        else {
            total_quantity_waste_text="";
        }
        if(!shredded_text.equals("")){
            shredded_text=activityWasteCollectedFormBinding.shopsText.getText().toString();
        }
        else {
            shredded_text="";
        }
        if(!compost_produced_text.equals("")){
            compost_produced_text=activityWasteCollectedFormBinding.compostProducedText.getText().toString();
        }
        else {
            compost_produced_text="";
        }
        if(!compost_sold_text.equals("")){
            compost_sold_text=activityWasteCollectedFormBinding.compostSoldText.getText().toString();
        }
        else {
            compost_sold_text="";
        }*/
        house_hold_text=activityWasteCollectedFormBinding.houseHoldText.getText().toString();
        shops_text=activityWasteCollectedFormBinding.shopsText.getText().toString();
        market_places_text=activityWasteCollectedFormBinding.marketPlacesText.getText().toString();
        hotels_text=activityWasteCollectedFormBinding.hotelsText.getText().toString();
        others_text=activityWasteCollectedFormBinding.othersText.getText().toString();
        total_quantity_waste_text=activityWasteCollectedFormBinding.totalQuantityWasteText.getText().toString();
        shredded_text=activityWasteCollectedFormBinding.shopsText.getText().toString();
        compost_produced_text=activityWasteCollectedFormBinding.compostProducedText.getText().toString();
        compost_sold_text=activityWasteCollectedFormBinding.compostSoldText.getText().toString();
        try {
            ContentValues values = new ContentValues();
            values.put("pvcode",pvcode);
            values.put("pvname",pvname);
            values.put("mcc_id",mcc_id);
            values.put("mcc_name",mcc_name);
            values.put("households_waste",house_hold_text);
            values.put("shops_waste",shops_text);
            values.put("market_waste",market_places_text);
            values.put("hotels_waste",hotels_text);
            values.put("others_waste",others_text);
            values.put("tot_bio_waste_collected",total_quantity_waste_text);
            values.put("tot_bio_waste_shredded",shredded_text);
            values.put("tot_bio_compost_produced",compost_produced_text);
            values.put("tot_bio_compost_sold",compost_sold_text);
            values.put("date_of_save",getCurrentDate());

            dbData.open();
            ArrayList<RealTimeMonitoringSystem> get_count_waste_table= new ArrayList<>();
            get_count_waste_table = dbData.getParticularWasteCollectedSaveTableRow(mcc_id,"");
            if(get_count_waste_table.size()>0){
                long return_id= db.update(DBHelper.WASTE_COLLECTED_SAVE_TABLE,values,"mcc_id = ? ", new String[]{mcc_id});
                if(return_id>0){
                    Toasty.success(this, getResources().getString(R.string.updated_success), Toast.LENGTH_LONG, true).show();
                    onBackPressed();
                }
            }

            else {
                long return_id = db.insert(DBHelper.WASTE_COLLECTED_SAVE_TABLE,null,values);
                if(return_id>0){
                    Toasty.success(this, getResources().getString(R.string.inserted_success), Toast.LENGTH_LONG, true).show();
                    onBackPressed();
                }
            }
        }
        catch (Exception e){

        }
    }

    public String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }


}
