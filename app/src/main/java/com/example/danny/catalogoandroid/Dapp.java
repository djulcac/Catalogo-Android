package com.example.danny.catalogoandroid;

/**
Datos de la app
 */
import android.app.Application;

public class Dapp extends  Application{
    private String key="123456";
    private String nombre = "-.-";
    private String usuario = "-.-";
    private String contrasenha = "-.-";
    // temporales
    private Producto temProducto;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public Producto getTemProducto() {
        return temProducto;
    }

    public void setTemProducto(Producto temProducto) {
        this.temProducto = temProducto;
    }
}
