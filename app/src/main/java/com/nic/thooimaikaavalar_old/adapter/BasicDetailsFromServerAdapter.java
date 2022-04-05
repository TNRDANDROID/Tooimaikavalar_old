package com.nic.thooimaikaavalar_old.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.thooimaikaavalar_old.ImageZoom.ImageMatrixTouchHandler;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.ViewAndEditMCCDetaila;
import com.nic.thooimaikaavalar_old.activity.WasteCollectedForm;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.databinding.BasicDetailsAdapterBinding;
import com.nic.thooimaikaavalar_old.databinding.BasicDetailsAdapterFromServerBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class BasicDetailsFromServerAdapter extends RecyclerView.Adapter<BasicDetailsFromServerAdapter.MyViewHolder>{
            private LayoutInflater layoutInflater;
            private List<RealTimeMonitoringSystem> basicDetailsList;
            static JSONObject dataset=new JSONObject();
                    Context context;
                    int pos=-1;

            public BasicDetailsFromServerAdapter(List<RealTimeMonitoringSystem> capacityList,Context context){
                    this.basicDetailsList=capacityList;
                    this.context=context;
                    }

            @NonNull
            @Override
            public BasicDetailsFromServerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
                    if(layoutInflater==null){
                    layoutInflater=LayoutInflater.from(viewGroup.getContext());
                    }
                    BasicDetailsAdapterFromServerBinding basicDetailsAdapterBinding=
                    DataBindingUtil.inflate(layoutInflater, R.layout.basic_details_adapter_from_server,viewGroup,false);
                    return new MyViewHolder(basicDetailsAdapterBinding);
                    }

            @Override
            public void onBindViewHolder(@NonNull final BasicDetailsFromServerAdapter.MyViewHolder holder,final int position){

                /*Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Ubuntu-Medium.ttf");
                holder.basicDetailsAdapterBinding.date.setTypeface(typeface);
                holder.basicDetailsAdapterBinding.mccName.setTypeface(typeface);
                holder.basicDetailsAdapterBinding.villageName.setTypeface(typeface);
                holder.basicDetailsAdapterBinding.waterAvailabilityName.setTypeface(typeface);*/
                    String date = dateFormate(basicDetailsList.get(position).getDate_of_commencement(),"yes");
                    holder.basicDetailsAdapterBinding.date.setText("Date of Commencement -"+date);
                    holder.basicDetailsAdapterBinding.mccName.setText("MCC Name -"+basicDetailsList.get(position).getMcc_name());
                    holder.basicDetailsAdapterBinding.villageName.setText("Village Name -"+basicDetailsList.get(position).getPvName());
                    holder.basicDetailsAdapterBinding.waterAvailabilityName.setText("Water Available Name -"+basicDetailsList.get(position).getWater_supply_availability_name());
                    if(basicDetailsList.get(position).getComposting_center_image_available().equals("Y")){
                        holder.basicDetailsAdapterBinding.mccCenterImage.setImageBitmap(StringToBitMap(basicDetailsList.get(position).getCenter_image()));
                    }
                    else {

                        holder.basicDetailsAdapterBinding.mccCenterImage.setImageResource(R.drawable.micro_compos_image_icon);


                    }
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
                            ((ViewAndEditMCCDetaila)context).gotoDashboard(position);
                        }
                    });
                    holder.basicDetailsAdapterBinding.editIcon.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            ((ViewAndEditMCCDetaila)context).gotoEditMcc(position);
                        }
                    });

                    holder.basicDetailsAdapterBinding.mccCenterImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(basicDetailsList.get(position).getComposting_center_image_available().equals("Y")){
                                /*((ViewAndEditMCCDetaila)context).fullImagePreview(position);*/
                                ExpandedImage(holder.basicDetailsAdapterBinding.mccCenterImage.getDrawable());
                            }
                            else {
                            }
                        }
                    });

                    holder.basicDetailsAdapterBinding.collectDetailsLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, WasteCollectedForm.class);
                            intent.putExtra("pvcode",basicDetailsList.get(position).getPvCode());
                            intent.putExtra("pv_name",basicDetailsList.get(position).getPvName());
                            intent.putExtra("mcc_id",basicDetailsList.get(position).getMcc_id());
                            intent.putExtra("mcc_name",basicDetailsList.get(position).getMcc_name());
                            context.startActivity(intent);

                        }
                    });

            }

            @Override
            public int getItemCount(){
                    return basicDetailsList.size();
                    }

            public class MyViewHolder extends RecyclerView.ViewHolder {
                private BasicDetailsAdapterFromServerBinding basicDetailsAdapterBinding;

                public MyViewHolder(BasicDetailsAdapterFromServerBinding Binding) {
                    super(Binding.getRoot());
                    basicDetailsAdapterBinding = Binding;
                }
            }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
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
                    if(save_delete.equals("delete")) {
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
        String mcc_id = basicDetailsList.get(position).getMcc_id();




        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_micro_composting_delete");
            jsonObject.put("pvcode",pvcode);
            jsonObject.put("mcc_id",mcc_id);

            ((ViewAndEditMCCDetaila)context).DeleteMccData(jsonObject,basicDetailsList.get(position).getMcc_id(),"");

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

    private void ExpandedImage(Drawable profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_fullscreen_preview, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.image_preview);
            zoomImage.setImageDrawable(profile);

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(context);
            zoomImage.setOnTouchListener(imageMatrixTouchHandler);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setView(ImagePopupLayout);

            final AlertDialog alert = dialogBuilder.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_zoomInOut;
            alert.show();
            alert.getWindow().setBackgroundDrawableResource(R.color.full_transparent);
            alert.setCanceledOnTouchOutside(true);

            zoomImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
