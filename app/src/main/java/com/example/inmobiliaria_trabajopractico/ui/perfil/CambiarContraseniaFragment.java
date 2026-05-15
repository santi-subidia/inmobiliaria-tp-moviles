package com.example.inmobiliaria_trabajopractico.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentCambiarContraseniaBinding;

public class CambiarContraseniaFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private FragmentCambiarContraseniaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCambiarContraseniaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);
        mViewModel.resetCambioExitoso();

        mViewModel.getmContraseniaActualError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etContraseniaActual.setError(error);
            }
        });

        mViewModel.getmContraseniaNuevaError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etContraseniaNueva.setError(error);
            }
        });

        mViewModel.getmConfirmarContraseniaError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etConfirmarContrasenia.setError(error);
            }
        });

        mViewModel.getmCambioExitoso().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean exitoso) {
                if (exitoso) {
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        binding.etContraseniaActual.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorContraseniaActual();
            }
        });

        binding.etContraseniaNueva.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorContraseniaNueva();
            }
        });

        binding.etConfirmarContrasenia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorConfirmarContrasenia();
            }
        });

        binding.btnCambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actual = binding.etContraseniaActual.getText().toString();
                String nueva = binding.etContraseniaNueva.getText().toString();
                String confirmar = binding.etConfirmarContrasenia.getText().toString();
                mViewModel.cambiarContrasenia(actual, nueva, confirmar);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}