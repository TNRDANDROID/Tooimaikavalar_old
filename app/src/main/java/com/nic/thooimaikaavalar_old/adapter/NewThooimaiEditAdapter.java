package com.nic.thooimaikaavalar_old.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.NewThooimaiKavalarEdit;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.BasicDetailsAdapterBinding;
import com.nic.thooimaikaavalar_old.databinding.NewThooimaiEditAdapterBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class NewThooimaiEditAdapter extends RecyclerView.Adapter<NewThooimaiEditAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
    private com.nic.thooimaikaavalar_old.dataBase.dbData dbData;
        private List<RealTimeMonitoringSystem> basicDetailsList;
        static JSONObject dataset = new JSONObject();
                Context context;
                int pos=-1;

        public NewThooimaiEditAdapter(List<RealTimeMonitoringSystem> capacityList, Context context,dbData dbData) {
                this.basicDetailsList = capacityList;
                this.context = context;
                this.dbData = dbData;
                }

        @NonNull
        @Override
        public NewThooimaiEditAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                if (layoutInflater == null) {

                layoutInflater = LayoutInflater.from(viewGroup.getContext());
                }
                NewThooimaiEditAdapterBinding newThooimaiEditAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.new_thooimai_edit_adapter, viewGroup, false);
                return new MyViewHolder(newThooimaiEditAdapterBinding);
                }

        @Override

        public void onBindViewHolder(@NonNull final NewThooimaiEditAdapter.MyViewHolder holder, final int position) {
            holder.newThooimaiEditAdapterBinding.dateOfEngage.setText("Date Of Engage "+basicDetailsList.get(position).getDate_of_engagement());
            holder.newThooimaiEditAdapterBinding.dateOfTraining.setText("Date Of Training "+basicDetailsList.get(position).getDate_of_training_given());
            holder.newThooimaiEditAdapterBinding.name.setText("Name "+basicDetailsList.get(position).getName_of_the_thooimai_kaavalars());
            holder.newThooimaiEditAdapterBinding.mobileNumber.setText("Mobile "+basicDetailsList.get(position).getMobile_no());

            holder.newThooimaiEditAdapterBinding.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_and_delete_alert(position,"delete");

            }
            });

            holder.newThooimaiEditAdapterBinding.editIcon.setOnClickListener(new View.OnClickListener() {
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
            private NewThooimaiEditAdapterBinding newThooimaiEditAdapterBinding;

            public MyViewHolder(NewThooimaiEditAdapterBinding Binding) {
                super(Binding.getRoot());
                newThooimaiEditAdapterBinding = Binding;
            }
        }

            public Bitmap stringtoBitmap(String encodedImage){
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                return decodedByte;
            }

            public void uploadPending(int position) {

            }

            public void deletePending(int position) {
                dataset = new JSONObject();
                String pvcode = basicDetailsList.get(position).getPvCode();
                String mcc_id = basicDetailsList.get(position).getMcc_id();
                String thooimai_kaavalar_id = basicDetailsList.get(position).getThooimai_kaavalar_id();




                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_mcc_thooimai_kaavalar_delete");
                    jsonObject.put("thooimai_kaavalar_id",thooimai_kaavalar_id);
                    jsonObject.put("mcc_id",mcc_id);

                    ((NewThooimaiKavalarEdit)context).DeleteMccData(jsonObject,basicDetailsList.get(position).getThooimai_kaavalar_id(),"");

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
                                showAlert(position);
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

    public  void showAlert(int pos){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.update_dialog_thooimai_kaavalr);

            ImageView close_icon = (ImageView) dialog.findViewById(R.id.close_icon);
            EditText name = (EditText) dialog.findViewById(R.id.name);
            EditText mobile_number = (EditText) dialog.findViewById(R.id.mobile_number);
            TextView date_of_engage = (TextView) dialog.findViewById(R.id.date_of_engage);
            TextView date_of_training = (TextView) dialog.findViewById(R.id.date_of_training);
            Button update_btn = (Button) dialog.findViewById(R.id.update_btn);

            name.setText(basicDetailsList.get(pos).getName_of_the_thooimai_kaavalars());
            mobile_number.setText(basicDetailsList.get(pos).getMobile_no());
            date_of_engage.setText(dateFormate(basicDetailsList.get(pos).getDate_of_engagement(),""));
            date_of_training.setText(dateFormate(basicDetailsList.get(pos).getDate_of_training_given(),""));

            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            update_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity= (Activity) context;
                    if(!name.getText().toString().equals("")){
                        if(!mobile_number.getText().toString().equals("")&& Utils.isValidMobilenew(mobile_number.getText().toString())){
                            if(!date_of_engage.getText().toString().equals("")){
                                if(!date_of_training.getText().toString().equals("")){
                                    dbData.open();
                                    RealTimeMonitoringSystem realTimeMonitoringSystem = new RealTimeMonitoringSystem();
                                    realTimeMonitoringSystem.setMcc_id(basicDetailsList.get(pos).getMcc_id());
                                    realTimeMonitoringSystem.setName_of_the_thooimai_kaavalars(name.getText().toString());
                                    realTimeMonitoringSystem.setMobile_no(mobile_number.getText().toString());
                                    realTimeMonitoringSystem.setDate_of_engagement(date_of_engage.getText().toString());
                                    realTimeMonitoringSystem.setDate_of_training_given(date_of_training.getText().toString());
                                    realTimeMonitoringSystem.setThooimai_kaavalar_id(basicDetailsList.get(pos).getThooimai_kaavalar_id());
                                    ArrayList<RealTimeMonitoringSystem> count = dbData.getAllThooimaikaavalarListLocaltID(basicDetailsList.get(pos).getThooimai_kaavalar_id());
                                    if(count.size()>0){
                                       dbData.updateThooimaiKaavaleDetailsLocal(realTimeMonitoringSystem,basicDetailsList.get(pos).getThooimai_kaavalar_id());
                                        Toasty.success(context, context.getResources().getString(R.string.updated_success), Toast.LENGTH_LONG, true).show();
                                        dialog.dismiss();
                                    }
                                    else {
                                        dbData.insertThooimaiKaavaleDetailsLocal(realTimeMonitoringSystem);
                                        Toasty.success(context, context.getResources().getString(R.string.inserted_success), Toast.LENGTH_LONG, true).show();
                                        dialog.dismiss();
                                    }

                                }
                                else {
                                    Utils.showAlert(activity,context.getResources().getString(R.string.please_choose_date_of_training));
                                }
                            }
                            else {
                                Utils.showAlert(activity,context.getResources().getString(R.string.please_choose_date_of_engagement));
                            }
                        }
                        else {
                            Utils.showAlert(activity,context.getResources().getString(R.string.please_enter_valid_mobile_number));
                        }
                    }
                    else {
                        Utils.showAlert(activity,context.getResources().getString(R.string.please_enter_name));
                    }

                }
            });

            date_of_engage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    final int year = c.get(Calendar.YEAR);
                    final int month = c.get(Calendar.MONTH);
                    final int day = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    //dateFormate(date);
                                    date_of_engage.setText(dateFormate(date,"yes"));

                                }
                            }, year, month, day);
                    datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                    datePickerDialog.show();
                }
            });
            date_of_training.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    final int year = c.get(Calendar.YEAR);
                    final int month = c.get(Calendar.MONTH);
                    final int day = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    //dateFormate(date);
                                    date_of_training.setText(dateFormate(date,"yes"));

                                }
                            }, year, month, day);
                    datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                    datePickerDialog.show();
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String dateFormate( String strDate,String type ){
        try {
            SimpleDateFormat sdfSource = null;
            if(type.equals("")){
                // create SimpleDateFormat object with source string date format
                 sdfSource = new SimpleDateFormat(
                        "yyyy-mm-dd");
            }
            else {
                sdfSource = new SimpleDateFormat(
                        "dd-M-yyyy");
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

