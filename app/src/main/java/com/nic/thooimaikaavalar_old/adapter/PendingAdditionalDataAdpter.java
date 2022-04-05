package com.nic.thooimaikaavalar_old.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nic.thooimaikaavalar_old.BuildConfig;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.PendingScreen;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.PendingAdditionalDataAdapterBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PendingAdditionalDataAdpter extends RecyclerView.Adapter<PendingAdditionalDataAdpter.MyViewHolder> implements Filterable {
    private List<RealTimeMonitoringSystem> pendingListValues;
    private List<RealTimeMonitoringSystem> pendingListFiltered;

    private String letter;
    private Context context;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private dbData  dbData;
    private PrefManager prefManager;
    JSONObject dataSetStructure = new JSONObject();
    private LayoutInflater layoutInflater;
    public PendingAdditionalDataAdpter(Context context, List<RealTimeMonitoringSystem> pendingListValues) {

        this.context = context;
        prefManager = new PrefManager(context);
        dbData = new dbData(context);
        this.pendingListValues = pendingListValues;
        this.pendingListFiltered = pendingListValues;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        PendingAdditionalDataAdapterBinding pendingAdditionalDataAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.pending_additional_data_adapter, viewGroup, false);
        return new PendingAdditionalDataAdpter.MyViewHolder(pendingAdditionalDataAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        dbData.open();
        holder.pendingAdditionalDataAdapterBinding.workId.setText(String.valueOf(pendingListFiltered.get(position).getWorkId()));

        if((BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"))){
            if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_CHECK_DAM_PRODUCTION)){
                viewVisibilityFirst(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_PRODUCTION)){
                viewVisibilityTwo(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_PRODUCTION)){
                viewVisibilityThree(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_PRODUCTION)){
                viewVisibilityFour(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_AFFORESTATION_PRODUCTION)){
                viewVisibilityFive(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Farm_ponds_PRODUCTION)){
                viewVisibilitySix(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Rooftop_RWHS_PRODUCTION)){
                viewVisibilitySeven(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Recharge_shaft_PRODUCTION)){
                viewVisibilityEight(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Soakpit_PRODUCTION)){
                viewVisibilityNine(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_PRODUCTION)){
                viewVisibilityTen(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Recharge_Pit_PRODUCTION)){
                viewVisibilityEight(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_PERCOLATION_POND)){
                viewVisibilityFour(holder,position);
            }
        }
        else {
            if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_CHECK_DAM_LOCAL)){
                viewVisibilityFirst(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL)){
                viewVisibilityTwo(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL)){
                viewVisibilityThree(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_LOCAL)){
                viewVisibilityFour(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_AFFORESTATION_LOCAL)){
                viewVisibilityFive(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Farm_ponds_LOCAL)){
                viewVisibilitySix(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Rooftop_RWHS_LOCAL)){
                viewVisibilitySeven(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Recharge_shaft_LOCAL)){
                viewVisibilityEight(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Soakpit_LOCAL)){
                viewVisibilityNine(holder,position);
            }
            else if(pendingListFiltered.get(position).getWorkTypeID().equals(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_LOCAL)){
                viewVisibilityTen(holder,position);
            }

        }

        holder.pendingAdditionalDataAdapterBinding.syncTrackInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline()) {
                    prefManager.setDeleteWorkId(String.valueOf(pendingListFiltered.get(position).getWorkId()));
                    prefManager.setDeleteAdapterPosition(position);
                    new toUploadAdditionalTask().execute(pendingListFiltered.get(position).getWorkGroupID(),
                            String.valueOf(pendingListFiltered.get(position).getWorkId()),AppConstant.MAIN_WORK,pendingListFiltered.get(position).getWorkTypeID());
                } else {
                    Activity activity = (Activity) context;
                    Utils.showAlert(activity, "Turn On Mobile Data To Synchronize!");
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return pendingListFiltered == null ? 0 : pendingListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    pendingListFiltered = pendingListValues;
                } else {
                    List<RealTimeMonitoringSystem> filteredList = new ArrayList<>();
                    for (RealTimeMonitoringSystem row : pendingListValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPvName().toLowerCase().contains(charString.toLowerCase()) || row.getPvName().toLowerCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }

                    pendingListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pendingListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pendingListFiltered = (ArrayList<RealTimeMonitoringSystem>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private PendingAdditionalDataAdapterBinding pendingAdditionalDataAdapterBinding;

        public MyViewHolder(PendingAdditionalDataAdapterBinding Binding) {
            super(Binding.getRoot());
            pendingAdditionalDataAdapterBinding = Binding;
        }
    }


    public void removeSavedItem(int position) {
        pendingListFiltered.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, pendingListValues.size());
    }
    public void viewVisibilityFirst( MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);

        String length_text = "Length of weir (in m) - " +" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getLength()+"</b>"+"</font>";
        String height_text = "Height of weir (in m) - " +" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getHeight()+"</b>"+"</font>";
        String length_text_1 = "Length of water storage in upstream side of channel (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getLength_water_storage()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - " +" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";

        holder.pendingAdditionalDataAdapterBinding.length.setText(Html.fromHtml(length_text));
        holder.pendingAdditionalDataAdapterBinding.height.setText(Html.fromHtml(height_text));
        holder.pendingAdditionalDataAdapterBinding.lengthOfWaterStorage.setText(Html.fromHtml(length_text_1));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM1.setText(Html.fromHtml(observation));

    }
    public void viewVisibilityTwo( MyViewHolder holder,int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);


        String noOfTrenchesPerOneWorkSite = "No of trenches  per one work site - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getNo_trenches_per_worksite()+"</b>"+"</font>";
        String lengthInM = "Length (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getLength()+"</b>"+"</font>";
        String widthInM = "Width (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getWidth()+"</b>"+"</font>";
        String depthInM = "Depth (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getDepth()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";

        holder.pendingAdditionalDataAdapterBinding.noOfTrenchesPerOneWorkSite.setText(Html.fromHtml(noOfTrenchesPerOneWorkSite));
        holder.pendingAdditionalDataAdapterBinding.lengthInM.setText(Html.fromHtml(lengthInM));
        holder.pendingAdditionalDataAdapterBinding.widthInM.setText(Html.fromHtml(widthInM));
        holder.pendingAdditionalDataAdapterBinding.depthInM.setText(Html.fromHtml(depthInM));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM2.setText(Html.fromHtml(observation));

    }
    public void viewVisibilityThree( MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);

        String noOfUnitsPerWaterbody = "No of units per Water body -" +" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getNo_units_per_waterbody()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - " +" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";


        holder.pendingAdditionalDataAdapterBinding.noOfUnitsPerWaterbody.setText(Html.fromHtml(noOfUnitsPerWaterbody));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM3.setText(Html.fromHtml(observation));

    }
    public void viewVisibilityFour( MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);

        String noOfUnits4 = "No of units - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getNo_units()+"</b>"+"</font>";
        String lengthInM = "Length (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getLength()+"</b>"+"</font>";
        String widthInM = "Width (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getWidth()+"</b>"+"</font>";
        String depthInM = "Depth (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getDepth()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";



        holder.pendingAdditionalDataAdapterBinding.noOfUnits4.setText(Html.fromHtml(noOfUnits4));
        holder.pendingAdditionalDataAdapterBinding.lengthInM4.setText(Html.fromHtml(lengthInM));
        holder.pendingAdditionalDataAdapterBinding.widthInM4.setText(Html.fromHtml(widthInM));
        holder.pendingAdditionalDataAdapterBinding.depthInM4.setText(Html.fromHtml(depthInM));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM4.setText(Html.fromHtml(observation));

    }
    public void viewVisibilityFive(MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);


        String areaCoveredInHect5 = "Area covered (in Hect.) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getArea_covered()+"</b>"+"</font>";
        String noOfPlantationPerSite5 = "No of plantation per site - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getNo_plantation_per_site()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";

        holder.pendingAdditionalDataAdapterBinding.areaCoveredInHect5.setText(Html.fromHtml(areaCoveredInHect5));
        holder.pendingAdditionalDataAdapterBinding.noOfPlantationPerSite5.setText(Html.fromHtml(noOfPlantationPerSite5));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM5.setText(Html.fromHtml(observation));

    }
    public void viewVisibilitySix(MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);

        String noOfUnits4 = "No of units - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getNo_units()+"</b>"+"</font>";
        String lengthInM = "Length (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getLength()+"</b>"+"</font>";
        String widthInM = "Width (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getWidth()+"</b>"+"</font>";
        String depthInM = "Depth (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getDepth()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";


        holder.pendingAdditionalDataAdapterBinding.noOfUnits6.setText(Html.fromHtml(noOfUnits4));
        holder.pendingAdditionalDataAdapterBinding.lengthInM6.setText(Html.fromHtml(lengthInM));
        holder.pendingAdditionalDataAdapterBinding.widthInM6.setText(Html.fromHtml(widthInM));
        holder.pendingAdditionalDataAdapterBinding.depthInM6.setText(Html.fromHtml(depthInM));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM6.setText(Html.fromHtml(observation));


    }
    public void viewVisibilitySeven(MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);

        String storageStructureSumpAvailableYes = "If storage structure/sump available - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getStructure_sump_available_status()+"</b>"+"</font>";
        String lengthDiameterInM = "Length/diameter  (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getLength()+"</b>"+"</font>";
        String widthDiameterInM = "Width/diameter (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getWidth()+"</b>"+"</font>";
        String depthInM = "Depth (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getDepth()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";


        holder.pendingAdditionalDataAdapterBinding.storageStructureSumpAvailableYes.setText(Html.fromHtml(storageStructureSumpAvailableYes));
        holder.pendingAdditionalDataAdapterBinding.lengthDiameterInM.setText(Html.fromHtml(lengthDiameterInM));
        holder.pendingAdditionalDataAdapterBinding.widthDiameterInM.setText(Html.fromHtml(widthDiameterInM));
        holder.pendingAdditionalDataAdapterBinding.depthInM7.setText(Html.fromHtml(depthInM));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM7.setText(Html.fromHtml(observation));


    }
    public void viewVisibilityEight(MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);

        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM8.setText(Html.fromHtml(observation));

    }
    public void viewVisibilityNine(MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.VISIBLE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.GONE);

        String individualCommunity = "Individual/Community - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getIndividual_community()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";

        holder.pendingAdditionalDataAdapterBinding.individualCommunity.setText(Html.fromHtml(individualCommunity));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM9.setText(Html.fromHtml(observation));

    }
    public void viewVisibilityTen(MyViewHolder holder, int position){
        holder.pendingAdditionalDataAdapterBinding.id1.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id2.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id3.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id4.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id5.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id6.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id7.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id8.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id9.setVisibility(View.GONE);
        holder.pendingAdditionalDataAdapterBinding.id10.setVisibility(View.VISIBLE);

        String lenthOfChannelInM10 = "Length of channel (in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListFiltered.get(position).getLength_channel()+"</b>"+"</font>";
        String observation = "Observation (premonsoon GW depth in m) - "+" <font color='#EE0000'>"+"<b>"+pendingListValues.get(position).getObservation()+"</b>"+"</font>";


        holder.pendingAdditionalDataAdapterBinding.lenthOfChannelInM10.setText(Html.fromHtml(lenthOfChannelInM10));
        holder.pendingAdditionalDataAdapterBinding.observationPremonsoonGwDepthInM10.setText(Html.fromHtml(observation));

    }


    public class toUploadAdditionalTask extends AsyncTask<String, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            //dbData.open();

            String workGroupID=String.valueOf(params[0]);
            String workID=String.valueOf(params[1]);
            String type_of_work=String.valueOf(params[2]);
            String work_type_id=String.valueOf(params[3]);
            prefManager.setTypeOfWork(AppConstant.MAIN_WORK);
            JSONArray save_additional_data = new JSONArray();
            ArrayList<RealTimeMonitoringSystem> additionalData = dbData.selectAdditionalData(workID,type_of_work,"");
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(AppConstant.WORK_ID, workID);
                jsonObject.put(AppConstant.WORK_GROUP_ID, workGroupID);
                jsonObject.put(AppConstant.WORK_TYPE_ID, work_type_id);
//                jsonObject.put(AppConstant.TYPE_OF_WORK, AppConstant.MAIN_WORK);

            }catch (JSONException e){

            }
//            ArrayList<MITank> tanks = dbData.getAllCenterImageData(prefManager.getDistrictCode(),prefManager.getBlockCode());

            if (additionalData.size() > 0) {
                for (int i = 0; i < additionalData.size(); i++) {

                    try {

                        if((BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"))) {
                            if (work_type_id.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_PRODUCTION)) {
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("length_of_water_storage_in_upstream_side_of_channel_in_m", additionalData.get(i).getLength_water_storage());
                                jsonObject.put("height_of_weir_in_m", additionalData.get(i).getHeight());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_PRODUCTION)) {
                                jsonObject.put("no_of_trenches_per_one_work_site", additionalData.get(i).getNo_trenches_per_worksite());
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put("depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_PRODUCTION)) {
                                jsonObject.put("no_of_units_per_waterbody", additionalData.get(i).getNo_units_per_waterbody());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_PRODUCTION)) {
                                jsonObject.put("no_of_units", additionalData.get(i).getNo_units());
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put("depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_PRODUCTION)) {
                                jsonObject.put("area_covered_in_hect", additionalData.get(i).getArea_covered());
                                jsonObject.put("no_of_plantation_per_site", additionalData.get(i).getNo_plantation_per_site());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_PRODUCTION)) {
                                jsonObject.put("no_of_units", additionalData.get(i).getNo_units());
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put("depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_PRODUCTION)) {
                                jsonObject.put("is_storage_structure_or_sump_available", additionalData.get(i).getStructure_sump_available_status());
                                jsonObject.put(" length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put(" depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_PRODUCTION)) {
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Soakpit_PRODUCTION)) {
                                jsonObject.put("soakpit_type", additionalData.get(i).getIndividual_community());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_PRODUCTION)) {
                                jsonObject.put(" length_or_diameter_in_m", additionalData.get(i).getLength_channel());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Recharge_Pit_PRODUCTION)) {
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_PERCOLATION_POND)) {
                                jsonObject.put("no_of_units", additionalData.get(i).getNo_units());
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put("depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }

                        }
                        else {
                            if (work_type_id.equalsIgnoreCase(AppConstant.KEY_CHECK_DAM_LOCAL)) {
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("length_of_water_storage_in_upstream_side_of_channel_in_m", additionalData.get(i).getLength_water_storage());
                                jsonObject.put("height_of_weir_in_m", additionalData.get(i).getHeight());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_TRENCH_CUTTING_IN_BARREN_FALLOW_LANDS_LOCAL)) {
                                jsonObject.put("no_of_trenches_per_one_work_site", additionalData.get(i).getNo_trenches_per_worksite());
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put("depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Injection_well_recharge_shaft_in_water_bodies_LOCAL)) {
                                jsonObject.put("no_of_units_per_waterbody", additionalData.get(i).getNo_units_per_waterbody());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Recharge_wells_in_Rivers_Streams_LOCAL)) {
                                jsonObject.put("no_of_units", additionalData.get(i).getNo_units());
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put("depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_AFFORESTATION_LOCAL)) {
                                jsonObject.put("area_covered_in_hect", additionalData.get(i).getArea_covered());
                                jsonObject.put("no_of_plantation_per_site", additionalData.get(i).getNo_plantation_per_site());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Farm_ponds_LOCAL)) {
                                jsonObject.put("no_of_units", additionalData.get(i).getNo_units());
                                jsonObject.put("length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put("depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Rooftop_RWHS_LOCAL)) {
                                jsonObject.put("is_storage_structure_or_sump_available", additionalData.get(i).getStructure_sump_available_status());
                                jsonObject.put(" length_or_diameter_in_m", additionalData.get(i).getLength());
                                jsonObject.put("width_or_diameter_in_m", additionalData.get(i).getWidth());
                                jsonObject.put(" depth_in_m", additionalData.get(i).getDepth());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Recharge_shaft_LOCAL)) {
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Soakpit_LOCAL)) {
                                jsonObject.put("soakpit_type", additionalData.get(i).getIndividual_community());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                            else if (work_type_id.equalsIgnoreCase(AppConstant.KEY_Sunken_pond_and_desilting_of_channel_LOCAL)) {
                                jsonObject.put(" length_or_diameter_in_m", additionalData.get(i).getLength_channel());
                                jsonObject.put(AppConstant.KEY_OBSERVATION_SERVICE, additionalData.get(i).getObservation());
                                jsonObject.put(AppConstant.KEY_LATITUDE, additionalData.get(i).getLatitude());
                                jsonObject.put(AppConstant.KEY_LONGITUDE, additionalData.get(i).getLongitude());
                                jsonObject.put(AppConstant.DISTRICT_CODE, additionalData.get(i).getDistictCode());
                                jsonObject.put(AppConstant.BLOCK_CODE, additionalData.get(i).getBlockCode());
                                jsonObject.put(AppConstant.PV_CODE, additionalData.get(i).getPvCode());

                            }
                        }



                        save_additional_data.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dataSetStructure = new JSONObject();

                try {
                    dataSetStructure.put(AppConstant.KEY_SERVICE_ID,"work_additional_details_save");
                    dataSetStructure.put(AppConstant.KEY_ADDITIONAL_DETAILS,save_additional_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return dataSetStructure;
        }

        @Override
        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            ((PendingScreen)context).syncAdditionalDataList(dataset);
        }
    }

}
