package com.example.inmobiliaria_trabajopractico.ui.inmuble;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> inmuebleMutable;
    private MutableLiveData<String> textoDisponibilidadM;

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getInmueble() {
        if (inmuebleMutable == null) {
            inmuebleMutable = new MutableLiveData<>();
        }
        return inmuebleMutable;
    }

    public LiveData<String> getTextoDisponibilidad() {
        if (textoDisponibilidadM == null) {
            textoDisponibilidadM = new MutableLiveData<>();
        }
        return textoDisponibilidadM;
    }

    public void cargarInmueble(Inmueble inmueble) {
        this.inmuebleMutable.setValue(inmueble);
        actualizarTextoEstado(inmueble.isEstado());
    }

    public void cambiarDisponibilidad(Boolean disponible) {
        Inmueble inmueble = inmuebleMutable.getValue();
        if (inmueble != null) {
            inmueble.setEstado(disponible);

            String token = ApiClient.usarToken(getApplication());
            ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

            Call<Inmueble> call = servicio.actualizarInmueble(token, inmueble);

            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Inmueble inmuebleRepo = response.body();
                        inmuebleMutable.postValue(inmuebleRepo);

                        actualizarTextoEstado(inmuebleRepo.isEstado());

                        Log.d("INMUEBLE_DETALLE", "Disponibilidad cambiada");
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    Log.d("INMUEBLE_DETALLE", "Error: " + t.getMessage());
                }
            });
        }
    }
    private void actualizarTextoEstado(boolean estado) {
        if (estado) {
            textoDisponibilidadM.postValue("Disponible para alquilar");
        } else {
            textoDisponibilidadM.postValue("No disponible para alquilar");
        }
    }
}
