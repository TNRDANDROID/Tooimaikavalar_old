package com.nic.thooimaikaavalar_old.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.thooimaikaavalar_old.Fragment.SlideshowDialogFragment;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.FullImageAdapter;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ApiService;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.FullImageRecyclerBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.utils.UrlGenerator;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    private FullImageRecyclerBinding fullImageRecyclerBinding;
    public String OnOffType,work_id,mcc_id,component_id;
    private FullImageAdapter fullImageAdapter;
    private PrefManager prefManager;
    private  ArrayList<RealTimeMonitoringSystem> activityImage = new ArrayList<>();
    private dbData dbData = new dbData(this);
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullImageRecyclerBinding = DataBindingUtil.setContentView(this, R.layout.full_image_recycler);
        fullImageRecyclerBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefManager = new PrefManager(this);
        OnOffType = getIntent().getStringExtra("OnOffType");
        work_id = getIntent().getStringExtra(AppConstant.WORK_ID);
        mcc_id = getIntent().getStringExtra("mcc_id");
        component_id = getIntent().getStringExtra("component_id");

        fullImageRecyclerBinding.noDataGif.setVisibility(View.VISIBLE);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setVisibility(View.GONE);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setLayoutManager(mLayoutManager);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setItemAnimator(new DefaultItemAnimator());
        fullImageRecyclerBinding.imagePreviewRecyclerview.setHasFixedSize(true);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setNestedScrollingEnabled(false);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setFocusable(false);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setAdapter(fullImageAdapter);

        if(OnOffType.equalsIgnoreCase("Online")) {
            if (Utils.isOnline()) {
                getOnlineImage();
            }
        }
        else {
            //new fetchImagetask().execute();
            new fetchComponentImageTask().execute();
        }

    }
    public class fetchImagetask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {

          /*  final String dcode = prefManager.getDistrictCode();
            final String bcode = prefManager.getBlockCode();
            final String pvcode = prefManager.getPvCode();
            String type_of_work = "", cd_work_no = "", work_type_flag_le = "";*/

            if(OnOffType.equalsIgnoreCase("Offline")){
                //type_of_work = getIntent().getStringExtra(AppConstant.TYPE_OF_WORK);

               /* if (type_of_work.equalsIgnoreCase(AppConstant.ADDITIONAL_WORK)){
                    cd_work_no = getIntent().getStringExtra(AppConstant.CD_WORK_NO);
                    work_type_flag_le = getIntent().getStringExtra(AppConstant.WORK_TYPE_FLAG_LE);
                }*/
                dbData.open();
                activityImage = new ArrayList<>();
                activityImage = dbData.getParticularMCCIDImage(mcc_id);
            }

            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(final ArrayList<RealTimeMonitoringSystem> imageList) {
            super.onPostExecute(imageList);
            setAdapter();
        }
    }
    public class fetchComponentImageTask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {

            if(OnOffType.equalsIgnoreCase("Offline")){

                dbData.open();
                activityImage = new ArrayList<>();
                activityImage = dbData.getParticularMCCIDImage(mcc_id);
            }

            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(final ArrayList<RealTimeMonitoringSystem> imageList) {
            super.onPostExecute(imageList);
            if(imageList.size()>0){
                fullImageRecyclerBinding.noDataGif.setVisibility(View.GONE);
                fullImageRecyclerBinding.imagePreviewRecyclerview.setVisibility(View.VISIBLE);
                setAdapter();
            }
            else {
                fullImageRecyclerBinding.noDataGif.setVisibility(View.VISIBLE);
                fullImageRecyclerBinding.imagePreviewRecyclerview.setVisibility(View.GONE);
            }

        }
    }
    public void homePage() {
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Home", "Home");
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onClick(View view) {

    }

    public void getOnlineImage() {
        try {
            new ApiService(this).makeJSONObjectRequest("OnlineImage", Api.Method.POST, UrlGenerator.getWorkListUrl(), ImagesJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject ImagesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), ImagesListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("utils_ImageEncrydataSet", "" + authKey);
        return dataSet;
    }

    public JSONObject ImagesListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        /*if(getIntent().getStringExtra(AppConstant.TYPE_OF_WORK).equalsIgnoreCase(AppConstant.MAIN_WORK)){
            dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ONLINE_IMAGE_MAIN_WORK_SERVICE_ID);
        }else {
            dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ONLINE_IMAGE_ADDITIONAL_WORK_SERVICE_ID);
            dataSet.put(AppConstant.CD_PORT_WORK_ID_ONLINE_IMAGE, getIntent().getStringExtra(AppConstant.CD_WORK_NO));
        }
        dataSet.put(AppConstant.DISTRICT_CODE, getIntent().getStringExtra(AppConstant.DISTRICT_CODE));
        dataSet.put(AppConstant.BLOCK_CODE, getIntent().getStringExtra(AppConstant.BLOCK_CODE));
        dataSet.put(AppConstant.PV_CODE, getIntent().getStringExtra(AppConstant.PV_CODE));
        dataSet.put(AppConstant.WORK_ID, getIntent().getStringExtra(AppConstant.WORK_ID));
        dataSet.put(AppConstant.WORK_TYPE_FLAG_LE,  getIntent().getStringExtra(AppConstant.WORK_TYPE_FLAG_LE));*/
        dataSet.put(AppConstant.KEY_SERVICE_ID,"details_of_component_photo");
        dataSet.put("mcc_id",mcc_id);
        dataSet.put("component_id",component_id);
        Log.d("utils_imageDataset", "" + dataSet);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("OnlineImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    generateImageArrayList(jsonObject.getJSONArray("IMAGE"));
                    Log.d("Length", "" + jsonObject.getJSONArray("IMAGE").length());
                }
                else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
                    fullImageRecyclerBinding.noDataGif.setVisibility(View.VISIBLE);
                    fullImageRecyclerBinding.imagePreviewRecyclerview.setVisibility(View.GONE);

                }

                Log.d("resp_OnlineImage", "" + responseDecryptedBlockKey);
            }
            if ("DeleteDetails".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                Log.d("Query",responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                    Utils.showAlert(this,getResources().getString(R.string.successfully_deleted));
                    if (Utils.isOnline()) {
                        getOnlineImage();
                    }
                } else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
                    Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                }

                Log.d("DeleteDetails", "" + responseDecryptedSchemeKey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateImageArrayList(JSONArray jsonArray){
        if(jsonArray.length() > 0){
            activityImage = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++ ) {
                try {
                    RealTimeMonitoringSystem imageOnline = new RealTimeMonitoringSystem();
//                    imageOnline.setImageRemark(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_REMARK));
                    //imageOnline.setWorkStageName(jsonArray.getJSONObject(i).getString(AppConstant.WORK_SATGE_NAME));
                    if (!(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE).equalsIgnoreCase("null") || jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE).equalsIgnoreCase(""))) {
                        byte[] decodedString = Base64.decode(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageOnline.setImage(decodedByte);
                        imageOnline.setPhotograph_remark(jsonArray.getJSONObject(i).getString("photograph_remark"));
                        imageOnline.setPhotograph_serial_no(jsonArray.getJSONObject(i).getString("photograph_serial_no"));
                        imageOnline.setMcc_id(mcc_id);
                        activityImage.add(imageOnline);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        if(activityImage.size()>0){
            fullImageRecyclerBinding.noDataGif.setVisibility(View.GONE);
            fullImageRecyclerBinding.imagePreviewRecyclerview.setVisibility(View.VISIBLE);
            setAdapter();
        }
        else {
            fullImageRecyclerBinding.noDataGif.setVisibility(View.VISIBLE);
            fullImageRecyclerBinding.imagePreviewRecyclerview.setVisibility(View.GONE);
        }

    }
    public void setAdapter(){
        if(OnOffType.equalsIgnoreCase("Offline")) {
            fullImageAdapter = new FullImageAdapter(FullImageActivity.this,
                    activityImage, dbData,"Offline");
        }
        else if(OnOffType.equalsIgnoreCase("Online")){
            fullImageAdapter = new FullImageAdapter(FullImageActivity.this,
                    activityImage, dbData,"Online");
        }
/*
        fullImageRecyclerBinding.imagePreviewRecyclerview.addOnItemTouchListener(new FullImageAdapter.RecyclerTouchListener(getApplicationContext(), fullImageRecyclerBinding.imagePreviewRecyclerview, new FullImageAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", activityImage);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
*/
        fullImageRecyclerBinding.imagePreviewRecyclerview.setAdapter(fullImageAdapter);
        fullImageAdapter.notifyDataSetChanged();
    }
    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void DeletePhoto(JSONObject jsonObject){
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
    public void gotoFullImageView(int position){
        Bundle bundle = new Bundle();
        bundle.putSerializable("images", activityImage);
        bundle.putInt("position", position);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");
    }
}
