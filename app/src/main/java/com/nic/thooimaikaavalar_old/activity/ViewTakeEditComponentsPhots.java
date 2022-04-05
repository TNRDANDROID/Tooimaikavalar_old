package com.nic.thooimaikaavalar_old.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.ComponentsAdapter;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityViewTakeEditComponentsPhotsBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.MyLocationListener;
import com.nic.thooimaikaavalar_old.utils.CameraUtils;
import com.nic.thooimaikaavalar_old.utils.FontCache;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class ViewTakeEditComponentsPhots extends AppCompatActivity {

    ActivityViewTakeEditComponentsPhotsBinding viewTakeEditComponentsPhotsBinding;
    private ShimmerRecyclerView recyclerView;
    ArrayList<RealTimeMonitoringSystem> componentsList;
    ComponentsAdapter componentsAdapter;
    public com.nic.thooimaikaavalar_old.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    private SQLiteDatabase db;
    public static DBHelper dbHelper;

    ImageView imageView, image_view_preview;
    TextView latitude_text, longtitude_text;
    EditText myEditTextView;
    private List<View> viewArrayList = new ArrayList<>();
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;

    LocationManager mlocManager = null;
    LocationListener mlocListener;
    Double offlatTextValue, offlongTextValue;

    String photo_type ="";
    String mcc_id_ ="";
    String component_id_ ="";
    String component_name="";

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        viewTakeEditComponentsPhotsBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_take_edit_components_phots);
        viewTakeEditComponentsPhotsBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        prefManager = new PrefManager(this);

        viewTakeEditComponentsPhotsBinding.view1.setVisibility(View.VISIBLE);
        viewTakeEditComponentsPhotsBinding.view2.setVisibility(View.GONE);
        viewTakeEditComponentsPhotsBinding.tv1.setTextColor(getResources().getColor(R.color.white));
        viewTakeEditComponentsPhotsBinding.tv2.setTextColor(getResources().getColor(R.color.black));

        photo_type ="view_image";
        mcc_id_ = getIntent().getStringExtra("mcc_id");
        viewTakeEditComponentsPhotsBinding.addImageRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTakeEditComponentsPhotsBinding.view2.setVisibility(View.VISIBLE);
                viewTakeEditComponentsPhotsBinding.view1.setVisibility(View.GONE);
                viewTakeEditComponentsPhotsBinding.tv2.setTextColor(getResources().getColor(R.color.white));
                viewTakeEditComponentsPhotsBinding.tv1.setTextColor(getResources().getColor(R.color.black));
                photo_type ="add_image";

            }
        });
        viewTakeEditComponentsPhotsBinding.viewImageRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTakeEditComponentsPhotsBinding.view2.setVisibility(View.GONE);
                viewTakeEditComponentsPhotsBinding.view1.setVisibility(View.VISIBLE);
                viewTakeEditComponentsPhotsBinding.tv2.setTextColor(getResources().getColor(R.color.black));
                viewTakeEditComponentsPhotsBinding.tv1.setTextColor(getResources().getColor(R.color.white));
                photo_type ="view_image";

            }
        });

        initComponentsRecycler();

        viewTakeEditComponentsPhotsBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initializeUi();

    }

    public void initializeUi(){
        String date_of_commencement= dateFormate(getIntent().getStringExtra("date_of_commencement"),"yes");
        viewTakeEditComponentsPhotsBinding.villageName.setText("Village : "+getIntent().getStringExtra("village_name"));
        viewTakeEditComponentsPhotsBinding.mccName.setText("MCC Name : "+getIntent().getStringExtra("mcc_name"));
        viewTakeEditComponentsPhotsBinding.dateOfCommencement.setText("Date of Commencement : "+date_of_commencement);
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



    public void componentsAdapterItemClicked(int position){
        component_id_ = componentsList.get(position).getKEY_ID();
        component_name = componentsList.get(position).getKEY_photographs_name();
        if(photo_type.equals("add_image")) {
            imageWithDescription("", viewTakeEditComponentsPhotsBinding.scroolview);
        }
        else {
            Intent intent = new Intent(this, FullImageActivity.class);
            intent.putExtra("OnOffType","Online");
            intent.putExtra("mcc_id",mcc_id_);
            intent.putExtra("work_id","");
            intent.putExtra("component_id",component_id_);
            startActivity(intent);
        }
    }
    private void initComponentsRecycler() {
        recyclerView = viewTakeEditComponentsPhotsBinding.componentsRecycler;
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

    public class fetchScheduletask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {
            dbData.open();
            ArrayList<RealTimeMonitoringSystem> capacityList = new ArrayList<>();
            capacityList = dbData.getAll_photographs_of_mcc();
            Log.d("Capacity_COUNT", String.valueOf(capacityList.size()));
            return capacityList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> capacityList) {
            super.onPostExecute(capacityList);
            componentsList = new ArrayList<>();
            componentsList.addAll(capacityList);

            componentsAdapter = new ComponentsAdapter(capacityList,ViewTakeEditComponentsPhots.this);
            recyclerView.setAdapter(componentsAdapter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 1000);
        }
    }
    private void loadCards() {

        recyclerView.hideShimmerAdapter();
        //initNoOfThooimaiKaavalarRecyler();

    }

    public void imageWithDescription(final String type, final ScrollView scrollView) {
        //imageboolean = true;
        //dataset = new JSONObject();

        final Dialog dialog = new Dialog(this,
                R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_photo);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();


        final LinearLayout mobileNumberLayout = (LinearLayout) dialog.findViewById(R.id.mobile_number_layout);
        TextView cancel = (TextView) dialog.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button done = (Button) dialog.findViewById(R.id.btn_save_inspection);
        done.setGravity(Gravity.CENTER);
        done.setVisibility(View.VISIBLE);
        done.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.HEAVY));

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONArray imageJson = new JSONArray();
                long rowInserted=0;
                int childCount = mobileNumberLayout.getChildCount();
                int count = 0;
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        JSONArray imageArray = new JSONArray();

                        View vv = mobileNumberLayout.getChildAt(i);
                        imageView = vv.findViewById(R.id.image_view);
                        myEditTextView = vv.findViewById(R.id.description);
                        latitude_text = vv.findViewById(R.id.latitude);
                        longtitude_text = vv.findViewById(R.id.longtitude);


                        if(imageView.getDrawable()!=null){
                            if(!myEditTextView.getText().toString().equals("")){
                                count = count+1;
                                byte[] imageInByte = new byte[0];
                                String image_str = "";
                                String description="";
                                try {
                                    description = myEditTextView.getText().toString();
                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                                    imageInByte = baos.toByteArray();
                                    image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                                } catch (Exception e) {
                                    //imageboolean = false;
                                    Utils.showAlert(ViewTakeEditComponentsPhots.this, getResources().getString(R.string.at_least_capture_one_photo));
                                    //e.printStackTrace();
                                }

                                if (MyLocationListener.latitude > 0) {
                                    offlatTextValue = MyLocationListener.latitude;
                                    offlongTextValue =MyLocationListener.longitude;
                                }

                                // Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();

                        ContentValues imageValue = new ContentValues();

                        imageValue.put("mcc_id", mcc_id_);
                        imageValue.put("component_id", component_id_);
                        imageValue.put("latitude", latitude_text.getText().toString());
                        imageValue.put("longitude", longtitude_text.getText().toString());
                        imageValue.put("photograph_remark", description);
                        imageValue.put("image", image_str.trim());

                        rowInserted = db.insert(DBHelper.COMPOST_TUB_IMAGE_TABLE, null, imageValue);

                        if(count==childCount){
                            if(rowInserted>0){
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                dialog.dismiss();
                                //Toast.makeText(ViewTakeEditComponentsPhots.this, "Success", Toast.LENGTH_SHORT).show();
                                Toasty.success(ViewTakeEditComponentsPhots.this,getResources().getString(R.string.inserted_success),Toasty.LENGTH_SHORT);
                                onBackPressed();
                            }

                        }

                    }

                    else {
                                Utils.showAlert(ViewTakeEditComponentsPhots.this,getResources().getString(R.string.please_enter_description));
                    }
               }
               else {
                    Utils.showAlert(ViewTakeEditComponentsPhots.this,getResources().getString(R.string.please_capture_image));
               }


                    }


                }

                focusOnView(scrollView);

            }
        });
        Button btnAddMobile = (Button) dialog.findViewById(R.id.btn_add);
        btnAddMobile.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));
        viewArrayList.clear();
        btnAddMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(viewArrayList.size()<3) {
                     if (imageView.getDrawable() != null && viewArrayList.size() > 0 && !myEditTextView.getText().toString().equals("")) {
                         dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                         updateView(ViewTakeEditComponentsPhots.this, mobileNumberLayout, "", type);
                     } else {
                         Utils.showAlert(ViewTakeEditComponentsPhots.this, getResources().getString(R.string.first_capture_image_add_another));
                     }
                 }
                 else {
                     Utils.showAlert(ViewTakeEditComponentsPhots.this, getResources().getString(R.string.maximum_three_photos));

                 }
            }
        });
        updateView(this, mobileNumberLayout, "", type);

    }

    private final void focusOnView(final ScrollView your_scrollview) {
        your_scrollview.post(new Runnable() {
            @Override
            public void run() {
                your_scrollview.fullScroll(View.FOCUS_DOWN);
                //your_scrollview.scrollTo(0, your_EditBox.getY());
            }
        });
    }
    //Method for update single view based on email or mobile type
    public View updateView(final Activity activity, final LinearLayout emailOrMobileLayout, final String values, final String type) {
        final View hiddenInfo = activity.getLayoutInflater().inflate(R.layout.image_with_description, emailOrMobileLayout, false);
        final ImageView imageView_close = (ImageView) hiddenInfo.findViewById(R.id.imageView_close);
        imageView = (ImageView) hiddenInfo.findViewById(R.id.image_view);
        image_view_preview = (ImageView) hiddenInfo.findViewById(R.id.image_view_preview);
        myEditTextView = (EditText) hiddenInfo.findViewById(R.id.description);
        latitude_text = hiddenInfo.findViewById(R.id.latitude);
        longtitude_text = hiddenInfo.findViewById(R.id.longtitude);





        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imageView.setVisibility(View.VISIBLE);
                    if (viewArrayList.size() != 1) {
                        ((LinearLayout) hiddenInfo.getParent()).removeView(hiddenInfo);
                        viewArrayList.remove(hiddenInfo);
                    }

                } catch (IndexOutOfBoundsException a) {
                    a.printStackTrace();
                }
            }
        });
        image_view_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();
            }
        });
        emailOrMobileLayout.addView(hiddenInfo);

        View vv = emailOrMobileLayout.getChildAt(viewArrayList.size());
        EditText myEditTextView1 = (EditText) vv.findViewById(R.id.description);
        //myEditTextView1.setSelection(myEditTextView1.length());
        myEditTextView1.requestFocus();
        viewArrayList.add(hiddenInfo);
        return hiddenInfo;
    }

    public void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(ViewTakeEditComponentsPhots.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(ViewTakeEditComponentsPhots.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewTakeEditComponentsPhots.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(ViewTakeEditComponentsPhots.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewTakeEditComponentsPhots.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ViewTakeEditComponentsPhots.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(ViewTakeEditComponentsPhots.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(ViewTakeEditComponentsPhots.this, getResources().getString(R.string.satellite));
            }
        } else {
            Utils.showAlert(ViewTakeEditComponentsPhots.this, getResources().getString(R.string.gps_is_not_turned_on));
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
                        CameraUtils.openSettings(ViewTakeEditComponentsPhots.this);
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
            image_view_preview.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
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
            imageView.setImageBitmap(rotatedBitmap);
            latitude_text.setText(""+offlatTextValue);
            longtitude_text.setText(""+offlongTextValue);
//            cameraScreenBinding.imageView.showImage((getImageUri(rotatedBitmap)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap photo= (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                    image_view_preview.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    latitude_text.setText(""+offlatTextValue);
                    longtitude_text.setText(""+offlongTextValue);
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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


}
