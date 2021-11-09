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

public class AdapterCircuito  extends RecyclerView.Adapter<AdapterCircuito.viewholdercircuito> implements View.OnClickListener{
    List<Circuito> circuitoList;
    private View.OnClickListener listener;
    private RecyclerItemClick itemClick;

    public AdapterCircuito(List<Circuito> circuitoList, RecyclerItemClick itemClick) {
        this.circuitoList = circuitoList;
        this.itemClick=itemClick;
    }

    @NotNull
    @Override
    public viewholdercircuito onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ciruitos,parent,false);
        viewholdercircuito holder= new viewholdercircuito(v);

        v.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewholdercircuito holder, int position) {
        final Circuito circuito = circuitoList.get(position);
        holder.tv_nombre.setText(circuito.getNombre());
        String km=Integer.toString((int) circuito.getKilometros())+" km";
        String min=Integer.toString((int) circuito.getTiempo())+" min";
        holder.tv_distancia.setText(km);
        holder.tv_tiempo.setText(min);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.itemClick(circuito);
            }
        });
      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),DetalleActivity.class);
                intent.putExtra("Detalle Circuito", circuito);
                holder.itemView.getContext().startActivities(new Intent[]{intent});


            }
        });*/


    }

    @Override
    public int getItemCount() {
        return circuitoList.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }

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
    public interface RecyclerItemClick {
        void itemClick(Circuito item);
    }
}