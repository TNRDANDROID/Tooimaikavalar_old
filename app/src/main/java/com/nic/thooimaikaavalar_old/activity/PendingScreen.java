package com.nic.thooimaikaavalar_old.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.PendingAdditionalDataAdpter;
import com.nic.thooimaikaavalar_old.adapter.PendingScreenAdapter;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ApiService;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.PendingScreenBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.ProgressHUD;
import com.nic.thooimaikaavalar_old.utils.UrlGenerator;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PendingScreen extends AppCompatActivity implements Api.ServerResponseListener {
    private PendingScreenBinding pendingScreenBinding;
    private ShimmerRecyclerView recyclerView;
    private PrefManager prefManager;
    private SQLiteDatabase db;
    public static DBHelper dbHelper;
    public dbData dbData = new dbData(this);
    ArrayList<RealTimeMonitoringSystem> pendingList = new ArrayList<>();
    private PendingScreenAdapter pendingScreenAdapter;
    private PendingAdditionalDataAdpter pendingAdditionalDataAdpter;
    private SearchView searchView;
    JSONObject dataset = new JSONObject();
    private ProgressHUD progressHUD;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pendingScreenBinding = DataBindingUtil.setContentView(this, R.layout.pending_screen);
        pendingScreenBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(pendingScreenBinding.toolbar);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefManager = new PrefManager(this);

        Utils.setupUI(pendingScreenBinding.parentLayout,this);
        pendingList = new ArrayList<>();
        pendingScreenAdapter = new PendingScreenAdapter(PendingScreen.this,pendingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = pendingScreenBinding.pendingListRecycler;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //if(getUploadAdditionalListCount()>0){
            //pendingScreenBinding.selectionLayout.setVisibility(View.VISIBLE);
            pendingScreenBinding.uploadCapacityLayout.setBackgroundResource(R.drawable.white_background_left_two_corners_filled);
            pendingScreenBinding.uploadCapacity.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            pendingScreenBinding.uploadImageLayout.setBackgroundResource(R.drawable.white_background_right_two_corners);
            pendingScreenBinding.uploadImage.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));

            new fetchpendingAdditionalDatatask().execute();

        //}
        /*else {
            pendingScreenBinding.selectionLayout.setVisibility(View.GONE);
            new fetchpendingtask().execute();

        }*/


        pendingScreenBinding.uploadImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getUploadAdditionalListCount()>0) {

                    Utils.showAlert(PendingScreen.this,"First Upload Additional Details!");

                    }
                else {
                    pendingScreenBinding.uploadCapacityLayout.setBackgroundResource(R.drawable.white_background_left_two_corners);
                    pendingScreenBinding.uploadCapacity.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                    pendingScreenBinding.uploadImageLayout.setBackgroundResource(R.drawable.white_background_right_two_corners_filled);
                    pendingScreenBinding.uploadImage.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

                    new fetchpendingtask().execute();

                }

            }
        });
        pendingScreenBinding.uploadCapacityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingScreenBinding.uploadCapacityLayout.setBackgroundResource(R.drawable.white_background_left_two_corners_filled);
                pendingScreenBinding.uploadCapacity.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
                pendingScreenBinding.uploadImageLayout.setBackgroundResource(R.drawable.white_background_right_two_corners);
                pendingScreenBinding.uploadImage.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));

                new fetchpendingAdditionalDatatask().execute();

            }
        });

    }


    public int getUploadAdditionalListCount(){
        dbData.open();
        ArrayList<RealTimeMonitoringSystem> pendingList = new ArrayList<>();
        pendingList = dbData.selectAdditionalData("","","upload");
        return pendingList.size();
    }

    public class fetchpendingtask extends AsyncTask<JSONObject, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(JSONObject... params) {
            dbData.open();
            pendingList = new ArrayList<>();
            pendingList = dbData.getSavedWorkImage("","","","","");
            Log.d("PENDING_COUNT", String.valueOf(pendingList.size()));
            return pendingList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> pendingList) {
            super.onPostExecute(pendingList);
            if(pendingList.size() > 0){
                pendingScreenAdapter = new PendingScreenAdapter(PendingScreen.this,
                        pendingList);
                recyclerView.setAdapter(pendingScreenAdapter);
                recyclerView.showShimmerAdapter();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCards();
                    }
                }, 1000);
                pendingScreenBinding.pendingListRecyclerLayout.setVisibility(View.VISIBLE);
                pendingScreenBinding.noDataFoundLayout.setVisibility(View.GONE);
            }else {
                pendingScreenBinding.pendingListRecyclerLayout.setVisibility(View.GONE);
                pendingScreenBinding.noDataFoundLayout.setVisibility(View.VISIBLE);
            }

        }
    }

    public class fetchpendingAdditionalDatatask extends AsyncTask<JSONObject, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(JSONObject... params) {
            dbData.open();
            pendingList = new ArrayList<>();
            pendingList = dbData.selectAdditionalData("","","upload");
            Log.d("PENDING_COUNT", String.valueOf(pendingList.size()));
            return pendingList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> pendingList) {
            super.onPostExecute(pendingList);
            if(pendingList.size() > 0){
            pendingAdditionalDataAdpter = new PendingAdditionalDataAdpter(PendingScreen.this,
                    pendingList);
            recyclerView.setAdapter(pendingAdditionalDataAdpter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 1000);
                pendingScreenBinding.pendingListRecyclerLayout.setVisibility(View.VISIBLE);
                pendingScreenBinding.noDataFoundLayout.setVisibility(View.GONE);
            }else {
                pendingScreenBinding.pendingListRecyclerLayout.setVisibility(View.GONE);
                pendingScreenBinding.noDataFoundLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadCards() {

        recyclerView.hideShimmerAdapter();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

// Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

// listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
// filter recycler view when query submitted
                pendingScreenAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
// filter recycler view when text is changed
                pendingScreenAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    public class toUploadTask extends AsyncTask<RealTimeMonitoringSystem, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(RealTimeMonitoringSystem... realTimeValue) {
            dbData.open();
            JSONArray track_data = new JSONArray();
            ArrayList<RealTimeMonitoringSystem> assets = dbData.getSavedWorkImage("upload",realTimeValue[0].getDistictCode(),realTimeValue[0].getBlockCode(),realTimeValue[0].getPvCode(),String.valueOf(realTimeValue[0].getWorkId()));

            if (assets.size() > 0) {
                for (int i = 0; i < assets.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        String work_id = String.valueOf(assets.get(i).getWorkId());

                        jsonObject.put(AppConstant.WORK_ID,work_id);
                        jsonObject.put(AppConstant.WORK_GROUP_ID,assets.get(i).getWorkGroupID());
                        jsonObject.put(AppConstant.WORK_TYPE_ID,assets.get(i).getWorkTypeID());
                        jsonObject.put(AppConstant.DISTRICT_CODE,assets.get(i).getDistictCode());
                        jsonObject.put(AppConstant.BLOCK_CODE,assets.get(i).getBlockCode());
                        jsonObject.put(AppConstant.PV_CODE,assets.get(i).getPvCode());
                        jsonObject.put(AppConstant.TYPE_OF_WORK,assets.get(i).getTypeOfWork());
                        if(assets.get(i).getTypeOfWork().equalsIgnoreCase(AppConstant.ADDITIONAL_WORK)){
                            String cd_work_no = String.valueOf(assets.get(i).getCdWorkNo());
                            jsonObject.put(AppConstant.CD_WORK_NO,cd_work_no);
                            jsonObject.put(AppConstant.WORK_TYPE_FLAG_LE,assets.get(i).getWorkTypeFlagLe());
                            prefManager.setTypeOfWork(AppConstant.ADDITIONAL_WORK);
                            prefManager.setDeleteCdWorkNo(cd_work_no);
                            prefManager.setDeleteCdWorkTypeFlag(assets.get(i).getWorkTypeFlagLe());
                        }else {
                            prefManager.setTypeOfWork(AppConstant.MAIN_WORK);
                        }
                        jsonObject.put(AppConstant.WORK_STAGE_CODE,assets.get(i).getWorkStageCode());
                        jsonObject.put(AppConstant.KEY_LATITUDE,assets.get(i).getLatitude());
                        jsonObject.put(AppConstant.KEY_LONGITUDE,assets.get(i).getLongitude());
                        jsonObject.put(AppConstant.KEY_CREATED_DATE,assets.get(i).getCreatedDate());

                        Bitmap bitmap = assets.get(i).getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        byte[] imageInByte = baos.toByteArray();
                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                        jsonObject.put(AppConstant.KEY_IMAGES,image_str);
                        jsonObject.put(AppConstant.KEY_IMAGE_REMARK,assets.get(i).getImageRemark());

                        track_data.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dataset = new JSONObject();

                try {
                    dataset.put(AppConstant.KEY_SERVICE_ID,"work_phy_stage_save");
                    dataset.put(AppConstant.KEY_TRACK_DATA,track_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return dataset;
        }

        @Override
        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            syncData();
        }
    }
    public JSONObject syncAdditionalDataList(JSONObject saveCentreImageDataSet) {
        Log.d("saveadditionalDataList", "" + saveCentreImageDataSet.toString());
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveCentreImageDataSet.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveAdditionalDataList", Api.Method.POST, UrlGenerator.getWorkListUrl(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("additionalDataList", "" + dataSet);
        return dataSet;
    }

    public void syncData() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveImage", Api.Method.POST, UrlGenerator.getWorkListUrl(), saveListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject saveListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saveImage", "" + authKey);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("saveImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Your Data is Synchronized to the server!");
                    String type_of_work = prefManager.getTypeOfWork();
                    dbData.open();
                    if(type_of_work.equalsIgnoreCase(AppConstant.MAIN_WORK))
                    {
                        dbData.deleteWorkListTable();
                        db.delete(DBHelper.SAVE_IMAGE, "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and  type_of_work = ?", new String[]{prefManager.getDistrictCode(), prefManager.getBlockCode(), prefManager.getPvCode(), prefManager.getDeleteWorkId(),type_of_work});
                        pendingScreenAdapter.removeSavedItem(prefManager.getDeleteAdapterPosition());
                        pendingScreenAdapter.notifyDataSetChanged();
                    }else if(type_of_work.equalsIgnoreCase(AppConstant.ADDITIONAL_WORK))
                    {
                        dbData.deleteAdditionalListTable();
                        db.delete(DBHelper.SAVE_IMAGE, "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and  type_of_work = ? and cd_work_no = ? and work_type_flag_le = ?", new String[]{prefManager.getDistrictCode(), prefManager.getBlockCode(), prefManager.getPvCode(), prefManager.getDeleteWorkId(),type_of_work,prefManager.getDeleteCdWorkNo(),prefManager.getDeleteCdWorkTypeFlag()});
                        pendingScreenAdapter.removeSavedItem(prefManager.getDeleteAdapterPosition());
                        pendingScreenAdapter.notifyDataSetChanged();
                    }
                }
                else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    String type_of_work = prefManager.getTypeOfWork();
                    dbData.open();
                    if(type_of_work.equalsIgnoreCase(AppConstant.MAIN_WORK))
                    {
                        db.delete(DBHelper.SAVE_IMAGE, "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and  type_of_work = ?", new String[]{prefManager.getDistrictCode(), prefManager.getBlockCode(), prefManager.getPvCode(), prefManager.getDeleteWorkId(),type_of_work});
                        pendingScreenAdapter.removeSavedItem(prefManager.getDeleteAdapterPosition());
                        pendingScreenAdapter.notifyDataSetChanged();
                    }else if(type_of_work.equalsIgnoreCase(AppConstant.ADDITIONAL_WORK))
                    {
                        db.delete(DBHelper.SAVE_IMAGE, "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and  type_of_work = ? and cd_work_no = ? and work_type_flag_le = ?", new String[]{prefManager.getDistrictCode(), prefManager.getBlockCode(), prefManager.getPvCode(), prefManager.getDeleteWorkId(),type_of_work,prefManager.getDeleteCdWorkNo(),prefManager.getDeleteCdWorkTypeFlag()});
                        pendingScreenAdapter.removeSavedItem(prefManager.getDeleteAdapterPosition());
                        pendingScreenAdapter.notifyDataSetChanged();
                    }
                }
                Log.d("savedImage", "" + responseDecryptedBlockKey);
            }

            if ("saveAdditionalDataList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Your Data is Synchronized to the server!");
                    String type_of_work = prefManager.getTypeOfWork();
                    dbData.open();
                    if(type_of_work.equalsIgnoreCase(AppConstant.MAIN_WORK))
                    {

                        db.delete(DBHelper.SAVE_ADDITIONAL_DATA, " work_id = ? and  type_of_work = ?", new String[]{prefManager.getDeleteWorkId(),type_of_work});
                        pendingAdditionalDataAdpter.removeSavedItem(prefManager.getDeleteAdapterPosition());
                        pendingAdditionalDataAdpter.notifyDataSetChanged();
                    }
                }
                else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("ERROR")) {
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    String type_of_work = prefManager.getTypeOfWork();
                    dbData.open();
                    if(type_of_work.equalsIgnoreCase(AppConstant.MAIN_WORK))
                    {
                        db.delete(DBHelper.SAVE_ADDITIONAL_DATA, " work_id = ? and  type_of_work = ?", new String[]{prefManager.getDeleteWorkId(),type_of_work});
                        pendingAdditionalDataAdpter.removeSavedItem(prefManager.getDeleteAdapterPosition());
                        pendingAdditionalDataAdpter.notifyDataSetChanged();
                    }

                }
                Log.d("saveAdditionalDataList", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    @Override
    public void OnError(VolleyError volleyError) {

    }
}
