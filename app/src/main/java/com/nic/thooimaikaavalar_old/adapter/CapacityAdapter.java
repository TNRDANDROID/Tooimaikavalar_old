package com.nic.thooimaikaavalar_old.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.activity.NewMainPage;
import com.nic.thooimaikaavalar_old.databinding.CapacityRecylerAdapterBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import java.util.List;


public class CapacityAdapter extends RecyclerView.Adapter<CapacityAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<RealTimeMonitoringSystem> capacityList;
    Context context;
    int pos=-1;

    public CapacityAdapter(List<RealTimeMonitoringSystem> capacityList, Context context) {
        this.capacityList = capacityList;
        this.context = context;
    }

    @NonNull
    @Override
    public CapacityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        CapacityRecylerAdapterBinding capacityRecylerAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.capacity_recyler_adapter, viewGroup, false);
        return new MyViewHolder(capacityRecylerAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CapacityAdapter.MyViewHolder holder, final int position) {

        holder.capacityRecylerAdapterBinding.valueTv.setText(capacityList.get(position).getKEY_capacity_of_mcc_name());

        if(position==pos){
            holder.capacityRecylerAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        }
        else {
            holder.capacityRecylerAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        }

        holder.capacityRecylerAdapterBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos=position;
                notifyDataSetChanged();
                ((NewMainPage)context).capacityItemClicked(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return capacityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CapacityRecylerAdapterBinding capacityRecylerAdapterBinding;

        public MyViewHolder(CapacityRecylerAdapterBinding Binding) {
            super(Binding.getRoot());
            capacityRecylerAdapterBinding = Binding;
        }
    }
}
