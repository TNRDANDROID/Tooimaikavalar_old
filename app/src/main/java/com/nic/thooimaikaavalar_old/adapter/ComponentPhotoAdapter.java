package com.nic.thooimaikaavalar_old.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.nic.thooimaikaavalar_old.activity.FullImageActivity;
import com.nic.thooimaikaavalar_old.activity.NewPendingScreenActivity;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.BasicDetailsAdapterFromServerBinding;
import com.nic.thooimaikaavalar_old.databinding.ComponentPhotoAdapterBinding;
import com.nic.thooimaikaavalar_old.databinding.ComponentsAdapterBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ComponentPhotoAdapter extends RecyclerView.Adapter<ComponentPhotoAdapter.MyViewHolder>{
    private LayoutInflater layoutInflater;
    private List<RealTimeMonitoringSystem> basicDetailsList;
    static JSONObject dataset=new JSONObject();
    Context context;
    int pos=-1;
    private dbData dbData;

    public ComponentPhotoAdapter(List<RealTimeMonitoringSystem> capacityList,Context context,dbData dbData){
        this.basicDetailsList=capacityList;
        this.context=context;
        this.dbData=dbData;
    }

    @NonNull
    @Override
    public ComponentPhotoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(viewGroup.getContext());
        }
        ComponentPhotoAdapterBinding basicDetailsAdapterBinding=
                DataBindingUtil.inflate(layoutInflater, R.layout.component_photo_adapter,viewGroup,false);
        return new MyViewHolder(basicDetailsAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ComponentPhotoAdapter.MyViewHolder holder,final int position){
        holder.basicDetailsAdapterBinding.date.setText("Date of Commencement -"+basicDetailsList.get(position).getDate_of_commencement());
        holder.basicDetailsAdapterBinding.mccName.setText("MCC Name -"+basicDetailsList.get(position).getMcc_name());
        //holder.basicDetailsAdapterBinding.villageName.setText("Village Name -"+basicDetailsList.get(position).getPvName());
        //holder.basicDetailsAdapterBinding.waterAvailabilityName.setText("Water Available Name -"+basicDetailsList.get(position).getWater_supply_availability_name());
        //holder.basicDetailsAdapterBinding.mccCenterImage.setImageBitmap(stringtoBitmap(basicDetailsList.get(position).getCenter_image()));

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

        holder.basicDetailsAdapterBinding.mccCenterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("OnOffType","Offline");
                intent.putExtra("mcc_id",basicDetailsList.get(position).getMcc_id());
                intent.putExtra("work_id","");
                intent.putExtra("component_id","");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount(){
        return basicDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ComponentPhotoAdapterBinding basicDetailsAdapterBinding;

        public MyViewHolder(ComponentPhotoAdapterBinding Binding) {
            super(Binding.getRoot());
            basicDetailsAdapterBinding = Binding;
        }
    }
    public void deletePending(int position) {
        String key_id = basicDetailsList.get(position).getMcc_id();

        int sdsm = NewPendingScreenActivity.db.delete(DBHelper.COMPOST_TUB_IMAGE_TABLE, "mcc_id = ? ", new String[]{key_id});
        basicDetailsList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, basicDetailsList.size());
        Log.d("sdsm", String.valueOf(sdsm));
    }

    public void uploadPending(int position) {
        dbData.open();
        JSONArray thooimai_kaavalar_details = new JSONArray();
        dataset = new JSONObject();
        ArrayList<RealTimeMonitoringSystem> photos_details = new ArrayList<>();
        photos_details.addAll(dbData.getParticularMCCIDImage(basicDetailsList.get(position).getMcc_id()));
        for(int i=0; i<photos_details.size();i++){
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("component_id",photos_details.get(i).getComponent_id());
                jsonObject.put("image",BitMapToString(photos_details.get(i).getImage()));
                jsonObject.put("latitude",photos_details.get(i).getLatitude());
                jsonObject.put("longitude",photos_details.get(i).getLongitude());
                jsonObject.put("photograph_remark",photos_details.get(i).getPhotograph_remark());
                thooimai_kaavalar_details.put(jsonObject);
            }
            catch (JSONException e){
                e.printStackTrace();
            }


        }




        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_mcc_photographs_save");
            jsonObject.put("mcc_id",basicDetailsList.get(position).getMcc_id());
            jsonObject.put("component_details",thooimai_kaavalar_details);

            ((NewPendingScreenActivity)context).SyncData(jsonObject,basicDetailsList.get(position).getMcc_id(),"photos");

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


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
