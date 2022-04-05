package com.nic.thooimaikaavalar_old.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nic.thooimaikaavalar_old.R;

public class SampleHome extends AppCompatActivity {
    RelativeLayout first,second,third,fourth;
    TextView tt_1,tt_2,tt_3,tt_4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_home_screen);

        tt_1 = findViewById(R.id.tt_1);
        tt_2 = findViewById(R.id.tt_2);
        tt_3 = findViewById(R.id.tt_3);
        tt_4 = findViewById(R.id.tt_4);

        Context context;
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/" + "Ubuntu-Medium.ttf");

        tt_1.setTypeface(typeface);
        tt_2.setTypeface(typeface);
        tt_3.setTypeface(typeface);
        tt_4.setTypeface(typeface);




    }
}
