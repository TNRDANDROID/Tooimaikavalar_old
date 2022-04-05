package com.nic.thooimaikaavalar_old.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.CommonAdapter;
import com.nic.thooimaikaavalar_old.api.Api;
import com.nic.thooimaikaavalar_old.api.ServerResponse;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.CameraScreenBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.MyEditTextView;
import com.nic.thooimaikaavalar_old.support.MyLocationListener;
import com.nic.thooimaikaavalar_old.utils.CameraUtils;
import com.nic.thooimaikaavalar_old.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class CameraScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;

    LocationManager mlocManager = null;
    LocationListener mlocListener;
    Double offlatTextValue, offlongTextValue;
    private PrefManager prefManager;


    private List<View> viewArrayList = new ArrayList<>();
    private CameraScreenBinding cameraScreenBinding;

    Button btn_save;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.thooimaikaavalar_old.dataBase.dbData dbData = new dbData(this);
    private MyEditTextView description;
    private List<RealTimeMonitoringSystem> StageList = new ArrayList<>();
    String  pref_stage;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String type_of_work;



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraScreenBinding = DataBindingUtil.setContentView(this, R.layout.camera_screen);
        cameraScreenBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }


        intializeUI();
        loadOfflineStageListDBValues();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        cameraScreenBinding.imageViewPreview.setOnClickListener(this);
        cameraScreenBinding.imageView.setOnClickListener(this);
        cameraScreenBinding.backImg.setOnClickListener(this);
        cameraScreenBinding.homeImg.setOnClickListener(this);
        cameraScreenBinding.btnSave.setOnClickListener(this);

        cameraScreenBinding.stage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pref_stage = StageList.get(position).getWorkStageName();
                }
                else {
                    pref_stage="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        type_of_work =  getIntent().getStringExtra(AppConstant.TYPE_OF_WORK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.btn_save :
                saveImage();
                break;
        }
    }

    public void saveImage() {
        dbData.open();
        long id = 0; String whereClause = "";String[] whereArgs = null;
        String work_id = getIntent().getStringExtra(AppConstant.WORK_ID);
        String dcode = prefManager.getDistrictCode();
        String bcode = prefManager.getBlockCode();
        String pvcode = prefManager.getPvCode();
//        BigImageView imageView = (BigImageView) findViewById(R.id.image_view);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
//            File path = imageView.getCurrentImageFile();
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bitmap = BitmapFactory.decodeFile(path.getAbsolutePath(),bmOptions);
//            bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);

            if(!pref_stage.equals("")){
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                imageInByte = baos.toByteArray();
                image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                ContentValues values = new ContentValues();
                values.put(AppConstant.WORK_ID,work_id );
                values.put(AppConstant.WORK_GROUP_ID, getIntent().getStringExtra(AppConstant.WORK_GROUP_ID));
                values.put(AppConstant.WORK_TYPE_ID, getIntent().getStringExtra(AppConstant.WORK_TYPE_ID));
                values.put(AppConstant.TYPE_OF_WORK, type_of_work);
                if(type_of_work.equalsIgnoreCase(AppConstant.ADDITIONAL_WORK)) {
                    values.put(AppConstant.CD_WORK_NO, getIntent().getStringExtra(AppConstant.CD_WORK_NO));
                    values.put(AppConstant.WORK_TYPE_FLAG_LE, getIntent().getStringExtra(AppConstant.WORK_TYPE_FLAG_LE));
                }
                values.put(AppConstant.DISTRICT_CODE,dcode );
                values.put(AppConstant.BLOCK_CODE,bcode );
                values.put(AppConstant.PV_CODE,pvcode );
                values.put(AppConstant.WORK_STAGE_CODE,StageList.get(cameraScreenBinding.stage.getSelectedItemPosition()).getWorkStageCode() );
                values.put(AppConstant.WORK_SATGE_NAME,StageList.get(cameraScreenBinding.stage.getSelectedItemPosition()).getWorkStageName() );
                values.put(AppConstant.KEY_LATITUDE, offlatTextValue.toString());
                values.put(AppConstant.KEY_LONGITUDE, offlongTextValue.toString());
                values.put(AppConstant.KEY_IMAGES,image_str.trim());
                values.put(AppConstant.KEY_IMAGE_REMARK,cameraScreenBinding.description.getText().toString());
                values.put(AppConstant.KEY_CREATED_DATE,sdf.format(new Date()));


                if (type_of_work.equalsIgnoreCase(AppConstant.MAIN_WORK)) {
                    whereClause = "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and type_of_work = ?";
                    whereArgs = new String[]{dcode,bcode,pvcode,work_id,type_of_work};dbData.open();
                    ArrayList<RealTimeMonitoringSystem> imageOffline = dbData.selectImage(dcode,bcode,pvcode,work_id,AppConstant.MAIN_WORK,"","");

                    if(imageOffline.size() < 1) {
                        id = db.insert(DBHelper.SAVE_IMAGE, null, values);
                    }
                    else {
                        id = db.update(DBHelper.SAVE_IMAGE, values, whereClause, whereArgs);
                    }
                }else if(type_of_work.equalsIgnoreCase(AppConstant.ADDITIONAL_WORK)){

                    String cd_work_no = getIntent().getStringExtra(AppConstant.CD_WORK_NO);
                    String work_type_flag_le = getIntent().getStringExtra(AppConstant.WORK_TYPE_FLAG_LE);

                    whereClause = "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and type_of_work = ? and cd_work_no = ?";
                    whereArgs = new String[]{dcode,bcode,pvcode,work_id,type_of_work,cd_work_no};

                    ArrayList<RealTimeMonitoringSystem> imageOffline = dbData.selectImage(dcode,bcode,pvcode,work_id,AppConstant.ADDITIONAL_WORK,cd_work_no,work_type_flag_le );

                    if(imageOffline.size() < 1) {
                        id = db.insert(DBHelper.SAVE_IMAGE, null, values);
                    }
                    else {
                        id = db.update(DBHelper.SAVE_IMAGE, values, whereClause, whereArgs);
                    }
                }

                if(id > 0){
                    Toasty.success(this, "Success!", Toast.LENGTH_LONG, true).show();
                    onBackPressed();
                }
                Log.d("insIdsaveImageLatLong", String.valueOf(id));

            }
            else{
                Utils.showAlert(CameraScreen.this, "Please Select Stage");
            }


        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, "Atleast Capture one Photo");
            //e.printStackTrace();
        }
    }

    public void loadOfflineStageListDBValues() {
        StageList.clear();
        Cursor Stage = null;

        String tye_of_work = getIntent().getStringExtra(AppConstant.TYPE_OF_WORK);

        if(tye_of_work.equalsIgnoreCase(AppConstant.MAIN_WORK)){
            String workGroupId = getIntent().getStringExtra(AppConstant.WORK_GROUP_ID);
            String workTypeid = getIntent().getStringExtra(AppConstant.WORK_TYPE_ID);
            String currentStageCode = getIntent().getStringExtra(AppConstant.CURRENT_STAGE_OF_WORK);

          //  Stage = db.rawQuery("select * from " + DBHelper.WORK_STAGE_TABLE + "  where (work_group_id = " + workGroupId + " and work_type_id = " + workTypeid + ") order by work_stage_order asc", null);
            String Que = "select * from "+DBHelper.WORK_STAGE_TABLE+" where work_stage_order >(select work_stage_order from "+DBHelper.WORK_STAGE_TABLE+" where work_stage_code='"+currentStageCode+"' and work_group_id=" + workGroupId + "  and work_type_id=" + workTypeid + ")  and work_group_id=" + workGroupId + "  and work_type_id=" + workTypeid + " and work_stage_code != 11 order by work_stage_order";
            Log.d("Que",Que);
            Stage = db.rawQuery(Que, null);
        }
        else if(tye_of_work.equalsIgnoreCase(AppConstant.ADDITIONAL_WORK)){
            String workTypeID = getIntent().getStringExtra(AppConstant.CD_TYPE_ID);
            String currentStageCode = getIntent().getStringExtra(AppConstant.CURRENT_STAGE_OF_WORK);
            String workTypeFlag = getIntent().getStringExtra(AppConstant.WORK_TYPE_FLAG_LE);
           // Stage = db.rawQuery("select * from " + DBHelper.ADDITIONAL_WORK_STAGE_TABLE + "  where work_type_code =  "+ workTypecode + " order by work_stage_order asc", null);
            String sqlQry = SQLiteQueryBuilder.buildQueryString(false,DBHelper.ADDITIONAL_WORK_STAGE_TABLE, null, "work_stage_order>(select work_stage_order from "+DBHelper.ADDITIONAL_WORK_STAGE_TABLE+" where work_stage_code='"+currentStageCode+"' and work_type_code ="+workTypeID+" and cd_type_flag ='"+workTypeFlag+"') and work_type_code ="+workTypeID+" and cd_type_flag ='"+workTypeFlag+"' order by work_stage_order", null, null, null, null);
            String checkNotStarted = "select work_stage_order from "+DBHelper.ADDITIONAL_WORK_STAGE_TABLE+" where work_stage_code='"+currentStageCode+"' and work_type_code ="+workTypeID+" and cd_type_flag ='"+workTypeFlag+"'";
            if(currentStageCode.equalsIgnoreCase("1")){
                checkNotStarted = "1";
            }
            Stage = db.rawQuery("select * from "+DBHelper.ADDITIONAL_WORK_STAGE_TABLE+" where work_stage_order>("+checkNotStarted+") and work_type_code ="+workTypeID+" and cd_type_flag ='"+workTypeFlag+"' order by work_stage_order", null);
            Log.d("stageQuery",""+sqlQry);

        }

        StageList.clear();
        RealTimeMonitoringSystem stageListValue = new RealTimeMonitoringSystem();
        stageListValue.setWorkStageName("Select Stage");
        StageList.add(stageListValue);
        if (Stage.getCount() > 0) {
            if (Stage.moveToFirst()) {
                do {
                    RealTimeMonitoringSystem stageList = new RealTimeMonitoringSystem();
                    String stage = Stage.getString(Stage.getColumnIndexOrThrow(AppConstant.WORK_SATGE_NAME));
                    stageList.setWorkStageName(stage);
                    stageList.setWorkStageCode(Stage.getString(Stage.getColumnIndexOrThrow(AppConstant.WORK_STAGE_CODE)));
                    StageList.add(stageList);
                } while (Stage.moveToNext());
            }
        }

        cameraScreenBinding.stage.setAdapter(new CommonAdapter(this, StageList, "StageList"));
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
        if (ContextCompat.checkSelfPermission(CameraScreen.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraScreen.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(CameraScreen.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(CameraScreen.this, "Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            }
        } else {
            Utils.showAlert(CameraScreen.this, "GPS is not turned on...");
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
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(CameraScreen.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void previewCapturedImage() {
        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            cameraScreenBinding.imageViewPreview.setVisibility(View.GONE);
            cameraScreenBinding.imageView.setVisibility(View.VISIBLE);
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
            cameraScreenBinding.imageView.setImageBitmap(rotatedBitmap);
//            cameraScreenBinding.imageView.showImage((getImageUri(rotatedBitmap)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
                    cameraScreenBinding.imageView.setImageBitmap(photo);
                    cameraScreenBinding.imageViewPreview.setVisibility(View.GONE);
                    cameraScreenBinding.imageView.setVisibility(View.VISIBLE);
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
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // video successfully recorded
                // preview the recorded video
//                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }

    @Override
    public void OnError(VolleyError volleyError) {

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


}
