package com.example.inmobiliaria_trabajopractico.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.modelo.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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

        @GET("api/Inmubles")
        Call<Inmueble> getInmuble(@Header("Authorization") String token);




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


}
