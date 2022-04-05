package com.nic.thooimaikaavalar_old.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.WasteCollectedAdapterServer;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ApiService;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityViewWasteCollectedDetailsBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.utils.DateInterface;
import com.nic.thooimaikaavalar_old.utils.UrlGenerator;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ViewWasteCollectedDetails extends AppCompatActivity implements Api.ServerResponseListener, DateInterface {

    ActivityViewWasteCollectedDetailsBinding activity_view_waste_collected_details;
    private PrefManager prefManager;
    public com.nic.thooimaikaavalar_old.dataBase.dbData dbData = new dbData(this);
    ArrayList<RealTimeMonitoringSystem> wasteCollectedList;
    WasteCollectedAdapterServer wasteCollectedAdapterServer;
    int year=0,day=0,month=0;
    String fromDate,toDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_view_waste_collected_details = DataBindingUtil.setContentView(this, R.layout.activity_view_waste_collected_details);
        activity_view_waste_collected_details.setActivity(this);
        prefManager = new PrefManager(this);
        initializeUI();
        activity_view_waste_collected_details.dateOfSave.setText(getCurrentDate()+" to "+getCurrentDate());
        if(Utils.isOnline()){
            get_details_of_swachh_bharat_view("Today");
        }
        else {
            onBackPressed();
        }

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        activity_view_waste_collected_details.wasteCollectedRecycler.addItemDecoration(itemDecoration);

        activity_view_waste_collected_details.dateOfCommencementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDatePickerDialog(ViewWasteCollectedDetails.this);
            }
        });
    }

    public void initializeUI(){
        String date_of_commencement= dateFormate(getIntent().getStringExtra("date_of_commencement"),"yes");
        activity_view_waste_collected_details.villageName.setText("Village : "+getIntent().getStringExtra("village_name"));
        activity_view_waste_collected_details.mccName.setText("MCC Name : "+getIntent().getStringExtra("mcc_name"));
        activity_view_waste_collected_details.dateOfCommencement.setText("Date of Commencement : "+date_of_commencement);
        activity_view_waste_collected_details.sampleRl.setVisibility(View.GONE);
    }
    public  String dateFormate( String strDate,String type ){
        try {
            android.icu.text.SimpleDateFormat sdfSource =null;
            if(type.equals("")) {
                // create SimpleDateFormat object with source string date format
                sdfSource = new android.icu.text.SimpleDateFormat(
                        "dd-M-yyyy");
            }
            else {
                sdfSource = new android.icu.text.SimpleDateFormat(
                        "yyyy-mm-dd");
            }

            // parse the string into Date object
            Date date = sdfSource.parse(strDate);

            // create SimpleDateFormat object with desired date format
            android.icu.text.SimpleDateFormat sdfDestination = new SimpleDateFormat(
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

    public void get_details_of_swachh_bharat_view(String type) {
        try {
            new ApiService(this).makeJSONObjectRequest("details_of_swachh_bharat_view", Api.Method.POST, UrlGenerator.getWorkListUrl(), details_of_swachh_bharat_viewParams(type), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject details_of_swachh_bharat_viewParams(String type) throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), details_of_swachh_bharat_viewJsonParams(type).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("JSONRequest", "" + authKey);
        return dataSet;
    }

    private JSONObject details_of_swachh_bharat_viewJsonParams(String type) {
        JSONObject dataSet = new JSONObject();
        try {

            dataSet.put(AppConstant.KEY_SERVICE_ID, "details_of_swachh_bharat_view");
            dataSet.put("pvcode",getIntent().getStringExtra("pvcode"));
            dataSet.put("mcc_id",getIntent().getStringExtra("mcc_id"));
            if(type.equals("Today")){
                dataSet.put("from_date",getCurrentDate());
                dataSet.put("to_date",getCurrentDate());
            }
            else {
                //dataSet.put("date",activity_view_waste_collected_details.dateOfSave.getText().toString());
                dataSet.put("from_date",fromDate);
                dataSet.put("to_date",toDate);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("JSONRequest",dataSet.toString());
        return dataSet;
    }

    public void DeleteData(JSONObject jsonObject){
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), jsonObject.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);
            Log.d("EncryptedDetails", "" + dataSet);
            Log.d("JSONDetails", "" + jsonObject);
            try {
                new ApiService(this).makeJSONObjectRequest("details_of_swachh_bharat_delete", Api.Method.POST, UrlGenerator.getWorkListUrl(), dataSet, "not cache", this);
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

            if ("details_of_swachh_bharat_view".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insertdetails_of_swachh_bharat_view().execute(jsonObject);
                }
                Log.d("details_of_swachh_bharat_view", "" + responseDecryptedBlockKey);
            }
            if ("details_of_swachh_bharat_delete".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                    get_details_of_swachh_bharat_view("");
                }
                Log.d("details_of_swachh_bharat_delete", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void getDate(String dateValue) {
        String[] separated = dateValue.split(":");
        fromDate = separated[0]; // this will contain "Fruit"
        toDate = separated[1];
        activity_view_waste_collected_details.dateOfSave.setText(fromDate+" to "+toDate);
//        getInCompleteActivityList();
        if(Utils.isOnline()) {
            get_details_of_swachh_bharat_view("");
        }
        else {
            Utils.showAlert(ViewWasteCollectedDetails.this,"No Internet Connection");
        }
    }

    public class Insertdetails_of_swachh_bharat_view extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    wasteCollectedList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RealTimeMonitoringSystem waste_collected_detail = new RealTimeMonitoringSystem();
                        try {
                            waste_collected_detail.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                            waste_collected_detail.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                            waste_collected_detail.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                            waste_collected_detail.setMcc_id(jsonArray.getJSONObject(i).getString("mcc_id"));
                            waste_collected_detail.setHouseholds_waste(jsonArray.getJSONObject(i).getString("households_waste"));
                            waste_collected_detail.setShops_waste(jsonArray.getJSONObject(i).getString("shops_waste"));
                            waste_collected_detail.setMarket_waste(jsonArray.getJSONObject(i).getString("market_waste"));
                            waste_collected_detail.setHotels_waste(jsonArray.getJSONObject(i).getString("hotels_waste"));
                            waste_collected_detail.setOthers_waste(jsonArray.getJSONObject(i).getString("others_waste"));
                            waste_collected_detail.setTot_bio_waste_collected(jsonArray.getJSONObject(i).getString("tot_bio_waste_collected"));
                            waste_collected_detail.setTot_bio_waste_shredded(jsonArray.getJSONObject(i).getString("tot_bio_waste_shredded"));
                            waste_collected_detail.setTot_bio_compost_produced(jsonArray.getJSONObject(i).getString("tot_bio_compost_produced"));
                            waste_collected_detail.setTot_bio_compost_sold(jsonArray.getJSONObject(i).getString("tot_bio_compost_sold"));
                            waste_collected_detail.setDate_of_save(jsonArray.getJSONObject(i).getString("date"));
                            waste_collected_detail.setMcc_name(getIntent().getStringExtra("mcc_name"));
                            waste_collected_detail.setPvName(getIntent().getStringExtra("village_name"));

                            wasteCollectedList.add(waste_collected_detail);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(wasteCollectedList.size()>0){
                activity_view_waste_collected_details.wasteCollectedRecycler.setVisibility(View.VISIBLE);
                activity_view_waste_collected_details.wasteCollectedRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                wasteCollectedAdapterServer = new WasteCollectedAdapterServer(wasteCollectedList,ViewWasteCollectedDetails.this,dbData);
                activity_view_waste_collected_details.wasteCollectedRecycler.setAdapter(wasteCollectedAdapterServer);
            }
            else {
                activity_view_waste_collected_details.wasteCollectedRecycler.setVisibility(View.GONE);
            }
        }
    }


    public String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }


}
