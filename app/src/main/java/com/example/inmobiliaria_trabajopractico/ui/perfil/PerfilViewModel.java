package com.example.inmobiliaria_trabajopractico.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria_trabajopractico.modelo.Propietario;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Propietario> mPropietario;
    private MutableLiveData<Boolean> mEditando;
    private MutableLiveData<String> mTextoBoton;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.mEditando = new MutableLiveData<>(false);
        this.mTextoBoton = new MutableLiveData<>("Modificar Perfil");
    }

    public LiveData<Propietario> getmPropietario() {
        if (mPropietario == null) {
            mPropietario = new MutableLiveData<>();
        }
        return mPropietario;
    }

    public LiveData<Boolean> getmEditando() {
        return mEditando;
    }

    public LiveData<String> getmTextoBoton() {
        return mTextoBoton;
    }

    public void obtenerPerfil() {
        String token = ApiClient.usarToken(context);
        if (token != null) {
            ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();
            Call<Propietario> call = api.getPropietario(token);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mPropietario.postValue(response.body());
                    } else {
                        Log.d("PerfilViewModel", "Error al obtener perfil: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Log.d("PerfilViewModel", "Falla al obtener perfil: " + t.getMessage());
                }
            });
        }
    }

    public void accionBotonModificar(Propietario propietarioEditado) {
        if (mEditando.getValue()) {
            guardarCambios(propietarioEditado);
        } else {
            mEditando.setValue(true);
            mTextoBoton.setValue("Guardar Cambios");
        }
    }

    private void guardarCambios(Propietario p) {
        String token = ApiClient.usarToken(context);
        if (token != null) {
            ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();
            Call<Propietario> call = api.actualizarPropietario(token, p);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful()) {
                        mPropietario.postValue(response.body());
                        mEditando.setValue(false);
                        mTextoBoton.setValue("Modificar Perfil");
                        Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("PerfilViewModel", "Error al actualizar perfil: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Log.d("PerfilViewModel", "Falla al actualizar perfil: " + t.getMessage());
                }
            });
        }
    }
}
