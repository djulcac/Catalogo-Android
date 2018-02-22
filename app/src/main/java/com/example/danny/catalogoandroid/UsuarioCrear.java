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

public class UsuarioCrear extends AppCompatActivity {
    EditText nombre;
    EditText usuario;
    EditText contrasenha;
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_crear);

        nombre = (EditText)findViewById(R.id.nombre);
        usuario = (EditText)findViewById(R.id.usuario);
        contrasenha = (EditText)findViewById(R.id.contrasenha);
        textView1 = (TextView)findViewById(R.id.textView1);
    }
    public void aUsuarioPerfil(View view){
        //startActivity(new Intent(this,UsuarioPerfil.class));
        startActivity(new Intent(this,Main2Activity.class));
    }
    public void crear(View view){
        String e1 = nombre.getText().toString();
        String e2 = usuario.getText().toString();
        String e3 = contrasenha.getText().toString();
        if(e1.equals("")){
            mostrar("Ingrese Nombre");
            return ;
        }else if(e2.equals("")){
            mostrar("Ingrese Usuario");
            return ;
        }else if(e3.equals("")){
            mostrar("Ingrese contraseña");
            return ;
        }
        new Hilo().execute("crear",e2,e3,e1);
    }
    private void mostrar(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }
    public class Hilo extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... parametro) {
            if(parametro[0].equals("crear")) {
                try {
                    JSONObject salida = new JSONObject();
                    salida.put("salida","Inicio");
                    Usuarios usuarios = new Usuarios();
                    String s = usuarios.obtenerPorUser(parametro[1]);
                    JSONObject json = new JSONObject(s);
                    if(json.getString("estado").equals("1")){
                        salida.put("salida","Ya existe el usuario");;
                    }else if(json.getString("estado").equals("2")){
                        Usuarios u2 = new Usuarios();
                        JSONObject j2 = new JSONObject(u2.insertar("id",parametro[1],
                                parametro[2],
                                parametro[3]));
                        if(j2.getString("estado").equals("1")){
                            salida.put("usuario",parametro[1]);
                            salida.put("contrasenha",parametro[2]);
                            salida.put("nombre",parametro[3]);
                            salida.put("salida","correcto");
                        }else {
                            salida.put("salida", "Error creación:"+j2.toString());
                        }
                    }else {
                        salida.put("salida","Error conexión");
                    }
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
            if(json==null){
                mostrar("Error json");
                return;
            }
            try {
                mostrar(json.getString("salida"));
                if(json.getString("salida").equals("correcto")) {
                    Dapp d = (Dapp)getApplication();
                    d.setNombre(json.getString("nombre"));
                    d.setUsuario(json.getString("usuario"));
                    d.setContrasenha(json.getString("contrasenha"));
                    aUsuarioPerfil(null);
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
