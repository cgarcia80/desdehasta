package com.ar.desdehasta.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.desdehasta.R;
import com.ar.desdehasta.pojo.Circuito;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterCircuito  extends RecyclerView.Adapter<AdapterCircuito.viewholdercircuito> {

    List<Circuito> circuitoList;

    public AdapterCircuito(List<Circuito> circuitoList) {
        this.circuitoList = circuitoList;
    }

    @NotNull
    @Override
    public viewholdercircuito onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ciruitos,parent,false);
        viewholdercircuito holder= new viewholdercircuito(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewholdercircuito holder, int position) {
        Circuito circuito = circuitoList.get(position);
        holder.tv_nombre.setText(circuito.getNombre());
        String km=Integer.toString((int) circuito.getKilometros())+" km";
        String min=Integer.toString((int) circuito.getTiempo())+" min";
        holder.tv_distancia.setText(km);
        holder.tv_tiempo.setText(min);


    }

    @Override
    public int getItemCount() {
        return circuitoList.size();
    }

    public class viewholdercircuito extends RecyclerView.ViewHolder {
        TextView tv_nombre, tv_distancia, tv_tiempo;

        public viewholdercircuito(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_nombre=itemView.findViewById(R.id.tv_nombre);
            tv_distancia=itemView.findViewById(R.id.tv_distancia);
            tv_tiempo=itemView.findViewById(R.id.tv_tiempo);


        }
    }
}
