package com.example.inmobiliaria_trabajopractico.ui.inquilinos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> inmueblesAlquilados = new MutableLiveData<>();

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getInmueblesAlquilados() {
        return inmueblesAlquilados;
    }

    public void obtenerInmueblesAlquilados() {
        String token = ApiClient.usarToken(getApplication());
        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();

        Call<List<Inmueble>> call = api.getInmueblesAlquilados(token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmueblesAlquilados.postValue(response.body());
                } else {
                    Log.d("errorInquilinos", response.code() + " ");
                    Log.d("errorInquilinos", response.message());

                    if (response.code() == 401 || response.code() == 403) {
                        Toast.makeText(getApplication(), "No se obtuvieron Inmuebles Alquilados", Toast.LENGTH_LONG).show();
                        ApiClient.crearToken(getApplication(), "");
                        System.exit(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                Log.d("errorInquilinos", throwable.getMessage());
                Toast.makeText(getApplication(), "Error al obtener Inmuebles Alquilados", Toast.LENGTH_LONG).show();
            }
        });
    }
}