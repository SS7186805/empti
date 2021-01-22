package com.empti.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.allinterface.SelectContainerInterface;
import com.empti.app.model.OutletData;

import java.util.ArrayList;

public class TypeViewAdapter extends RecyclerView.Adapter<TypeViewAdapter.MyViewHolder> {

    private View view;
    LayoutInflater layoutInflater;
    ArrayList<OutletData> modelArr = new ArrayList<>();
    Context context;
    SelectContainerInterface callBack;

    public TypeViewAdapter(Context context, ArrayList<OutletData> modelArr, SelectContainerInterface callBack) {
        this.context = context;
        this.modelArr = modelArr;
        this.callBack = callBack;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notify(ArrayList<OutletData> modelArr) {
        this.modelArr = modelArr;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TypeViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.type_view_list_item, parent, false);
        return new TypeViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewAdapter.MyViewHolder holder, int position) {
        holder.type_txt.setText(modelArr.get(position).getName());
        holder.type_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.clickInfoView(position, modelArr.get(position).getName(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type_txt = itemView.findViewById(R.id.type_txt);
        }
    }
}

