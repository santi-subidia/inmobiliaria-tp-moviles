package com.example.inmobiliaria_trabajopractico.ui.inmuble;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentInmuebleBinding;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentPerfilBinding;

public class InmuebleFragment extends Fragment {

    private InmuebleViewModel mViewModel;
    private FragmentInmuebleBinding binding;

    public static InmuebleFragment newInstance() {
        return new InmuebleFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(InmuebleViewModel.class);
        mViewModel.getListaInmuebles().observe(getViewLifecycleOwner(), inmuebles -> {
            InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, getLayoutInflater());
            binding.rVwInmubles.setAdapter(adapter);
            GridLayoutManager glm = new GridLayoutManager(getContext(),
                    2, GridLayoutManager.VERTICAL, false);
            binding.rVwInmubles.setLayoutManager(glm);

        });
        mViewModel.obtenerListaInmuebles();

        binding.fabCrear.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_inmuebles_to_crearInmuebleFragment);
        });

        return binding.getRoot();
    }


}