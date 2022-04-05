package com.nic.thooimaikaavalar_old.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.NewThooimaiEditAdapter;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ApiService;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityNewThooimaiKavalarEditBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.utils.UrlGenerator;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewThooimaiKavalarEdit extends AppCompatActivity implements Api.ServerResponseListener{
    private PrefManager prefManager;
    public com.nic.thooimaikaavalar_old.dataBase.dbData dbData = new dbData(this);
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    NewThooimaiEditAdapter newThooimaiEditAdapter;
    String mcc_id_="";
    String delete_mcc_id="";
    ActivityNewThooimaiKavalarEditBinding activityNewThooimaiKavalarEditBinding;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activityNewThooimaiKavalarEditBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_thooimai_kavalar_edit);
        activityNewThooimaiKavalarEditBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mcc_id_ =getIntent().getStringExtra("mcc_id");

        getKaavalarListserverList();

        activityNewThooimaiKavalarEditBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getKaavalarListserverList() {
        dbData.open();
        ArrayList<RealTimeMonitoringSystem> getAllBasicDetails =  dbData.getAllThooimaikaavalarListServer(mcc_id_);

        if(getAllBasicDetails.size()>0){
            activityNewThooimaiKavalarEditBinding.recyler.setVisibility(View.VISIBLE);
            activityNewThooimaiKavalarEditBinding.noDataText.setVisibility(View.GONE);
            activityNewThooimaiKavalarEditBinding.recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            newThooimaiEditAdapter = new NewThooimaiEditAdapter(getAllBasicDetails,this,dbData);
            activityNewThooimaiKavalarEditBinding.recyler.setAdapter(newThooimaiEditAdapter);
        }
        else {
            activityNewThooimaiKavalarEditBinding.recyler.setVisibility(View.GONE);
            activityNewThooimaiKavalarEditBinding.noDataText.setVisibility(View.VISIBLE);
        }

    }

    public void DeleteMccData(JSONObject jsonObject, String key_id_n, String type_){
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


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("DeleteDetails".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                Log.d("Query",responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                    //Utils.showAlert(this,"Successfully Deleted");
                    int sds = db.delete(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_SERVER, "thooimai_kaavalar_id = ? ", new String[]{delete_mcc_id});
                    getKaavalarListserverList();

                } else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
                    Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                }

                Log.d("DeleteDetails", "" + responseDecryptedSchemeKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }


}
