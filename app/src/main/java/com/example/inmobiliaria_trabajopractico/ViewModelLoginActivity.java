package com.example.inmobiliaria_trabajopractico;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria_trabajopractico.modelo.Propietario;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewModelLoginActivity extends AndroidViewModel {
    private MutableLiveData<String> mensaje;
    private MutableLiveData<Boolean> cargando;
    private Context context;

    public static final int SESION_VALIDA = 1;
    public static final int SESION_INVALIDA = 2;
    public static final int ERROR_RED = 3;

    private MutableLiveData<Integer> resultadoValidacion;

    public ViewModelLoginActivity(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        cargando = new MutableLiveData<>(false);
    }

    public LiveData<String> getMensaje() {
        if (mensaje == null) {
            mensaje = new MutableLiveData<>();
        }
        return mensaje;
    }

    public LiveData<Boolean> getCargando() {
        return cargando;
    }

    public LiveData<Integer> getResultadoValidacion() {
        if (resultadoValidacion == null) {
            resultadoValidacion = new MutableLiveData<>();
        }
        return resultadoValidacion;
    }

    public void validarSesion() {
        String token = ApiClient.usarToken(context);
        if (token == null) {
            resultadoValidacion.setValue(SESION_INVALIDA);
            return;
        }

        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();
        Call<Propietario> call = api.getPropietario(token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    resultadoValidacion.setValue(SESION_VALIDA);
                } else if (response.code() == 401) {
                    ApiClient.eliminarToken(context);
                    resultadoValidacion.setValue(SESION_INVALIDA);
                } else {
                    resultadoValidacion.setValue(SESION_VALIDA);
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("Login", "Sin conexión al validar sesión: " + t.getMessage());
                resultadoValidacion.setValue(ERROR_RED);
            }
        });
    }

    public void llamarInmobiliaria() {
        String telefono = "2664553747";

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + telefono));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public void recuperarDatos(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mensaje.setValue("Por favor, complete todos los campos");
            return;
        }

        cargando.setValue(true);
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<String> call = servicio.login(email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                cargando.setValue(false);
                if (response.isSuccessful()) {
                    String token = response.body();
                    ApiClient.crearToken(context, token);
                    Log.d("token", token);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    if (response.code() == 400) {
                        mensaje.setValue("Usuario o contraseña incorrectos");
                    } else {
                        mensaje.setValue("Error del servidor. Intente más tarde");
                    }
                    Log.d("Error", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cargando.setValue(false);
                mensaje.setValue("Error de conexión. Verifique su red");
                Log.d("mensaje", t.getMessage());
            }
        });
    }
}