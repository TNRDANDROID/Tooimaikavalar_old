package com.nic.thooimaikaavalar_old.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.CapacityAdapter;
import com.nic.thooimaikaavalar_old.adapter.CommonAdapter;
import com.nic.thooimaikaavalar_old.adapter.NoOfThooimaiKaavalarList;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ApiService;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityNewMainPageBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.MyLocationListener;
import com.nic.thooimaikaavalar_old.support.ProgressHUD;
import com.nic.thooimaikaavalar_old.utils.CameraUtils;
import com.nic.thooimaikaavalar_old.utils.UrlGenerator;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class NewMainPage extends AppCompatActivity implements Api.ServerResponseListener {

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;

    LocationManager mlocManager = null;
    LocationListener mlocListener;
    Double offlatTextValue, offlongTextValue;


    public dbData dbData = new dbData(this);
    private PrefManager prefManager;
    private SQLiteDatabase db;
    public static DBHelper dbHelper;
    private ProgressHUD progressHUD;
    private ShimmerRecyclerView recyclerView;
    private ShimmerRecyclerView recyclerView1;
    CapacityAdapter capacityAdapter;
    NoOfThooimaiKaavalarList noOfThooimaiKaavalarList;
    int capacity_position,no_of_thooimai_kavalar_count;
    String capacity_name ="", thooimai_kavalar_name="",capacity_id;
    ArrayList<RealTimeMonitoringSystem> capacityListNew;
    ArrayList<RealTimeMonitoringSystem> thooiMaiKavaalarListNew;
    private List<RealTimeMonitoringSystem> HabitationOrdered  = new ArrayList<>();
    private List<RealTimeMonitoringSystem> Habitation = new ArrayList<>();
    private List<RealTimeMonitoringSystem> waterSupplyList = new ArrayList<>();
    private List<RealTimeMonitoringSystem> waterSupplyListOrdered = new ArrayList<>();
    String water_supply_id="";
    String water_supply_others_name="";
    String mcc_center_image="";
    int year=0,day=0,month=0;

    //////edit flag///
    String new_mcc_id="";
    String new_mcc_name="";
    String new_capacity_id="";
    String new_capacity_name="";
    String new_water_supply_availability_id="";
    String new_water_supply_availability_name="";
    String new_water_supply_availability_other="";
    String new_mcc_center_image="";
    String new_date_of_commencement="";
    //////********** ///

    public ActivityNewMainPageBinding activityNewMainPageBinding;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        activityNewMainPageBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_main_page);
        activityNewMainPageBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        prefManager = new PrefManager(this);

        activityNewMainPageBinding.pleaseSpecify.setVisibility(View.GONE);
        activityNewMainPageBinding.specifyLayout.setVisibility(View.GONE);
        //habitationFilterSpinner(prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode());
        waterSupplySpinner();
        initCapacityRecyler();
        activityNewMainPageBinding.habitationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    prefManager.setHabCode(Habitation.get(position).getHabCode());

                }
                else {
                    prefManager.setHabCode("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activityNewMainPageBinding.waterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    water_supply_id = waterSupplyList.get(i).getKEY_ID();
                    if (water_supply_id.equals("3")) {
                        activityNewMainPageBinding.pleaseSpecify.setVisibility(View.VISIBLE);
                        activityNewMainPageBinding.specifyLayout.setVisibility(View.VISIBLE);
                    } else {
                        activityNewMainPageBinding.pleaseSpecify.setVisibility(View.GONE);
                        activityNewMainPageBinding.specifyLayout.setVisibility(View.GONE);

                    }
                }
                else {
                    water_supply_id="";
                    activityNewMainPageBinding.pleaseSpecify.setVisibility(View.GONE);
                    activityNewMainPageBinding.specifyLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        activityNewMainPageBinding.dateOfCommencementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickdate();
            }
        });

        activityNewMainPageBinding.captureMccLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLatLong();
            }
        });

        activityNewMainPageBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFieldCondition();
            }
        });


        activityNewMainPageBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initializeUI(){
        prefManager.setPvCode(getIntent().getStringExtra(AppConstant.PV_CODE));
        new_mcc_id = getIntent().getStringExtra("mcc_id");
        capacity_id = getIntent().getStringExtra("capacity_id");
        new_capacity_name= getIntent().getStringExtra("capacity_name");
        new_mcc_name = getIntent().getStringExtra("mcc_name");
        water_supply_id = getIntent().getStringExtra("water_supply_availability_id");
        new_water_supply_availability_other = getIntent().getStringExtra("water_supply_availability_other");
        new_date_of_commencement = getIntent().getStringExtra("date_of_commencement");
        mcc_center_image = getIntent().getStringExtra("center_image");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!new_mcc_id.equals("")){
                    offlatTextValue = Double.valueOf(getIntent().getStringExtra("latitude"));
                    offlongTextValue = Double.valueOf(getIntent().getStringExtra("longtitude"));
                    activityNewMainPageBinding.mccName.setText(new_mcc_name);
                    int pos=-1;
                    int water_pos =-1;
                    for (int i = 0; i < capacityListNew.size(); i++) {
                        if (capacityListNew.get(i).getKEY_ID().equals(capacity_id)) {
                            pos = i;
                            // break;  // uncomment to get the first instance
                        }
                    }
                    for (int i = 0; i < waterSupplyList.size(); i++) {
                        if (waterSupplyList.get(i).getKEY_ID().equals(water_supply_id)) {
                            water_pos = i;
                            // break;  // uncomment to get the first instance
                        }
                    }
                    recyclerView.findViewHolderForAdapterPosition(pos).itemView.performClick();
                    activityNewMainPageBinding.waterSpinner.setSelection(water_pos);
                    activityNewMainPageBinding.specifyText.setText(new_water_supply_availability_other);
                    activityNewMainPageBinding.dateOfCommencement.setText(dateFormate(new_date_of_commencement,"yes"));
                    if(mcc_center_image!=null&&!mcc_center_image.equals("")){
                        activityNewMainPageBinding.mccCenterImage.setVisibility(View.VISIBLE);
                        activityNewMainPageBinding.mccCaptureIcon.setVisibility(View.GONE);
                        activityNewMainPageBinding.mccCenterImage.setImageBitmap(StringToBitMap(mcc_center_image));
                    }
                }
            }
        },1000);

    }

    private void initCapacityRecyler() {
        recyclerView = activityNewMainPageBinding.capacityList;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new fetchScheduletask().execute();
            }
        },1000);
    }
    private void initNoOfThooimaiKaavalarRecyler() {
        recyclerView1 = activityNewMainPageBinding.noOfThooimaiKaavalrList;
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setNestedScrollingEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new fetchNoOfThooimaiKaavalartask().execute();
            }
        },2000);
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        String urlType = serverResponse.getApi();
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("SaveDetails".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                }
                Log.d("SaveDetails", "" + responseDecryptedBlockKey);
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
            ArrayList<RealTimeMonitoringSystem> capacityList = new ArrayList<>();
            capacityList = dbData.getAll_capacity_of_mcc();
            Log.d("Capacity_COUNT", String.valueOf(capacityList.size()));
            return capacityList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> capacityList) {
            super.onPostExecute(capacityList);
            capacityListNew = new ArrayList<>();
            capacityListNew.addAll(capacityList);

            capacityAdapter = new CapacityAdapter(capacityList,NewMainPage.this);
            recyclerView.setAdapter(capacityAdapter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 1000);
        }
    }
    public class fetchNoOfThooimaiKaavalartask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {
            dbData.open();
            ArrayList<RealTimeMonitoringSystem> capacityList = new ArrayList<>();
            capacityList = dbData.getAll_thooimai_kaavalars();
            Log.d("ThooimaiKaaval_COUNT", String.valueOf(capacityList.size()));
            return capacityList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> capacityList) {
            super.onPostExecute(capacityList);
            thooiMaiKavaalarListNew = new ArrayList<>();
            thooiMaiKavaalarListNew.addAll(capacityList);
            noOfThooimaiKaavalarList = new NoOfThooimaiKaavalarList(capacityList,NewMainPage.this);
            recyclerView1.setAdapter(noOfThooimaiKaavalarList);
            recyclerView1.showShimmerAdapter();
            recyclerView1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards1();
                }
            }, 1000);
        }
    }
    private void loadCards() {

        recyclerView.hideShimmerAdapter();
        initializeUI();
        //initNoOfThooimaiKaavalarRecyler();

    }
    private void loadCards1() {
        recyclerView1.hideShimmerAdapter();
    }

    public void capacityItemClicked(int position){
        capacity_name = capacityListNew.get(position).getKEY_capacity_of_mcc_name();
        capacity_id = capacityListNew.get(position).getKEY_ID();
    }
    public void thooiMaiKaavalarItemClicked(int position){
        if(!capacity_name.equals("")) {
            thooimai_kavalar_name = thooiMaiKavaalarListNew.get(position).getKEY_thooimai_kaavalars_name();
            no_of_thooimai_kavalar_count= Integer.parseInt(thooimai_kavalar_name.substring(thooimai_kavalar_name.length()-1));
            Intent intent =new Intent(NewMainPage.this,AddThooimaiKaavalarDetails.class);
            intent.putExtra("count",no_of_thooimai_kavalar_count);
            startActivity(intent);
        }
        else {
            Utils.showAlert(NewMainPage.this,getResources().getString(R.string.choose_capacity_of_mcc_in_tonnes));
            //initNoOfThooimaiKaavalarRecyler();
        }
    }

    public void habitationFilterSpinner(String dcode,String bcode, String pvcode) {
        Cursor HABList = null;
        HABList = db.rawQuery("SELECT * FROM " + DBHelper.HABITATION_TABLE_NAME + " where dcode = '" + dcode + "'and bcode = '" + bcode + "' and pvcode = '" + pvcode + "' order by habitation_name asc", null);

        Habitation.clear();
        HabitationOrdered.clear();

        if (HABList.getCount() > 0) {
            if (HABList.moveToFirst()) {
                do {
                    RealTimeMonitoringSystem habList = new RealTimeMonitoringSystem();
                    String districtCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE));
                    String blockCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.BLOCK_CODE));
                    String pvCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String habCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABB_CODE));
                    String habName = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABITATION_NAME));
                    String habName_ta = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABITATION_NAME_TA));

                    habList.setDistictCode(districtCode);
                    habList.setBlockCode(blockCode);
                    habList.setPvCode(pvCode);
                    habList.setHabCode(habCode);
                    habList.setHabitationName(habName);
                    habList.setHabitationNameTa(habName_ta);

                    HabitationOrdered.add(habList);
                } while (HABList.moveToNext());
            }
            Log.d("Habitationspinnersize", "" + HabitationOrdered.size());

        }
        Collections.sort(HabitationOrdered, (lhs, rhs) -> lhs.getHabitationNameTa().compareTo(rhs.getHabitationNameTa()));
        RealTimeMonitoringSystem habitationListValue = new RealTimeMonitoringSystem();
        habitationListValue.setHabitationName(getResources().getString(R.string.select_habitation));
        habitationListValue.setHabitationNameTa(getResources().getString(R.string.select_habitation));
        Habitation.add(habitationListValue);
        for (int i = 0; i < HabitationOrdered.size(); i++) {
            RealTimeMonitoringSystem habList = new RealTimeMonitoringSystem();
            String districtCode = HabitationOrdered.get(i).getDistictCode();
            String blockCode = HabitationOrdered.get(i).getBlockCode();
            String pvCode = HabitationOrdered.get(i).getPvCode();
            String habCode = HabitationOrdered.get(i).getHabCode();
            String habName = HabitationOrdered.get(i).getHabitationName();
            String habName_ta = HabitationOrdered.get(i).getHabitationNameTa();

            habList.setDistictCode(districtCode);
            habList.setBlockCode(blockCode);
            habList.setPvCode(pvCode);
            habList.setHabCode(habCode);
            habList.setHabitationName(habName);
            habList.setHabitationNameTa(habName_ta);

            Habitation.add(habList);
        }
        activityNewMainPageBinding.habitationSpinner.setAdapter(new CommonAdapter(this, Habitation, "HabitationList"));
    }
    public void waterSupplySpinner(){

        Cursor waterSupplyListDb = null;
        waterSupplyListDb = db.rawQuery("SELECT * FROM " + DBHelper.SWM_WATER_SUPPLY_AVAILABILITY_LIST ,null);

        waterSupplyList.clear();
        waterSupplyListOrdered.clear();

        if (waterSupplyListDb.getCount() > 0) {
            if (waterSupplyListDb.moveToFirst()) {
                do {
                    RealTimeMonitoringSystem habList = new RealTimeMonitoringSystem();
                    String id = waterSupplyListDb.getString(waterSupplyListDb.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String water_supply_name = waterSupplyListDb.getString(waterSupplyListDb.getColumnIndexOrThrow(AppConstant.KEY_water_supply_availability_name));

                    habList.setKEY_ID(id);
                    habList.setKEY_water_supply_availability_name(water_supply_name);

                    waterSupplyListOrdered.add(habList);
                } while (waterSupplyListDb.moveToNext());
            }
            Log.d("waterSupplyListSize", "" + waterSupplyListOrdered.size());

        }
        Collections.sort(waterSupplyListOrdered, (lhs, rhs) -> lhs.getKEY_water_supply_availability_name().compareTo(rhs.getKEY_water_supply_availability_name()));
        RealTimeMonitoringSystem habitationListValue = new RealTimeMonitoringSystem();
        habitationListValue.setKEY_ID("0");
        habitationListValue.setKEY_water_supply_availability_name(getResources().getString(R.string.select_water_supply));
        waterSupplyList.add(habitationListValue);
        for (int i = 0; i < waterSupplyListOrdered.size(); i++) {
            RealTimeMonitoringSystem habList = new RealTimeMonitoringSystem();
            String id = waterSupplyListOrdered.get(i).getKEY_ID();
            String water_supply_name = waterSupplyListOrdered.get(i).getKEY_water_supply_availability_name();


            habList.setKEY_ID(id);
            habList.setKEY_water_supply_availability_name(water_supply_name);

            waterSupplyList.add(habList);
        }
        activityNewMainPageBinding.waterSpinner.setAdapter(new CommonAdapter(this, waterSupplyList, "waterSupplyList"));

    }


    private void captureImage() {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(this, file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        if (MyLocationListener.latitude > 0) {
            offlatTextValue = MyLocationListener.latitude;
            offlongTextValue = MyLocationListener.longitude;
        }
    }

    public void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);

        //API level 9 and up
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        Integer gpsFreqInMillis = 1000;
        Integer gpsFreqInDistance = 1;


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(NewMainPage.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            mlocManager.requestLocationUpdates(gpsFreqInMillis, gpsFreqInDistance, criteria, mlocListener, null);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(NewMainPage.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewMainPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(NewMainPage.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewMainPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewMainPage.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(NewMainPage.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(NewMainPage.this, getResources().getString(R.string.satellite));
            }
        } else {
            Utils.showAlert(NewMainPage.this, getResources().getString(R.string.gps_is_not_turned_on));
        }
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            } else {
//                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.permission_required))
                .setMessage(getResources().getString(R.string.camera_need_permission))
                .setPositiveButton(getResources().getString(R.string.goto_settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(NewMainPage.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void previewCapturedImage() {
        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            activityNewMainPageBinding.mccCaptureIcon.setVisibility(View.GONE);
            activityNewMainPageBinding.mccCenterImage.setVisibility(View.VISIBLE);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageStoragePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            activityNewMainPageBinding.mccCenterImage.setImageBitmap(rotatedBitmap);
            mcc_center_image = bitmapToString(rotatedBitmap);
//            cameraScreenBinding.imageView.showImage((getImageUri(rotatedBitmap)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            mcc_center_image = "";
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap photo= (Bitmap) data.getExtras().get("data");
                    mcc_center_image = bitmapToString(photo);
                    activityNewMainPageBinding.mccCenterImage.setImageBitmap(photo);
                    activityNewMainPageBinding.mccCaptureIcon.setVisibility(View.GONE);
                    activityNewMainPageBinding.mccCenterImage.setVisibility(View.VISIBLE);
                }
                else {
                    // Refreshing the gallery
                    CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                    // successfully captured the image
                    // display it in image view
                    previewCapturedImage();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.user_canceled_image_capture), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.sorry_failed_to_capture), Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // video successfully recorded
                // preview the recorded video
//                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.user_canceled_video), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.sorry_failed_capture_video), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    public void checkFieldCondition(){
        if(!activityNewMainPageBinding.mccName.getText().toString().equals("")){
            if(!capacity_name.equals("")){
                if(!water_supply_id.equals("")){
                    if(activityNewMainPageBinding.specifyLayout.getVisibility()==View.VISIBLE){
                        if(!activityNewMainPageBinding.specifyText.getText().toString().equals("")){
                            if(!activityNewMainPageBinding.dateOfCommencement.getText().toString().equals("")){
                                if(!mcc_center_image.equals("")){
                                    saveLocalOrServer();
                                }
                                else {
                                    Utils.showAlert(this,getResources().getString(R.string.please_capture_mcc_center));
                                }
                            }
                            else {
                                Utils.showAlert(this,getResources().getString(R.string.select_date_of_commencement));
                            }
                        }
                        else {
                            Utils.showAlert(this,getResources().getString(R.string.please_specify));
                        }
                    }
                    else {
                        if(!activityNewMainPageBinding.dateOfCommencement.getText().toString().equals("")){
                            if(!mcc_center_image.equals("")){
                                saveLocalOrServer();
                            }
                            else {
                                Utils.showAlert(this,getResources().getString(R.string.please_capture_mcc_center));
                            }
                        }
                        else {
                            Utils.showAlert(this,getResources().getString(R.string.select_date_of_commencement));
                        }
                    }
                }
                else {
                    Utils.showAlert(this, getResources().getString(R.string.select_water_supply));
                }
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.choose_capacity_of_mcc_in_tonnes));
            }
        }
        else {
            Utils.showAlert(this,getResources().getString(R.string.please_enter_mcc_name));
        }
    }
    public void saveLocalOrServer(){
        long id = 0; String whereClause = "";String[] whereArgs = null;
        String pvcode = prefManager.getPvCode();
        String dcode = prefManager.getDistrictCode();
        String bcode = prefManager.getBlockCode();
        String mcc_name = activityNewMainPageBinding.mccName.getText().toString();
        String capacity_of_mcc_id = capacity_id;
        String water_supply_availability_id = water_supply_id;
        String water_supply_availability_other = activityNewMainPageBinding.specifyText.getText().toString();
        String date_of_commencement = activityNewMainPageBinding.dateOfCommencement.getText().toString();
        String image = mcc_center_image;
        String latitude = String.valueOf(offlatTextValue);
        String longitude = String.valueOf(offlongTextValue);

        ContentValues values = new ContentValues();

        //if(Utils.isOnline())
        {
            values.put("dcode",dcode);
            values.put("bcode",bcode);
            values.put("pvcode",pvcode);
            values.put("mcc_id",new_mcc_id);
            values.put("mcc_name",mcc_name);
            values.put("capacity_of_mcc_id",capacity_of_mcc_id);
            values.put("water_supply_availability_id",water_supply_availability_id);
            values.put("water_supply_availability_other",water_supply_availability_other);
            values.put("date_of_commencement",date_of_commencement);
            values.put("image",image);
            values.put("latitude",latitude);
            values.put("longitude",longitude);

            whereClause = "mcc_id = ?";
            whereArgs = new String[]{new_mcc_id};dbData.open();

            if(new_mcc_id.equals("")) {
                id = db.insert(DBHelper.BASIC_DETAILS_OF_MCC_SAVE, null, values);
                if(id > 0){
                    Toasty.success(this, getResources().getString(R.string.inserted_success), Toast.LENGTH_LONG, true).show();
                    onBackPressed();
                }

            }
            else if(!new_mcc_id.equals("")) {
                ArrayList<RealTimeMonitoringSystem> imageOffline = dbData.getParticularVillageBasicDetails(new_mcc_id);
                if(imageOffline.size()>0){
                    id = db.update(DBHelper.BASIC_DETAILS_OF_MCC_SAVE, values, whereClause, whereArgs);
                    if(id > 0){
                        Toasty.success(this, getResources().getString(R.string.updated_success), Toast.LENGTH_LONG, true).show();
                        onBackPressed();
                    }

                }
                else {
                    id = db.insert(DBHelper.BASIC_DETAILS_OF_MCC_SAVE, null, values);
                    if(id > 0){
                        Toasty.success(this, getResources().getString(R.string.inserted_success), Toast.LENGTH_LONG, true).show();
                        onBackPressed();
                    }

                }

            }

            /*if(imageOffline.size() < 1) {
                id = db.insert(DBHelper.BASIC_DETAILS_OF_MCC_SAVE, null, values);
            }
            else {
                id = db.update(DBHelper.BASIC_DETAILS_OF_MCC_SAVE, values, whereClause, whereArgs);
            }*/
            Log.d("insIdsaveImage", String.valueOf(id));


        }
/*
        else {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_micro_composting_save");
                jsonObject.put("pvcode",pvcode);
                jsonObject.put("mcc_name",mcc_name);
                jsonObject.put("capacity_of_mcc_id",capacity_of_mcc_id);
                jsonObject.put("water_supply_availability_id",water_supply_availability_id);
                jsonObject.put("water_supply_availability_other",water_supply_availability_other);
                jsonObject.put("date_of_commencement",date_of_commencement);
                jsonObject.put("image",image);
                jsonObject.put("latitude",latitude);
                jsonObject.put("longitude",longitude);

                String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), jsonObject.toString());
                JSONObject dataSet = new JSONObject();
                dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
                dataSet.put(AppConstant.DATA_CONTENT, authKey);
                Log.d("basicENCDetails", "" + authKey);
                Log.d("basicJSONDetails", "" + jsonObject);
                SyncData(dataSet);

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
*/

    }

    public String bitmapToString(Bitmap bitmap){
        byte[] imageInByte = new byte[0];
        String image_str = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        imageInByte = baos.toByteArray();
        image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        return image_str;
    }

    public void SyncData(JSONObject jsonObject) {
        try {
            new ApiService(this).makeJSONObjectRequest("SaveDetails", Api.Method.POST, UrlGenerator.getWorkListUrl(), jsonObject, "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void pickdate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //dateFormate(date);
                        activityNewMainPageBinding.dateOfCommencement.setText(dateFormate(date,""));

                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
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

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewMainPage.this,HomePage.class);
        intent.putExtra("Home","Home");
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
