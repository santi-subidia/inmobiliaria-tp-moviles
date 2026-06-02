package com.example.inmobiliaria_trabajopractico.ui.inquilinos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.ItemInquilinoBinding;
import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;

import java.util.List;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.ViewHolder> {
    private List<Inmueble> lista;
    private Context context;

    public InquilinoAdapter(List<Inmueble> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInquilinoBinding binding = ItemInquilinoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = lista.get(position);

        holder.binding.tvDireccion.setText(inmueble.getDireccion());

        Glide.with(context)
                .load(inmueble.getImagenFullUrl())
                .into(holder.binding.ivImagenInmueble);

        holder.binding.btnVerInquilino.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);
            Navigation.findNavController(v).navigate(R.id.nav_detalle_inquilino, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemInquilinoBinding binding;

        public ViewHolder(@NonNull ItemInquilinoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}