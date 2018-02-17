package com.example.danny.catalogoandroid;

import android.graphics.Bitmap;

public class Producto{
    private String id="";
    private String idTienda="";
    private String idImagen="";
    private String rutaImagen="";
    private String nombreProducto="";
    private String precio="";
    private String descripcion="";
    private String miUrl="";
    private String tienda="";
    private String latitud="";
    private String longitud="";

    private Bitmap imagen=null;
    private Boolean existeImagen=false;
    public Producto(){

    }
    public Producto(String nombreProducto,String precio,String descripcion,Bitmap bitmap){
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.descripcion=descripcion;
        this.imagen = bitmap;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getMiUrl() {
        return miUrl;
    }

    public void setMiUrl(String miUrl) {
        this.miUrl = miUrl;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Boolean getExisteImagen() {
        return existeImagen;
    }

    public void setExisteImagen(Boolean existeImagen) {
        this.existeImagen = existeImagen;
    }
}