package com.example.danny.catalogoandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductoVer extends AppCompatActivity {
    ImageView imagen;
    TextView nombre;
    TextView precio;
    TextView descripcion;
    TextView miUrl;
    TextView tienda;
    TextView latlon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producto_ver);

        imagen = (ImageView)findViewById(R.id.imagen);
        nombre = (TextView)findViewById(R.id.nombre);
        precio = (TextView)findViewById(R.id.precio);
        descripcion = (TextView)findViewById(R.id.descripcion);
        miUrl = (TextView)findViewById(R.id.mi_url);
        tienda = (TextView)findViewById(R.id.tienda);
        latlon = (TextView)findViewById(R.id.latlng);

        Dapp d = (Dapp)getApplication();
        Producto p = d.getTemProducto();
        imagen.setImageBitmap(p.getImagen());
        nombre.setText(p.getNombreProducto());
        precio.setText("s/. "+p.getPrecio());
        descripcion.setText(p.getDescripcion());
        miUrl.setText(p.getMiUrl());
        tienda.setText(p.getTienda());
        if(p.getLatitud().length()>0 && p.getLongitud().length()>0)
            latlon.setText(p.getLatitud()+","+p.getLongitud());
    }
    public void verMapa(View view){
        String ll = latlon.getText().toString();
        if(ll.length()<1)return;
        String ti = tienda.getText().toString();
        Uri location = Uri.parse("geo:"+ll+"?z=14&q="+ll+"("+ti+")"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }
    public void verWeb(View view){
        String ut = miUrl.getText().toString();
        if(ut.length()<1)return;
        Uri webpage = Uri.parse(ut);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }
}
