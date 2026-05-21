package com.example.inmobiliaria_trabajopractico.modelo;

import com.google.gson.annotations.SerializedName;
import com.example.inmobiliaria_trabajopractico.modelo.Propietario;
import java.io.Serializable;

public class Inmueble implements Serializable {
    @SerializedName("idInmueble")
    private int id;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private int superficie;
    @SerializedName("valor")
    private double precio;
    private String imagen;
    @SerializedName("disponible")
    private boolean estado;
    @SerializedName("idPropietario")
    private int propietarioId;
    @SerializedName("duenio")
    private Propietario propietario;

    public Inmueble(int id, String direccion, String uso, String tipo, int ambientes, int superficie, double precio, String imagen, boolean estado, int propietarioId, Propietario propietario) {
        this.id = id;
        this.direccion = direccion;
        this.uso = uso;
        this.tipo = tipo;
        this.ambientes = ambientes;
        this.superficie = superficie;
        this.precio = precio;
        this.imagen = imagen;
        this.estado = estado;
        this.propietarioId = propietarioId;
        this.propietario = propietario;
    }

    public Inmueble() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getAmbientes() { return ambientes; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }
    public int getSuperficie() { return superficie; }
    public void setSuperficie(int superficie) { this.superficie = superficie; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getPropietarioId() { return propietarioId; }
    public void setPropietarioId(int propietarioId) { this.propietarioId = propietarioId; }
    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }

    public String getImagenFullUrl() {
        if (imagen != null && !imagen.isEmpty()) {
            String urlMod = "https://capacitacion.alwaysdata.net/" + imagen.replace("\\", "/");
            return urlMod.replace("//","/");
        }
        return null;
    }
}