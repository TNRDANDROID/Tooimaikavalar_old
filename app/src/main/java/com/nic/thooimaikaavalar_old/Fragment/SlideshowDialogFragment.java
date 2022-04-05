package com.nic.thooimaikaavalar_old.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import java.util.ArrayList;


public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<RealTimeMonitoringSystem> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDescription,lblDate,lblType;
    private int selectedPosition = 0;

   public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDescription = (TextView) v.findViewById(R.id.description);
        lblType = (TextView) v.findViewById(R.id.type);
        lblDate = (TextView) v.findViewById(R.id.date);

        images = (ArrayList<RealTimeMonitoringSystem>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.i(TAG, "position: " + selectedPosition);
        Log.i(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        RealTimeMonitoringSystem image = images.get(position);
        if(image.getPhotograph_remark() != null) {
            if (!image.getPhotograph_remark().equalsIgnoreCase("")) {
                lblDescription.setVisibility(View.VISIBLE);
                lblDescription.setText(image.getPhotograph_remark());
            } else {
                lblDescription.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            RealTimeMonitoringSystem image = images.get(position);

            Glide.with(getActivity()).load(image.getImage())
                    .thumbnail(0.5f)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
