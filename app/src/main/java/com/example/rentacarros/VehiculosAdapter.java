package com.example.rentacarros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VehiculosAdapter extends RecyclerView.Adapter<VehiculosAdapter.VehicViewHolder> {
    ArrayList<Vehiculos> VehiculosList;
    public VehiculosAdapter(ArrayList<Vehiculos> vehiculosList){this.VehiculosList = vehiculosList;}



    @NonNull
    @Override
    public VehiculosAdapter.VehicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vvehic = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_vehic_item,null, false);
        return new VehicViewHolder(vvehic);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculosAdapter.VehicViewHolder holder, int position) {
        holder.estado.setText(VehiculosList.get(position).getEstado());
        holder.marca.setText(VehiculosList.get(position).getMarca().toString());
        holder.placa.setText(VehiculosList.get(position).getPlaca().toString());
    }

    @Override
    public int getItemCount() {
        return VehiculosList.size();
    }

    public class VehicViewHolder extends RecyclerView.ViewHolder {
        TextView estado, marca, placa;

        public VehicViewHolder(@NonNull View itemView) {
            super(itemView);
            estado = itemView.findViewById(R.id.etestado);
            marca = itemView.findViewById(R.id.etmarca);
            placa = itemView.findViewById(R.id.etplaca);
        }
    }
}
