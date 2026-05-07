package com.example.inmobiliaria_trabajopractico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria_trabajopractico.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ViewModelLoginActivity vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(ViewModelLoginActivity.class);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                vm.recuperarDatos(email, password);
            }
        });
    }











   /* private void login() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (email.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }*/


}