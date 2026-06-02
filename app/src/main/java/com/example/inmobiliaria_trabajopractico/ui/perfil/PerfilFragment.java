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
import com.example.inmobiliaria_trabajopractico.databinding.FragmentPerfilBinding;
import com.example.inmobiliaria_trabajopractico.modelo.Propietario;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private FragmentPerfilBinding binding;
    private Propietario propietarioActual;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

        mViewModel.getmPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                propietarioActual = propietario;
                binding.etNombrePerfil.setText(propietario.getNombre());
                binding.etApellidoPerfil.setText(propietario.getApellido());
                binding.etDniPerfil.setText(propietario.getDni());
                binding.etTelefonoPerfil.setText(propietario.getTelefono());
                binding.etEmailPerfil.setText(propietario.getEmail());
            }
        });

        mViewModel.getmEditando().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean editando) {
                binding.etNombrePerfil.setEnabled(editando);
                binding.etApellidoPerfil.setEnabled(editando);
                binding.etDniPerfil.setEnabled(editando);
                binding.etTelefonoPerfil.setEnabled(editando);
                binding.etEmailPerfil.setEnabled(editando);
            }
        });

        mViewModel.getmTextoBoton().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String texto) {
                binding.btnModificarPerfil.setText(texto);
            }
        });

        mViewModel.getmNombreError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etNombrePerfil.setError(error);
            }
        });

        mViewModel.getmApellidoError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etApellidoPerfil.setError(error);
            }
        });

        mViewModel.getmDniError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etDniPerfil.setError(error);
            }
        });

        mViewModel.getmTelefonoError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etTelefonoPerfil.setError(error);
            }
        });

        mViewModel.getmEmailError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etEmailPerfil.setError(error);
            }
        });

        binding.etNombrePerfil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorNombre();
            }
        });

        binding.etApellidoPerfil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorApellido();
            }
        });

        binding.etDniPerfil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorDni();
            }
        });

        binding.etTelefonoPerfil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorTelefono();
            }
        });

        binding.etEmailPerfil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mViewModel.limpiarErrorEmail();
            }
        });

        binding.btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (propietarioActual != null) {
                    propietarioActual.setNombre(binding.etNombrePerfil.getText().toString());
                    propietarioActual.setApellido(binding.etApellidoPerfil.getText().toString());
                    propietarioActual.setDni(binding.etDniPerfil.getText().toString());
                    propietarioActual.setTelefono(binding.etTelefonoPerfil.getText().toString());
                    propietarioActual.setEmail(binding.etEmailPerfil.getText().toString());
                    propietarioActual.setClave(null);

                    mViewModel.accionBotonModificar(propietarioActual);
                }
            }
        });

        binding.btnCambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_cambiar_contrasenia);
            }
        });

        mViewModel.obtenerPerfil();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}