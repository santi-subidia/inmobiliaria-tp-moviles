package com.example.inmobiliaria_trabajopractico.ui.contrato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentContratoBinding;
import com.example.inmobiliaria_trabajopractico.modelo.Contrato;
import java.text.NumberFormat;
import java.util.Locale;

public class ContratoFragment extends Fragment {

    private ContratoViewModel mViewModel;
    private FragmentContratoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContratoBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ContratoViewModel.class);

        int inmuebleId = getArguments() != null ? getArguments().getInt("inmuebleId", -1) : -1;
        if (inmuebleId == -1) {
            Navigation.findNavController(requireView()).popBackStack();
            return binding.getRoot();
        }

        setupObservers();
        mViewModel.buscarContrato(inmuebleId);

        return binding.getRoot();
    }

    private void setupObservers() {
        mViewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            if (contrato != null) {
                binding.llContratoContent.setVisibility(View.VISIBLE);
                binding.llErrorContent.setVisibility(View.GONE);
                populateContrato(contrato);
            }
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.llErrorContent.setVisibility(View.VISIBLE);
                binding.llContratoContent.setVisibility(View.GONE);
                binding.tvErrorMensaje.setText(error);
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getCargando().observe(getViewLifecycleOwner(), cargando -> {
            binding.pbCargando.setVisibility(cargando ? View.VISIBLE : View.GONE);
        });
    }

    private void populateContrato(Contrato contrato) {
        if (contrato.getInquilino() != null) {
            binding.tvInquilinoNombre.setText(contrato.getInquilino().getNombreFull());
            binding.tvContratoDni.setText(contrato.getInquilino().getDni());
            binding.tvContratoTelefono.setText(contrato.getInquilino().getTelefono());
            binding.tvContratoEmail.setText(contrato.getInquilino().getEmail());
        }
        binding.tvContratoFechaInicio.setText(contrato.getFechaInicio());
        binding.tvContratoFechaFin.setText(contrato.getFechaFin());

        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "AR"));
        binding.tvContratoMonto.setText("$ " + nf.format(contrato.getMontoAlquiler()));

        binding.tvContratoEstado.setText(contrato.isEstado() ? "Vigente" : "Finalizado");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
