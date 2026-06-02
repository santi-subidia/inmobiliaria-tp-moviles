package com.example.inmobiliaria_trabajopractico.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentInquilinosBinding;

public class InquilinosFragment extends Fragment {
    private InquilinosViewModel viewModel;
    private FragmentInquilinosBinding binding;
    private InquilinoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvInquilinos.setLayoutManager(gridLayoutManager);

        viewModel.getInmueblesAlquilados().observe(getViewLifecycleOwner(), inmuebles -> {
            adapter = new InquilinoAdapter(inmuebles, getContext());
            binding.rvInquilinos.setAdapter(adapter);
        });

        viewModel.obtenerInmueblesAlquilados();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}