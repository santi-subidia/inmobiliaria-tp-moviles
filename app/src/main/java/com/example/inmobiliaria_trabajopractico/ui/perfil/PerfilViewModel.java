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

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Propietario> mPropietario;
    private MutableLiveData<Boolean> mEditando;
    private MutableLiveData<String> mTextoBoton;
    private MutableLiveData<String> mNombreError;
    private MutableLiveData<String> mApellidoError;
    private MutableLiveData<String> mDniError;
    private MutableLiveData<String> mTelefonoError;
    private MutableLiveData<String> mEmailError;
    private MutableLiveData<String> mContraseniaActualError;
    private MutableLiveData<String> mContraseniaNuevaError;
    private MutableLiveData<String> mConfirmarContraseniaError;
    private MutableLiveData<Boolean> mCambioExitoso;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.mEditando = new MutableLiveData<>(false);
        this.mTextoBoton = new MutableLiveData<>("Modificar Perfil");
        this.mNombreError = new MutableLiveData<>();
        this.mApellidoError = new MutableLiveData<>();
        this.mDniError = new MutableLiveData<>();
        this.mTelefonoError = new MutableLiveData<>();
        this.mEmailError = new MutableLiveData<>();
        this.mContraseniaActualError = new MutableLiveData<>();
        this.mContraseniaNuevaError = new MutableLiveData<>();
        this.mConfirmarContraseniaError = new MutableLiveData<>();
        this.mCambioExitoso = new MutableLiveData<>();
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

    public LiveData<String> getmNombreError() {
        return mNombreError;
    }

    public LiveData<String> getmApellidoError() {
        return mApellidoError;
    }

    public LiveData<String> getmDniError() {
        return mDniError;
    }

    public LiveData<String> getmTelefonoError() {
        return mTelefonoError;
    }

    public LiveData<String> getmEmailError() {
        return mEmailError;
    }

    public LiveData<String> getmContraseniaActualError() {
        return mContraseniaActualError;
    }

    public LiveData<String> getmContraseniaNuevaError() {
        return mContraseniaNuevaError;
    }

    public LiveData<String> getmConfirmarContraseniaError() {
        return mConfirmarContraseniaError;
    }

    public LiveData<Boolean> getmCambioExitoso() {
        return mCambioExitoso;
    }

    public void resetCambioExitoso() {
        mCambioExitoso.setValue(false);
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
            if (validarCampos(propietarioEditado)) {
                guardarCambios(propietarioEditado);
            }
        } else {
            mEditando.setValue(true);
            mTextoBoton.setValue("Guardar Cambios");
        }
    }

    private boolean validarCampos(Propietario p) {
        boolean esValido = true;

        // Nombre
        String nombre = p.getNombre() != null ? p.getNombre().trim() : "";
        if (nombre.isEmpty()) {
            mNombreError.setValue("El nombre es obligatorio");
            esValido = false;
        } else if (nombre.length() < 2) {
            mNombreError.setValue("El nombre debe tener al menos 2 caracteres");
            esValido = false;
        } else {
            mNombreError.setValue(null);
        }

        // Apellido
        String apellido = p.getApellido() != null ? p.getApellido().trim() : "";
        if (apellido.isEmpty()) {
            mApellidoError.setValue("El apellido es obligatorio");
            esValido = false;
        } else if (apellido.length() < 2) {
            mApellidoError.setValue("El apellido debe tener al menos 2 caracteres");
            esValido = false;
        } else {
            mApellidoError.setValue(null);
        }

        // DNI
        String dni = p.getDni() != null ? p.getDni().trim() : "";
        if (dni.isEmpty()) {
            mDniError.setValue("El DNI es obligatorio");
            esValido = false;
        } else if (!dni.matches("\\d+")) {
            mDniError.setValue("El DNI debe contener solo números");
            esValido = false;
        } else if (dni.length() < 7 || dni.length() > 8) {
            mDniError.setValue("El DNI debe tener entre 7 y 8 dígitos");
            esValido = false;
        } else {
            mDniError.setValue(null);
        }

        // Teléfono
        String telefono = p.getTelefono() != null ? p.getTelefono().trim() : "";
        if (telefono.isEmpty()) {
            mTelefonoError.setValue("El teléfono es obligatorio");
            esValido = false;
        } else if (telefono.length() < 6) {
            mTelefonoError.setValue("El teléfono debe tener al menos 6 caracteres");
            esValido = false;
        } else {
            mTelefonoError.setValue(null);
        }

        // Email
        String email = p.getEmail() != null ? p.getEmail().trim() : "";
        if (email.isEmpty()) {
            mEmailError.setValue("El email es obligatorio");
            esValido = false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            mEmailError.setValue("Ingrese un email válido");
            esValido = false;
        } else {
            mEmailError.setValue(null);
        }

        return esValido;
    }

    public void limpiarErrorNombre() {
        mNombreError.setValue(null);
    }

    public void limpiarErrorApellido() {
        mApellidoError.setValue(null);
    }

    public void limpiarErrorDni() {
        mDniError.setValue(null);
    }

    public void limpiarErrorTelefono() {
        mTelefonoError.setValue(null);
    }

    public void limpiarErrorEmail() {
        mEmailError.setValue(null);
    }

    public void limpiarErrorContraseniaActual() {
        mContraseniaActualError.setValue(null);
    }

    public void limpiarErrorContraseniaNueva() {
        mContraseniaNuevaError.setValue(null);
    }

    public void limpiarErrorConfirmarContrasenia() {
        mConfirmarContraseniaError.setValue(null);
    }

    public void cambiarContrasenia(String actual, String nueva, String confirmar) {
        if (validarContrasenias(actual, nueva, confirmar)) {
            ejecutarCambioContrasenia(actual, nueva);
        }
    }

    private boolean validarContrasenias(String actual, String nueva, String confirmar) {
        boolean esValido = true;

        // Contraseña actual
        if (actual.isEmpty()) {
            mContraseniaActualError.setValue("Ingrese la contraseña actual");
            esValido = false;
        } else {
            mContraseniaActualError.setValue(null);
        }

        // Nueva contraseña
        if (nueva.isEmpty()) {
            mContraseniaNuevaError.setValue("Ingrese la nueva contraseña");
            esValido = false;
        } else if (nueva.length() < 6) {
            mContraseniaNuevaError.setValue("La contraseña debe tener al menos 6 caracteres");
            esValido = false;
        } else {
            mContraseniaNuevaError.setValue(null);
        }

        // Confirmar contraseña
        if (confirmar.isEmpty()) {
            mConfirmarContraseniaError.setValue("Confirme la nueva contraseña");
            esValido = false;
        } else if (!confirmar.equals(nueva)) {
            mConfirmarContraseniaError.setValue("Las contraseñas no coinciden");
            esValido = false;
        } else {
            mConfirmarContraseniaError.setValue(null);
        }

        return esValido;
    }

    private void ejecutarCambioContrasenia(String actual, String nueva) {
        String token = ApiClient.usarToken(context);
        if (token != null) {
            ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();
            Call<Void> call = api.cambiarContrasenia(token, actual, nueva);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        mCambioExitoso.setValue(true);
                        Toast.makeText(context, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        mCambioExitoso.setValue(false);
                        if (response.code() == 401) {
                            mContraseniaActualError.setValue("Contraseña actual incorrecta");
                        } else {
                            Toast.makeText(context, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    mCambioExitoso.setValue(false);
                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
                    Log.d("PerfilViewModel", "Falla al cambiar contraseña: " + t.getMessage());
                }
            });
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