package com.nic.thooimaikaavalar_old.adapter;

import android.app.Dialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
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
import com.nic.thooimaikaavalar_old.activity.ViewWasteCollectedDetails;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.databinding.WasteCollectedAdapterItemBinding;
import com.nic.thooimaikaavalar_old.databinding.WasteCollectedRecyclerItemServerBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class WasteCollectedAdapterServer extends  RecyclerView.Adapter<WasteCollectedAdapterServer.MyViewHolder>{
    private LayoutInflater layoutInflater;
    private List<RealTimeMonitoringSystem> basicDetailsList;
    static JSONObject dataset=new JSONObject();
    Context context;
    int pos=-1;
    private com.nic.thooimaikaavalar_old.dataBase.dbData dbData;

    public WasteCollectedAdapterServer(List<RealTimeMonitoringSystem> capacityList, Context context, com.nic.thooimaikaavalar_old.dataBase.dbData dbData){
            this.basicDetailsList=capacityList;
            this.context=context;
            this.dbData=dbData;
        }

    @NonNull
    @Override
    public WasteCollectedAdapterServer.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
            if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(viewGroup.getContext());
            }
            WasteCollectedRecyclerItemServerBinding wasteCollectedRecyclerItemServerBinding=
            DataBindingUtil.inflate(layoutInflater, R.layout.waste_collected_recycler_item_server,viewGroup,false);
            return new MyViewHolder(wasteCollectedRecyclerItemServerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final WasteCollectedAdapterServer.MyViewHolder holder,final int position) {

        holder.wasteCollectedRecyclerItemServerBinding.mccId.setText(basicDetailsList.get(position).getMcc_id());
        holder.wasteCollectedRecyclerItemServerBinding.mccName.setText(basicDetailsList.get(position).getMcc_name());
        holder.wasteCollectedRecyclerItemServerBinding.villageName.setText(basicDetailsList.get(position).getPvName());
        if(basicDetailsList.get(position).getHouseholds_waste()!=null&&!basicDetailsList.get(position).getHouseholds_waste().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.householdsWasteLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.householdsWaste.setText(basicDetailsList.get(position).getHouseholds_waste()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.householdsWasteLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.householdsWaste.setText("");
        }
        if(basicDetailsList.get(position).getShops_waste()!=null&&!basicDetailsList.get(position).getShops_waste().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.shopsWasteLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.shopsWaste.setText(basicDetailsList.get(position).getShops_waste()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.shopsWasteLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.shopsWaste.setText("");
        }
        if(basicDetailsList.get(position).getMarket_waste()!=null&&!basicDetailsList.get(position).getMarket_waste().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.marketWasteLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.marketWaste.setText(basicDetailsList.get(position).getMarket_waste()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.marketWasteLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.marketWaste.setText("");
        }
        if(basicDetailsList.get(position).getHotels_waste()!=null&&!basicDetailsList.get(position).getHotels_waste().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.hotelsWasteLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.hotelsWaste.setText(basicDetailsList.get(position).getHotels_waste()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.hotelsWasteLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.hotelsWaste.setText("");
        }
        if(basicDetailsList.get(position).getOthers_waste()!=null&&!basicDetailsList.get(position).getOthers_waste().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.othersWasteLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.othersWaste.setText(basicDetailsList.get(position).getOthers_waste()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.othersWasteLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.othersWaste.setText("");
        }
        if(basicDetailsList.get(position).getTot_bio_waste_collected()!=null&&!basicDetailsList.get(position).getTot_bio_waste_collected().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteCollectedLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteCollected.setText(basicDetailsList.get(position).getTot_bio_waste_collected()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteCollectedLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteCollected.setText("");
        }
        if(basicDetailsList.get(position).getTot_bio_waste_shredded()!=null&&!basicDetailsList.get(position).getTot_bio_waste_shredded().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteShreddedLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteShredded.setText(basicDetailsList.get(position).getTot_bio_waste_shredded()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteShreddedLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioWasteShredded.setText("");
        }
        if(basicDetailsList.get(position).getTot_bio_compost_produced()!=null&&!basicDetailsList.get(position).getTot_bio_compost_produced().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostProducedLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostProduced.setText(basicDetailsList.get(position).getTot_bio_compost_produced()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostProducedLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostProduced.setText("");
        }
        if(basicDetailsList.get(position).getTot_bio_compost_sold()!=null&&!basicDetailsList.get(position).getTot_bio_compost_sold().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostSoldLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostSold.setText(basicDetailsList.get(position).getTot_bio_compost_sold()+" Kg");
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostSoldLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.totBioCompostSold.setText("");
        }

         if(basicDetailsList.get(position).getDate_of_save()!=null&&!basicDetailsList.get(position).getDate_of_save().equals("")){
            holder.wasteCollectedRecyclerItemServerBinding.dateLayout.setVisibility(View.VISIBLE);
            holder.wasteCollectedRecyclerItemServerBinding.date.setText(dateFormate(basicDetailsList.get(position).getDate_of_save(),"Yes"));
        }
        else {
            holder.wasteCollectedRecyclerItemServerBinding.dateLayout.setVisibility(View.GONE);
            holder.wasteCollectedRecyclerItemServerBinding.date.setText("");
        }

        holder.wasteCollectedRecyclerItemServerBinding.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"delete");
            }
        });

    }



    @Override
    public int getItemCount(){
            return basicDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private WasteCollectedRecyclerItemServerBinding wasteCollectedRecyclerItemServerBinding;

        public MyViewHolder(WasteCollectedRecyclerItemServerBinding Binding) {
            super(Binding.getRoot());
            wasteCollectedRecyclerItemServerBinding = Binding;
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
                text.setText("Do You Want to Upload?");
            }
            else if(save_delete.equals("delete")){
                text.setText("Do You Want to Delete?");
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
        dataset = new JSONObject();
        String pvcode = basicDetailsList.get(position).getPvCode();
        String mcc_id= basicDetailsList.get(position).getMcc_id();
        String date = basicDetailsList.get(position).getDate_of_save();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_swachh_bharat_delete");
            if(basicDetailsList.get(position).getMcc_id().equals("")){

            }
            else {
                jsonObject.put("mcc_id",basicDetailsList.get(position).getMcc_id());
            }
            jsonObject.put("pvcode",pvcode);
            jsonObject.put("mcc_id",mcc_id);
            jsonObject.put("date",date);


            ((ViewWasteCollectedDetails)context).DeleteData(jsonObject);

        }
        catch (JSONException e){
            e.printStackTrace();
        }

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


}


