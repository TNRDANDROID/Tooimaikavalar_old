package com.nic.thooimaikaavalar_old.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.nic.thooimaikaavalar_old.BuildConfig;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.CommonAdapter;
import com.nic.thooimaikaavalar_old.adapter.WorkListAdapter;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ApiService;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityWorkListBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.MyDividerItemDecoration;
import com.nic.thooimaikaavalar_old.support.ProgressHUD;
import com.nic.thooimaikaavalar_old.utils.UrlGenerator;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkListScreen extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener {
    private ActivityWorkListBinding activityWorkListBinding;
    private ShimmerRecyclerView recyclerView;
    private WorkListAdapter workListAdapter;
    public dbData dbData = new dbData(this);
    private SearchView searchView;
    String pref_Scheme, pref_finYear;
    private ArrayList<RealTimeMonitoringSystem> WorkList = new ArrayList<>();
    private List<RealTimeMonitoringSystem> Scheme = new ArrayList<>();
    private List<RealTimeMonitoringSystem> FinYearList = new ArrayList<>();
    private PrefManager prefManager;
    private SQLiteDatabase db;
    public static DBHelper dbHelper;
    private ProgressHUD progressHUD;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWorkListBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_list);
        activityWorkListBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefManager = new PrefManager(this);
        prefManager.setPvCode(getIntent().getStringExtra(AppConstant.PV_CODE));
        setSupportActionBar(activityWorkListBinding.toolbar);
        initRecyclerView();
        workListAdapter = new WorkListAdapter(WorkListScreen.this, WorkList,dbData);
        recyclerView.setAdapter(workListAdapter);
        loadOfflineFinYearListDBValues();
        activityWorkListBinding.finyearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pref_finYear = FinYearList.get(position).getFinancialYear();
                    prefManager.setFinancialyearName(pref_finYear);
                    loadOfflineSchemeListDBValues(pref_finYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        activityWorkListBinding.schemeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pref_Scheme = Scheme.get(position).getSchemeName();
                    String additionalDataStatus = Scheme.get(position).getAdditional_data_status();
                    prefManager.setSchemeName(pref_Scheme);
                    prefManager.setAdditionalDataStatus(additionalDataStatus);
                    WorkList = new ArrayList<>();
                    workListAdapter.notifyDataSetChanged();
                    prefManager.setKeySpinnerSelectedSchemeSeqId(Scheme.get(position).getSchemeSequentialID());
                    if(Utils.isOnline()){
                        getWorkList();
                    }
                    else {
                        // initRecyclerView();
                        new fetchScheduletask().execute();
                    }
//                    JSONObject jsonObject = new JSONObject();
////                    try {
////                        jsonObject.put(AppConstant.KEY_SCHEME_SEQUENTIAL_ID,WorkList.get(position).getSchemeSequentialID());
////                        jsonObject.put(AppConstant.FINANCIAL_YEAR,WorkList.get(position).getFinancialYear());
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initRecyclerView() {
        activityWorkListBinding.workList.setVisibility(View.VISIBLE);
        recyclerView = activityWorkListBinding.workList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

       // new fetchScheduletask().execute();
    }

    public class fetchScheduletask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {
            dbData.open();
            WorkList = new ArrayList<>();
            WorkList = dbData.getAllWorkLIst("fetch",pref_finYear,prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode(),prefManager.getKeySpinnerSelectedSchemeSeqId());
            Log.d("WORKLIST_COUNT", String.valueOf(WorkList.size()));
            return WorkList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> workList) {
            super.onPostExecute(workList);
            if(!Utils.isOnline()) {
                if (workList.size() == 0) {
                    Utils.showAlert(WorkListScreen.this, "No Data Available in Local Database. Please, Turn On mobile data");
                }
            }
            for (int i=0;i<WorkList.size();i++){
                for (int j=i+1;j<WorkList.size();j++){
                    if(WorkList.get(i).getWorkId().equals(WorkList.get(j).getWorkId())){
                        WorkList.remove(j);
                        j--;
                    }
                }
            }
            workListAdapter = new WorkListAdapter(WorkListScreen.this, WorkList,dbData);
            recyclerView.setAdapter(workListAdapter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 2000);

        }

        private void loadCards() {

            recyclerView.hideShimmerAdapter();
        }
    }

    public void loadOfflineFinYearListDBValues() {
        FinYearList.clear();
        Cursor FinYear = null;
        FinYear = db.rawQuery("select * from " + DBHelper.FINANCIAL_YEAR_TABLE_NAME, null);

        RealTimeMonitoringSystem finYearListValue = new RealTimeMonitoringSystem();
        finYearListValue.setFinancialYear("Select Financial year");
        FinYearList.add(finYearListValue);
        if (FinYear.getCount() > 0) {
            if (FinYear.moveToFirst()) {
                do {
                    RealTimeMonitoringSystem finyearList = new RealTimeMonitoringSystem();
                    String financialYear = FinYear.getString(FinYear.getColumnIndexOrThrow(AppConstant.FINANCIAL_YEAR));
                    finyearList.setFinancialYear(financialYear);
                    FinYearList.add(finyearList);
                } while (FinYear.moveToNext());
            }
        }

        activityWorkListBinding.finyearSpinner.setAdapter(new CommonAdapter(this, FinYearList, "FinYearList"));
    }

    public void loadOfflineSchemeListDBValues(String fin_year) {
        Cursor SchemeList = null;
        String Qury = "SELECT * FROM " + DBHelper.SCHEME_TABLE_NAME + " where fin_year = '" + fin_year + "'";
        Log.d("Schemequery",""+Qury);
        SchemeList = db.rawQuery(Qury, null);

        Scheme.clear();
        RealTimeMonitoringSystem schemeListValue = new RealTimeMonitoringSystem();
        schemeListValue.setSchemeName("Select Scheme");
        Scheme.add(schemeListValue);
        if (SchemeList.getCount() > 0) {
            if (SchemeList.moveToFirst()) {
                do {
                    RealTimeMonitoringSystem schemeList = new RealTimeMonitoringSystem();
                    Integer schemeSequentialID = SchemeList.getInt(SchemeList.getColumnIndexOrThrow(AppConstant.KEY_SCHEME_SEQUENTIAL_ID));
                    String schemeName = SchemeList.getString(SchemeList.getColumnIndexOrThrow(AppConstant.KEY_SCHEME_NAME));
                    String financial_year = SchemeList.getString(SchemeList.getColumnIndexOrThrow(AppConstant.FINANCIAL_YEAR));
                    String additional_data_status = SchemeList.getString(SchemeList.getColumnIndexOrThrow(AppConstant.KEY_ADDITIONAL_DATA_STATUS));
                    schemeList.setSchemeSequentialID(schemeSequentialID);
                    schemeList.setSchemeName(schemeName);
                    schemeList.setFinancialYear(financial_year);
                    schemeList.setAdditional_data_status(additional_data_status);
                    Scheme.add(schemeList);

                } while (SchemeList.moveToNext());
            }
        }
        activityWorkListBinding.schemeSpinner.setAdapter(new CommonAdapter(this, Scheme, "SchemeList"));
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
                workListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                workListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
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
    public void onClick(View view) {

    }

    public void getWorkList() {
        try {
            new ApiService(this).makeJSONObjectRequest("WorkList", Api.Method.POST, UrlGenerator.getWorkListUrl(), workListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject workListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.workListBlockWiseJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("workList", "" + authKey);
        return dataSet;
    }

    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("WorkList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                Log.d("Query",responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertWorkListTask().execute(jsonObject.getJSONObject(AppConstant.JSON_DATA));
                    JSONArray jsondata = jsonObject.getJSONObject(AppConstant.JSON_DATA).getJSONArray(AppConstant.ADDITIONAL_WORK);
                    if (jsondata.length() > 0) {
                        new InsertAdditioanlListTask().execute(jsonObject.getJSONObject(AppConstant.JSON_DATA));
                    }
                    JSONArray jsondata2 = jsonObject.getJSONObject(AppConstant.JSON_DATA).getJSONArray("work_additional_details");
                    if (jsondata2.length() > 0) {
                        new InsertAdditioanlDetailsListTask().execute(jsonObject.getJSONObject(AppConstant.JSON_DATA));
                    }
                    new fetchScheduletask().execute();
                } else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
//                    dbData.open();
//                    if(Utils.isOnline()){
//                        dbData.deleteWorkListTable();
//                    }
                    new fetchScheduletask().execute();
                    Utils.showAlert(this,"NO RECORD FOUND!");
                }
                String authKey = responseDecryptedSchemeKey.toString();
                int maxLogSize = 4000;
                for(int i = 0; i <= authKey.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i+1) * maxLogSize;
                    end = end > authKey.length() ? authKey.length() : end;
                    Log.v("to_send", authKey.substring(start, end));
                }
                Log.d("WorkListResp", "" + responseDecryptedSchemeKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class InsertWorkListTask extends AsyncTask<JSONObject, Void, Void> {

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

                   // db.execSQL("delete from "+ DBHelper.WORK_LIST_TABLE_BASED_ON_FINYEAR_VIlLAGE);
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.MAIN_WORK);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RealTimeMonitoringSystem workList = new RealTimeMonitoringSystem();
                        try {
                            workList.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                            workList.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            workList.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                            workList.setWorkId(jsonArray.getJSONObject(i).getInt(AppConstant.WORK_ID));
                            workList.setSchemeGroupID(jsonArray.getJSONObject(i).getInt(AppConstant.SCHEME_GROUP_ID));
                            workList.setSchemeID(jsonArray.getJSONObject(i).getInt(AppConstant.SCHEME_ID));
                            workList.setSchemeGroupName(jsonArray.getJSONObject(i).getString(AppConstant.SCHEME_GROUP_NAME));
                            workList.setSchemeName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_SCHEME_NAME));
                            workList.setFinancialYear(jsonArray.getJSONObject(i).getString(AppConstant.FINANCIAL_YEAR));
                            workList.setAgencyName(jsonArray.getJSONObject(i).getString(AppConstant.AGENCY_NAME));
                            workList.setWorkGroupNmae(jsonArray.getJSONObject(i).getString(AppConstant.WORK_GROUP_NAME));
                            workList.setWorkName(jsonArray.getJSONObject(i).getString(AppConstant.WORK_NAME));
                            workList.setWorkGroupID(jsonArray.getJSONObject(i).getString(AppConstant.WORK_GROUP_ID));
                            workList.setWorkTypeID(jsonArray.getJSONObject(i).getString(AppConstant.WORK_TYPE));
                            workList.setCurrentStage(jsonArray.getJSONObject(i).getInt(AppConstant.CURRENT_STAGE_OF_WORK));
                            workList.setIntialAmount(jsonArray.getJSONObject(i).getString(AppConstant.AS_VALUE));
                            workList.setAmountSpendSoFar(jsonArray.getJSONObject(i).getString(AppConstant.AMOUNT_SPENT_SOFAR));
                            workList.setStageName(jsonArray.getJSONObject(i).getString(AppConstant.STAGE_NAME));
                            workList.setBeneficiaryName(jsonArray.getJSONObject(i).getString(AppConstant.BENEFICIARY_NAME));
                            workList.setBeneficiaryFatherName(jsonArray.getJSONObject(i).getString(AppConstant.BENEFICIARY_FATHER_NAME));
                            workList.setWorkTypeName(jsonArray.getJSONObject(i).getString(AppConstant.WORK_TYPE_NAME));
                            workList.setYnCompleted(jsonArray.getJSONObject(i).getString(AppConstant.YN_COMPLETED));
                            workList.setCdProtWorkYn(jsonArray.getJSONObject(i).getString(AppConstant.CD_PROT_WORK_YN));
                            workList.setStateCode(jsonArray.getJSONObject(i).getInt(AppConstant.STATE_CODE));
                            workList.setDistrictName(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_NAME));
                            workList.setBlockName(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_NAME));
                            workList.setPvName(jsonArray.getJSONObject(i).getString(AppConstant.PV_NAME));
                            workList.setCommunity(jsonArray.getJSONObject(i).getString(AppConstant.COMMUNITY_NAME));
                            workList.setGender(jsonArray.getJSONObject(i).getString(AppConstant.GENDER));
                            workList.setLastVisitedDate(jsonArray.getJSONObject(i).getString(AppConstant.LAST_VISITED_DATE));
                            workList.setImageAvailable(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_AVAILABLE));

                            dbData.insertWorkList(workList);

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
            progressHUD = ProgressHUD.show(WorkListScreen.this, "Downloading", true, false, null);

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressHUD!=null){
                progressHUD.cancel();
            }
        }
    }

    public class InsertAdditioanlListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            dbData.open();
//            if(Utils.isOnline()){
//                dbData.deleteAdditionalListTable();
//            }
            ArrayList<RealTimeMonitoringSystem> workList_count = dbData.getAllAdditionalWork("",pref_finYear,prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode(),prefManager.getKeySpinnerSelectedSchemeSeqId());
            if (workList_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.ADDITIONAL_WORK);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RealTimeMonitoringSystem additioanlList = new RealTimeMonitoringSystem();
                        try {

                            additioanlList.setDistictCode(prefManager.getDistrictCode());
                            additioanlList.setBlockCode(prefManager.getBlockCode());
                            additioanlList.setPvCode(prefManager.getPvCode());
                            additioanlList.setSchemeID(jsonArray.getJSONObject(i).getInt(AppConstant.SCHEME_ID));
                            additioanlList.setFinancialYear(jsonArray.getJSONObject(i).getString(AppConstant.FINANCIAL_YEAR));
                            additioanlList.setWorkId(jsonArray.getJSONObject(i).getInt(AppConstant.WORK_ID));
                            additioanlList.setWorkGroupID(jsonArray.getJSONObject(i).getString(AppConstant.WORK_GROUP_ID));
                            additioanlList.setRoadName(jsonArray.getJSONObject(i).getString(AppConstant.ROAD_NAME));
                            additioanlList.setCdWorkNo(jsonArray.getJSONObject(i).getInt(AppConstant.CD_WORK_NO));
                            additioanlList.setCdCode(jsonArray.getJSONObject(i).getInt(AppConstant.CD_CODE));
                            additioanlList.setCdName(jsonArray.getJSONObject(i).getString(AppConstant.CD_NAME));
                            additioanlList.setChainageMeter(jsonArray.getJSONObject(i).getString(AppConstant.CHAINAGE_METER));
                            additioanlList.setCurrentStage(jsonArray.getJSONObject(i).getInt(AppConstant.CURRENT_STAGE_OF_WORK));
                            additioanlList.setCdTypeId(jsonArray.getJSONObject(i).getInt(AppConstant.CD_TYPE_ID));
                            additioanlList.setWorkTypeFlagLe(jsonArray.getJSONObject(i).getString(AppConstant.WORK_TYPE_FLAG_LE));
                            additioanlList.setWorkStageName(jsonArray.getJSONObject(i).getString(AppConstant.WORK_SATGE_NAME));
                            additioanlList.setImageAvailable(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_AVAILABLE));


                            dbData.insertAdditionalWorkList(additioanlList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
           }
            return null;
        }
    }
    public class InsertAdditioanlDetailsListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            dbData.open();
            if(Utils.isOnline()){
                dbData.deleteAdditionalDetailsListTable();
            }
            ArrayList<RealTimeMonitoringSystem> workList_count = dbData.selectAdditionalDetailsList("","","get");
            if (workList_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray("work_additional_details");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RealTimeMonitoringSystem additioanlList = new RealTimeMonitoringSystem();
                        try {

                            additioanlList.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                            additioanlList.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            additioanlList.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                            additioanlList.setWorkId(jsonArray.getJSONObject(i).getInt(AppConstant.WORK_ID));
                            additioanlList.setWorkGroupID(jsonArray.getJSONObject(i).getString(AppConstant.WORK_GROUP_ID));
                            additioanlList.setWorkTypeID(jsonArray.getJSONObject(i).getString(AppConstant.WORK_TYPE));
                            String workTypeID=jsonArray.getJSONObject(i).getString(AppConstant.WORK_TYPE);

                            if((BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"))){
                                if(workTypeID.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_PRODUCTION)){
                                    additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                    additioanlList.setHeight(jsonArray.getJSONObject(i).getString("height_of_weir_in_m"));
                                    additioanlList.setLength_water_storage(jsonArray.getJSONObject(i).getString("length_of_water_storage_in_upstream_side_of_channel_in_m"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_PRODUCTION)){
                                    additioanlList.setNo_trenches_per_worksite(jsonArray.getJSONObject(i).getString("no_of_trenches_per_one_work_site"));
                                    additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                    additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                    additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));

                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_PRODUCTION)){
                                    additioanlList.setNo_units_per_waterbody(jsonArray.getJSONObject(i).getString("no_of_units_per_waterbody"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_PRODUCTION)){
                                    additioanlList.setNo_units(jsonArray.getJSONObject(i).getString("no_of_units"));
                                    additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                    additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                    additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_PRODUCTION)){
                                    additioanlList.setArea_covered(jsonArray.getJSONObject(i).getString("area_covered_in_hect"));
                                    additioanlList.setNo_plantation_per_site(jsonArray.getJSONObject(i).getString("no_of_plantation_per_site"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_PRODUCTION)){
                                    additioanlList.setNo_units(jsonArray.getJSONObject(i).getString("no_of_units"));
                                    additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                    additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                    additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_PRODUCTION)){
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Soakpit_PRODUCTION)){
                                    additioanlList.setIndividual_community(jsonArray.getJSONObject(i).getString("soakpit_type"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_PRODUCTION)){
                                    additioanlList.setLength_channel(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));

                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_LOCAL)){
                                    additioanlList.setStructure_sump_available_status(jsonArray.getJSONObject(i).getString("is_storage_structure_or_sump_available"));
                                    additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                    additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                    additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));

                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_Pit_PRODUCTION)){
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }
                                else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_PERCOLATION_POND)){
                                    additioanlList.setNo_units(jsonArray.getJSONObject(i).getString("no_of_units"));
                                    additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                    additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                    additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                    additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                    additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                    additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                                }

                            }
                            else {
                            if(workTypeID.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_LOCAL)){
                                additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                additioanlList.setHeight(jsonArray.getJSONObject(i).getString("height_of_weir_in_m"));
                                additioanlList.setLength_water_storage(jsonArray.getJSONObject(i).getString("length_of_water_storage_in_upstream_side_of_channel_in_m"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL)){
                                additioanlList.setNo_trenches_per_worksite(jsonArray.getJSONObject(i).getString("no_of_trenches_per_one_work_site"));
                                additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));

                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL)){
                                additioanlList.setNo_units_per_waterbody(jsonArray.getJSONObject(i).getString("no_of_units_per_waterbody"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_LOCAL)){
                                additioanlList.setNo_units(jsonArray.getJSONObject(i).getString("no_of_units"));
                                additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_LOCAL)){
                                additioanlList.setArea_covered(jsonArray.getJSONObject(i).getString("area_covered_in_hect"));
                                additioanlList.setNo_plantation_per_site(jsonArray.getJSONObject(i).getString("no_of_plantation_per_site"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_LOCAL)){
                                additioanlList.setNo_units(jsonArray.getJSONObject(i).getString("no_of_units"));
                                additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_LOCAL)){
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Soakpit_LOCAL)){
                                additioanlList.setIndividual_community(jsonArray.getJSONObject(i).getString("soakpit_type"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));
                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_LOCAL)){
                                additioanlList.setLength_channel(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));

                            }
                            else if(workTypeID.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_LOCAL)){
                                additioanlList.setStructure_sump_available_status(jsonArray.getJSONObject(i).getString("is_storage_structure_or_sump_available"));
                                additioanlList.setLength(jsonArray.getJSONObject(i).getString("length_or_diameter_in_m"));
                                additioanlList.setWidth(jsonArray.getJSONObject(i).getString("width_or_diameter_in_m"));
                                additioanlList.setDepth(jsonArray.getJSONObject(i).getString("depth_in_m"));
                                additioanlList.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                additioanlList.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                additioanlList.setObservation(jsonArray.getJSONObject(i).getString("observation_premonsoon_gw_depth_in_m"));

                            }
                            }

                            dbData.insertAdditionalDetailsList(additioanlList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
           }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        workListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(workListAdapter);
    }
}

