package com.example.danny.catalogoandroid;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UsuarioPerfil extends AppCompatActivity {
    ImageView usuario_imagen;
    TextView nombre;
    TextView usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_perfil);
        usuario_imagen = (ImageView)findViewById(R.id.usuario_imagen);
        nombre = (TextView)findViewById(R.id.nombre);
        usuario = (TextView)findViewById(R.id.usuario);

        usuario_imagen.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.usuario));
        Dapp d = (Dapp)getApplication();
        nombre.setText(d.getNombre());
        usuario.setText(d.getUsuario());
    }
    public void buscar(View view){
        startActivity(new Intent(this,ProductoBuscar.class));
    }
}
