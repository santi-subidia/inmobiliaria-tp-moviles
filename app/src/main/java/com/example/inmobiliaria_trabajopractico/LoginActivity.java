package com.example.inmobiliaria_trabajopractico;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria_trabajopractico.databinding.ActivityLoginBinding;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityLoginBinding binding;
    private ViewModelLoginActivity vm;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private long ultimoMovimiento = 0;
    private static final int TIEMPO_ENTRE_SACUDIDAS = 2000;
    private static final float FUERZA_SACUDIDA = 18.0f;

    private static final int REQUEST_CALL_PHONE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())
                .create(ViewModelLoginActivity.class);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

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

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double fuerza = Math.sqrt(x * x + y * y + z * z);

            long ahora = System.currentTimeMillis();

            if (fuerza > FUERZA_SACUDIDA && ahora - ultimoMovimiento > TIEMPO_ENTRE_SACUDIDAS) {
                ultimoMovimiento = ahora;
                verificarPermisoYLlamar();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se usa
    }

    private void verificarPermisoYLlamar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {

            vm.llamarInmobiliaria();

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                vm.llamarInmobiliaria();
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void irAMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}