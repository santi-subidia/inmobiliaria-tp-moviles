package com.example.inmobiliaria_trabajopractico.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliaria_trabajopractico.modelo.Contrato;
import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.modelo.Pago;
import com.example.inmobiliaria_trabajopractico.modelo.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {
    public final static String BASE_URL ="https://capacitacion.alwaysdata.net/";
    public static MiServicioInmobiliaria getServicio(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(MiServicioInmobiliaria.class);
    }
    public interface MiServicioInmobiliaria{
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        //llamar propietario
        @GET("api/Propietarios")
        Call<Propietario> getPropietario(@Header("Authorization") String token);

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> cambiarContrasenia(@Header("Authorization") String token,
                                      @Field("currentPassword") String actual,
                                      @Field("newPassword")  String nueva);

        @GET("api/Inmuebles")
        Call<List<Inmueble>> getInmuebles(@Header("Authorization") String token);
        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);

        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> crearInmueble(@Header("Authorization") String token,
                                     @Part MultipartBody.Part imagen,
                                     @Part("inmueble") RequestBody inmuebleJson);

        @GET("api/contratos/inmueble/{id}")
        Call<Contrato> getContratoPorInmueble(@Header("Authorization") String token, @Path("id") int inmuebleId);

        @GET("api/pagos/contrato/{id}")
        Call<List<Pago>> getPagosPorContrato(@Header("Authorization") String token, @Path("id") int contratoId);

    }
    public static void crearToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token","Bearer " + token);
        editor.apply();
    }
    public static String usarToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }

    public static void eliminarToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("token");
        editor.apply();
    }


}
