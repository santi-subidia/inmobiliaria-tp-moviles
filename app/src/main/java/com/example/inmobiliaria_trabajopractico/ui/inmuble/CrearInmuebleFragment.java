package com.example.inmobiliaria_trabajopractico.ui.inmuble;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentCrearInmuebleBinding;

public class CrearInmuebleFragment extends Fragment {

    private CrearInmuebleViewModel mViewModel;
    private FragmentCrearInmuebleBinding binding;
    private Uri imagenUri;

    private final String[] usosArray = {"Residencial", "Comercial", "Industrial"};
    private final String[] tiposArray = {"Casa", "Departamento", "Local", "Terreno", "Oficina"};

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    abrirSelectorImagen();
                } else {
                    Toast.makeText(getContext(), "Permiso requerido para seleccionar imagen", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imagenUri = result.getData().getData();
                    if (imagenUri != null) {
                        mViewModel.setImagen(imagenUri);
                        mostrarImagen(imagenUri);
                    }
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(CrearInmuebleViewModel.class);

        setupSpinners();
        setupListeners();
        observarEstado();

        return binding.getRoot();
    }

    private void setupSpinners() {
        ArrayAdapter<String> usoAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                usosArray
        );
        usoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUso.setAdapter(usoAdapter);

        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                tiposArray
        );
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTipo.setAdapter(tipoAdapter);
    }

    private void setupListeners() {
        binding.btnSeleccionarImagen.setOnClickListener(v -> verificarPermisoYAbrirSelector());
        binding.btnCrear.setOnClickListener(v -> crearInmueble());
    }

    private void observarEstado() {
        mViewModel.getExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito) {
                Toast.makeText(getContext(), "Inmueble creado exitosamente", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.nav_inmuebles);
            }
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getErrorDireccion().observe(getViewLifecycleOwner(), err ->
                binding.tilDireccion.setError(err));
        mViewModel.getErrorAmbientes().observe(getViewLifecycleOwner(), err ->
                binding.tilAmbientes.setError(err));
        mViewModel.getErrorSuperficie().observe(getViewLifecycleOwner(), err ->
                binding.tilSuperficie.setError(err));
        mViewModel.getErrorPrecio().observe(getViewLifecycleOwner(), err ->
                binding.tilPrecio.setError(err));
        mViewModel.getErrorImagen().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verificarPermisoYAbrirSelector() {
        String permiso = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(requireContext(), permiso) == PackageManager.PERMISSION_GRANTED) {
            abrirSelectorImagen();
        } else {
            requestPermissionLauncher.launch(permiso);
        }
    }

    private void abrirSelectorImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Seleccionar imagen"));
    }

    private void mostrarImagen(Uri uri) {
        Glide.with(getContext())
                .load(uri)
                .placeholder(R.drawable.loading)
                .error(R.drawable.house)
                .into(binding.ivCrearImagen);
    }

    private void crearInmueble() {
        Toast.makeText(getContext(), "Cargando...", Toast.LENGTH_SHORT).show();

        String direccion = binding.etDireccion.getText() != null ? binding.etDireccion.getText().toString() : "";
        String uso = binding.spUso.getSelectedItem() != null ? binding.spUso.getSelectedItem().toString() : "";
        String tipo = binding.spTipo.getSelectedItem() != null ? binding.spTipo.getSelectedItem().toString() : "";
        String ambientesStr = binding.etAmbientes.getText() != null ? binding.etAmbientes.getText().toString() : "";
        String superficieStr = binding.etSuperficie.getText() != null ? binding.etSuperficie.getText().toString() : "";
        String precioStr = binding.etPrecio.getText() != null ? binding.etPrecio.getText().toString() : "";
        boolean estado = binding.cbEstado.isChecked();

        mViewModel.crearInmueble(direccion, uso, tipo, ambientesStr, superficieStr, precioStr, estado);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}