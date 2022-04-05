package com.nic.thooimaikaavalar_old.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import java.util.List;

/**
 * Created by shanmugapriyan on 25/05/16.
 */
public class CommonAdapter extends BaseAdapter {
    private List<RealTimeMonitoringSystem> realTimeMonitoringSystems;
    private Context mContext;
    private String type;


    public CommonAdapter(Context mContext, List<RealTimeMonitoringSystem> realTimeMonitoringSystems, String type) {
        this.realTimeMonitoringSystems = realTimeMonitoringSystems;
        this.mContext = mContext;
        this.type = type;
    }


    public int getCount() {
        return realTimeMonitoringSystems.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.spinner_drop_down_item, parent, false);
//        TextView tv_type = (TextView) view.findViewById(R.id.tv_spinner_item);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);
        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);
        RealTimeMonitoringSystem realTimeMonitoringSystem = realTimeMonitoringSystems.get(position);

        if (type.equalsIgnoreCase("SchemeList")) {
            tv_type.setText(realTimeMonitoringSystem.getSchemeName());
        } else if (type.equalsIgnoreCase("FinYearList")) {
            tv_type.setText(realTimeMonitoringSystem.getFinancialYear());
        } else if (type.equalsIgnoreCase("StageList")) {
            tv_type.setText(realTimeMonitoringSystem.getWorkStageName());
        }
        else if (type.equalsIgnoreCase("HabitationList")) {
            tv_type.setText(realTimeMonitoringSystem.getHabitationName());
        }
        else if (type.equalsIgnoreCase("waterSupplyList")) {
            tv_type.setText(realTimeMonitoringSystem.getKEY_water_supply_availability_name());
        }
        else if (type.equalsIgnoreCase("villageList")) {
            tv_type.setText(realTimeMonitoringSystem.getPvName());
        }
        return view;
    }
}
