package com.example.inmobiliaria_trabajopractico.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentDetalleInquilinoBinding;
import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.modelo.Inquilino;

public class DetalleInquilinoFragment extends Fragment {

    private FragmentDetalleInquilinoBinding binding;
    private DetalleInquilinoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);

        binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                vm.obtenerContratoDelInmueble(inmueble.getId());
            }
        }

        vm.getContratoM().observe(getViewLifecycleOwner(), contrato -> {
            if (contrato != null && contrato.getInquilino() != null) {
                Inquilino inquilino = contrato.getInquilino();

                binding.tvCodigoInquilino.setText(String.valueOf(inquilino.getId()));
                binding.tvNombreInquilino.setText(inquilino.getNombre());
                binding.tvApellidoInquilino.setText(inquilino.getApellido());
                binding.tvDniInquilino.setText(inquilino.getDni());
                binding.tvEmailInquilino.setText(inquilino.getEmail());
                binding.tvTelefonoInquilino.setText(inquilino.getTelefono());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evitamos fugas de memoria limpiando el binding
    }
}