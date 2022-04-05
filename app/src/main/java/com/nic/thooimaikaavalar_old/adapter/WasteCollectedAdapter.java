package com.nic.thooimaikaavalar_old.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.NewPendingScreenActivity;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ComponentPhotoAdapterBinding;
import com.nic.thooimaikaavalar_old.databinding.WasteCollectedAdapterItemBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WasteCollectedAdapter extends RecyclerView.Adapter<WasteCollectedAdapter.MyViewHolder>{
    private LayoutInflater layoutInflater;
    private List<RealTimeMonitoringSystem> basicDetailsList;
    static JSONObject dataset=new JSONObject();
    Context context;
    int pos=-1;
    private com.nic.thooimaikaavalar_old.dataBase.dbData dbData;

    public WasteCollectedAdapter(List<RealTimeMonitoringSystem> capacityList,Context context,dbData dbData){
        this.basicDetailsList=capacityList;
        this.context=context;
        this.dbData=dbData;
    }

    @NonNull
    @Override
    public WasteCollectedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(viewGroup.getContext());
        }
        WasteCollectedAdapterItemBinding basicDetailsAdapterBinding=
                DataBindingUtil.inflate(layoutInflater, R.layout.waste_collected_adapter_item,viewGroup,false);
        return new MyViewHolder(basicDetailsAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final WasteCollectedAdapter.MyViewHolder holder,final int position){
        holder.basicDetailsAdapterBinding.date.setText("Date of Save -"+basicDetailsList.get(position).getDate_of_save());
        holder.basicDetailsAdapterBinding.mccName.setText("MCC Name -"+basicDetailsList.get(position).getMcc_name());
        holder.basicDetailsAdapterBinding.villageName.setText("Village Name -"+basicDetailsList.get(position).getPvName());
        holder.basicDetailsAdapterBinding.mccId.setText("Mcc_id -"+basicDetailsList.get(position).getMcc_id());

        holder.basicDetailsAdapterBinding.deleteIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                save_and_delete_alert(position,"delete");
            }
        });
        holder.basicDetailsAdapterBinding.uploadIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //((ViewAndEditMCCDetaila)context).gotoDashboard(position);
                save_and_delete_alert(position,"save");
            }
        });


    }

    @Override
    public int getItemCount(){
        return basicDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private WasteCollectedAdapterItemBinding basicDetailsAdapterBinding;

        public MyViewHolder(WasteCollectedAdapterItemBinding Binding) {
            super(Binding.getRoot());
            basicDetailsAdapterBinding = Binding;
        }
    }

    public void uploadPending(int position) {
        dbData.open();

        dataset = new JSONObject();
        /*ArrayList<RealTimeMonitoringSystem> photos_details = new ArrayList<>();
        photos_details.addAll(dbData.getParticularWasteCollectedSaveTableRow(basicDetailsList.get(position).getMcc_id(),""));
        for(int i=0; i<photos_details.size();i++){
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("pvcode",photos_details.get(i).getPvCode());
                jsonObject.put("households_waste",photos_details.get(i).getHouseholds_waste());
                jsonObject.put("shops_waste",photos_details.get(i).getShops_waste());
                jsonObject.put("market_waste",photos_details.get(i).getMarket_waste());
                jsonObject.put("hotels_waste",photos_details.get(i).getHotels_waste());
                jsonObject.put("others_waste",photos_details.get(i).getOthers_waste());
                jsonObject.put("tot_bio_waste_collected",photos_details.get(i).getTot_bio_waste_collected());
                jsonObject.put("tot_bio_waste_shredded",photos_details.get(i).getTot_bio_waste_shredded());
                jsonObject.put("tot_bio_compost_produced",photos_details.get(i).getTot_bio_compost_produced());
                jsonObject.put("tot_bio_compost_sold",photos_details.get(i).getTot_bio_compost_sold());
                jsonObject.put("date",photos_details.get(i).getDate_of_save());
                waste_collected_details.put(jsonObject);
            }
            catch (JSONException e){
                e.printStackTrace();
            }


        }

*/


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_swachh_bharat_save");
            jsonObject.put("pvcode",basicDetailsList.get(position).getPvCode());
            jsonObject.put("mcc_id",basicDetailsList.get(position).getMcc_id());
            jsonObject.put("households_waste",basicDetailsList.get(position).getHouseholds_waste());
            jsonObject.put("shops_waste",basicDetailsList.get(position).getShops_waste());
            jsonObject.put("market_waste",basicDetailsList.get(position).getMarket_waste());
            jsonObject.put("hotels_waste",basicDetailsList.get(position).getHotels_waste());
            jsonObject.put("others_waste",basicDetailsList.get(position).getOthers_waste());
            jsonObject.put("tot_bio_waste_collected",basicDetailsList.get(position).getTot_bio_waste_collected());
            jsonObject.put("tot_bio_waste_shredded",basicDetailsList.get(position).getTot_bio_waste_shredded());
            jsonObject.put("tot_bio_compost_produced",basicDetailsList.get(position).getTot_bio_compost_produced());
            jsonObject.put("tot_bio_compost_sold",basicDetailsList.get(position).getTot_bio_compost_sold());
            jsonObject.put("date",basicDetailsList.get(position).getDate_of_save());


            ((NewPendingScreenActivity)context).SyncData(jsonObject,basicDetailsList.get(position).getMcc_id(),"waste_collected");

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }
    public void save_and_delete_alert(int position,String save_delete){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            if(save_delete.equals("save")) {
                text.setText(context.getResources().getString(R.string.do_you_wnat_to_upload));
            }
            else if(save_delete.equals("delete")){
                text.setText(context.getResources().getString(R.string.do_you_wnat_to_delete));
            }

            Button yesButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button noButton = (Button) dialog.findViewById(R.id.btn_cancel);
            noButton.setVisibility(View.VISIBLE);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(save_delete.equals("save")) {
                        uploadPending(position);
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("delete")) {
                        deletePending(position);
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void deletePending(int position) {
        String key_id = basicDetailsList.get(position).getMcc_id();

        int sdsm = NewPendingScreenActivity.db.delete(DBHelper.WASTE_COLLECTED_SAVE_TABLE, "mcc_id = ? ", new String[]{key_id});
        basicDetailsList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, basicDetailsList.size());
        Log.d("sdsm", String.valueOf(sdsm));
    }


}

