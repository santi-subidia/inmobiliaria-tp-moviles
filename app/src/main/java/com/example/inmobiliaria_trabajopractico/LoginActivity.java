package com.example.inmobiliaria_trabajopractico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria_trabajopractico.databinding.ActivityLoginBinding;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ViewModelLoginActivity vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())
                .create(ViewModelLoginActivity.class);

        String token = ApiClient.usarToken(getApplicationContext());
        if (token != null) {
            binding.btnLogin.setEnabled(false);
            vm.validarSesion();
        }

        vm.getResultadoValidacion().observe(this, resultado -> {
            switch (resultado) {
                case ViewModelLoginActivity.SESION_VALIDA:
                    irAMain();
                    break;
                case ViewModelLoginActivity.SESION_INVALIDA:
                    binding.btnLogin.setEnabled(true);
                    break;
                case ViewModelLoginActivity.ERROR_RED:
                    irAMain();
                    break;
            }
        });

        vm.getMensaje().observe(this, mensaje -> {
            if (mensaje != null) {
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

        vm.getCargando().observe(this, cargando -> {
            binding.btnLogin.setEnabled(!cargando);
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                vm.recuperarDatos(email, password);
            }
        });
    }

    private void irAMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}