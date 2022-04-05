package com.nic.thooimaikaavalar_old.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.nic.thooimaikaavalar_old.databinding.BasicDetailsAdapterBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BasicDetailsAdapter extends RecyclerView.Adapter<BasicDetailsAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<RealTimeMonitoringSystem> basicDetailsList;
    static JSONObject dataset = new JSONObject();
    Context context;
    int pos=-1;

    public BasicDetailsAdapter(List<RealTimeMonitoringSystem> capacityList, Context context) {
        this.basicDetailsList = capacityList;
        this.context = context;
    }

    @NonNull
    @Override
    public BasicDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        BasicDetailsAdapterBinding basicDetailsAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.basic_details_adapter, viewGroup, false);
        return new MyViewHolder(basicDetailsAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final BasicDetailsAdapter.MyViewHolder holder, final int position) {
        holder.basicDetailsAdapterBinding.date.setText(basicDetailsList.get(position).getDate_of_commencement());
        holder.basicDetailsAdapterBinding.mccName.setText(basicDetailsList.get(position).getMcc_name());
        holder.basicDetailsAdapterBinding.mccCenterImage.setImageBitmap(stringtoBitmap(basicDetailsList.get(position).getCenter_image()));

        holder.basicDetailsAdapterBinding.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_and_delete_alert(position,"delete");
            }
        });
        holder.basicDetailsAdapterBinding.uploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_and_delete_alert(position,"save");
            }
        });
    }

    @Override
    public int getItemCount() {
        return basicDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private BasicDetailsAdapterBinding basicDetailsAdapterBinding;

        public MyViewHolder(BasicDetailsAdapterBinding Binding) {
            super(Binding.getRoot());
            basicDetailsAdapterBinding = Binding;
        }
    }

    public Bitmap stringtoBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public void uploadPending(int position) {
        dataset = new JSONObject();
        String pvcode = basicDetailsList.get(position).getPvCode();
        String dcode = basicDetailsList.get(position).getDistictCode();
        String bcode = basicDetailsList.get(position).getBlockCode();
        String mcc_name = basicDetailsList.get(position).getMcc_name();
        String capacity_of_mcc_id = basicDetailsList.get(position).getCapacity_of_mcc_id();
        String water_supply_availability_id = basicDetailsList.get(position).getWater_supply_availability_id();
        String water_supply_availability_other = basicDetailsList.get(position).getWater_supply_availability_other();
        String date_of_commencement = basicDetailsList.get(position).getDate_of_commencement();
        String image = basicDetailsList.get(position).getCenter_image();
        String latitude = basicDetailsList.get(position).getCenter_image_latitude();
        String longitude = basicDetailsList.get(position).getCenter_image_longtitude();



        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_micro_composting_save");
            if(basicDetailsList.get(position).getMcc_id().equals("")){

            }
            else {
                jsonObject.put("mcc_id",basicDetailsList.get(position).getMcc_id());
            }
            jsonObject.put("pvcode",pvcode);
            jsonObject.put("mcc_name",mcc_name);
            jsonObject.put("capacity_of_mcc_id",capacity_of_mcc_id);
            jsonObject.put("water_supply_availability_id",water_supply_availability_id);
            jsonObject.put("water_supply_availability_other",water_supply_availability_other);
            jsonObject.put("date_of_commencement",date_of_commencement);
            jsonObject.put("image",image);
            jsonObject.put("latitude",latitude);
            jsonObject.put("longitude",longitude);

            ((NewPendingScreenActivity)context).SyncData(jsonObject,basicDetailsList.get(position).getKEY_ID(),"basic");

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void deletePending(int position) {
        String key_id = basicDetailsList.get(position).getKEY_ID();

        int sdsm = NewPendingScreenActivity.db.delete(DBHelper.BASIC_DETAILS_OF_MCC_SAVE, "id = ? ", new String[]{key_id});
        basicDetailsList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, basicDetailsList.size());
        Log.d("sdsm", String.valueOf(sdsm));
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


}

