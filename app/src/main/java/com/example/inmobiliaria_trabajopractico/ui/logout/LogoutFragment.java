package com.example.inmobiliaria_trabajopractico.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.inmobiliaria_trabajopractico.LoginActivity;
import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mostrarDialogoCerrarSesion();
    }

    private void mostrarDialogoCerrarSesion() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Cierre de sesión")
                .setMessage("¿Está seguro de que desea cerrar la sesión?")
                .setNegativeButton("CANCELAR", (dialog, which) -> {
                    dialog.dismiss();
                    Navigation.findNavController(requireView()).navigate(R.id.nav_inicio);
                })
                .setPositiveButton("ACEPTAR", (dialog, which) -> {
                    cerrarSesion();
                })
                .setCancelable(false)
                .show();
    }

    private void cerrarSesion() {
        ApiClient.eliminarToken(requireContext());

        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        requireActivity().finish();
    }
}