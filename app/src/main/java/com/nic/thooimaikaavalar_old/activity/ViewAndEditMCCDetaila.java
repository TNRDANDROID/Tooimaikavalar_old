package com.nic.thooimaikaavalar_old.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.BasicDetailsFromServerAdapter;
import com.nic.thooimaikaavalar_old.adapter.CommonAdapter;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ApiService;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityViewAndEditMCCDetailaBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.ProgressHUD;
import com.nic.thooimaikaavalar_old.utils.UrlGenerator;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAndEditMCCDetaila extends AppCompatActivity implements Api.ServerResponseListener {

    public com.nic.thooimaikaavalar_old.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    private SQLiteDatabase db;
    public static DBHelper dbHelper;
    String pv_code;
    ArrayList<RealTimeMonitoringSystem> villageList;
    ArrayList<RealTimeMonitoringSystem> basicDetailsServerList;
    private ProgressHUD progressHUD;

    BasicDetailsFromServerAdapter basicDetailsFromServerAdapter;

    ActivityViewAndEditMCCDetailaBinding activity_view_and_edit_m_c_c_detaila;
    String delete_mcc_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activity_view_and_edit_m_c_c_detaila = DataBindingUtil.setContentView(this, R.layout.activity_view_and_edit_m_c_c_detaila);
        activity_view_and_edit_m_c_c_detaila.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        activity_view_and_edit_m_c_c_detaila.mccRecyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activity_view_and_edit_m_c_c_detaila.mccReclclerRl.setVisibility(View.GONE);
        activity_view_and_edit_m_c_c_detaila.noDataText.setVisibility(View.GONE);
        activity_view_and_edit_m_c_c_detaila.mccRecyler.setVisibility(View.GONE);
        activity_view_and_edit_m_c_c_detaila.previewImageLayout.setVisibility(View.GONE);

        activity_view_and_edit_m_c_c_detaila.villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    pv_code = villageList.get(i).getPvCode();
                    if(Utils.isOnline()){
                        getBasicMccList();
                    }
                    else {
                        new fetchBasicDetailsFromServerTask().execute();
                    }

                }
                else {
                    activity_view_and_edit_m_c_c_detaila.mccReclclerRl.setVisibility(View.VISIBLE);
                    activity_view_and_edit_m_c_c_detaila.noDataText.setVisibility(View.VISIBLE);
                    activity_view_and_edit_m_c_c_detaila.mccRecyler.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new fetchScheduletask().execute();

        activity_view_and_edit_m_c_c_detaila.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity_view_and_edit_m_c_c_detaila.previewImageLayout.getVisibility()==View.VISIBLE){
                    activity_view_and_edit_m_c_c_detaila.previewImageLayout.setVisibility(View.GONE);
                }
                else {
                    finish();
                }

            }
        });
        activity_view_and_edit_m_c_c_detaila.closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_view_and_edit_m_c_c_detaila.previewImageLayout.setVisibility(View.GONE);
            }
        });

    }

    public void getBasicMccList() {
        try {
            new ApiService(this).makeJSONObjectRequest("MCCList", Api.Method.POST, UrlGenerator.getWorkListUrl(), workListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject workListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), workListBlockWiseJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("workList", "" + authKey);
        return dataSet;
    }

    public  JSONObject workListBlockWiseJsonParams(Activity activity) throws JSONException {

        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "details_of_micro_composting_view");
        dataSet.put(AppConstant.PV_CODE, pv_code);
        Log.d("objectworkLis", "" + dataSet);
        return dataSet;
    }




    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("MCCList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                Log.d("Query",responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertBasicListFromServerTask().execute(jsonObject);


                } else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
                    Utils.showAlert(this,getResources().getString(R.string.no_record_found));
                    activity_view_and_edit_m_c_c_detaila.mccReclclerRl.setVisibility(View.VISIBLE);
                    activity_view_and_edit_m_c_c_detaila.noDataText.setVisibility(View.VISIBLE);
                    activity_view_and_edit_m_c_c_detaila.mccRecyler.setVisibility(View.GONE);
                }

                Log.d("MCCList", "" + responseDecryptedSchemeKey);
            }
            if ("DeleteDetails".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                Log.d("Query",responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                    Utils.showAlert(this,getResources().getString(R.string.successfully_deleted));
                    int sdsm = db.delete(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL, "mcc_id = ? ", new String[]{delete_mcc_id});
                    int sds = db.delete(DBHelper.BASIC_DETAILS_OF_MCC_SAVE, "mcc_id = ? ", new String[]{delete_mcc_id});
                    int sd = db.delete(DBHelper.COMPOST_TUB_IMAGE_TABLE, "mcc_id = ? ", new String[]{delete_mcc_id});
                    int sd1 = db.delete(DBHelper.WASTE_COLLECTED_SAVE_TABLE, "mcc_id = ? ", new String[]{delete_mcc_id});
                    getBasicMccList();

                } else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
                    Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                }

                Log.d("MCCList", "" + responseDecryptedSchemeKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class fetchScheduletask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {
            dbData.open();
            villageList = new ArrayList<>();
            RealTimeMonitoringSystem realTimeMonitoringSystem = new RealTimeMonitoringSystem();
            realTimeMonitoringSystem.setPvCode("0");
            realTimeMonitoringSystem.setPvName("Select Village");
            villageList.add(realTimeMonitoringSystem);
            villageList.addAll(dbData.getAll_Village());
            Log.d("VILLAGE_COUNT", String.valueOf(villageList.size()));
            return villageList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> villageList) {
            super.onPostExecute(villageList);
            if(villageList.size()>0){
                activity_view_and_edit_m_c_c_detaila.villageSpinner.setAdapter(new CommonAdapter(ViewAndEditMCCDetaila.this, villageList, "villageList"));

            }
        }
    }

    public class fetchBasicDetailsFromServerTask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {
            dbData.open();
            basicDetailsServerList = new ArrayList<>();
            basicDetailsServerList.addAll(dbData.getAllBasicDetailsFromServer(pv_code));
            Log.d("getAllBasicDetailsFromServer", String.valueOf(basicDetailsServerList.size()));
            return basicDetailsServerList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> villageList) {
            super.onPostExecute(villageList);
            if(villageList.size()>0){
                activity_view_and_edit_m_c_c_detaila.mccRecyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                activity_view_and_edit_m_c_c_detaila.mccReclclerRl.setVisibility(View.VISIBLE);
                activity_view_and_edit_m_c_c_detaila.noDataText.setVisibility(View.GONE);
                activity_view_and_edit_m_c_c_detaila.mccRecyler.setVisibility(View.VISIBLE);
                basicDetailsFromServerAdapter = new BasicDetailsFromServerAdapter(basicDetailsServerList,ViewAndEditMCCDetaila.this);
                activity_view_and_edit_m_c_c_detaila.mccRecyler.setAdapter(basicDetailsFromServerAdapter);
            }
            else {
                activity_view_and_edit_m_c_c_detaila.mccReclclerRl.setVisibility(View.VISIBLE);
                activity_view_and_edit_m_c_c_detaila.noDataText.setVisibility(View.VISIBLE);
                activity_view_and_edit_m_c_c_detaila.mccRecyler.setVisibility(View.GONE);

            }
        }
    }

    public class InsertBasicListFromServerTask extends AsyncTask<JSONObject, Void, Void> {

        private  boolean running = true;

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            ArrayList<RealTimeMonitoringSystem> workList_count = dbData.getAllWorkLIst("insert",pref_finYear,prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode());
//            if (workList_count.size() <= 0) {
//                running = true;
//            }else {
//                running = false;
//              //  Utils.showAlert(WorkListScreen.this,"Already Downloaded");
//            }
//        }

        @Override
        protected Void doInBackground(JSONObject... params) {

            dbData.open();
            /*In online Delete DB to fetch the Api data*/
//            if(Utils.isOnline()){
//                dbData.deleteWorkListTable();
//            }
            // ArrayList<RealTimeMonitoringSystem> workList_count = dbData.getAllWorkLIst("insert",pref_finYear,prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode(),0);
            //if (workList_count.size() <= 0) {
            if (params.length > 0) {
                String where="pvcode=?";
                String where_new="mcc_id=?";
                db.delete(DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER, where, new String[]{pv_code}) ;
                db.delete(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER, where, new String[]{pv_code}) ;
                 //db.execSQL("delete from "+ DBHelper.BASIC_DETAILS_OF_MCC_SAVE_SERVER);
                //db.execSQL("delete from "+ DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER);
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                JSONArray jsonArray2 = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    RealTimeMonitoringSystem workList = new RealTimeMonitoringSystem();
                    try {
                        workList.setMcc_id(jsonArray.getJSONObject(i).getString("mcc_id"));
                        workList.setMcc_name(jsonArray.getJSONObject(i).getString("mcc_name"));
                        workList.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                        workList.setDistrictName(jsonArray.getJSONObject(i).getString("dname"));
                        workList.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                        workList.setBlockName(jsonArray.getJSONObject(i).getString("bname"));
                        workList.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                        workList.setPvName(jsonArray.getJSONObject(i).getString("pvname"));
                        workList.setCapacity_of_mcc_id(jsonArray.getJSONObject(i).getString("capacity_of_mcc_id"));
                        workList.setCapacity_of_mcc_name(jsonArray.getJSONObject(i).getString("capacity_of_mcc_name"));
                        workList.setNo_of_thooimai_kaavalars(jsonArray.getJSONObject(i).getString("no_of_thooimai_kaavalars"));
                        workList.setWater_supply_availability_id(jsonArray.getJSONObject(i).getString("water_supply_availability_id"));
                        workList.setWater_supply_availability_name(jsonArray.getJSONObject(i).getString("water_supply_availability_name"));
                        workList.setWater_supply_availability_other(jsonArray.getJSONObject(i).getString("water_supply_availability_other"));
                        workList.setDate_of_commencement(jsonArray.getJSONObject(i).getString("date_of_commencement"));
                        workList.setCenter_image_latitude(jsonArray.getJSONObject(i).getString("latitude"));
                        workList.setCenter_image_longtitude(jsonArray.getJSONObject(i).getString("longitude"));
                        //workList.setCenter_image(jsonArray.getJSONObject(i).getString("composting_center_image"));
                        workList.setComposting_center_image_available(jsonArray.getJSONObject(i).getString("composting_center_image_available"));
                        if(jsonArray.getJSONObject(i).getString("composting_center_image_available").equals("Y")){
                            workList.setCenter_image(jsonArray.getJSONObject(i).getString("composting_center_image"));
                        }
                        jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("thooimai_kaavalar_details");
                        //jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("component_details");

                        try {
                            if(jsonArray1.length()>0) {

                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    RealTimeMonitoringSystem thooimaiList = new RealTimeMonitoringSystem();
                                    thooimaiList.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                                    thooimaiList.setDistrictName(jsonArray.getJSONObject(i).getString("dname"));
                                    thooimaiList.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                                    thooimaiList.setBlockName(jsonArray.getJSONObject(i).getString("bname"));
                                    thooimaiList.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                                    thooimaiList.setPvName(jsonArray.getJSONObject(i).getString("pvname"));
                                    thooimaiList.setMcc_id(jsonArray1.getJSONObject(j).getString("mcc_id"));
                                    thooimaiList.setThooimai_kaavalar_id(jsonArray1.getJSONObject(j).getString("thooimai_kaavalar_id"));
                                    thooimaiList.setName_of_the_thooimai_kaavalars(jsonArray1.getJSONObject(j).getString("name_of_the_thooimai_kaavalars"));
                                    thooimaiList.setMobile_no(jsonArray1.getJSONObject(j).getString("mobile_no"));
                                    thooimaiList.setDate_of_engagement(jsonArray1.getJSONObject(j).getString("date_of_engagement"));
                                    thooimaiList.setDate_of_training_given(jsonArray1.getJSONObject(j).getString("date_of_training_given"));

                                    dbData.insertThooimaKavalarListFromServer(thooimaiList);
                                }
                            }

                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }
                        //workList.setWorkTypeName(jsonArray.getJSONObject(i).getString("component_details"));


                        dbData.insertBasicDetailsFromServer(workList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            // }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHUD = ProgressHUD.show(ViewAndEditMCCDetaila.this, "Downloading", true, false, null);

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressHUD!=null){
                progressHUD.cancel();
                new fetchBasicDetailsFromServerTask().execute();
            }
        }
    }


    public void gotoDashboard(int position){
        Intent intent = new Intent(ViewAndEditMCCDetaila.this,NewDashborad.class);
        intent.putExtra("mcc_id",basicDetailsServerList.get(position).getMcc_id());
        intent.putExtra("village_name",basicDetailsServerList.get(position).getPvName());
        intent.putExtra("date_of_commencement",basicDetailsServerList.get(position).getDate_of_commencement());
        intent.putExtra("mcc_name",basicDetailsServerList.get(position).getMcc_name());
        intent.putExtra("pvcode",basicDetailsServerList.get(position).getPvCode());
        startActivity(intent);
    }
    public void gotoEditMcc(int position){
        Intent intent = new Intent(ViewAndEditMCCDetaila.this,NewMainPage.class);
        intent.putExtra(AppConstant.PV_CODE,basicDetailsServerList.get(position).getPvCode());
        intent.putExtra("mcc_id",basicDetailsServerList.get(position).getMcc_id());
        intent.putExtra("mcc_name",basicDetailsServerList.get(position).getMcc_name());
        intent.putExtra("capacity_id",basicDetailsServerList.get(position).getCapacity_of_mcc_id());
        intent.putExtra("capacity_name",basicDetailsServerList.get(position).getCapacity_of_mcc_name());
        intent.putExtra("water_supply_availability_id",basicDetailsServerList.get(position).getWater_supply_availability_id());
        intent.putExtra("water_supply_availability_name",basicDetailsServerList.get(position).getKEY_water_supply_availability_name());
        intent.putExtra("water_supply_availability_other",basicDetailsServerList.get(position).getWater_supply_availability_other());
        intent.putExtra("center_image",basicDetailsServerList.get(position).getCenter_image());
        intent.putExtra("date_of_commencement",basicDetailsServerList.get(position).getDate_of_commencement());
        intent.putExtra("latitude",basicDetailsServerList.get(position).getCenter_image_latitude());
        intent.putExtra("longtitude",basicDetailsServerList.get(position).getCenter_image_longtitude());
        startActivity(intent);
    }

    public void DeleteMccData(JSONObject jsonObject,String key_id_n,String type_){
        delete_mcc_id = key_id_n;
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), jsonObject.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);
            Log.d("EncryptedDetails", "" + dataSet);
            Log.d("JSONDetails", "" + jsonObject);
            try {
                new ApiService(this).makeJSONObjectRequest("DeleteDetails", Api.Method.POST, UrlGenerator.getWorkListUrl(), dataSet, "not cache", this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (JSONException e){

        }


    }

    public void fullImagePreview(int position){
        activity_view_and_edit_m_c_c_detaila.previewImageLayout.setVisibility(View.VISIBLE);
        activity_view_and_edit_m_c_c_detaila.imagePreviewFull.setImageBitmap(StringToBitMap(basicDetailsServerList.get(position).getCenter_image()));
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if(activity_view_and_edit_m_c_c_detaila.previewImageLayout.getVisibility()==View.VISIBLE){
            activity_view_and_edit_m_c_c_detaila.previewImageLayout.setVisibility(View.GONE);
        }
        else {
        super.onBackPressed();
        }
    }
}
