package com.example.inmobiliaria_trabajopractico.ui.pago;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.inmobiliaria_trabajopractico.modelo.Pago;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> pagosMutable;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<Boolean> cargandoMutable;

    public PagoViewModel(@NonNull Application application) {
        super(application);
        cargandoMutable = new MutableLiveData<>();
        cargandoMutable.setValue(false);
    }

    public LiveData<List<Pago>> getPagos() {
        if (pagosMutable == null) {
            pagosMutable = new MutableLiveData<>();
        }
        return pagosMutable;
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

    public void buscarPagos(int contratoId) {
        cargandoMutable.setValue(true);
        if (errorMutable != null) errorMutable.setValue(null);

        String token = ApiClient.usarToken(getApplication());
        if (token == null) {
            errorMutable.setValue("No hay sesión activa");
            cargandoMutable.setValue(false);
            return;
        }

        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<List<Pago>> call = servicio.getPagosPorContrato(token, contratoId);

        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                cargandoMutable.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    pagosMutable.postValue(response.body());
                } else if (response.code() == 404) {
                    pagosMutable.postValue(new ArrayList<>());
                } else {
                    errorMutable.postValue("Error del servidor. Intente más tarde");
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                cargandoMutable.postValue(false);
                errorMutable.postValue("Error de conexión. Verifique su red");
            }
        });
    }
}
