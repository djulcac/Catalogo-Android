package com.example.danny.catalogoandroid;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Productos {
    // info base de datos
    private String key;
    private String bdUrl = "https://example.com/";
    private String bdinsertar = bdUrl +"1insertar.php";
    private String bdobtenerPorId = bdUrl + "2obtener_por_id_p.php";
    private String bdobtenerTodo = bdUrl + "3obtener_todo_p.php";
    private String bdactualizar = bdUrl + "4actualizar.php";
    private String bdeliminar = bdUrl + "5eliminar.php";
    public Productos(){
        Dapp d = new Dapp();
        this.key = d.getKey();
    }

    public String insertar(String id,String id_tienda,String id_imagen,String ruta_imagen,
                           String nombre_producto,String precio,String descripcion,String mi_url,
                           String tienda,String latitud,String longitud){
        String devuelve = "inicio";
        try {
            String cadena = bdinsertar;
            JSONObject json = new JSONObject();
            json.put("key",key);
            json.put("id",id);
            json.put("id_tienda",id_tienda);
            json.put("id_imagen",id_imagen);
            json.put("ruta_imagen",ruta_imagen);
            json.put("nombre_producto",nombre_producto);
            json.put("precio",precio);
            json.put("descripcion",descripcion);
            json.put("mi_url",mi_url);
            json.put("tienda",tienda);
            json.put("latitud",latitud);
            json.put("longitud",longitud);
            JSONObject sal = obtenerJson(cadena,json);

            String resulJSON = sal.getString("estado");
            if(resulJSON=="1"){
                devuelve = "Correcto!!";
            }else if(resulJSON=="2"){
                devuelve = "No se puede realizar la operacion";
            }else if(resulJSON=="10"){
                devuelve = "key no es correcto";
            }else {
                devuelve = "error desconocido";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            devuelve += ":error json insertar";
        }

        return devuelve;
    }
    public String obtenerPorId(String id){
        String devuelve = "inicio";
        try {
            String cadena = bdobtenerPorId;
            JSONObject json = new JSONObject();
            json.put("key",key);
            json.put("id",id);
            JSONObject sal = obtenerJson(cadena,json);

            String resulJSON = sal.getString("estado");
            if(resulJSON=="1"){
                devuelve = "Correcto!!"+sal.toString();
            }else if(resulJSON=="2"){
                devuelve = "No se puede realizar la operacion";
            }else if(resulJSON=="10"){
                devuelve = "key no es correcto";
            }else {
                devuelve = "error desconocido";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            devuelve += ":error json insertar";
        }

        return devuelve;
    }
    public String obtenerTodo(){
        String devuelve = "inicio";
        try {
            String cadena = bdobtenerTodo;
            JSONObject json = new JSONObject();
            json.put("key",key);
            JSONObject sal = obtenerJson(cadena,json);
            return sal.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            devuelve =null;
        }

        return devuelve;
    }
    public String actualizar(String id,String id_tienda,String id_imagen,String ruta_imagen,
                             String nombre_producto,String precio,String descripcion,String mi_url,
                             String tienda,String latitud,String longitud){
        String devuelve = "inicio";
        try {
            String cadena = bdactualizar;
            JSONObject json = new JSONObject();
            json.put("key",key);
            json.put("id",id);
            json.put("id_tienda",id_tienda);
            json.put("id_imagen",id_imagen);
            json.put("ruta_imagen",ruta_imagen);
            json.put("nombre_producto",nombre_producto);
            json.put("precio",precio);
            json.put("descripcion",descripcion);
            json.put("mi_url",mi_url);
            json.put("tienda",tienda);
            json.put("latitud",latitud);
            json.put("longitud",longitud);
            JSONObject sal = obtenerJson(cadena,json);

            String resulJSON = sal.getString("estado");
            if(resulJSON=="1"){
                devuelve = "Correcto!!"+sal.toString();
            }else if(resulJSON=="2"){
                devuelve = "No se puede realizar la operacion";
            }else if(resulJSON=="10"){
                devuelve = "key no es correcto";
            }else {
                devuelve = "error desconocido";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            devuelve += ":error json insertar";
        }

        return devuelve;
    }
    public String eliminar(String id){
        String devuelve = "inicio";
        try {
            String cadena = bdeliminar;
            JSONObject json = new JSONObject();
            json.put("key",key);
            json.put("id",id);
            JSONObject sal = obtenerJson(cadena,json);

            String resulJSON = sal.getString("estado");
            if(resulJSON=="1"){
                devuelve = "Correcto!!"+sal.toString();
            }else if(resulJSON=="2"){
                devuelve = "No se puede realizar la operacion";
            }else if(resulJSON=="10"){
                devuelve = "key no es correcto";
            }else {
                devuelve = "error desconocido";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            devuelve += ":error json insertar";
        }

        return devuelve;
    }

    private JSONObject obtenerJson(String url_path,JSONObject json_input) {
        JSONObject devuelve = new JSONObject();
        try {

            URL url = new URL(url_path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 1.5; es-ES) Ejemplo HTTP");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();
            // crear objeto json
            JSONObject json = json_input;

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json.toString());
            writer.flush();
            writer.close();

            int respuesta = connection.getResponseCode();
            StringBuilder resul = new StringBuilder();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                line = reader.readLine();
                while (line != null) {
                    resul.append(line);
                    line = reader.readLine();
                }
                reader.close();
                devuelve = new JSONObject(resul.toString());
                devuelve.put("salida", "Operacion finalizada");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devuelve;
    }
}
