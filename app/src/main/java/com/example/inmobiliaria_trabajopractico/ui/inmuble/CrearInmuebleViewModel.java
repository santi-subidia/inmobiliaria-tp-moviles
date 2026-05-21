package com.example.inmobiliaria_trabajopractico.ui.inmuble;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearInmuebleViewModel extends AndroidViewModel {

    private static final String TAG = "CREAR_INMUEBLE";

    private MutableLiveData<Boolean> exito;
    private MutableLiveData<String> error;

    private final MutableLiveData<String> errorDireccion = new MutableLiveData<>();
    private final MutableLiveData<String> errorAmbientes = new MutableLiveData<>();
    private final MutableLiveData<String> errorSuperficie = new MutableLiveData<>();
    private final MutableLiveData<String> errorPrecio = new MutableLiveData<>();
    private final MutableLiveData<String> errorImagen = new MutableLiveData<>();

    private File imagenFile;

    public CrearInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getExito() {
        if (exito == null) exito = new MutableLiveData<>();
        return exito;
    }

    public LiveData<String> getError() {
        if (error == null) error = new MutableLiveData<>();
        return error;
    }

    public LiveData<String> getErrorDireccion() { return errorDireccion; }
    public LiveData<String> getErrorAmbientes() { return errorAmbientes; }
    public LiveData<String> getErrorSuperficie() { return errorSuperficie; }
    public LiveData<String> getErrorPrecio() { return errorPrecio; }
    public LiveData<String> getErrorImagen() { return errorImagen; }

    public void setImagen(Uri uri) {
        imagenFile = uriToFile(uri);
    }

    private File uriToFile(Uri uri) {
        try {
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                errorImagen.setValue("Error al procesar imagen");
                return null;
            }

            String extension = determinarExtension(uri);
            File tempFile = File.createTempFile("upload_", "." + extension, getApplication().getCacheDir());

            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            Log.e(TAG, "Error convirtiendo URI a archivo: " + e.getMessage());
            errorImagen.setValue("Error al procesar imagen");
            return null;
        }
    }

    private String determinarExtension(Uri uri) {
        String mimeType = getApplication().getContentResolver().getType(uri);
        if (mimeType != null) {
            if (mimeType.equals("image/png")) return "png";
            if (mimeType.equals("image/gif")) return "gif";
            if (mimeType.equals("image/webp")) return "webp";
        }
        return "jpg";
    }

    public void crearInmueble(String direccion, String uso, String tipo,
                              String ambientesStr, String superficieStr,
                              String precioStr, boolean estado) {

        errorDireccion.setValue(null);
        errorAmbientes.setValue(null);
        errorSuperficie.setValue(null);
        errorPrecio.setValue(null);
        errorImagen.setValue(null);

        boolean hasErrors = false;

        if (direccion == null || direccion.trim().isEmpty()) {
            errorDireccion.setValue("Ingrese la dirección");
            hasErrors = true;
        }

        if (ambientesStr == null || ambientesStr.trim().isEmpty()) {
            errorAmbientes.setValue("Ingrese la cantidad de ambientes");
            hasErrors = true;
        }

        if (superficieStr == null || superficieStr.trim().isEmpty()) {
            errorSuperficie.setValue("Ingrese la superficie");
            hasErrors = true;
        }

        if (precioStr == null || precioStr.trim().isEmpty()) {
            errorPrecio.setValue("Ingrese el precio");
            hasErrors = true;
        }

        if (imagenFile == null) {
            errorImagen.setValue("Seleccione una imagen");
            hasErrors = true;
        }

        if (hasErrors) return;

        int ambientes;
        try {
            ambientes = Integer.parseInt(ambientesStr.trim());
        } catch (NumberFormatException e) {
            errorAmbientes.setValue("La cantidad de ambientes debe ser un número entero");
            return;
        }

        int superficie;
        try {
            superficie = Integer.parseInt(superficieStr.trim());
        } catch (NumberFormatException e) {
            errorSuperficie.setValue("La superficie debe ser un número entero");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr.trim());
        } catch (NumberFormatException e) {
            errorPrecio.setValue("El precio debe ser un número válido");
            return;
        }

        enviarInmueble(direccion.trim(), uso, tipo, ambientes, superficie, precio, estado);
    }

    private void enviarInmueble(String direccion, String uso, String tipo,
                                int ambientes, int superficie, double precio,
                                boolean estado) {

        String token = ApiClient.usarToken(getApplication());
        if (token == null) {
            error.setValue("No hay sesión activa");
            return;
        }

        Inmueble inmueble = new Inmueble();

        inmueble.setDireccion(direccion);
        inmueble.setUso(uso);
        inmueble.setTipo(tipo);
        inmueble.setAmbientes(ambientes);
        inmueble.setSuperficie(superficie);
        inmueble.setPrecio(precio);
        inmueble.setEstado(estado);

        String inmuebleJson = new Gson().toJson(inmueble);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json"), inmuebleJson);

        String mimeType = getMimeType(imagenFile.getName());
        RequestBody imagenBody = RequestBody.create(MediaType.parse(mimeType), imagenFile);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", imagenFile.getName(), imagenBody);

        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<Inmueble> call = servicio.crearInmueble(token, imagenPart, inmuebleBody);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    exito.postValue(true);
                } else {
                    String mensajeError = "Error al crear inmueble: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "Error " + response.code() + ": " + response.errorBody().string());
                        } catch (Exception e) {
                            Log.e(TAG, "Error leyendo errorBody: " + e.getMessage());
                        }
                    }
                    error.postValue(mensajeError);
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e(TAG, "Fallo de red: ", t);
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    private String getMimeType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        return "image/png";
    }
}