package com.example.inmobiliaria_trabajopractico.ui.contrato;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.inmobiliaria_trabajopractico.modelo.Contrato;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoViewModel extends AndroidViewModel {

    private MutableLiveData<Contrato> contratoMutable;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<Boolean> cargandoMutable;

    public ContratoViewModel(@NonNull Application application) {
        super(application);
        cargandoMutable = new MutableLiveData<>();
        cargandoMutable.setValue(false);
    }

    public LiveData<Contrato> getContrato() {
        if (contratoMutable == null) {
            contratoMutable = new MutableLiveData<>();
        }
        return contratoMutable;
    }

    public LiveData<String> getError() {
        if (errorMutable == null) {
            errorMutable = new MutableLiveData<>();
        }
        return errorMutable;
    }

    public LiveData<Boolean> getCargando() {
        return cargandoMutable;
    }

    public void buscarContrato(int inmuebleId) {
        cargandoMutable.setValue(true);
        if (errorMutable != null) errorMutable.setValue(null);

        String token = ApiClient.usarToken(getApplication());
        if (token == null) {
            errorMutable.setValue("No hay sesión activa");
            cargandoMutable.setValue(false);
            return;
        }

        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<Contrato> call = servicio.getContratoPorInmueble(token, inmuebleId);

        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                cargandoMutable.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    contratoMutable.postValue(response.body());
                } else if (response.code() == 404) {
                    errorMutable.postValue("Contrato no encontrado");
                } else {
                    errorMutable.postValue("Error del servidor. Intente más tarde");
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                cargandoMutable.postValue(false);
                errorMutable.postValue("Error de conexión. Verifique su red");
            }
        });
    }
}
