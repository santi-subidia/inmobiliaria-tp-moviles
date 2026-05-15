package com.example.inmobiliaria_trabajopractico.ui.inmuble;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {
 /*   private MutableLiveData<Inmueble> mInmueble;*/
    private MutableLiveData<List<Inmueble>> listaInmuebles = new MutableLiveData<>();

    public InmuebleViewModel(@NonNull Application application) {
        super(application);
    }

 /*   public LiveData<Inmueble> getMInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }*/

    public LiveData<List<Inmueble>> getListaInmuebles() {
        return listaInmuebles;
    }

 /*   public void recuperarInmueble(Bundle bundle) {
        Inmueble inmueble = (Inmueble) bundle.get("inmuebleBundle");
        if (inmueble != null) {
            if (mInmueble == null) {
                mInmueble = new MutableLiveData<>();
            }
            mInmueble.setValue(inmueble);
        }
    }*/

    public void obtenerListaInmuebles() {
        String token = ApiClient.usarToken(getApplication());

        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();

        Call<List<Inmueble>> call = api.getInmuebles(token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()) {
                    listaInmuebles.postValue(response.body());
                } else {
                    Log.d("errorInmueble", response.code()+" ");
                    Log.d("errorInmueble", response.message());

                    if (response.code() == 401 ||response.code() == 403){
                    Toast.makeText(getApplication(), "No se obtuvieron Inmuebles", Toast.LENGTH_LONG).show();
                    ApiClient.crearToken(getApplication(), "");//metodo que pisa el token si ubo error.
                    System.exit(0);

                }}
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                Log.d("errorInmueble", throwable.getMessage());
                Toast.makeText(getApplication(), "Error al obtener Inmuebles", Toast.LENGTH_LONG).show();
            }
        });
    }
}