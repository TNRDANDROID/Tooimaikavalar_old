package com.nic.thooimaikaavalar_old.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.databinding.NoOfThooimaiKaavalarAdapterBinding;
import com.nic.thooimaikaavalar_old.databinding.ThooimaiKavaalarDetailsAddLayoutAdapterBinding;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class ThooimaiKaavalrEnterDetailsAdapter extends RecyclerView.Adapter<ThooimaiKaavalrEnterDetailsAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    int counts;
    int Pos=-1;
    Context context;

    public ThooimaiKaavalrEnterDetailsAdapter(int counts, Context context) {
        this.counts =counts;
        this.context = context;
    }

    @NonNull
    @Override
    public ThooimaiKaavalrEnterDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ThooimaiKavaalarDetailsAddLayoutAdapterBinding thooimaiKavaalarDetailsAddLayoutAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.thooimai_kavaalar_details_add_layout_adapter, viewGroup, false);
        return new MyViewHolder(thooimaiKavaalarDetailsAddLayoutAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ThooimaiKaavalrEnterDetailsAdapter.MyViewHolder holder, final int position) {

        holder.thooimaiKavaalarDetailsAddLayoutAdapterBinding.dateOfEngage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                //dateFormate(date);
                                holder.thooimaiKavaalarDetailsAddLayoutAdapterBinding.dateOfEngage.setText(dateFormate(date,""));

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        holder.thooimaiKavaalarDetailsAddLayoutAdapterBinding.dateOfTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                //dateFormate(date);
                                holder.thooimaiKavaalarDetailsAddLayoutAdapterBinding.dateOfTraining.setText(dateFormate(date,""));

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return counts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ThooimaiKavaalarDetailsAddLayoutAdapterBinding thooimaiKavaalarDetailsAddLayoutAdapterBinding;

        public MyViewHolder(ThooimaiKavaalarDetailsAddLayoutAdapterBinding Binding) {
            super(Binding.getRoot());
            thooimaiKavaalarDetailsAddLayoutAdapterBinding = Binding;
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
        }
        catch (ParseException pe) {
            System.out.println("Parse Exception : " + pe);
        }
        return strDate;
    }


}
