package com.example.inmobiliaria_trabajopractico.ui.pago;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentPagoBinding;

public class PagoFragment extends Fragment {

    private PagoViewModel mViewModel;
    private FragmentPagoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPagoBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(PagoViewModel.class);

        int contratoId = getArguments() != null ? getArguments().getInt("contratoId", -1) : -1;
        if (contratoId == -1) {
            Toast.makeText(getContext(), "Contrato no especificado", Toast.LENGTH_SHORT).show();
            return binding.getRoot();
        }

        setupObservers();
        mViewModel.buscarPagos(contratoId);

        return binding.getRoot();
    }

    private void setupObservers() {
        mViewModel.getCargando().observe(getViewLifecycleOwner(), cargando -> {
            binding.pbPagos.setVisibility(cargando ? View.VISIBLE : View.GONE);
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.tvErrorPago.setVisibility(View.VISIBLE);
                binding.tvErrorPago.setText(error);
                binding.rvPagos.setVisibility(View.GONE);
                binding.tvEmptyPago.setVisibility(View.GONE);
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getPagos().observe(getViewLifecycleOwner(), pagos -> {
            if (pagos != null) {
                if (pagos.isEmpty()) {
                    binding.tvEmptyPago.setVisibility(View.VISIBLE);
                    binding.rvPagos.setVisibility(View.GONE);
                    binding.tvErrorPago.setVisibility(View.GONE);
                } else {
                    binding.rvPagos.setVisibility(View.VISIBLE);
                    binding.tvEmptyPago.setVisibility(View.GONE);
                    binding.tvErrorPago.setVisibility(View.GONE);
                    PagoAdapter adapter = new PagoAdapter(pagos, getLayoutInflater());
                    binding.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.rvPagos.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
