package com.example.inmobiliaria_trabajopractico.ui.inquilinos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_trabajopractico.modelo.Contrato;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private MutableLiveData<Contrato> contratoM = new MutableLiveData<>();

    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContratoM() {
        return contratoM;
    }

    public void obtenerContratoDelInmueble(int idInmueble) {
        String token = ApiClient.usarToken(getApplication());

        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();

        Call<Contrato> call = api.getContratoPorInmueble(token, idInmueble);

        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contratoM.postValue(response.body());
                } else {
                    Log.d("errorDetalleInquilino", response.code() + " ");
                    Log.d("errorDetalleInquilino", response.message());

                    if (response.code() == 401 || response.code() == 403) {
                        Toast.makeText(getApplication(), "Sesión expirada o no autorizada", Toast.LENGTH_LONG).show();
                        ApiClient.crearToken(getApplication(), ""); // Pisa el token
                        System.exit(0);
                    } else if (response.code() == 404) {
                        Toast.makeText(getApplication(), "No hay contrato activo para este inmueble", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable throwable) {
                Log.d("errorDetalleInquilino", throwable.getMessage());
                Toast.makeText(getApplication(), "Error al obtener Contrato", Toast.LENGTH_LONG).show();
            }
        });
    }
}