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
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.BasicDetailsAdapterBinding;
import com.nic.thooimaikaavalar_old.databinding.ThooimaiAdapterFromLocalBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ThooimaiKaavarListLocalAdapter extends RecyclerView.Adapter<ThooimaiKaavarListLocalAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<RealTimeMonitoringSystem> basicDetailsList;
    static JSONObject dataset = new JSONObject();
    Context context;
    private dbData dbData;
    int pos=-1;

    public ThooimaiKaavarListLocalAdapter(List<RealTimeMonitoringSystem> capacityList, Context context,dbData dbData) {
        this.basicDetailsList = capacityList;
        this.context = context;
        this.dbData = dbData;
    }

    @NonNull
    @Override
    public ThooimaiKaavarListLocalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ThooimaiAdapterFromLocalBinding basicDetailsAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.thooimai_adapter_from_local, viewGroup, false);
        return new MyViewHolder(basicDetailsAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ThooimaiKaavarListLocalAdapter.MyViewHolder holder, final int position) {
        /*holder.basicDetailsAdapterBinding.dateOfEngage.setText(basicDetailsList.get(position).getDate_of_engagement());
        holder.basicDetailsAdapterBinding.dateOfTraining.setText(basicDetailsList.get(position).getDate_of_training_given());
        holder.basicDetailsAdapterBinding.name.setText(basicDetailsList.get(position).getName_of_the_thooimai_kaavalars());
        holder.basicDetailsAdapterBinding.mobileNumber.setText(basicDetailsList.get(position).getMobile_no());*/
        holder.basicDetailsAdapterBinding.name.setText(basicDetailsList.get(position).getMcc_name());
        holder.basicDetailsAdapterBinding.mobileNumber.setText(basicDetailsList.get(position).getMcc_id());
        holder.basicDetailsAdapterBinding.dateOfTraining.setText(basicDetailsList.get(position).getDate_of_commencement());
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
        private ThooimaiAdapterFromLocalBinding basicDetailsAdapterBinding;

        public MyViewHolder(ThooimaiAdapterFromLocalBinding Binding) {
            super(Binding.getRoot());
            basicDetailsAdapterBinding = Binding;
        }
    }

    public Bitmap stringToBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public void uploadPending(int position) {
        dbData.open();
        JSONArray thooimai_kaavalar_details = new JSONArray();
        dataset = new JSONObject();
        ArrayList<RealTimeMonitoringSystem> thooimai_kaavalar_detailsList = new ArrayList<>();
        thooimai_kaavalar_detailsList.addAll(dbData.getAllThooimaikaavalarListLocal(basicDetailsList.get(position).getMcc_id()));
        for(int i=0; i<thooimai_kaavalar_detailsList.size();i++){
            JSONObject jsonObject = new JSONObject();

            try {
                if(thooimai_kaavalar_detailsList.get(i).getThooimai_kaavalar_id().equals("")){

                }
                else {
                    jsonObject.put("thooimai_kaavalar_id",thooimai_kaavalar_detailsList.get(i).getThooimai_kaavalar_id());
                }
                jsonObject.put("name_of_the_thooimai_kaavalars",thooimai_kaavalar_detailsList.get(i).getName_of_the_thooimai_kaavalars());
                jsonObject.put("mobile_no",thooimai_kaavalar_detailsList.get(i).getMobile_no());
                jsonObject.put("date_of_engagement",thooimai_kaavalar_detailsList.get(i).getDate_of_engagement());
                jsonObject.put("date_of_training_given",thooimai_kaavalar_detailsList.get(i).getDate_of_training_given());
                thooimai_kaavalar_details.put(jsonObject);
            }
            catch (JSONException e){
                e.printStackTrace();
            }


        }




        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_mcc_thooimai_kaavalar_save");
            jsonObject.put("mcc_id",basicDetailsList.get(position).getMcc_id());
            jsonObject.put("thooimai_kaavalar_details",thooimai_kaavalar_details);

            ((NewPendingScreenActivity)context).SyncData(jsonObject,basicDetailsList.get(position).getMcc_id(),"thooimai");

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void deletePending(int position) {
        String key_id = basicDetailsList.get(position).getKEY_ID();

        int sdsm = NewPendingScreenActivity.db.delete(DBHelper.THOOIMAI_KAAVALARS_DETAIL_OF_MCC_SAVE_LOCAL, "id = ? ", new String[]{key_id});
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


}


