package com.nic.thooimaikaavalar_old.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import java.util.ArrayList;


public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context){
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

    /****** DISTRICT TABLE *****/
    public RealTimeMonitoringSystem insertDistrict(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, realTimeMonitoringSystem.getDistictCode());
        values.put(AppConstant.DISTRICT_NAME, realTimeMonitoringSystem.getDistrictName());

        long id = db.insert(DBHelper.DISTRICT_TABLE_NAME,null,values);
        Log.d("Inserted_id_district", String.valueOf(id));

        return realTimeMonitoringSystem;
    }

    public ArrayList<RealTimeMonitoringSystem > getAll_District() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DISTRICT_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setDistrictName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** BLOCKTABLE *****/
    public RealTimeMonitoringSystem insertBlock(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, realTimeMonitoringSystem.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, realTimeMonitoringSystem.getBlockCode());
        values.put(AppConstant.BLOCK_NAME, realTimeMonitoringSystem.getBlockName());

        long id = db.insert(DBHelper.BLOCK_TABLE_NAME,null,values);
        Log.d("Inserted_id_block", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public ArrayList<RealTimeMonitoringSystem > getAll_Block() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BLOCK_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setBlockName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** VILLAGE TABLE *****/
    public RealTimeMonitoringSystem insertVillage(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, realTimeMonitoringSystem.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, realTimeMonitoringSystem.getBlockCode());
        values.put(AppConstant.PV_CODE, realTimeMonitoringSystem.getPvCode());
        values.put(AppConstant.PV_NAME, realTimeMonitoringSystem.getPvName());

        long id = db.insert(DBHelper.VILLAGE_TABLE_NAME,null,values);
        Log.d("Inserted_id_village", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public RealTimeMonitoringSystem insertHabitation(RealTimeMonitoringSystem kvvtSurvey) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, kvvtSurvey.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, kvvtSurvey.getBlockCode());
        values.put(AppConstant.PV_CODE, kvvtSurvey.getPvCode());
        values.put(AppConstant.HABB_CODE, kvvtSurvey.getHabCode());
        values.put(AppConstant.HABITATION_NAME, kvvtSurvey.getHabitationName());
        values.put(AppConstant.HABITATION_NAME_TA, kvvtSurvey.getHabitationNameTa());

        long id = db.insert(DBHelper.HABITATION_TABLE_NAME,null,values);
        Log.d("Inserted_id_habitation", String.valueOf(id));

        return kvvtSurvey;
    }
    public RealTimeMonitoringSystem insertBasicDetailsFromServer(RealTimeMonitoringSystem kvvtSurvey) {

        ContentValues values = new ContentValues();
        values.put("mcc_id", kvvtSurvey.getMcc_id());
        values.put("mcc_name", kvvtSurvey.getMcc_name());
        values.put("dcode", kvvtSurvey.getDistictCode());
        values.put("dname", kvvtSurvey.getDistrictName());
        values.put("bcode", kvvtSurvey.getBlockCode());
        values.put("bname", kvvtSurvey.getBlockName());
        values.put("pvcode", kvvtSurvey.getPvCode());
        values.put("pvname", kvvtSurvey.getPvName());
        values.put("capacity_of_mcc_id", kvvtSurvey.getCapacity_of_mcc_id());
        values.put("capacity_of_mcc_name", kvvtSurvey.getCapacity_of_mcc_name());
        values.put("no_of_thooimai_kaavalars", kvvtSurvey.getNo_of_thooimai_kaavalars());
        values.put("water_supply_availability_id", kvvtSurvey.getWater_supply_availability_id());
        values.put("water_supply_availability_name", kvvtSurvey.getWater_supply_availability_name());
        values.put("water_supply_availability_other", kvvtSurvey.getWater_supply_availability_other());
        values.put("date_of_commencement", kvvtSurvey.getDate_of_commencement());
        values.put("latitude", kvvtSurvey.getCenter_image_latitude());
        values.put("longitude", kvvtSurvey.getCenter_image_longtitude());
        values.put("composting_center_image", kvvtSurvey.getCenter_image());
        values.put("composting_center_image_available", kvvtSurvey.getComposting_center_image_available());

        long id = db.insert(DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER,null,values);
        Log.d("Inserted_id_basic_server", String.valueOf(id));

        return kvvtSurvey;
    }

    public RealTimeMonitoringSystem insertThooimaKavalarListFromServer(RealTimeMonitoringSystem kvvtSurvey) {

        ContentValues values = new ContentValues();
        values.put("dcode", kvvtSurvey.getDistictCode());
        values.put("dname", kvvtSurvey.getDistrictName());
        values.put("bcode", kvvtSurvey.getBlockCode());
        values.put("bname", kvvtSurvey.getBlockName());
        values.put("pvcode", kvvtSurvey.getPvCode());
        values.put("pvname", kvvtSurvey.getPvName());
        values.put("mcc_id", kvvtSurvey.getMcc_id());
        values.put("thooimai_kaavalar_id", kvvtSurvey.getThooimai_kaavalar_id());
        values.put("name_of_the_thooimai_kaavalars", kvvtSurvey.getName_of_the_thooimai_kaavalars());
        values.put("mobile_no", kvvtSurvey.getMobile_no());
        values.put("date_of_engagement", kvvtSurvey.getDate_of_engagement());
        values.put("date_of_training_given", kvvtSurvey.getDate_of_training_given());

        long id = db.insert(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER,null,values);
        Log.d("Inserted_thooimai__server", String.valueOf(id));

        return kvvtSurvey;
    }

    public RealTimeMonitoringSystem insertThooimaiKaavaleDetailsLocal(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put("mcc_id", realTimeMonitoringSystem.getMcc_id());
        values.put("name_of_the_thooimai_kaavalars", realTimeMonitoringSystem.getName_of_the_thooimai_kaavalars());
        values.put("mobile_no", realTimeMonitoringSystem.getMobile_no());
        values.put("thooimai_kaavalar_id", realTimeMonitoringSystem.getThooimai_kaavalar_id());
        values.put("date_of_engagement", realTimeMonitoringSystem.getDate_of_engagement());
        values.put("date_of_training_given", realTimeMonitoringSystem.getDate_of_training_given());

        long id = db.insert(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL,null,values);
        Log.d("Inserted_id_THOOIMAI", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public RealTimeMonitoringSystem updateThooimaiKaavaleDetailsLocal(RealTimeMonitoringSystem realTimeMonitoringSystem,String id1) {
        String whereClause = "";String[] whereArgs = null;
        whereClause = "thooimai_kaavalar_id = ?";
        whereArgs = new String[]{id1};

        ContentValues values = new ContentValues();
        values.put("mcc_id", realTimeMonitoringSystem.getMcc_id());
        values.put("name_of_the_thooimai_kaavalars", realTimeMonitoringSystem.getName_of_the_thooimai_kaavalars());
        values.put("mobile_no", realTimeMonitoringSystem.getMobile_no());
        values.put("thooimai_kaavalar_id", realTimeMonitoringSystem.getThooimai_kaavalar_id());
        values.put("date_of_engagement", realTimeMonitoringSystem.getDate_of_engagement());
        values.put("date_of_training_given", realTimeMonitoringSystem.getDate_of_training_given());

        //long id = db.insert(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL,null,values);
        long id = db.update(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL,values,whereClause,whereArgs);
        Log.d("Inserted_id_THOOIMAI", String.valueOf(id));

        return realTimeMonitoringSystem;
    }

    public ArrayList<RealTimeMonitoringSystem > getAll_Village() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.VILLAGE_TABLE_NAME+" order by pvname asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem> getAll_Habitation(String dcode, String bcode) {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.HABITATION_TABLE_NAME+" where dcode = "+dcode+" and bcode = "+bcode+" order by habitation_name asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setHabCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABB_CODE)));
                    card.setHabitationName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABITATION_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }


    public RealTimeMonitoringSystem Insertswm_capacity_of_mcc_in_tonesTask(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ID, realTimeMonitoringSystem.getKEY_ID());
        values.put(AppConstant.KEY_capacity_of_mcc_name, realTimeMonitoringSystem.getKEY_capacity_of_mcc_name());


        long id = db.insert(DBHelper.SWM_CAPACITY_OF_MCC_IN_TONNES_LIST,null,values);
        Log.d("Inserted_capacity_of", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public RealTimeMonitoringSystem Insertswm_no_of_thooimai_kaavalarsTask(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ID, realTimeMonitoringSystem.getKEY_ID());
        values.put(AppConstant.KEY_thooimai_kaavalars_name, realTimeMonitoringSystem.getKEY_thooimai_kaavalars_name());

        long id = db.insert(DBHelper.SWM_NO_OF_THOOIMAI_KAAVALARS_LIST,null,values);
        Log.d("Insert_kaavalars_name", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public RealTimeMonitoringSystem Insertswm_photographs_of_mcc_componentsTask(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ID, realTimeMonitoringSystem.getKEY_ID());
        values.put(AppConstant.KEY_photographs_name, realTimeMonitoringSystem.getKEY_photographs_name());


        long id = db.insert(DBHelper.SWM_PHOTOGRAPHS_OF_MCC_COMPONENTS_LIST,null,values);
        Log.d("Inserted_photographs", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public RealTimeMonitoringSystem Insertswm_water_supply_availabilityTask(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ID, realTimeMonitoringSystem.getKEY_ID());
        values.put(AppConstant.KEY_water_supply_availability_name, realTimeMonitoringSystem.getKEY_water_supply_availability_name());


        long id = db.insert(DBHelper.SWM_WATER_SUPPLY_AVAILABILITY_LIST,null,values);
        Log.d("Inserted_water_supply", String.valueOf(id));

        return realTimeMonitoringSystem;
    }

    public ArrayList<RealTimeMonitoringSystem > getAll_capacity_of_mcc() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.SWM_CAPACITY_OF_MCC_IN_TONNES_LIST+" order by capacity_of_mcc_name asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setKEY_capacity_of_mcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_capacity_of_mcc_name)));


                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getAll_thooimai_kaavalars() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.SWM_NO_OF_THOOIMAI_KAAVALARS_LIST+" order by thooimai_kaavalars_name asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setKEY_thooimai_kaavalars_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_thooimai_kaavalars_name)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getAll_photographs_of_mcc() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.SWM_PHOTOGRAPHS_OF_MCC_COMPONENTS_LIST+" order by photographs_name asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setKEY_photographs_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_photographs_name)));


                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getAll_water_supply_availability() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.SWM_WATER_SUPPLY_AVAILABILITY_LIST+" order by water_supply_availability_name asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setKEY_water_supply_availability_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_water_supply_availability_name)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getParticularVillageBasicDetails(String mcc_id) {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BASIC_DETAILS_OF_MCC_SAVE+" where mcc_id = "+mcc_id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setMcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_name")));
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setCapacity_of_mcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("capacity_of_mcc_id")));
                    card.setWater_supply_availability_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_availability_id")));
                    card.setWater_supply_availability_other(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_availability_other")));
                    card.setDate_of_commencement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_commencement")));
                    card.setCenter_image(cursor.getString(cursor
                            .getColumnIndexOrThrow("image")));
                    card.setCenter_image_latitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("latitude")));
                    card.setCenter_image_longtitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("longitude")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getAllBasicDetails() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BASIC_DETAILS_OF_MCC_SAVE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setMcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_name")));
                    card.setCapacity_of_mcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("capacity_of_mcc_id")));
                    card.setWater_supply_availability_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_availability_id")));
                    card.setWater_supply_availability_other(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_availability_other")));
                    card.setDate_of_commencement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_commencement")));
                    card.setCenter_image(cursor.getString(cursor
                            .getColumnIndexOrThrow("image")));
                    card.setCenter_image_latitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("latitude")));
                    card.setCenter_image_longtitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("longitude")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<RealTimeMonitoringSystem > getAllBasicDetailsFromServer(String pvcode) {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER+" where pvcode = "+pvcode,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setMcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_name")));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("dcode")));
                    card.setDistrictName(cursor.getString(cursor
                            .getColumnIndexOrThrow("dname")));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("bcode")));
                    card.setBlockName(cursor.getString(cursor
                            .getColumnIndexOrThrow("bname")));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("pvcode")));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow("pvname")));
                    card.setCapacity_of_mcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("capacity_of_mcc_id")));
                    card.setCapacity_of_mcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("capacity_of_mcc_name")));
                    card.setNo_of_thooimai_kaavalars(cursor.getString(cursor
                            .getColumnIndexOrThrow("no_of_thooimai_kaavalars")));
                    card.setWater_supply_availability_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_availability_id")));
                    card.setWater_supply_availability_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_availability_name")));
                    card.setWater_supply_availability_other(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_availability_other")));
                    card.setDate_of_commencement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_commencement")));
                    card.setComposting_center_image_available(cursor.getString(cursor
                            .getColumnIndexOrThrow("composting_center_image_available")));
                    card.setCenter_image(cursor.getString(cursor
                            .getColumnIndexOrThrow("composting_center_image")));
                    card.setCenter_image_latitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("latitude")));
                    card.setCenter_image_longtitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("longitude")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<RealTimeMonitoringSystem > getAllThooimaikaavalarListLocaltID(String t_id) {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL+" where thooimai_kaavalar_id = "+t_id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setName_of_the_thooimai_kaavalars(cursor.getString(cursor
                            .getColumnIndexOrThrow("name_of_the_thooimai_kaavalars")));
                    card.setMobile_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("mobile_no")));
                    card.setDate_of_engagement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_engagement")));
                    card.setDate_of_training_given(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_training_given")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getAllThooimaikaavalarListLocal(String mcc_id) {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL+" where mcc_id = "+mcc_id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setName_of_the_thooimai_kaavalars(cursor.getString(cursor
                            .getColumnIndexOrThrow("name_of_the_thooimai_kaavalars")));
                    card.setThooimai_kaavalar_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("thooimai_kaavalar_id")));
                    card.setMobile_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("mobile_no")));
                    card.setDate_of_engagement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_engagement")));
                    card.setDate_of_training_given(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_training_given")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getAllThooimaikaavalarListServer(String mcc_id) {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER+" where mcc_id = "+mcc_id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setThooimai_kaavalar_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("thooimai_kaavalar_id")));
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setName_of_the_thooimai_kaavalars(cursor.getString(cursor
                            .getColumnIndexOrThrow("name_of_the_thooimai_kaavalars")));
                    card.setMobile_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("mobile_no")));
                    card.setDate_of_engagement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_engagement")));
                    card.setDate_of_training_given(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_training_given")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getAllThooimaikaavalarListLocalAll() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            /*String Query = "select  a.mcc_id , b.mcc_name as mcc_name ,b.date_of_commencement as date_of_commencement from (select distinct mcc_id from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL+") a \n" +
                    "left join (select * from "+DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER+") b \n" +
                    "on a.mcc_id = b.mcc_id";*/
            String Query = "select mcc_id ,mcc_name , date_of_commencement from "+DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER+" where mcc_id in (select distinct mcc_id from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL+")";
            //cursor = db.rawQuery("select * from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL,null);
            cursor = db.rawQuery(Query,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    /*card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));*/
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setMcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_name")));
                    card.setDate_of_commencement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_commencement")));
                    /*card.setDate_of_engagement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_engagement")));
                    card.setDate_of_training_given(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_training_given")));
*/
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<RealTimeMonitoringSystem > getAllComponentsPendingScreen() {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            /*String Query = "select  a.mcc_id , b.mcc_name as mcc_name ,b.date_of_commencement as date_of_commencement from (select distinct mcc_id from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL+") a \n" +
                    "left join (select * from "+DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER+") b \n" +
                    "on a.mcc_id = b.mcc_id";*/
            String Query = "select mcc_id ,mcc_name , date_of_commencement from "+DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER+" where mcc_id in (select distinct mcc_id from "+DBHelper.COMPOST_TUB_IMAGE_TABLE+")";
            //cursor = db.rawQuery("select * from "+DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL,null);
            cursor = db.rawQuery(Query,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    /*card.setKEY_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));*/
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setMcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_name")));
                    card.setDate_of_commencement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_commencement")));
                    /*card.setDate_of_engagement(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_engagement")));
                    card.setDate_of_training_given(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_training_given")));
*/
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem > getParticularMCCIDImage(String mcc_id) {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.COMPOST_TUB_IMAGE_TABLE+" where mcc_id = "+mcc_id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setComponent_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("component_id")));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("latitude")));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("longitude")));
                    card.setPhotograph_remark(cursor.getString(cursor
                            .getColumnIndexOrThrow("photograph_remark")));
                    card.setImage(decodedByte);

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<RealTimeMonitoringSystem > getParticularWasteCollectedSaveTableRow(String mcc_id,String type) {

        ArrayList<RealTimeMonitoringSystem > cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            if(type.equals("All")){
                cursor = db.rawQuery("select * from "+DBHelper.WASTE_COLLECTED_SAVE_TABLE,null);
            }
            else {
                cursor = db.rawQuery("select * from "+DBHelper.WASTE_COLLECTED_SAVE_TABLE+" where mcc_id = "+mcc_id,null);
            }

            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    RealTimeMonitoringSystem  card = new RealTimeMonitoringSystem ();
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("pvcode")));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow("pvname")));
                    card.setMcc_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_id")));
                    card.setMcc_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("mcc_name")));
                    card.setHouseholds_waste(cursor.getString(cursor
                            .getColumnIndexOrThrow("households_waste")));
                    card.setShops_waste(cursor.getString(cursor
                            .getColumnIndexOrThrow("shops_waste")));
                    card.setMarket_waste(cursor.getString(cursor
                            .getColumnIndexOrThrow("market_waste")));
                    card.setHotels_waste(cursor.getString(cursor
                            .getColumnIndexOrThrow("hotels_waste")));
                    card.setOthers_waste(cursor.getString(cursor
                            .getColumnIndexOrThrow("others_waste")));
                    card.setTot_bio_waste_collected(cursor.getString(cursor
                            .getColumnIndexOrThrow("tot_bio_waste_collected")));
                    card.setTot_bio_waste_shredded(cursor.getString(cursor
                            .getColumnIndexOrThrow("tot_bio_waste_shredded")));
                    card.setTot_bio_compost_produced(cursor.getString(cursor
                            .getColumnIndexOrThrow("tot_bio_compost_produced")));
                    card.setTot_bio_compost_sold(cursor.getString(cursor
                            .getColumnIndexOrThrow("tot_bio_compost_sold")));
                    card.setDate_of_save(cursor.getString(cursor
                            .getColumnIndexOrThrow("date_of_save")));


                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }



    ////////////// ************************** Now Not Need ///////////////////////
    public RealTimeMonitoringSystem insertScheme(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_SCHEME_SEQUENTIAL_ID, realTimeMonitoringSystem.getSchemeSequentialID());
        values.put(AppConstant.KEY_SCHEME_NAME, realTimeMonitoringSystem.getSchemeName());
        values.put(AppConstant.FINANCIAL_YEAR, realTimeMonitoringSystem.getFinancialYear());
        values.put(AppConstant.KEY_ADDITIONAL_DATA_STATUS, realTimeMonitoringSystem.getAdditional_data_status());

        long id = db.insert(DBHelper.SCHEME_TABLE_NAME, null, values);
        Log.d("Inserted_id_Scheme", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public ArrayList<RealTimeMonitoringSystem> getAll_Scheme() {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + DBHelper.SCHEME_TABLE_NAME, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setSchemeSequentialID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEME_SEQUENTIAL_ID)));
                    card.setSchemeName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEME_NAME)));
                    card.setFinancialYear(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.FINANCIAL_YEAR)));
                    card.setAdditional_data_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ADDITIONAL_DATA_STATUS)));
                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public RealTimeMonitoringSystem insertFinYear(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.FINANCIAL_YEAR, realTimeMonitoringSystem.getFinancialYear());

        long id = db.insert(DBHelper.FINANCIAL_YEAR_TABLE_NAME, null, values);
        Log.d("Inserted_id_FinYear", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public ArrayList<RealTimeMonitoringSystem> getAll_FinYear() {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + DBHelper.FINANCIAL_YEAR_TABLE_NAME, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setFinancialYear(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.FINANCIAL_YEAR)));

                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public RealTimeMonitoringSystem insertStage(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.WORK_GROUP_ID, realTimeMonitoringSystem.getWorkGroupID());
        values.put(AppConstant.WORK_TYPE_ID, realTimeMonitoringSystem.getWorkTypeID());
        values.put(AppConstant.WORK_STAGE_ORDER, realTimeMonitoringSystem.getWorkStageOrder());
        values.put(AppConstant.WORK_STAGE_CODE, realTimeMonitoringSystem.getWorkStageCode());
        values.put(AppConstant.WORK_SATGE_NAME, realTimeMonitoringSystem.getWorkStageName());
        values.put(AppConstant.KEY_ADDITIONAL_DATA_STATUS, realTimeMonitoringSystem.getAdditional_data_status());

        long id = db.insert(DBHelper.WORK_STAGE_TABLE, null, values);
        Log.d("Inserted_id_work_Stage", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public ArrayList<RealTimeMonitoringSystem> getAll_Stage() {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + DBHelper.WORK_STAGE_TABLE, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setWorkGroupID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setWorkTypeID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_TYPE_ID)));
                    card.setWorkStageOrder(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_ORDER)));
                    card.setWorkStageCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_CODE)));

                    card.setWorkStageName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_SATGE_NAME)));
                    card.setAdditional_data_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ADDITIONAL_DATA_STATUS)));

                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem> getStage(String workGroupId, String workTypeid) {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+ DBHelper.WORK_STAGE_TABLE+" where work_group_id=" + workGroupId + "  and work_type_id=" + workTypeid, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setWorkGroupID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setWorkTypeID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_TYPE_ID)));
                    card.setWorkStageOrder(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_ORDER)));
                    card.setWorkStageCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_CODE)));

                    card.setWorkStageName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_SATGE_NAME)));
                    card.setAdditional_data_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ADDITIONAL_DATA_STATUS)));

                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public RealTimeMonitoringSystem insertAdditionalStage(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.WORK_TYPE_CODE, realTimeMonitoringSystem.getWorkTypeCode());
        values.put(AppConstant.WORK_STAGE_ORDER, realTimeMonitoringSystem.getWorkStageOrder());
        values.put(AppConstant.WORK_STAGE_CODE, realTimeMonitoringSystem.getWorkStageCode());
        values.put(AppConstant.WORK_SATGE_NAME, realTimeMonitoringSystem.getWorkStageName());
        values.put(AppConstant.CD_TYPE_FLAG, realTimeMonitoringSystem.getWorkTypeFlagLe());

        long id = db.insert(DBHelper.ADDITIONAL_WORK_STAGE_TABLE, null, values);
        Log.d("Inserted_id_Add_Stage", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public ArrayList<RealTimeMonitoringSystem> getAdditionalStage() {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + DBHelper.ADDITIONAL_WORK_STAGE_TABLE, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setWorkTypeCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_TYPE_CODE)));;
                    card.setWorkStageOrder(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_ORDER)));
                    card.setWorkStageCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_CODE)));

                    card.setWorkStageName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_SATGE_NAME)));
                    card.setWorkTypeFlagLe(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.CD_TYPE_FLAG)));

                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public RealTimeMonitoringSystem insertWorkList(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, realTimeMonitoringSystem.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, realTimeMonitoringSystem.getBlockCode());
        values.put(AppConstant.PV_CODE, realTimeMonitoringSystem.getPvCode());
        values.put(AppConstant.WORK_ID, realTimeMonitoringSystem.getWorkId());
        values.put(AppConstant.SCHEME_GROUP_ID, realTimeMonitoringSystem.getSchemeGroupID());
        values.put(AppConstant.SCHEME_ID, realTimeMonitoringSystem.getSchemeID());
        values.put(AppConstant.SCHEME_GROUP_NAME, realTimeMonitoringSystem.getSchemeGroupName());
        values.put(AppConstant.KEY_SCHEME_NAME, realTimeMonitoringSystem.getSchemeName());
        values.put(AppConstant.FINANCIAL_YEAR, realTimeMonitoringSystem.getFinancialYear());
        values.put(AppConstant.AGENCY_NAME, realTimeMonitoringSystem.getAgencyName());
        values.put(AppConstant.WORK_GROUP_NAME, realTimeMonitoringSystem.getWorkGroupNmae());
        values.put(AppConstant.WORK_NAME, realTimeMonitoringSystem.getWorkName());
        values.put(AppConstant.WORK_GROUP_ID, realTimeMonitoringSystem.getWorkGroupID());
        values.put(AppConstant.WORK_TYPE, realTimeMonitoringSystem.getWorkTypeID());
        values.put(AppConstant.CURRENT_STAGE_OF_WORK, realTimeMonitoringSystem.getCurrentStage());
        values.put(AppConstant.AS_VALUE, realTimeMonitoringSystem.getAsValue());
        values.put(AppConstant.AMOUNT_SPENT_SOFAR, realTimeMonitoringSystem.getAmountSpendSoFar());
        values.put(AppConstant.STAGE_NAME, realTimeMonitoringSystem.getStageName());
        values.put(AppConstant.BENEFICIARY_NAME, realTimeMonitoringSystem.getBeneficiaryName());
        values.put(AppConstant.BENEFICIARY_FATHER_NAME, realTimeMonitoringSystem.getBeneficiaryFatherName());
        values.put(AppConstant.WORK_TYPE_NAME, realTimeMonitoringSystem.getWorkTypeName());
        values.put(AppConstant.YN_COMPLETED, realTimeMonitoringSystem.getYnCompleted());
        values.put(AppConstant.CD_PROT_WORK_YN, realTimeMonitoringSystem.getCdProtWorkYn());
        values.put(AppConstant.STATE_CODE, realTimeMonitoringSystem.getStateCode());
        values.put(AppConstant.DISTRICT_NAME, realTimeMonitoringSystem.getDistrictName());
        values.put(AppConstant.BLOCK_NAME, realTimeMonitoringSystem.getBlockName());
        values.put(AppConstant.PV_NAME, realTimeMonitoringSystem.getPvName());
        values.put(AppConstant.COMMUNITY_NAME, realTimeMonitoringSystem.getCommunity());
        values.put(AppConstant.GENDER, realTimeMonitoringSystem.getGender());
        values.put(AppConstant.LAST_VISITED_DATE, realTimeMonitoringSystem.getLastVisitedDate());
        values.put(AppConstant.KEY_IMAGE_AVAILABLE, realTimeMonitoringSystem.getImageAvailable());

        long id = db.insert(DBHelper.WORK_LIST_TABLE_BASED_ON_FINYEAR_VIlLAGE, null, values);
        Log.d("Inserted_id_Worklist", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public ArrayList<RealTimeMonitoringSystem> getAllWorkLIst(String purpose, String fin_year, String dcode, String bcode, String pvcode,Integer schemeSeqId) {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String condition = "";

        if(purpose.equalsIgnoreCase("fetch")) {
            condition = " where fin_year = '" + fin_year + "' and dcode = "+dcode+" and bcode = "+bcode+ " and pvcode = "+pvcode+ " and scheme_id = "+schemeSeqId+ " and current_stage_of_work != 10";
        }else{
            condition = " where fin_year = '" + fin_year + "' and dcode = "+dcode+" and bcode = "+bcode+ " and pvcode = "+pvcode+ " and current_stage_of_work != 10";

        }

        try {
            cursor = db.rawQuery("select * from " + DBHelper.WORK_LIST_TABLE_BASED_ON_FINYEAR_VIlLAGE +  condition, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();

                    card.setDistictCode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setWorkId(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WORK_ID)));
                    card.setSchemeGroupID(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.SCHEME_GROUP_ID)));
                    card.setSchemeID(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.SCHEME_ID)));
                    card.setSchemeGroupName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.SCHEME_GROUP_NAME)));
                    card.setSchemeName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_SCHEME_NAME)));
                    card.setFinancialYear(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FINANCIAL_YEAR)));
                    card.setAgencyName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.AGENCY_NAME)));
                    card.setWorkGroupNmae(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_GROUP_NAME)));
                    card.setWorkName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_NAME)));
                    card.setWorkGroupID(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setWorkTypeID(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_TYPE)));
                    card.setCurrentStage(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.CURRENT_STAGE_OF_WORK)));
                    card.setAsValue(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.AS_VALUE)));
                    card.setAmountSpendSoFar(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.AMOUNT_SPENT_SOFAR)));
                    card.setStageName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STAGE_NAME)));
                    card.setBeneficiaryName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.BENEFICIARY_NAME)));
                    card.setBeneficiaryFatherName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.BENEFICIARY_FATHER_NAME)));
                    card.setWorkTypeName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_TYPE_NAME)));
                    card.setYnCompleted(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.YN_COMPLETED)));
                    card.setCdProtWorkYn(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.CD_PROT_WORK_YN)));
                    card.setStateCode(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.STATE_CODE)));
                    card.setDistrictName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_NAME)));
                    card.setBlockName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.BLOCK_NAME)));
                    card.setPvName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PV_NAME)));
                    card.setCommunity(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.COMMUNITY_NAME)));
                    card.setGender(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.GENDER)));
                    card.setLastVisitedDate(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LAST_VISITED_DATE)));
                    card.setImageAvailable(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGE_AVAILABLE)));

                    /*card.setLength("");
                    card.setWidth("");
                    card.setHeight("");
                    card.setLength_water_storage("");
                    card.setObservation("");
                    card.setNo_trenches_per_worksite("");
                    card.setNo_units("");
                    card.setDepth("");
                    card.setNo_units_per_waterbody("");
                    card.setArea_covered("");
                    card.setNo_plantation_per_site("");
                    card.setStructure_sump_available_status("");
                    card.setIndividual_community("");
                    card.setLength_channel("");*/

                    cards.add(card);
                }
            }
        } catch (Exception e) {
               Log.d("Excep" +
                       "", "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public RealTimeMonitoringSystem insertAdditionalWorkList(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, realTimeMonitoringSystem.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, realTimeMonitoringSystem.getBlockCode());
        values.put(AppConstant.PV_CODE, realTimeMonitoringSystem.getPvCode());
        values.put(AppConstant.SCHEME_ID, realTimeMonitoringSystem.getSchemeID());
        values.put(AppConstant.FINANCIAL_YEAR, realTimeMonitoringSystem.getFinancialYear());
        values.put(AppConstant.WORK_ID, realTimeMonitoringSystem.getWorkId());
        values.put(AppConstant.WORK_GROUP_ID, realTimeMonitoringSystem.getWorkGroupID());
        values.put(AppConstant.ROAD_NAME, realTimeMonitoringSystem.getRoadName());
        values.put(AppConstant.CD_WORK_NO, realTimeMonitoringSystem.getCdWorkNo());
        values.put(AppConstant.CURRENT_STAGE_OF_WORK, realTimeMonitoringSystem.getCurrentStage());
        values.put(AppConstant.CD_TYPE_ID, realTimeMonitoringSystem.getCdTypeId());
        values.put(AppConstant.WORK_TYPE_FLAG_LE, realTimeMonitoringSystem.getWorkTypeFlagLe());
        values.put(AppConstant.CD_CODE, realTimeMonitoringSystem.getCdCode());
        values.put(AppConstant.CD_NAME, realTimeMonitoringSystem.getCdName());
        values.put(AppConstant.CHAINAGE_METER, realTimeMonitoringSystem.getChainageMeter());
        values.put(AppConstant.WORK_SATGE_NAME, realTimeMonitoringSystem.getWorkStageName());
        values.put(AppConstant.KEY_IMAGE_AVAILABLE, realTimeMonitoringSystem.getImageAvailable());


        long id = db.insert(DBHelper.ADDITIONAL_WORK_LIST, null, values);
        Log.d("Inserted_id_Additional", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public RealTimeMonitoringSystem insertAdditionalDetailsList(RealTimeMonitoringSystem realTimeMonitoringSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, realTimeMonitoringSystem.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, realTimeMonitoringSystem.getBlockCode());
        values.put(AppConstant.PV_CODE, realTimeMonitoringSystem.getPvCode());
        values.put(AppConstant.WORK_ID, realTimeMonitoringSystem.getWorkId());
        values.put(AppConstant.WORK_GROUP_ID, realTimeMonitoringSystem.getWorkGroupID());
        values.put(AppConstant.WORK_TYPE_ID, realTimeMonitoringSystem.getWorkTypeID());
        values.put(AppConstant.TYPE_OF_WORK, realTimeMonitoringSystem.getTypeOfWork());
        values.put(AppConstant.KEY_LENGTH_WATER_STORAGE, realTimeMonitoringSystem.getLength_water_storage());
        values.put(AppConstant.KEY_LATITUDE, realTimeMonitoringSystem.getLatitude());
        values.put(AppConstant.KEY_LONGITUDE, realTimeMonitoringSystem.getLongitude());
        values.put(AppConstant.KEY_OBSERVATION, realTimeMonitoringSystem.getObservation());
        values.put(AppConstant.KEY_NO_TRENCHES_PER_WORKSITE, realTimeMonitoringSystem.getNo_trenches_per_worksite());
        values.put(AppConstant.KEY_NO_UNITS, realTimeMonitoringSystem.getNo_units());
        values.put(AppConstant.KEY_LENGTH, realTimeMonitoringSystem.getLength());
        values.put(AppConstant.KEY_WIDTH, realTimeMonitoringSystem.getWidth());
        values.put(AppConstant.KEY_HEIGHT, realTimeMonitoringSystem.getHeight());
        values.put(AppConstant.KEY_DEPTH, realTimeMonitoringSystem.getDepth());
        values.put(AppConstant.KEY_NO_UNITS_PER_WATERBODY, realTimeMonitoringSystem.getNo_units_per_waterbody());
        values.put(AppConstant.KEY_AREA_COVERED, realTimeMonitoringSystem.getArea_covered());
        values.put(AppConstant.KEY_NO_PLANTATION_PER_SITE, realTimeMonitoringSystem.getNo_plantation_per_site());
        values.put(AppConstant.KEY_STRUCTURE_SUMP_AVAILABLE_STATUS, realTimeMonitoringSystem.getStructure_sump_available_status());
        values.put(AppConstant.KEY_INDIVIDUAL_COMMUNITY, realTimeMonitoringSystem.getIndividual_community());
        values.put(AppConstant.KEY_LENGTH_CHANNEL, realTimeMonitoringSystem.getLength_channel());

        long id = db.insert(DBHelper.GET_ADDITIONAL_DETAILS_LIST, null, values);
        Log.d("Inserted_id_AddData", String.valueOf(id));

        return realTimeMonitoringSystem;
    }
    public ArrayList<RealTimeMonitoringSystem> getAllAdditionalWork(String work_id,String fin_year, String dcode, String bcode, String pvcode,Integer schemeSeqId) {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        String condition = "";

        if (work_id != "") {
            //condition = " where work_id = " + work_id + " and fin_year = '" + fin_year + "' and dcode = " + dcode + " and bcode = " + bcode + " and pvcode = " + pvcode+ " and scheme_id = " + schemeSeqId+ " and current_stage_of_work != 10";
            condition = " where work_id = " + work_id + " and fin_year = '" + fin_year + "' and dcode = " + dcode + " and bcode = " + bcode + " and pvcode = " + pvcode+ " and scheme_id = " + schemeSeqId;

        }else {
           // condition = " where fin_year = '" + fin_year + "' and dcode = " + dcode + " and bcode = " + bcode + " and pvcode = " + pvcode+ " and current_stage_of_work != 10";
            condition = " where fin_year = '" + fin_year + "' and dcode = " + dcode + " and bcode = " + bcode + " and pvcode = " + pvcode;
        }


        try {
            cursor = db.rawQuery("select * from " + DBHelper.ADDITIONAL_WORK_LIST + condition, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();

                    card.setDistictCode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setSchemeID(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.SCHEME_ID))));
                    card.setFinancialYear(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FINANCIAL_YEAR)));
                    card.setWorkId(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WORK_ID)));
                    card.setWorkGroupID(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setRoadName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.ROAD_NAME)));
                    card.setCdWorkNo(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.CD_WORK_NO)));
                    card.setCurrentStage(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.CURRENT_STAGE_OF_WORK)));
                    card.setCdTypeId(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.CD_TYPE_ID)));
                    card.setWorkTypeFlagLe(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_TYPE_FLAG_LE)));
                    card.setCdCode(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.CD_CODE)));
                    card.setCdName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.CD_NAME)));
                    card.setChainageMeter(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.CHAINAGE_METER)));
                    card.setWorkStageName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WORK_SATGE_NAME)));
                    card.setImageAvailable(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGE_AVAILABLE)));

                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem> getSavedWorkImage(String purpose,String dcode,String bcode,String pvcode,String work_id) {

        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "server_flag = ? ";
        String[] selectionArgs = new String[]{"0"};

        if(purpose.equalsIgnoreCase("upload")) {
            selection = "server_flag = ? and dcode = ? and bcode = ? and pvcode = ? and work_id = ?";
            selectionArgs = new String[]{"0",dcode,bcode,pvcode,work_id};
        }




        try {
            cursor = db.query(DBHelper.SAVE_IMAGE,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setWorkId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_ID)));
                    card.setWorkGroupID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setCdWorkNo(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.CD_WORK_NO)));
                    card.setWorkTypeFlagLe(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_TYPE_FLAG_LE)));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndex(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setTypeOfWork(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.TYPE_OF_WORK)));
                    card.setWorkStageCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_CODE)));
                    card.setWorkStageName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_SATGE_NAME)));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LATITUDE)));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LONGITUDE)));
                    card.setImage(decodedByte);
                    card.setImageRemark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_REMARK)));
                    card.setCreatedDate(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CREATED_DATE)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem> selectImage(String dcode,String bcode, String pvcode,String work_id,String type_of_work,String cd_work_no,String work_type_flag_le) {
        db.isOpen();
        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;
        if (type_of_work.equalsIgnoreCase(AppConstant.ADDITIONAL_WORK)) {
            selection = "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and type_of_work = ? and cd_work_no = ? and work_type_flag_le = ?";
            selectionArgs = new String[]{dcode,bcode,pvcode,work_id,type_of_work,cd_work_no,work_type_flag_le};
        }else {
            selection = "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and type_of_work = ?";
            selectionArgs = new String[]{dcode,bcode,pvcode,work_id,type_of_work};
        }

        try {
            cursor = db.query(DBHelper.SAVE_IMAGE,
                    new String[]{"*"}, selection,selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setWorkId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_ID)));
                    card.setWorkGroupID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setCdWorkNo(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.CD_WORK_NO)));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndex(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setTypeOfWork(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.TYPE_OF_WORK)));
                    card.setWorkStageCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_STAGE_CODE)));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LATITUDE)));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LONGITUDE)));
                    card.setImage(decodedByte);
                    card.setImageRemark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_REMARK)));
                    card.setCreatedDate(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CREATED_DATE)));
                    card.setWorkStageName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_SATGE_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem> selectAdditionalData(String work_id, String type_of_work, String purpose) {
        db.isOpen();
        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;

        if(purpose.equalsIgnoreCase("upload")) {
            selection = "";
            selectionArgs = new String[]{};
        }else {
            selection = "work_id = ? and type_of_work = ?";
            selectionArgs = new String[]{work_id,type_of_work};
        }


        try {
            cursor = db.query(DBHelper.SAVE_ADDITIONAL_DATA,
                    new String[]{"*"}, selection,selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setWorkId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_ID)));
                    card.setWorkGroupID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setWorkTypeID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_TYPE_ID)));
                    card.setTypeOfWork(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.TYPE_OF_WORK)));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndex(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setLength_water_storage(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH_WATER_STORAGE)));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LATITUDE)));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LONGITUDE)));
                    card.setObservation(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OBSERVATION)));
                    card.setNo_trenches_per_worksite(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_TRENCHES_PER_WORKSITE)));
                    card.setNo_units(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_UNITS)));
                    card.setLength(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH)));
                    card.setWidth(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_WIDTH)));
                    card.setHeight(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HEIGHT)));
                    card.setDepth(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DEPTH)));
                    card.setNo_units_per_waterbody(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_UNITS_PER_WATERBODY)));
                    card.setArea_covered(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_AREA_COVERED)));
                    card.setNo_plantation_per_site(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_PLANTATION_PER_SITE)));
                    card.setStructure_sump_available_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_STRUCTURE_SUMP_AVAILABLE_STATUS)));
                    card.setIndividual_community(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_INDIVIDUAL_COMMUNITY)));
                    card.setLength_channel(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH_CHANNEL)));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<RealTimeMonitoringSystem> selectAdditionalDetailsList(String work_id, String work_type_id, String purpose) {
        db.isOpen();
        ArrayList<RealTimeMonitoringSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;

        if(purpose.equalsIgnoreCase("get")) {
            selection = "";
            selectionArgs = new String[]{};
        }else {
            selection = "work_id = ? and work_type_id = ?";
            selectionArgs = new String[]{work_id,work_type_id};
        }


        try {
            cursor = db.query(DBHelper.GET_ADDITIONAL_DETAILS_LIST,
                    new String[]{"*"}, selection,selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    RealTimeMonitoringSystem card = new RealTimeMonitoringSystem();
                    card.setWorkId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_ID)));
                    card.setWorkGroupID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_GROUP_ID)));
                    card.setWorkTypeID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.WORK_TYPE_ID)));
                    card.setTypeOfWork(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.TYPE_OF_WORK)));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndex(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setLength_water_storage(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH_WATER_STORAGE)));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LATITUDE)));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LONGITUDE)));
                    card.setObservation(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OBSERVATION)));
                    card.setNo_trenches_per_worksite(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_TRENCHES_PER_WORKSITE)));
                    card.setNo_units(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_UNITS)));
                    card.setLength(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH)));
                    card.setWidth(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_WIDTH)));
                    card.setHeight(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HEIGHT)));
                    card.setDepth(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DEPTH)));
                    card.setNo_units_per_waterbody(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_UNITS_PER_WATERBODY)));
                    card.setArea_covered(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_AREA_COVERED)));
                    card.setNo_plantation_per_site(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_PLANTATION_PER_SITE)));
                    card.setStructure_sump_available_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_STRUCTURE_SUMP_AVAILABLE_STATUS)));
                    card.setIndividual_community(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_INDIVIDUAL_COMMUNITY)));
                    card.setLength_channel(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH_CHANNEL)));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
////////////////////// ******************************* ////////////


    public void deleteDistrictTable() {
        db.execSQL("delete from " + DBHelper.DISTRICT_TABLE_NAME);
    }

    public void deleteBlockTable() {
        db.execSQL("delete from " + DBHelper.BLOCK_TABLE_NAME);
    }

    public void deleteVillageTable() {
        db.execSQL("delete from " + DBHelper.VILLAGE_TABLE_NAME);
    }

    public void deleteFinYearTable() { db.execSQL("delete from " + DBHelper.FINANCIAL_YEAR_TABLE_NAME); }

    public void deleteSchemeTable() {
        db.execSQL("delete from " + DBHelper.SCHEME_TABLE_NAME);
    }

    public void deleteWorkStageTable() {
        db.execSQL("delete from " + DBHelper.WORK_STAGE_TABLE);
    }

    public void deleteWorkListTable() {
        db.execSQL("delete from " + DBHelper.WORK_LIST_TABLE_BASED_ON_FINYEAR_VIlLAGE);
    }

    public void deleteAdditionalListTable() {
        db.execSQL("delete from " + DBHelper.ADDITIONAL_WORK_LIST);
    }
    public void deleteSaveAdditionalDataTable() {
        db.execSQL("delete from " + DBHelper.SAVE_ADDITIONAL_DATA);
    }
    public void deleteAdditionalDetailsListTable() {
        db.execSQL("delete from " + DBHelper.GET_ADDITIONAL_DETAILS_LIST);
    }
    public void deleteSWM_CAPACITY_OF_MCC_IN_TONNES_LIST() {
        db.execSQL("delete from " + DBHelper.SWM_CAPACITY_OF_MCC_IN_TONNES_LIST);
    }
    public void deleteSWM_NO_OF_THOOIMAI_KAAVALARS_LIST() {
        db.execSQL("delete from " + DBHelper.SWM_NO_OF_THOOIMAI_KAAVALARS_LIST);
    }
    public void deleteSWM_PHOTOGRAPHS_OF_MCC_COMPONENTS_LIST() {
        db.execSQL("delete from " + DBHelper.SWM_PHOTOGRAPHS_OF_MCC_COMPONENTS_LIST);
    }
    public void deleteSWM_WATER_SUPPLY_AVAILABILITY_LIST() {
        db.execSQL("delete from " + DBHelper.SWM_WATER_SUPPLY_AVAILABILITY_LIST);
    }
    public void deleteBASIC_DETAILS_OF_MCC_SAVE() {
        db.execSQL("delete from " + DBHelper.BASIC_DETAILS_OF_MCC_SAVE);
    }
    public void deleteBASIC_DETAILS_OF_MCC_SAVE_SERVER() {
        db.execSQL("delete from " + DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER);
    }
    public void deleteTHOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER() {
        db.execSQL("delete from " + DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER);
    }
    public void deleteTHOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL() {
        db.execSQL("delete from " + DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL);
    }
    public void deleteCOMPOST_TUB_IMAGE_TABLE() {
        db.execSQL("delete from " + DBHelper.COMPOST_TUB_IMAGE_TABLE);
    }
    public void deleteWASTE_COLLECTED_SAVE_TABLE() {
        db.execSQL("delete from " + DBHelper.WASTE_COLLECTED_SAVE_TABLE);
    }


    public void deleteAll() {
        deleteDistrictTable();
        deleteBlockTable();
        deleteVillageTable();
        deleteFinYearTable();
        deleteSchemeTable();
        deleteWorkStageTable();
        deleteWorkListTable();
        deleteAdditionalListTable();
        deleteSaveAdditionalDataTable();
        deleteAdditionalDetailsListTable();

        deleteSWM_CAPACITY_OF_MCC_IN_TONNES_LIST();
        deleteSWM_NO_OF_THOOIMAI_KAAVALARS_LIST();
        deleteSWM_PHOTOGRAPHS_OF_MCC_COMPONENTS_LIST();
        deleteSWM_WATER_SUPPLY_AVAILABILITY_LIST();
        deleteBASIC_DETAILS_OF_MCC_SAVE();
        deleteBASIC_DETAILS_OF_MCC_SAVE_SERVER();
        deleteTHOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER();
        deleteTHOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL();
        deleteCOMPOST_TUB_IMAGE_TABLE();
        deleteWASTE_COLLECTED_SAVE_TABLE();
    }



}
