package com.example.danny.catalogoandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText usuario;
    EditText contrasenha;
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = (EditText)findViewById(R.id.usuario);
        contrasenha= (EditText)findViewById(R.id.contrasenha);
        textView1 = (TextView)findViewById(R.id.mostrar);
    }

    public void login(View view){
        String e1 = usuario.getText().toString();
        String e2 = contrasenha.getText().toString();
        if(e1.equals("")){
            mostrar("Ingrese Usuario");
            return ;
        }else if(e2.equals("")){
            mostrar("Ingrese Contraseña");
            return ;
        }
        new Hilo().execute("login",e1,e2);
    }
    public void usuarioPerfil(View view){
        Intent intent = new Intent(this,UsuarioPerfil.class);
        startActivity(intent);
    }
    public void usuarioCrear(View view){
        Intent intent = new Intent(this,UsuarioCrear.class);
        startActivity(intent);
    }
    public void atajoBuscar(View view){
        Intent intent = new Intent(this,ProductoBuscar.class);
        startActivity(intent);
    }
    private void mostrar(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }

    public class Hilo extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... parametro) {
            if(parametro[0].equals("login")) {
                try {
                    JSONObject salida = new JSONObject();
                    salida.put("salida","Inicio");
                    Usuarios usuarios = new Usuarios();
                    String s = usuarios.obtenerPorUser(parametro[1]);
                    JSONObject json = new JSONObject(s);
                    if(json.getString("estado").equals("1")){
                        if(json.getJSONObject("usuario").getString("pass").equals(parametro[2])){
                            salida.put("usuario",parametro[1]);
                            salida.put("contrasenha",parametro[2]);
                            salida.put("nombre",json.getJSONObject("usuario").getString("nombre"));
                            salida.put("salida","correcto");
                            return salida;
                        }
                        salida.put("salida","Error contraseña");
                        return salida;
                    }
                    if(json.getString("estado").equals("2")){
                        salida.put("salida","No existe el usuario");
                        return salida;
                    }
                    salida.put("salida","No elección");
                    return salida;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mostrar("Procesando...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            //super.onPostExecute(aVoid);
            try {
                String s = json.getString("salida").toString();
                mostrar(s);
                if(s.equals("correcto")){
                    Dapp d = (Dapp)getApplication();
                    d.setNombre(json.getString("nombre"));
                    d.setUsuario(json.getString("usuario"));
                    d.setContrasenha(json.getString("contrasenha"));
                    usuarioPerfil(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mostrar("Error");
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(JSONObject aVoid) {
            super.onCancelled(aVoid);
        }
    }
}
