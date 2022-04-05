package com.nic.thooimaikaavalar_old.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nic.thooimaikaavalar_old.BuildConfig;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.AdditionalWorkScreen;
import com.nic.thooimaikaavalar_old.activity.CameraScreen;
import com.nic.thooimaikaavalar_old.activity.FullImageActivity;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.AdapterWorkListBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.MyLocationListener;
import com.nic.thooimaikaavalar_old.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_AFFORESTATION_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_CHECK_DAM_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_Farm_ponds_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_Recharge_shaft_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_Recharge_wells_in_Rivers_Streams_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_Rooftop_RWHS_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_Soakpit_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_Sunken_pond_and_desilting_of_channel_LOCAL;
import static com.nic.thooimaikaavalar_old.constant.AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL;

public class WorkListAdapter extends PagedListAdapter<RealTimeMonitoringSystem,WorkListAdapter.MyViewHolder> implements Filterable {
    private List<RealTimeMonitoringSystem> WorkListValues;
    private List<RealTimeMonitoringSystem> WorkListValuesFiltered;
    private String letter;
    private Context context;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    public final String dcode,bcode,pvcode;
    PrefManager prefManager;
    private final dbData dbData;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    public String Length ="";
    public String Width ="";
    public String Height ="";
    public String Length_water_storage ="";
    public String Observation ="";
    public String No_trenches_per_worksite ="";
    public String No_units ="";
    public String Depth ="";
    public String No_units_per_waterbody ="";
    public String Area_covered ="";
    public String No_plantation_per_site ="";
    public String Structure_sump_available_status ="";
    public String structure_sump_radio_status ="";
    public String Individual_community_status ="";
    public String Individual_community_radio_status ="";
    public String Length_channel ="";
    ContentValues values;
    Activity activity ;
    LinearLayout id1,id2,id3,id4,id5,id6,id7,id8,id9,id10;
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Double offlatTextValue, offlongTextValue;
    AlertDialog dialog;

    boolean getLatlang=false;
    private LayoutInflater layoutInflater;
    private static DiffUtil.ItemCallback<RealTimeMonitoringSystem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RealTimeMonitoringSystem>() {
                @Override
                public boolean areItemsTheSame(RealTimeMonitoringSystem oldItem, RealTimeMonitoringSystem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(RealTimeMonitoringSystem oldItem, RealTimeMonitoringSystem newItem) {
                    return oldItem.equals(newItem);
                }
            };
    public WorkListAdapter(Context context, List<RealTimeMonitoringSystem> WorkListValues,dbData dbData) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.WorkListValues = WorkListValues;
        this.WorkListValuesFiltered = WorkListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
        dcode = prefManager.getDistrictCode();
        bcode = prefManager.getBlockCode();
        pvcode = prefManager.getPvCode();
        activity = (Activity) context;
        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AdapterWorkListBinding adapterWorkListBinding;

        public MyViewHolder(AdapterWorkListBinding Binding) {
            super(Binding.getRoot());
            adapterWorkListBinding = Binding;
        }
    }

    @Override
    public WorkListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        AdapterWorkListBinding adapterWorkListBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_work_list, viewGroup, false);
        return new WorkListAdapter.MyViewHolder(adapterWorkListBinding);
    }

    @Override
    public void onBindViewHolder(WorkListAdapter.MyViewHolder holder, final int position) {
        dbData.open();
        String beneficary_name = WorkListValuesFiltered.get(position).getBeneficiaryName();
        Log.d("beneficary_name",beneficary_name);
        String workGroupId = String.valueOf(WorkListValuesFiltered.get(position).getWorkGroupID());
        String workTypeid = String.valueOf(WorkListValuesFiltered.get(position).getWorkTypeID());
        String currentStageCode = String.valueOf(WorkListValuesFiltered.get(position).getCurrentStage());

        if(!beneficary_name.isEmpty()) {
            beneficary_name = " (" + WorkListValuesFiltered.get(position).getBeneficiaryName() + ")";
        }
        else {
            beneficary_name = "";
        }

        holder.adapterWorkListBinding.workid.setText(WorkListValuesFiltered.get(position).getWorkId() + beneficary_name);
        if(WorkListValuesFiltered.get(position).getSchemeGroupName().isEmpty()){
            holder.adapterWorkListBinding.tvSchemeGroupNameLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvSchemeGroupNameLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvSchemeGroupName.setText(WorkListValuesFiltered.get(position).getSchemeGroupName());
        }

        if(WorkListValuesFiltered.get(position).getSchemeName().isEmpty()){
            holder.adapterWorkListBinding.tvSchemeLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvSchemeLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvScheme.setText(WorkListValuesFiltered.get(position).getSchemeName());
        }

        if(WorkListValuesFiltered.get(position).getFinancialYear().isEmpty()){
            holder.adapterWorkListBinding.tvFinancialYearLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvFinancialYearLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvFinancialYear.setText(WorkListValuesFiltered.get(position).getFinancialYear());
        }

        if(WorkListValuesFiltered.get(position).getAgencyName().isEmpty()){
            holder.adapterWorkListBinding.tvAgencyNameLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvAgencyNameLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvAgencyName.setText(WorkListValuesFiltered.get(position).getAgencyName());
        }

        if(WorkListValuesFiltered.get(position).getWorkGroupNmae().isEmpty()){
            holder.adapterWorkListBinding.tvWorkGroupNameLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvWorkGroupNameLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvWorkGroupName.setText(WorkListValuesFiltered.get(position).getWorkGroupNmae());
        }

        if(WorkListValuesFiltered.get(position).getWorkName().isEmpty()){
            holder.adapterWorkListBinding.tvWorkNameLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvWorkNameLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvWorkName.setText(WorkListValuesFiltered.get(position).getWorkName());
        }

        if(WorkListValuesFiltered.get(position).getBlockName().isEmpty()){
            holder.adapterWorkListBinding.tvBlockLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvBlockLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvBlock.setText(WorkListValuesFiltered.get(position).getBlockName());
        }

        if(WorkListValuesFiltered.get(position).getPvName().isEmpty()){
            holder.adapterWorkListBinding.tvVillageLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvVillageLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvVillage.setText(WorkListValuesFiltered.get(position).getPvName());
        }

        if(WorkListValuesFiltered.get(position).getStageName().isEmpty()){
            holder.adapterWorkListBinding.tvCurrentStageLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvCurrentStageLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvCurrentStage.setText(WorkListValuesFiltered.get(position).getStageName());
        }

        if(WorkListValuesFiltered.get(position).getBeneficiaryFatherName().isEmpty()){
            holder.adapterWorkListBinding.tvBeneficiaryFatherNameLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvBeneficiaryFatherNameLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvBeneficiaryFatherName.setText(WorkListValuesFiltered.get(position).getBeneficiaryFatherName());
        }

        if(WorkListValuesFiltered.get(position).getGender().isEmpty()){
            holder.adapterWorkListBinding.tvGenderLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvGenderLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvGender.setText(WorkListValuesFiltered.get(position).getGender());
        }

        if(WorkListValuesFiltered.get(position).getCommunity().isEmpty()){
            holder.adapterWorkListBinding.tvCommunityLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvCommunityLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvCommunity.setText(WorkListValuesFiltered.get(position).getCommunity());
        }

        if(WorkListValuesFiltered.get(position).getIntialAmount() == null){
            holder.adapterWorkListBinding.tvInitialAmountLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvInitialAmountLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvInitialAmount.setText(WorkListValuesFiltered.get(position).getIntialAmount());
        }

        if(WorkListValuesFiltered.get(position).getAmountSpendSoFar().isEmpty()){
            holder.adapterWorkListBinding.tvAmountSpentSoFarLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvAmountSpentSoFarLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvAmountSpentSoFar.setText(WorkListValuesFiltered.get(position).getAmountSpendSoFar());
        }

        if(WorkListValuesFiltered.get(position).getLastVisitedDate().isEmpty()){
            holder.adapterWorkListBinding.tvLastVisitedDateLayout.setVisibility(View.GONE);
        }else {
            holder.adapterWorkListBinding.tvLastVisitedDateLayout.setVisibility(View.VISIBLE);
            holder.adapterWorkListBinding.tvLastVisitedDate.setText(Utils.formatDateReverse(WorkListValuesFiltered.get(position).getLastVisitedDate()));

        }


        if(WorkListValuesFiltered.get(position).getCdProtWorkYn().equalsIgnoreCase("Y")){
            holder.adapterWorkListBinding.viewAdditionalWorks.setVisibility(View.VISIBLE);
        }else {
            holder.adapterWorkListBinding.viewAdditionalWorks.setVisibility(View.GONE);
        }
        final String work_id = String.valueOf(WorkListValuesFiltered.get(position).getWorkId());

        ArrayList<RealTimeMonitoringSystem> imageOffline = dbData.selectImage(dcode,bcode,pvcode,work_id,AppConstant.MAIN_WORK,"","");

        ArrayList<RealTimeMonitoringSystem> stage_count = dbData.getStage(workGroupId,workTypeid);

        final String additional_data_status=stage_count.get(0).getAdditional_data_status();

        holder.adapterWorkListBinding.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(additional_data_status.equals("Y") && prefManager.getAdditionalDataStatus().equalsIgnoreCase("Y")){
                    getLocationPermissionWithLatLong();
                    if(getLatlang){
                        showAlert(context,position);
                    }
                }else {
                    openCameraScreen(position);
                }

            }
        });

        holder.adapterWorkListBinding.viewAdditionalWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdditionalWorkList(position);
            }
        });


        if(imageOffline.size() > 0) {
            holder.adapterWorkListBinding.viewOfflineImage.setVisibility(View.VISIBLE);
        }
        else {
            holder.adapterWorkListBinding.viewOfflineImage.setVisibility(View.GONE);
        }

        holder.adapterWorkListBinding.viewOfflineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOfflineImages(work_id,AppConstant.MAIN_WORK,"Offline","","","");
            }
        });
        if (WorkListValuesFiltered.get(position).getImageAvailable().equalsIgnoreCase("Y")) {
            holder.adapterWorkListBinding.viewOnlineImage.setVisibility(View.VISIBLE);
        }
        else if(WorkListValuesFiltered.get(position).getImageAvailable().equalsIgnoreCase("N")){
            holder.adapterWorkListBinding.viewOnlineImage.setVisibility(View.GONE);
        }
        holder.adapterWorkListBinding.viewOnlineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOfflineImages(work_id,AppConstant.MAIN_WORK,"Online",dcode,bcode,pvcode);
            }
        });


        String sql = "select * from "+ DBHelper.WORK_STAGE_TABLE+" where work_stage_order >(select work_stage_order from "+DBHelper.WORK_STAGE_TABLE+" where work_stage_code='"+currentStageCode+"' and work_group_id=" + workGroupId + "  and work_type_id=" + workTypeid + ")  and work_group_id=" + workGroupId + "  and work_type_id=" + workTypeid + " and work_stage_code != 11 order by work_stage_order";
        Log.d("que",sql);
        Cursor Stage = db.rawQuery(sql, null);
       /* ArrayList<RealTimeMonitoringSystem> testResult=new ArrayList<>();
        ArrayList<RealTimeMonitoringSystem> testResult1=new ArrayList<>();
        ArrayList<RealTimeMonitoringSystem> testResult2=new ArrayList<>();
        testResult.addAll(getAll_Stage());
        for(int i=0;i<testResult.size();i++){
            if(workGroupId.equals(testResult.get(i).getWorkGroupID())){
                if(workTypeid.equals(testResult.get(i).getWorkTypeID())){
                    testResult1.add(testResult.get(i));
                }

            }
        }

        testResult2.addAll(testResult1);
*/
        if(Stage.getCount() > 0 ){
            holder.adapterWorkListBinding.takePhoto.setVisibility(View.VISIBLE);
        }
        else {
            holder.adapterWorkListBinding.takePhoto.setVisibility(View.GONE);
        }

        holder.adapterWorkListBinding.viewLengthDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDetails(context,position);
            }
        });
    }

    public  void showAlert(final Context context, final int position){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.alert_dialog_for_scheme, null);
            dialog = builder.create();
            ArrayList<RealTimeMonitoringSystem> additionalDataOffline = new ArrayList<>();;

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setView(dialogView, 0, 0, 0, 0);
            dialog.setCancelable(true);
            dialog.show();

            String work_id = String.valueOf(WorkListValuesFiltered.get(position).getWorkId());
            String workGroupID = WorkListValuesFiltered.get(position).getWorkGroupID();
            String workTypeID = WorkListValuesFiltered.get(position).getWorkTypeID();
            dbData.open();
            ArrayList<RealTimeMonitoringSystem> additionalDataSavedOffline = dbData.selectAdditionalData(work_id,AppConstant.MAIN_WORK,"");
            ArrayList<RealTimeMonitoringSystem> additionalDetailsOffline = dbData.selectAdditionalDetailsList(work_id,workTypeID,"");
            if (additionalDataSavedOffline.size() == 1) {
                additionalDataOffline=additionalDataSavedOffline;
            }else if (additionalDetailsOffline.size() == 1) {
                additionalDataOffline=additionalDetailsOffline;
            }
            TextView submit_update_btn = (TextView) dialog.findViewById(R.id.submit_update_btn);
            final ImageView close_icon = (ImageView) dialog.findViewById(R.id.close_icon);
            final TextView skip = (TextView) dialog.findViewById(R.id.skip);
            id1 = (LinearLayout) dialog.findViewById(R.id.id_1);
            id2 = (LinearLayout) dialog.findViewById(R.id.id_2);
            id3 = (LinearLayout) dialog.findViewById(R.id.id_3);
            id4 = (LinearLayout) dialog.findViewById(R.id.id_4);
            id5 = (LinearLayout) dialog.findViewById(R.id.id_5);
            id6 = (LinearLayout) dialog.findViewById(R.id.id_6);
            id7 = (LinearLayout) dialog.findViewById(R.id.id_7);
            id8 = (LinearLayout) dialog.findViewById(R.id.id_8);
            id9 = (LinearLayout) dialog.findViewById(R.id.id_9);
            id10 = (LinearLayout) dialog.findViewById(R.id.id_10);
            id1.setVisibility(View.GONE);
            id2.setVisibility(View.GONE);
            id3.setVisibility(View.GONE);
            id4.setVisibility(View.GONE);
            id5.setVisibility(View.GONE);
            id6.setVisibility(View.GONE);
            id7.setVisibility(View.GONE);
            id8.setVisibility(View.GONE);
            id9.setVisibility(View.GONE);
            id10.setVisibility(View.GONE);

            final Activity activity = (Activity) context;
            skip.setVisibility(View.GONE);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCameraScreen(position);
                    dialog.dismiss();
                }
            });
            callVisibility(workTypeID);
            if((BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"))) {
                if (workTypeID.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_PRODUCTION)) {

                    final EditText length = (EditText) dialog.findViewById(R.id.length);
                    final EditText height = (EditText) dialog.findViewById(R.id.height);
                    final EditText length_of_water_storage = (EditText) dialog.findViewById(R.id.length_of_water_storage);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_1);

                    if (additionalDataOffline.size() == 1) {
                        Length = additionalDataOffline.get(0).getLength();
                        Height = additionalDataOffline.get(0).getHeight();
                        Length_water_storage = additionalDataOffline.get(0).getLength_water_storage();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Length_water_storage.equalsIgnoreCase("")) {
                        length_of_water_storage.setText(Length_water_storage);
                    }
                    if (!Height.equalsIgnoreCase("")) {
                        height.setText(Height);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length.equalsIgnoreCase("") && !Length_water_storage.equalsIgnoreCase("")
                            && !Height.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!length.getText().toString().equalsIgnoreCase("")) {
                                if (!length_of_water_storage.getText().toString().equalsIgnoreCase("")) {
                                    if (!height.getText().toString().equalsIgnoreCase("")) {
                                        if (!observation.getText().toString().equalsIgnoreCase("")) {
                                            Length = length.getText().toString();
                                            Length_water_storage = length_of_water_storage.getText().toString();
                                            Height = height.getText().toString();
                                            Observation = observation.getText().toString();
                                            saveAdditionalData(position);
                                        } else {
                                            Utils.showAlert(activity, "Enter Observation!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter Height!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter Length Of Water Storage!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter Length!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length.setText("");
                            length_of_water_storage.setText("");
                            height.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_PRODUCTION)) {

                    final EditText no_of_trenches_per_one_work_site = (EditText) dialog.findViewById(R.id.no_of_trenches_per_one_work_site);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_2);

                    if (additionalDataOffline.size() == 1) {
                        No_trenches_per_worksite = additionalDataOffline.get(0).getNo_trenches_per_worksite();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("")) {
                        no_of_trenches_per_one_work_site.setText(No_trenches_per_worksite);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_trenches_per_one_work_site.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_trenches_per_worksite = no_of_trenches_per_one_work_site.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of trenches per work site!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_trenches_per_one_work_site.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_PRODUCTION)) {

                    final EditText no_of_units_per_water_body = (EditText) dialog.findViewById(R.id.no_of_units_per_waterbody);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_3);

                    if (additionalDataOffline.size() == 1) {
                        No_units_per_waterbody = additionalDataOffline.get(0).getNo_units_per_waterbody();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("")) {
                        no_of_units_per_water_body.setText(No_units_per_waterbody);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units_per_water_body.getText().toString().equalsIgnoreCase("")) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    No_units_per_waterbody = no_of_units_per_water_body.getText().toString();
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units per water body!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units_per_water_body.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_PRODUCTION)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_4);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_4);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_4);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_4);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_units = no_of_units.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_PRODUCTION)) {

                    final EditText area_covered_in_hect_5 = (EditText) dialog.findViewById(R.id.area_covered_in_hect_5);
                    final EditText no_of_plantation_per_site_5 = (EditText) dialog.findViewById(R.id.no_of_plantation_per_site_5);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_5);

                    if (additionalDataOffline.size() == 1) {
                        Area_covered = additionalDataOffline.get(0).getArea_covered();
                        No_plantation_per_site = additionalDataOffline.get(0).getNo_plantation_per_site();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Area_covered.equalsIgnoreCase("")) {
                        area_covered_in_hect_5.setText(Area_covered);
                    }
                    if (!No_plantation_per_site.equalsIgnoreCase("")) {
                        no_of_plantation_per_site_5.setText(No_plantation_per_site);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Area_covered.equalsIgnoreCase("") && !No_plantation_per_site.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!area_covered_in_hect_5.getText().toString().equalsIgnoreCase("")) {
                                if (!no_of_plantation_per_site_5.getText().toString().equalsIgnoreCase("")) {
                                    if (!observation.getText().toString().equalsIgnoreCase("")) {
                                        Area_covered = area_covered_in_hect_5.getText().toString();
                                        No_plantation_per_site = no_of_plantation_per_site_5.getText().toString();
                                        Observation = observation.getText().toString();
                                        saveAdditionalData(position);
                                    } else {
                                        Utils.showAlert(activity, "Enter Observation!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter no of plantation per site!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter area covered in hectare!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            area_covered_in_hect_5.setText("");
                            no_of_plantation_per_site_5.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_PRODUCTION)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units_6);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_6);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_6);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_6);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_6);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_units = no_of_units.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_PRODUCTION)) {

                    final RadioGroup radioYN = (RadioGroup) dialog.findViewById(R.id.radioYN);
                    final RadioButton storage_structure_sump_available_yes = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_yes);
                    final RadioButton storage_structure_sump_available_no = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_no);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_diameter_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_diameter_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_7);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_7);

                    storage_structure_sump_available_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "Y";
                                radioYN.clearCheck();
                                storage_structure_sump_available_yes.setEnabled(true);
                                storage_structure_sump_available_yes.setChecked(true);
                                storage_structure_sump_available_no.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    storage_structure_sump_available_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "N";
                                radioYN.clearCheck();
                                storage_structure_sump_available_no.setEnabled(true);
                                storage_structure_sump_available_no.setChecked(true);
                                storage_structure_sump_available_yes.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Structure_sump_available_status = additionalDataOffline.get(0).getStructure_sump_available_status();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("Y")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_yes.setEnabled(true);
                        storage_structure_sump_available_yes.setChecked(true);
                        storage_structure_sump_available_no.setChecked(false);
                    } else if (!Structure_sump_available_status.equalsIgnoreCase("N")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_no.setEnabled(true);
                        storage_structure_sump_available_no.setChecked(true);
                        storage_structure_sump_available_yes.setChecked(false);
                    } else {
                        radioYN.clearCheck();
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (storage_structure_sump_available_yes.isChecked() || storage_structure_sump_available_no.isChecked()) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                Structure_sump_available_status = structure_sump_radio_status;
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter storage structure/sump available status!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioYN.clearCheck();
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_PRODUCTION)) {

                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_8);

                    if (additionalDataOffline.size() == 1) {
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                Observation = observation.getText().toString();
                                saveAdditionalData(position);
                            } else {
                                Utils.showAlert(activity, "Enter Observation!");
                            }


                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Soakpit_PRODUCTION)) {

                    final RadioGroup radioInd_Comm = (RadioGroup) dialog.findViewById(R.id.radioInd_Comm);
                    final RadioButton individual = (RadioButton) dialog.findViewById(R.id.individual);
                    final RadioButton community = (RadioButton) dialog.findViewById(R.id.community);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_9);

                    individual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "1";
                                radioInd_Comm.clearCheck();
                                individual.setEnabled(true);
                                individual.setChecked(true);
                                community.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    community.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "2";
                                radioInd_Comm.clearCheck();
                                community.setEnabled(true);
                                community.setChecked(true);
                                individual.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Individual_community_status = additionalDataOffline.get(0).getIndividual_community();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Individual_community_status.equalsIgnoreCase("1")) {
                        radioInd_Comm.clearCheck();
                        individual.setEnabled(true);
                        individual.setChecked(true);
                        community.setChecked(false);
                    } else if (!Individual_community_status.equalsIgnoreCase("2")) {
                        radioInd_Comm.clearCheck();
                        community.setEnabled(true);
                        community.setChecked(true);
                        individual.setChecked(false);
                    } else {
                        radioInd_Comm.clearCheck();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Individual_community_status.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (individual.isChecked() || community.isChecked()) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    Individual_community_status = Individual_community_radio_status;
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Select Individual/Community!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioInd_Comm.clearCheck();
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_PRODUCTION)) {

                    final EditText length_of_channel = (EditText) dialog.findViewById(R.id.length_of_channel_in_m_10);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_10);

                    if (additionalDataOffline.size() == 1) {
                        Length_channel = additionalDataOffline.get(0).getLength_channel();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length_channel.equalsIgnoreCase("")) {
                        length_of_channel.setText(Length_channel);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length_channel.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!length_of_channel.getText().toString().equalsIgnoreCase("")) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    Length_channel = length_of_channel.getText().toString();
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter length of channel!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length_of_channel.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_Pit_PRODUCTION)) {

                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_8);

                    if (additionalDataOffline.size() == 1) {
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                Observation = observation.getText().toString();
                                saveAdditionalData(position);
                            } else {
                                Utils.showAlert(activity, "Enter Observation!");
                            }


                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_PERCOLATION_POND)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_4);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_4);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_4);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_4);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_units = no_of_units.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }

            }
            else {
                if (workTypeID.equalsIgnoreCase(KEY_CHECK_DAM_LOCAL)) {

                    final EditText length = (EditText) dialog.findViewById(R.id.length);
                    final EditText height = (EditText) dialog.findViewById(R.id.height);
                    final EditText length_of_water_storage = (EditText) dialog.findViewById(R.id.length_of_water_storage);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_1);

                    if (additionalDataOffline.size() == 1) {
                        Length = additionalDataOffline.get(0).getLength();
                        Height = additionalDataOffline.get(0).getHeight();
                        Length_water_storage = additionalDataOffline.get(0).getLength_water_storage();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Length_water_storage.equalsIgnoreCase("")) {
                        length_of_water_storage.setText(Length_water_storage);
                    }
                    if (!Height.equalsIgnoreCase("")) {
                        height.setText(Height);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length.equalsIgnoreCase("") && !Length_water_storage.equalsIgnoreCase("")
                            && !Height.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!length.getText().toString().equalsIgnoreCase("")) {
                                if (!length_of_water_storage.getText().toString().equalsIgnoreCase("")) {
                                    if (!height.getText().toString().equalsIgnoreCase("")) {
                                        if (!observation.getText().toString().equalsIgnoreCase("")) {
                                            Length = length.getText().toString();
                                            Length_water_storage = length_of_water_storage.getText().toString();
                                            Height = height.getText().toString();
                                            Observation = observation.getText().toString();
                                            saveAdditionalData(position);
                                        } else {
                                            Utils.showAlert(activity, "Enter Observation!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter Height!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter Length Of Water Storage!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter Length!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length.setText("");
                            length_of_water_storage.setText("");
                            height.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL)) {

                    final EditText no_of_trenches_per_one_work_site = (EditText) dialog.findViewById(R.id.no_of_trenches_per_one_work_site);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_2);

                    if (additionalDataOffline.size() == 1) {
                        No_trenches_per_worksite = additionalDataOffline.get(0).getNo_trenches_per_worksite();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("")) {
                        no_of_trenches_per_one_work_site.setText(No_trenches_per_worksite);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_trenches_per_one_work_site.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_trenches_per_worksite = no_of_trenches_per_one_work_site.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of trenches per work site!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_trenches_per_one_work_site.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL)) {

                    final EditText no_of_units_per_water_body = (EditText) dialog.findViewById(R.id.no_of_units_per_waterbody);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_3);

                    if (additionalDataOffline.size() == 1) {
                        No_units_per_waterbody = additionalDataOffline.get(0).getNo_units_per_waterbody();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("")) {
                        no_of_units_per_water_body.setText(No_units_per_waterbody);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units_per_water_body.getText().toString().equalsIgnoreCase("")) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    No_units_per_waterbody = no_of_units_per_water_body.getText().toString();
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units per water body!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units_per_water_body.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Recharge_wells_in_Rivers_Streams_LOCAL)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_4);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_4);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_4);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_4);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_units = no_of_units.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_AFFORESTATION_LOCAL)) {

                    final EditText area_covered_in_hect_5 = (EditText) dialog.findViewById(R.id.area_covered_in_hect_5);
                    final EditText no_of_plantation_per_site_5 = (EditText) dialog.findViewById(R.id.no_of_plantation_per_site_5);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_5);

                    if (additionalDataOffline.size() == 1) {
                        Area_covered = additionalDataOffline.get(0).getArea_covered();
                        No_plantation_per_site = additionalDataOffline.get(0).getNo_plantation_per_site();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Area_covered.equalsIgnoreCase("")) {
                        area_covered_in_hect_5.setText(Area_covered);
                    }
                    if (!No_plantation_per_site.equalsIgnoreCase("")) {
                        no_of_plantation_per_site_5.setText(No_plantation_per_site);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Area_covered.equalsIgnoreCase("") && !No_plantation_per_site.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!area_covered_in_hect_5.getText().toString().equalsIgnoreCase("")) {
                                if (!no_of_plantation_per_site_5.getText().toString().equalsIgnoreCase("")) {
                                    if (!observation.getText().toString().equalsIgnoreCase("")) {
                                        Area_covered = area_covered_in_hect_5.getText().toString();
                                        No_plantation_per_site = no_of_plantation_per_site_5.getText().toString();
                                        Observation = observation.getText().toString();
                                        saveAdditionalData(position);
                                    } else {
                                        Utils.showAlert(activity, "Enter Observation!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter no of plantation per site!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter area covered in hectare!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            area_covered_in_hect_5.setText("");
                            no_of_plantation_per_site_5.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Farm_ponds_LOCAL)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units_6);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_6);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_6);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_6);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_6);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_units = no_of_units.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Rooftop_RWHS_LOCAL)) {

                    final RadioGroup radioYN = (RadioGroup) dialog.findViewById(R.id.radioYN);
                    final RadioButton storage_structure_sump_available_yes = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_yes);
                    final RadioButton storage_structure_sump_available_no = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_no);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_diameter_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_diameter_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_7);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_7);

                    storage_structure_sump_available_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "Y";
                                radioYN.clearCheck();
                                storage_structure_sump_available_yes.setEnabled(true);
                                storage_structure_sump_available_yes.setChecked(true);
                                storage_structure_sump_available_no.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    storage_structure_sump_available_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "N";
                                radioYN.clearCheck();
                                storage_structure_sump_available_no.setEnabled(true);
                                storage_structure_sump_available_no.setChecked(true);
                                storage_structure_sump_available_yes.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Structure_sump_available_status = additionalDataOffline.get(0).getStructure_sump_available_status();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("Y")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_yes.setEnabled(true);
                        storage_structure_sump_available_yes.setChecked(true);
                        storage_structure_sump_available_no.setChecked(false);
                    } else if (!Structure_sump_available_status.equalsIgnoreCase("N")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_no.setEnabled(true);
                        storage_structure_sump_available_no.setChecked(true);
                        storage_structure_sump_available_yes.setChecked(false);
                    } else {
                        radioYN.clearCheck();
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (storage_structure_sump_available_yes.isChecked() || storage_structure_sump_available_no.isChecked()) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                Structure_sump_available_status = structure_sump_radio_status;
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter storage structure/sump available status!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioYN.clearCheck();
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Recharge_shaft_LOCAL)) {

                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_8);

                    if (additionalDataOffline.size() == 1) {
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                Observation = observation.getText().toString();
                                saveAdditionalData(position);
                            } else {
                                Utils.showAlert(activity, "Enter Observation!");
                            }


                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Soakpit_LOCAL)) {

                    final RadioGroup radioInd_Comm = (RadioGroup) dialog.findViewById(R.id.radioInd_Comm);
                    final RadioButton individual = (RadioButton) dialog.findViewById(R.id.individual);
                    final RadioButton community = (RadioButton) dialog.findViewById(R.id.community);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_9);

                    individual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "1";
                                radioInd_Comm.clearCheck();
                                individual.setEnabled(true);
                                individual.setChecked(true);
                                community.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    community.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "2";
                                radioInd_Comm.clearCheck();
                                community.setEnabled(true);
                                community.setChecked(true);
                                individual.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Individual_community_status = additionalDataOffline.get(0).getIndividual_community();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Individual_community_status.equalsIgnoreCase("1")) {
                        radioInd_Comm.clearCheck();
                        individual.setEnabled(true);
                        individual.setChecked(true);
                        community.setChecked(false);
                    } else if (!Individual_community_status.equalsIgnoreCase("2")) {
                        radioInd_Comm.clearCheck();
                        community.setEnabled(true);
                        community.setChecked(true);
                        individual.setChecked(false);
                    } else {
                        radioInd_Comm.clearCheck();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Individual_community_status.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (individual.isChecked() || community.isChecked()) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    Individual_community_status = Individual_community_radio_status;
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Select Individual/Community!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioInd_Comm.clearCheck();
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Sunken_pond_and_desilting_of_channel_LOCAL)) {

                    final EditText length_of_channel = (EditText) dialog.findViewById(R.id.length_of_channel_in_m_10);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_10);

                    if (additionalDataOffline.size() == 1) {
                        Length_channel = additionalDataOffline.get(0).getLength_channel();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length_channel.equalsIgnoreCase("")) {
                        length_of_channel.setText(Length_channel);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length_channel.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!length_of_channel.getText().toString().equalsIgnoreCase("")) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    Length_channel = length_of_channel.getText().toString();
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter length of channel!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length_of_channel.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVisibility(String workTypeID) {
        if((BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"))) {
            if (workTypeID.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_PRODUCTION)) {
                id1.setVisibility(View.VISIBLE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.VISIBLE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.VISIBLE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.VISIBLE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.VISIBLE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.VISIBLE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.VISIBLE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.VISIBLE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Soakpit_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.VISIBLE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.VISIBLE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_Pit_PRODUCTION)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.VISIBLE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_PERCOLATION_POND)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.VISIBLE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }

        }
        else  {

            if (workTypeID.equalsIgnoreCase(KEY_CHECK_DAM_LOCAL)) {
                id1.setVisibility(View.VISIBLE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.VISIBLE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.VISIBLE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_Recharge_wells_in_Rivers_Streams_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.VISIBLE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_AFFORESTATION_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.VISIBLE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_Farm_ponds_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.VISIBLE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_Rooftop_RWHS_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.VISIBLE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_Recharge_shaft_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.VISIBLE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_Soakpit_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.VISIBLE);
                id10.setVisibility(View.GONE);
            }
            else if (workTypeID.equalsIgnoreCase(KEY_Sunken_pond_and_desilting_of_channel_LOCAL)) {
                id1.setVisibility(View.GONE);
                id2.setVisibility(View.GONE);
                id3.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id7.setVisibility(View.GONE);
                id8.setVisibility(View.GONE);
                id9.setVisibility(View.GONE);
                id10.setVisibility(View.VISIBLE);
            }


        }

    }
    public  void showAlertDetails(final Context context, final int position){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.alert_dialog_for_scheme, null);
            dialog = builder.create();
            ArrayList<RealTimeMonitoringSystem> additionalDataOffline = new ArrayList<>();;

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setView(dialogView, 0, 0, 0, 0);
            dialog.setCancelable(true);
            dialog.show();

            String work_id = String.valueOf(WorkListValuesFiltered.get(position).getWorkId());
            String workGroupID = WorkListValuesFiltered.get(position).getWorkGroupID();
            String workTypeID = WorkListValuesFiltered.get(position).getWorkTypeID();
            dbData.open();
            ArrayList<RealTimeMonitoringSystem> additionalDataSavedOffline = dbData.selectAdditionalData(work_id,AppConstant.MAIN_WORK,"");
            ArrayList<RealTimeMonitoringSystem> additionalDetailsOffline = dbData.selectAdditionalDetailsList(work_id,workTypeID,"");
            if (additionalDataSavedOffline.size() == 1) {
                additionalDataOffline=additionalDataSavedOffline;
            }else if (additionalDetailsOffline.size() == 1) {
                additionalDataOffline=additionalDetailsOffline;
            }
            TextView submit_update_btn = (TextView) dialog.findViewById(R.id.submit_update_btn);
            final ImageView close_icon = (ImageView) dialog.findViewById(R.id.close_icon);
            final TextView skip = (TextView) dialog.findViewById(R.id.skip);
            id1 = (LinearLayout) dialog.findViewById(R.id.id_1);
            id2 = (LinearLayout) dialog.findViewById(R.id.id_2);
            id3 = (LinearLayout) dialog.findViewById(R.id.id_3);
            id4 = (LinearLayout) dialog.findViewById(R.id.id_4);
            id5 = (LinearLayout) dialog.findViewById(R.id.id_5);
            id6 = (LinearLayout) dialog.findViewById(R.id.id_6);
            id7 = (LinearLayout) dialog.findViewById(R.id.id_7);
            id8 = (LinearLayout) dialog.findViewById(R.id.id_8);
            id9 = (LinearLayout) dialog.findViewById(R.id.id_9);
            id10 = (LinearLayout) dialog.findViewById(R.id.id_10);
            id1.setVisibility(View.GONE);
            id2.setVisibility(View.GONE);
            id3.setVisibility(View.GONE);
            id4.setVisibility(View.GONE);
            id5.setVisibility(View.GONE);
            id6.setVisibility(View.GONE);
            id7.setVisibility(View.GONE);
            id8.setVisibility(View.GONE);
            id9.setVisibility(View.GONE);
            id10.setVisibility(View.GONE);

            final Activity activity = (Activity) context;
            skip.setVisibility(View.GONE);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCameraScreen(position);
                    dialog.dismiss();
                }
            });
            callVisibility(workTypeID);
            if((BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"))) {
                if (workTypeID.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_PRODUCTION)) {

                    final EditText length = (EditText) dialog.findViewById(R.id.length);
                    final EditText height = (EditText) dialog.findViewById(R.id.height);
                    final EditText length_of_water_storage = (EditText) dialog.findViewById(R.id.length_of_water_storage);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_1);

                    length.setFocusable(false);
                    height.setFocusable(false);
                    length_of_water_storage.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        Length = additionalDataOffline.get(0).getLength();
                        Height = additionalDataOffline.get(0).getHeight();
                        Length_water_storage = additionalDataOffline.get(0).getLength_water_storage();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Length_water_storage.equalsIgnoreCase("")) {
                        length_of_water_storage.setText(Length_water_storage);
                    }
                    if (!Height.equalsIgnoreCase("")) {
                        height.setText(Height);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length.equalsIgnoreCase("") && !Length_water_storage.equalsIgnoreCase("")
                            && !Height.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);
                    } else {
                        skip.setVisibility(View.GONE);
                        length.setText("");
                        length_of_water_storage.setText("");
                        height.setText("");
                        observation.setText("");
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }


                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length.setText("");
                            length_of_water_storage.setText("");
                            height.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_PRODUCTION)) {

                    final EditText no_of_trenches_per_one_work_site = (EditText) dialog.findViewById(R.id.no_of_trenches_per_one_work_site);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_2);

                    no_of_trenches_per_one_work_site.setFocusable(false);
                    length.setFocusable(false);
                    width.setFocusable(false);
                    depth.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        No_trenches_per_worksite = additionalDataOffline.get(0).getNo_trenches_per_worksite();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("")) {
                        no_of_trenches_per_one_work_site.setText(No_trenches_per_worksite);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);
                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        no_of_trenches_per_one_work_site.setText("");
                        length.setText("");
                        width.setText("");
                        depth.setText("");
                        observation.setText("");
                        dialog.dismiss();
                    }


                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_trenches_per_one_work_site.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_PRODUCTION)) {

                    final EditText no_of_units_per_water_body = (EditText) dialog.findViewById(R.id.no_of_units_per_waterbody);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_3);

                    no_of_units_per_water_body.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        No_units_per_waterbody = additionalDataOffline.get(0).getNo_units_per_waterbody();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("")) {
                        no_of_units_per_water_body.setText(No_units_per_waterbody);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        no_of_units_per_water_body.setText("");
                        observation.setText("");
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units_per_water_body.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_PRODUCTION)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_4);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_4);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_4);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_4);

                    no_of_units.setFocusable(false);
                    length.setFocusable(false);
                    width.setFocusable(false);
                    depth.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        no_of_units.setText("");
                        length.setText("");
                        width.setText("");
                        depth.setText("");
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_PRODUCTION)) {

                    final EditText area_covered_in_hect_5 = (EditText) dialog.findViewById(R.id.area_covered_in_hect_5);
                    final EditText no_of_plantation_per_site_5 = (EditText) dialog.findViewById(R.id.no_of_plantation_per_site_5);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_5);

                    area_covered_in_hect_5.setFocusable(false);
                    no_of_plantation_per_site_5.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        Area_covered = additionalDataOffline.get(0).getArea_covered();
                        No_plantation_per_site = additionalDataOffline.get(0).getNo_plantation_per_site();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Area_covered.equalsIgnoreCase("")) {
                        area_covered_in_hect_5.setText(Area_covered);
                    }
                    if (!No_plantation_per_site.equalsIgnoreCase("")) {
                        no_of_plantation_per_site_5.setText(No_plantation_per_site);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Area_covered.equalsIgnoreCase("") && !No_plantation_per_site.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        area_covered_in_hect_5.setText("");
                        no_of_plantation_per_site_5.setText("");
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            area_covered_in_hect_5.setText("");
                            no_of_plantation_per_site_5.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_PRODUCTION)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units_6);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_6);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_6);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_6);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_6);

                    no_of_units.setFocusable(false);
                    length.setFocusable(false);
                    width.setFocusable(false);
                    depth.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added", Toast.LENGTH_SHORT).show();
                        no_of_units.setText("");
                        length.setText("");
                        width.setText("");
                        depth.setText("");
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_PRODUCTION)) {

                    final RadioGroup radioYN = (RadioGroup) dialog.findViewById(R.id.radioYN);
                    final RadioButton storage_structure_sump_available_yes = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_yes);
                    final RadioButton storage_structure_sump_available_no = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_no);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_diameter_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_diameter_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_7);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_7);

                    length.setFocusable(false);
                    width.setFocusable(false);
                    depth.setFocusable(false);
                    observation.setFocusable(false);
                    storage_structure_sump_available_no.setClickable(false);
                    storage_structure_sump_available_yes.setClickable(false);

                    storage_structure_sump_available_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "Y";
                                radioYN.clearCheck();
                                storage_structure_sump_available_yes.setEnabled(true);
                                storage_structure_sump_available_yes.setChecked(true);
                                storage_structure_sump_available_no.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    storage_structure_sump_available_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "N";
                                radioYN.clearCheck();
                                storage_structure_sump_available_no.setEnabled(true);
                                storage_structure_sump_available_no.setChecked(true);
                                storage_structure_sump_available_yes.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Structure_sump_available_status = additionalDataOffline.get(0).getStructure_sump_available_status();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("Y")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_yes.setEnabled(true);
                        storage_structure_sump_available_yes.setChecked(true);
                        storage_structure_sump_available_no.setChecked(false);
                    } else if (!Structure_sump_available_status.equalsIgnoreCase("N")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_no.setEnabled(true);
                        storage_structure_sump_available_no.setChecked(true);
                        storage_structure_sump_available_yes.setChecked(false);
                    } else {
                        radioYN.clearCheck();
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);
                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        radioYN.clearCheck();
                        length.setText("");
                        width.setText("");
                        depth.setText("");
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioYN.clearCheck();
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_PRODUCTION)) {

                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_8);

                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Soakpit_PRODUCTION)) {

                    final RadioGroup radioInd_Comm = (RadioGroup) dialog.findViewById(R.id.radioInd_Comm);
                    final RadioButton individual = (RadioButton) dialog.findViewById(R.id.individual);
                    final RadioButton community = (RadioButton) dialog.findViewById(R.id.community);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_9);

                    observation.setFocusable(false);
                    individual.setClickable(false);
                    community.setClickable(false);

                    individual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "1";
                                radioInd_Comm.clearCheck();
                                individual.setEnabled(true);
                                individual.setChecked(true);
                                community.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    community.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "2";
                                radioInd_Comm.clearCheck();
                                community.setEnabled(true);
                                community.setChecked(true);
                                individual.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Individual_community_status = additionalDataOffline.get(0).getIndividual_community();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Individual_community_status.equalsIgnoreCase("1")) {
                        radioInd_Comm.clearCheck();
                        individual.setEnabled(true);
                        individual.setChecked(true);
                        community.setChecked(false);
                    } else if (!Individual_community_status.equalsIgnoreCase("2")) {
                        radioInd_Comm.clearCheck();
                        community.setEnabled(true);
                        community.setChecked(true);
                        individual.setChecked(false);
                    } else {
                        radioInd_Comm.clearCheck();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Individual_community_status.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        radioInd_Comm.clearCheck();
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioInd_Comm.clearCheck();
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_PRODUCTION)) {

                    final EditText length_of_channel = (EditText) dialog.findViewById(R.id.length_of_channel_in_m_10);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_10);

                    length_of_channel.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        Length_channel = additionalDataOffline.get(0).getLength_channel();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length_channel.equalsIgnoreCase("")) {
                        length_of_channel.setText(Length_channel);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length_channel.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        length_of_channel.setText("");
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length_of_channel.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_Pit_PRODUCTION)) {

                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_8);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_PERCOLATION_POND)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_4);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_4);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_4);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_4);

                    no_of_units.setFocusable(false);
                    length.setFocusable(false);
                    width.setFocusable(false);
                    depth.setFocusable(false);
                    observation.setFocusable(false);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setVisibility(View.GONE);

                    } else {
                        skip.setVisibility(View.GONE);
                        Toast.makeText(context, "No Values Added!", Toast.LENGTH_SHORT).show();
                        no_of_units.setText("");
                        length.setText("");
                        width.setText("");
                        depth.setText("");
                        observation.setText("");
                        dialog.dismiss();
                    }

                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }

            }
            else {
                if (workTypeID.equalsIgnoreCase(KEY_CHECK_DAM_LOCAL)) {

                    final EditText length = (EditText) dialog.findViewById(R.id.length);
                    final EditText height = (EditText) dialog.findViewById(R.id.height);
                    final EditText length_of_water_storage = (EditText) dialog.findViewById(R.id.length_of_water_storage);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_1);

                    if (additionalDataOffline.size() == 1) {
                        Length = additionalDataOffline.get(0).getLength();
                        Height = additionalDataOffline.get(0).getHeight();
                        Length_water_storage = additionalDataOffline.get(0).getLength_water_storage();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Length_water_storage.equalsIgnoreCase("")) {
                        length_of_water_storage.setText(Length_water_storage);
                    }
                    if (!Height.equalsIgnoreCase("")) {
                        height.setText(Height);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length.equalsIgnoreCase("") && !Length_water_storage.equalsIgnoreCase("")
                            && !Height.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!length.getText().toString().equalsIgnoreCase("")) {
                                if (!length_of_water_storage.getText().toString().equalsIgnoreCase("")) {
                                    if (!height.getText().toString().equalsIgnoreCase("")) {
                                        if (!observation.getText().toString().equalsIgnoreCase("")) {
                                            Length = length.getText().toString();
                                            Length_water_storage = length_of_water_storage.getText().toString();
                                            Height = height.getText().toString();
                                            Observation = observation.getText().toString();
                                            saveAdditionalData(position);
                                        } else {
                                            Utils.showAlert(activity, "Enter Observation!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter Height!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter Length Of Water Storage!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter Length!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length.setText("");
                            length_of_water_storage.setText("");
                            height.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL)) {

                    final EditText no_of_trenches_per_one_work_site = (EditText) dialog.findViewById(R.id.no_of_trenches_per_one_work_site);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_2);

                    if (additionalDataOffline.size() == 1) {
                        No_trenches_per_worksite = additionalDataOffline.get(0).getNo_trenches_per_worksite();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("")) {
                        no_of_trenches_per_one_work_site.setText(No_trenches_per_worksite);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_trenches_per_worksite.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_trenches_per_one_work_site.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_trenches_per_worksite = no_of_trenches_per_one_work_site.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of trenches per work site!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_trenches_per_one_work_site.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL)) {

                    final EditText no_of_units_per_water_body = (EditText) dialog.findViewById(R.id.no_of_units_per_waterbody);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_3);

                    if (additionalDataOffline.size() == 1) {
                        No_units_per_waterbody = additionalDataOffline.get(0).getNo_units_per_waterbody();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("")) {
                        no_of_units_per_water_body.setText(No_units_per_waterbody);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units_per_waterbody.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units_per_water_body.getText().toString().equalsIgnoreCase("")) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    No_units_per_waterbody = no_of_units_per_water_body.getText().toString();
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units per water body!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units_per_water_body.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Recharge_wells_in_Rivers_Streams_LOCAL)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_4);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_4);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_4);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_4);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_units = no_of_units.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_AFFORESTATION_LOCAL)) {

                    final EditText area_covered_in_hect_5 = (EditText) dialog.findViewById(R.id.area_covered_in_hect_5);
                    final EditText no_of_plantation_per_site_5 = (EditText) dialog.findViewById(R.id.no_of_plantation_per_site_5);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_5);

                    if (additionalDataOffline.size() == 1) {
                        Area_covered = additionalDataOffline.get(0).getArea_covered();
                        No_plantation_per_site = additionalDataOffline.get(0).getNo_plantation_per_site();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Area_covered.equalsIgnoreCase("")) {
                        area_covered_in_hect_5.setText(Area_covered);
                    }
                    if (!No_plantation_per_site.equalsIgnoreCase("")) {
                        no_of_plantation_per_site_5.setText(No_plantation_per_site);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Area_covered.equalsIgnoreCase("") && !No_plantation_per_site.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!area_covered_in_hect_5.getText().toString().equalsIgnoreCase("")) {
                                if (!no_of_plantation_per_site_5.getText().toString().equalsIgnoreCase("")) {
                                    if (!observation.getText().toString().equalsIgnoreCase("")) {
                                        Area_covered = area_covered_in_hect_5.getText().toString();
                                        No_plantation_per_site = no_of_plantation_per_site_5.getText().toString();
                                        Observation = observation.getText().toString();
                                        saveAdditionalData(position);
                                    } else {
                                        Utils.showAlert(activity, "Enter Observation!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter no of plantation per site!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter area covered in hectare!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            area_covered_in_hect_5.setText("");
                            no_of_plantation_per_site_5.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Farm_ponds_LOCAL)) {

                    final EditText no_of_units = (EditText) dialog.findViewById(R.id.no_of_units_6);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_in_m_6);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_in_m_6);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_6);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_6);

                    if (additionalDataOffline.size() == 1) {
                        No_units = additionalDataOffline.get(0).getNo_units();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!No_units.equalsIgnoreCase("")) {
                        no_of_units.setText(No_units);
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!No_units.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!no_of_units.getText().toString().equalsIgnoreCase("")) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                No_units = no_of_units.getText().toString();
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter no of units!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_of_units.setText("");
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Rooftop_RWHS_LOCAL)) {

                    final RadioGroup radioYN = (RadioGroup) dialog.findViewById(R.id.radioYN);
                    final RadioButton storage_structure_sump_available_yes = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_yes);
                    final RadioButton storage_structure_sump_available_no = (RadioButton) dialog.findViewById(R.id.storage_structure_sump_available_no);
                    final EditText length = (EditText) dialog.findViewById(R.id.length_diameter_in_m);
                    final EditText width = (EditText) dialog.findViewById(R.id.width_diameter_in_m);
                    final EditText depth = (EditText) dialog.findViewById(R.id.depth_in_m_7);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_7);

                    storage_structure_sump_available_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "Y";
                                radioYN.clearCheck();
                                storage_structure_sump_available_yes.setEnabled(true);
                                storage_structure_sump_available_yes.setChecked(true);
                                storage_structure_sump_available_no.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    storage_structure_sump_available_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                structure_sump_radio_status = "N";
                                radioYN.clearCheck();
                                storage_structure_sump_available_no.setEnabled(true);
                                storage_structure_sump_available_no.setChecked(true);
                                storage_structure_sump_available_yes.setChecked(false);
                                Log.d("radioYN", structure_sump_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Structure_sump_available_status = additionalDataOffline.get(0).getStructure_sump_available_status();
                        Length = additionalDataOffline.get(0).getLength();
                        Width = additionalDataOffline.get(0).getWidth();
                        Depth = additionalDataOffline.get(0).getDepth();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("Y")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_yes.setEnabled(true);
                        storage_structure_sump_available_yes.setChecked(true);
                        storage_structure_sump_available_no.setChecked(false);
                    } else if (!Structure_sump_available_status.equalsIgnoreCase("N")) {
                        radioYN.clearCheck();
                        storage_structure_sump_available_no.setEnabled(true);
                        storage_structure_sump_available_no.setChecked(true);
                        storage_structure_sump_available_yes.setChecked(false);
                    } else {
                        radioYN.clearCheck();
                    }
                    if (!Length.equalsIgnoreCase("")) {
                        length.setText(Length);
                    }
                    if (!Width.equalsIgnoreCase("")) {
                        width.setText(Width);
                    }
                    if (!Depth.equalsIgnoreCase("")) {
                        depth.setText(Depth);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Structure_sump_available_status.equalsIgnoreCase("") && !Length.equalsIgnoreCase("") && !Width.equalsIgnoreCase("")
                            && !Depth.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (storage_structure_sump_available_yes.isChecked() || storage_structure_sump_available_no.isChecked()) {
                                if (!length.getText().toString().equalsIgnoreCase("")) {
                                    if (!width.getText().toString().equalsIgnoreCase("")) {
                                        if (!depth.getText().toString().equalsIgnoreCase("")) {
                                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                                Structure_sump_available_status = structure_sump_radio_status;
                                                Length = length.getText().toString();
                                                Width = width.getText().toString();
                                                Depth = depth.getText().toString();
                                                Observation = observation.getText().toString();
                                                saveAdditionalData(position);
                                            } else {
                                                Utils.showAlert(activity, "Enter Observation!");
                                            }
                                        } else {
                                            Utils.showAlert(activity, "Enter depth!");
                                        }
                                    } else {
                                        Utils.showAlert(activity, "Enter width!");
                                    }
                                } else {
                                    Utils.showAlert(activity, "Enter length!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter storage structure/sump available status!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioYN.clearCheck();
                            length.setText("");
                            width.setText("");
                            depth.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Recharge_shaft_LOCAL)) {

                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_8);

                    if (additionalDataOffline.size() == 1) {
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!observation.getText().toString().equalsIgnoreCase("")) {
                                Observation = observation.getText().toString();
                                saveAdditionalData(position);
                            } else {
                                Utils.showAlert(activity, "Enter Observation!");
                            }


                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Soakpit_LOCAL)) {

                    final RadioGroup radioInd_Comm = (RadioGroup) dialog.findViewById(R.id.radioInd_Comm);
                    final RadioButton individual = (RadioButton) dialog.findViewById(R.id.individual);
                    final RadioButton community = (RadioButton) dialog.findViewById(R.id.community);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_9);

                    individual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "1";
                                radioInd_Comm.clearCheck();
                                individual.setEnabled(true);
                                individual.setChecked(true);
                                community.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    community.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                Individual_community_radio_status = "2";
                                radioInd_Comm.clearCheck();
                                community.setEnabled(true);
                                community.setChecked(true);
                                individual.setChecked(false);
                                Log.d("radioInd_Comm", Individual_community_radio_status);
                            }
                        }
                    });
                    if (additionalDataOffline.size() == 1) {
                        Individual_community_status = additionalDataOffline.get(0).getIndividual_community();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Individual_community_status.equalsIgnoreCase("1")) {
                        radioInd_Comm.clearCheck();
                        individual.setEnabled(true);
                        individual.setChecked(true);
                        community.setChecked(false);
                    } else if (!Individual_community_status.equalsIgnoreCase("2")) {
                        radioInd_Comm.clearCheck();
                        community.setEnabled(true);
                        community.setChecked(true);
                        individual.setChecked(false);
                    } else {
                        radioInd_Comm.clearCheck();
                    }

                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Individual_community_status.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (individual.isChecked() || community.isChecked()) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    Individual_community_status = Individual_community_radio_status;
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Select Individual/Community!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            radioInd_Comm.clearCheck();
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
                else if (workTypeID.equalsIgnoreCase(KEY_Sunken_pond_and_desilting_of_channel_LOCAL)) {

                    final EditText length_of_channel = (EditText) dialog.findViewById(R.id.length_of_channel_in_m_10);
                    final EditText observation = (EditText) dialog.findViewById(R.id.observation_premonsoon_gw_depth_in_m_10);

                    if (additionalDataOffline.size() == 1) {
                        Length_channel = additionalDataOffline.get(0).getLength_channel();
                        Observation = additionalDataOffline.get(0).getObservation();
                    }

                    if (!Length_channel.equalsIgnoreCase("")) {
                        length_of_channel.setText(Length_channel);
                    }
                    if (!Observation.equalsIgnoreCase("")) {
                        observation.setText(Observation);
                    }

                    if (!Length_channel.equalsIgnoreCase("") && !Observation.equalsIgnoreCase("")) {
                        skip.setVisibility(View.VISIBLE);
                        submit_update_btn.setText("Update");
                    } else {
                        skip.setVisibility(View.GONE);
                        submit_update_btn.setText("Submit");
                    }

                    submit_update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!length_of_channel.getText().toString().equalsIgnoreCase("")) {
                                if (!observation.getText().toString().equalsIgnoreCase("")) {
                                    Length_channel = length_of_channel.getText().toString();
                                    Observation = observation.getText().toString();
                                    saveAdditionalData(position);
                                } else {
                                    Utils.showAlert(activity, "Enter Observation!");
                                }
                            } else {
                                Utils.showAlert(activity, "Enter length of channel!");
                            }

                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            length_of_channel.setText("");
                            observation.setText("");
                            dialog.dismiss();
                        }
                    });

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    WorkListValuesFiltered = WorkListValues;
                } else {
                    List<RealTimeMonitoringSystem> filteredList = new ArrayList<>();
                    for (RealTimeMonitoringSystem row : WorkListValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getWorkId().toString().toLowerCase().contains(charString.toLowerCase()) || row.getBeneficiaryName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    WorkListValuesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = WorkListValuesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                WorkListValuesFiltered = (ArrayList<RealTimeMonitoringSystem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void openCameraScreen(int pos) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, CameraScreen.class);
        intent.putExtra(AppConstant.TYPE_OF_WORK,AppConstant.MAIN_WORK);
        intent.putExtra(AppConstant.WORK_ID,String.valueOf(WorkListValuesFiltered.get(pos).getWorkId()));
        intent.putExtra(AppConstant.WORK_GROUP_ID,WorkListValuesFiltered.get(pos).getWorkGroupID());
        intent.putExtra(AppConstant.WORK_TYPE_ID,WorkListValuesFiltered.get(pos).getWorkTypeID());
        intent.putExtra(AppConstant.CURRENT_STAGE_OF_WORK,String.valueOf(WorkListValuesFiltered.get(pos).getCurrentStage()));
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void saveAdditionalData(int position) {
        dbData.open();
        long id = 0; String whereClause = "";String[] whereArgs = null;
        String work_id = String.valueOf(WorkListValuesFiltered.get(position).getWorkId());
        String workGroupID = /*"1"*/WorkListValuesFiltered.get(position).getWorkGroupID();
        String workTypeID = WorkListValuesFiltered.get(position).getWorkTypeID();
        String dcode = prefManager.getDistrictCode();
        String bcode = prefManager.getBlockCode();
        String pvcode = prefManager.getPvCode();
        if(getLatlang) {
            try {
                values = new ContentValues();
                values.put(AppConstant.WORK_ID, work_id);
                values.put(AppConstant.DISTRICT_CODE, dcode);
                values.put(AppConstant.BLOCK_CODE, bcode);
                values.put(AppConstant.PV_CODE, pvcode);
                values.put(AppConstant.WORK_GROUP_ID, workGroupID);
                values.put(AppConstant.WORK_TYPE_ID, workTypeID);
                values.put(AppConstant.TYPE_OF_WORK, AppConstant.MAIN_WORK);
                values.put(AppConstant.KEY_LATITUDE, offlatTextValue);
                values.put(AppConstant.KEY_LONGITUDE, offlongTextValue);

                if((BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"))) {
                    if (workTypeID.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_PRODUCTION)) {
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_LENGTH_WATER_STORAGE, Length_water_storage);
                        values.put(AppConstant.KEY_HEIGHT, Height);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_PRODUCTION)) {
                        values.put(AppConstant.KEY_NO_TRENCHES_PER_WORKSITE, No_trenches_per_worksite);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_PRODUCTION)) {
                        values.put(AppConstant.KEY_NO_UNITS_PER_WATERBODY, No_units_per_waterbody);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_PRODUCTION)) {
                        values.put(AppConstant.KEY_NO_UNITS, No_units);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_PRODUCTION)) {
                        values.put(AppConstant.KEY_AREA_COVERED, Area_covered);
                        values.put(AppConstant.KEY_NO_PLANTATION_PER_SITE, No_plantation_per_site);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_PRODUCTION)) {
                        values.put(AppConstant.KEY_NO_UNITS, No_units);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_PRODUCTION)) {
                        values.put(AppConstant.KEY_STRUCTURE_SUMP_AVAILABLE_STATUS, Structure_sump_available_status);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_PRODUCTION)) {
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Soakpit_PRODUCTION)) {
                        values.put(AppConstant.KEY_INDIVIDUAL_COMMUNITY, Individual_community_status);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_PRODUCTION)) {
                        values.put(AppConstant.KEY_LENGTH_CHANNEL, Length_channel);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_Recharge_Pit_PRODUCTION)) {
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(AppConstant.KEY_PERCOLATION_POND)) {
                        values.put(AppConstant.KEY_NO_UNITS, No_units);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }


                }
                else {
                    if (workTypeID.equalsIgnoreCase(KEY_CHECK_DAM_LOCAL)) {
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_LENGTH_WATER_STORAGE, Length_water_storage);
                        values.put(AppConstant.KEY_HEIGHT, Height);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL)) {
                        values.put(AppConstant.KEY_NO_TRENCHES_PER_WORKSITE, No_trenches_per_worksite);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL)) {
                        values.put(AppConstant.KEY_NO_UNITS_PER_WATERBODY, No_units_per_waterbody);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_Recharge_wells_in_Rivers_Streams_LOCAL)) {
                        values.put(AppConstant.KEY_NO_UNITS, No_units);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_AFFORESTATION_LOCAL)) {
                        values.put(AppConstant.KEY_AREA_COVERED, Area_covered);
                        values.put(AppConstant.KEY_NO_PLANTATION_PER_SITE, No_plantation_per_site);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_Farm_ponds_LOCAL)) {
                        values.put(AppConstant.KEY_NO_UNITS, No_units);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_Rooftop_RWHS_LOCAL)) {
                        values.put(AppConstant.KEY_STRUCTURE_SUMP_AVAILABLE_STATUS, Structure_sump_available_status);
                        values.put(AppConstant.KEY_LENGTH, Length);
                        values.put(AppConstant.KEY_WIDTH, Width);
                        values.put(AppConstant.KEY_DEPTH, Depth);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_Recharge_shaft_LOCAL)) {
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_Soakpit_LOCAL)) {
                        values.put(AppConstant.KEY_INDIVIDUAL_COMMUNITY, Individual_community_status);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                    else if (workTypeID.equalsIgnoreCase(KEY_Sunken_pond_and_desilting_of_channel_LOCAL)) {
                        values.put(AppConstant.KEY_LENGTH_CHANNEL, Length_channel);
                        values.put(AppConstant.KEY_OBSERVATION, Observation);
                    }
                }

                ArrayList<RealTimeMonitoringSystem> additionalDataOffline = dbData.selectAdditionalData(work_id, AppConstant.MAIN_WORK, "");

                if (additionalDataOffline.size() < 1) {
                    id = db.insert(DBHelper.SAVE_ADDITIONAL_DATA, null, values);
                    if (id > 0) {
                        dialog.dismiss();
                        Toasty.success(activity, "Inserted Successfully!", Toast.LENGTH_LONG, true).show();
                        openCameraScreen(position);
                    }
                } else {
                    id = db.update(DBHelper.SAVE_ADDITIONAL_DATA, values, whereClause, whereArgs);
                    if (id > 0) {
                        dialog.dismiss();
                        Toasty.success(activity, "Updated Successfully!", Toast.LENGTH_LONG, true).show();
                        openCameraScreen(position);
                    }
                }


                Log.d("insIdAdditionalData", String.valueOf(id));
                Log.d("values", String.valueOf(values));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            //Utils.showAlert(activity,"Something Wrong");
        }
    }

    private void getLocationPermissionWithLatLong() {
        mlocManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(activity,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    activity.requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (MyLocationListener.latitude > 0) {
                        offlatTextValue = MyLocationListener.latitude;
                        offlongTextValue = MyLocationListener.longitude;
                        getLatlang=true;
                    }
//                            checkPermissionForCamera();
                } else {
                    getLatlang=false;
//                    captureImage();
                }
            } else {
                getLatlang=false;
                Utils.showAlert(activity, "Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            }
        } else {
            getLatlang=false;
            Utils.showAlert(activity, "GPS is not turned on...");
        }
    }



    public void openAdditionalWorkList(int pos) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, AdditionalWorkScreen.class);
        intent.putExtra(AppConstant.WORK_ID,String.valueOf(WorkListValuesFiltered.get(pos).getWorkId()));
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public int getItemCount() {
        return WorkListValuesFiltered == null ? 0 : WorkListValuesFiltered.size();
    }

    public void viewOfflineImages(String work_id,String type_of_work,String OnOffType,String dcode,String bcode,String pvcode) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(AppConstant.WORK_ID,work_id);
        intent.putExtra("OnOffType",OnOffType);

        if(OnOffType.equalsIgnoreCase("Offline")){
            intent.putExtra(AppConstant.TYPE_OF_WORK,type_of_work);
            activity.startActivity(intent);
        }
        else if(OnOffType.equalsIgnoreCase("Online")) {
            if(Utils.isOnline()){
                intent.putExtra(AppConstant.DISTRICT_CODE,dcode);
                intent.putExtra(AppConstant.BLOCK_CODE,bcode);
                intent.putExtra(AppConstant.PV_CODE,pvcode);
                intent.putExtra(AppConstant.TYPE_OF_WORK,type_of_work);
                activity.startActivity(intent);
            }else {
                Utils.showAlert(activity,"Your Internet seems to be Offline.Images can be viewed only in Online mode.");
            }
        }


        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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

}

