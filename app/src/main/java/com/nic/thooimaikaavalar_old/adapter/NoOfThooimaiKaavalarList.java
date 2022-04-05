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
import com.nic.thooimaikaavalar_old.databinding.NoOfThooimaiKaavalarAdapterBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import java.util.List;


public class NoOfThooimaiKaavalarList extends RecyclerView.Adapter<NoOfThooimaiKaavalarList.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<RealTimeMonitoringSystem> NoOfList;
    int Pos=-1;
    Context context;

    public NoOfThooimaiKaavalarList(List<RealTimeMonitoringSystem> capacityList, Context context) {
        this.NoOfList = capacityList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoOfThooimaiKaavalarList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        NoOfThooimaiKaavalarAdapterBinding noOfThooimaiKaavalarAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.no_of_thooimai_kaavalar_adapter, viewGroup, false);
        return new MyViewHolder(noOfThooimaiKaavalarAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoOfThooimaiKaavalarList.MyViewHolder holder, final int position) {

        holder.noOfThooimaiKaavalarAdapterBinding.valueTv.setText(NoOfList.get(position).getKEY_thooimai_kaavalars_name());

        if(Pos==position){
            holder.noOfThooimaiKaavalarAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        }
        else {
            holder.noOfThooimaiKaavalarAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        }

        holder.noOfThooimaiKaavalarAdapterBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pos=position;
                notifyDataSetChanged();
                ((NewMainPage)context).thooiMaiKaavalarItemClicked(Pos);

            }
        });
    }

    @Override
    public int getItemCount() {
        return NoOfList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private NoOfThooimaiKaavalarAdapterBinding noOfThooimaiKaavalarAdapterBinding;

        public MyViewHolder(NoOfThooimaiKaavalarAdapterBinding Binding) {
            super(Binding.getRoot());
            noOfThooimaiKaavalarAdapterBinding = Binding;
        }
    }
}
