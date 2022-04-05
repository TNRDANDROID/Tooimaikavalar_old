package com.nic.thooimaikaavalar_old.adapter;

import android.app.Dialog;
import android.content.Context;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.FullImageActivity;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.GalleryThumbnailBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FullImageAdapter extends RecyclerView.Adapter<FullImageAdapter.MyViewHolder> {

    private Context context;
    private PrefManager prefManager;
    private List<RealTimeMonitoringSystem> imagePreviewlistvalues;
    private final dbData dbData;
    private LayoutInflater layoutInflater;
    String type;

    public FullImageAdapter(Context context, List<RealTimeMonitoringSystem> imagePreviewlistvalues, dbData dbData,String type) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.imagePreviewlistvalues = imagePreviewlistvalues;
        this.type =type;
    }

    @Override
    public FullImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        GalleryThumbnailBinding galleryThumbnailBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.gallery_thumbnail, viewGroup, false);
        return new FullImageAdapter.MyViewHolder(galleryThumbnailBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private GalleryThumbnailBinding galleryThumbnailBinding;

        public MyViewHolder(GalleryThumbnailBinding Binding) {
            super(Binding.getRoot());
            galleryThumbnailBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        holder.description.setText(imagePreviewlistvalues.get(position).getImageRemark());
//        holder.thumbnail.setImageBitmap(imagePreviewlistvalues.get(position).getImage());
//        holder.title.setText(imagePreviewlistvalues.get(position).getType()+" Activity");
        Glide.with(context).load(imagePreviewlistvalues.get(position).getImage())
                .thumbnail(0.5f)
                .into(holder.galleryThumbnailBinding.thumbnail);

        if(type.equals("Online")){
            holder.galleryThumbnailBinding.deleteIcon.setVisibility(View.VISIBLE);
        }
        else {
            holder.galleryThumbnailBinding.deleteIcon.setVisibility(View.GONE);
        }

        holder.galleryThumbnailBinding.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_and_delete_alert(position,"delete");
            }
        });

        holder.galleryThumbnailBinding.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FullImageActivity)context).gotoFullImageView(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return imagePreviewlistvalues.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FullImageAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FullImageAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void deletePending(int position) {
        String mcc_id = imagePreviewlistvalues.get(position).getMcc_id();
        String photograph_serial_no = imagePreviewlistvalues.get(position).getPhotograph_serial_no();




        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"details_of_mcc_photographs_delete");
            jsonObject.put("photograph_serial_no",photograph_serial_no);
            jsonObject.put("mcc_id",mcc_id);

            ((FullImageActivity)context).DeletePhoto(jsonObject);

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
                text.setText("Do You Want to Edit?");
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


}
