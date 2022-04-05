package com.nic.thooimaikaavalar_old.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.CameraScreen;
import com.nic.thooimaikaavalar_old.activity.FullImageActivity;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.AdapterAdditionalListBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AdditionalListAdapter extends PagedListAdapter<RealTimeMonitoringSystem, AdditionalListAdapter.MyViewHolder> implements Filterable {
    private List<RealTimeMonitoringSystem> AdditionalListValues;
    private List<RealTimeMonitoringSystem> AdditionalListValuesFiltered;
    private String letter;
    private Context context;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private final dbData dbData;
    PrefManager prefManager;
    public final String dcode,bcode,pvcode;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;    private LayoutInflater layoutInflater;
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
    public AdditionalListAdapter(Context context, List<RealTimeMonitoringSystem> WorkListValues,dbData dbData) {
        super(DIFF_CALLBACK);
        this.context = context;
        prefManager = new PrefManager(context);
        this.AdditionalListValues = WorkListValues;
        this.AdditionalListValuesFiltered = WorkListValues;
        this.dbData = dbData;
        dcode = prefManager.getDistrictCode();
        bcode = prefManager.getBlockCode();
        pvcode = prefManager.getPvCode();
        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AdapterAdditionalListBinding adapterAdditionalListBinding;

        public MyViewHolder(AdapterAdditionalListBinding Binding) {
            super(Binding.getRoot());
            adapterAdditionalListBinding = Binding;
        }
    }

    @Override
    public AdditionalListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        AdapterAdditionalListBinding adapterAdditionalListBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_additional_list, viewGroup, false);
        return new AdditionalListAdapter.MyViewHolder(adapterAdditionalListBinding);
    }

    @Override
    public void onBindViewHolder(AdditionalListAdapter.MyViewHolder holder, final int position) {

       // holder.adapterAdditionalListBinding.workid.setText(String.valueOf(AdditionalListValuesFiltered.get(position).getWorkId()));
        if(AdditionalListValuesFiltered.get(position).getCdName().isEmpty()){
            holder.adapterAdditionalListBinding.tvCdWorkNameLayout.setVisibility(View.GONE);
        }else {
            holder.adapterAdditionalListBinding.tvCdWorkNameLayout.setVisibility(View.VISIBLE);
            holder.adapterAdditionalListBinding.tvCdWorkName.setText(AdditionalListValuesFiltered.get(position).getCdName());
        }

        if(AdditionalListValuesFiltered.get(position).getChainageMeter().isEmpty()){
            holder.adapterAdditionalListBinding.tvChainageLayout.setVisibility(View.GONE);
        }else {
            holder.adapterAdditionalListBinding.tvChainageLayout.setVisibility(View.VISIBLE);
            holder.adapterAdditionalListBinding.tvChainage.setText(AdditionalListValuesFiltered.get(position).getChainageMeter());
        }

        if(AdditionalListValuesFiltered.get(position).getWorkStageName().isEmpty()){
            holder.adapterAdditionalListBinding.tvWorkstageLayout.setVisibility(View.GONE);
        }else {
            holder.adapterAdditionalListBinding.tvWorkstageLayout.setVisibility(View.VISIBLE);
            holder.adapterAdditionalListBinding.tvWorkstage.setText(AdditionalListValuesFiltered.get(position).getWorkStageName());
        }

        String workTypeID = String.valueOf(AdditionalListValuesFiltered.get(position).getCdTypeId());
        String currentStageCode = String.valueOf(AdditionalListValuesFiltered.get(position).getCurrentStage());
        String workTypeFlag = AdditionalListValuesFiltered.get(position).getWorkTypeFlagLe();
        String sql = "select * from "+DBHelper.ADDITIONAL_WORK_STAGE_TABLE+" where work_stage_order>(select work_stage_order from "+DBHelper.ADDITIONAL_WORK_STAGE_TABLE+" where work_stage_code='"+currentStageCode+"' and work_type_code ="+workTypeID+" and cd_type_flag ='"+workTypeFlag+"') and work_type_code ="+workTypeID+" and cd_type_flag ='"+workTypeFlag+"' order by work_stage_order";
        Cursor Stage = db.rawQuery(sql, null);
        Log.d("CdWork",""+sql);

        if(Stage.getCount() > 0 || currentStageCode.equalsIgnoreCase("1")){
            holder.adapterAdditionalListBinding.takePhoto.setVisibility(View.VISIBLE);
        }
        else {
            holder.adapterAdditionalListBinding.takePhoto.setVisibility(View.GONE);
        }

        holder.adapterAdditionalListBinding.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraScreen(position);
            }
        });

        final String work_id = String.valueOf(AdditionalListValuesFiltered.get(position).getWorkId());
        final String cd_work_no = String.valueOf(AdditionalListValuesFiltered.get(position).getCdWorkNo());
        final String work_type_flag_le = String.valueOf(AdditionalListValuesFiltered.get(position).getWorkTypeFlagLe());

        ArrayList<RealTimeMonitoringSystem> imageOffline = dbData.selectImage(dcode,bcode,pvcode,work_id,AppConstant.ADDITIONAL_WORK,cd_work_no,work_type_flag_le);

        if(imageOffline.size() > 0) {
            holder.adapterAdditionalListBinding.viewOfflineImage.setVisibility(View.VISIBLE);
        }
        else {
            holder.adapterAdditionalListBinding.viewOfflineImage.setVisibility(View.GONE);
        }

        holder.adapterAdditionalListBinding.viewOfflineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOfflineImages(work_id,cd_work_no,work_type_flag_le,AppConstant.ADDITIONAL_WORK,"Offline","","","");
            }
        });

        if (AdditionalListValuesFiltered.get(position).getImageAvailable().equalsIgnoreCase("Y")) {
            holder.adapterAdditionalListBinding.viewOnlineImage.setVisibility(View.VISIBLE);
        }
        else if(AdditionalListValuesFiltered.get(position).getImageAvailable().equalsIgnoreCase("N")){
            holder.adapterAdditionalListBinding.viewOnlineImage.setVisibility(View.GONE);
        }
        holder.adapterAdditionalListBinding.viewOnlineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOfflineImages(work_id,cd_work_no,work_type_flag_le,AppConstant.ADDITIONAL_WORK,"Online",dcode,bcode,pvcode);
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    AdditionalListValuesFiltered = AdditionalListValues;
                } else {
                    List<RealTimeMonitoringSystem> filteredList = new ArrayList<>();
                    for (RealTimeMonitoringSystem row : AdditionalListValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getWorkId().toString().toLowerCase().contains(charString.toLowerCase()) || row.getBeneficiaryName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    AdditionalListValuesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = AdditionalListValuesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                AdditionalListValuesFiltered = (ArrayList<RealTimeMonitoringSystem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void openCameraScreen(int pos) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, CameraScreen.class);
        intent.putExtra(AppConstant.TYPE_OF_WORK,AppConstant.ADDITIONAL_WORK);
        intent.putExtra(AppConstant.WORK_GROUP_ID,String.valueOf(AdditionalListValuesFiltered.get(pos).getWorkGroupID()));
        intent.putExtra(AppConstant.CD_WORK_NO,String.valueOf(AdditionalListValuesFiltered.get(pos).getCdWorkNo()));
        intent.putExtra(AppConstant.WORK_ID,String.valueOf(AdditionalListValuesFiltered.get(pos).getWorkId()));
        intent.putExtra(AppConstant.CD_CODE,String.valueOf(AdditionalListValuesFiltered.get(pos).getCdCode()));
        intent.putExtra(AppConstant.CURRENT_STAGE_OF_WORK,String.valueOf(AdditionalListValuesFiltered.get(pos).getCurrentStage()));
        intent.putExtra(AppConstant.CD_TYPE_ID,String.valueOf(AdditionalListValuesFiltered.get(pos).getCdTypeId()));
        intent.putExtra(AppConstant.WORK_TYPE_FLAG_LE,AdditionalListValuesFiltered.get(pos).getWorkTypeFlagLe());
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public int getItemCount() {
        return AdditionalListValuesFiltered == null ? 0 : AdditionalListValuesFiltered.size();
    }

    public void viewOfflineImages(String work_id,String cd_work_no,String work_type_flag_le,String type_of_work,String OnOffType,String dcode,String bcode,String pvcode) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(AppConstant.WORK_ID,work_id);
        intent.putExtra(AppConstant.CD_WORK_NO,cd_work_no);
        intent.putExtra(AppConstant.WORK_TYPE_FLAG_LE,work_type_flag_le);
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
}

