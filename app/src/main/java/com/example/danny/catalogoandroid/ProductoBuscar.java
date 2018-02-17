package com.example.danny.catalogoandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProductoBuscar extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    ArrayList<Producto>listTodo; //todos los elementos
    ArrayList<Producto>list; // lista que se muestra
    EditText consulta;
    // para cargar imagen progresivamente
    int temIdProducto;
    int temMaxProducto=3;
    ArrayList<Producto> temArrayProducto;
    // busqueda, para guardar la respuesta
    ArrayList<Integer>temIBusqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producto_buscar);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrescarBusqueda();
            }
        });
        consulta = (EditText)findViewById(R.id.consulta);
        listView = (ListView)findViewById(R.id.productos);

        list=new ArrayList<Producto>();
        AdapterProducto adapterProducto = new AdapterProducto(this,list);
        listView.setAdapter(adapterProducto);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dapp d = (Dapp)getApplication();
                d.setTemProducto(list.get(position));
                Intent intent = new Intent(view.getContext(),ProductoVer.class);
                startActivity(intent);

            }
        });

        // obtener todos los productos
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.usuario);
        list.add(new Producto("Producto","0.0","Descripción",null));

        listTodo = new ArrayList<Producto>();
        temIBusqueda = new ArrayList<Integer>();
        Hilo hilo = new Hilo();
        hilo.execute("obtenerTodo");
    }
    // los primeros productos que se muestra al usuario
    public void primeraVista(){
        int maximo = temMaxProducto-2;
        temArrayProducto = new ArrayList<Producto>();
        int j=0;

        for(int i=0;i<listTodo.size()&& j<maximo;i++){
            if(i%3==1){
                temArrayProducto.add(listTodo.get(i));
                j++;
            }
        }
        list.clear();
        list.addAll(temArrayProducto);
        //HiloImagen2 hilo = new HiloImagen2();
        //hilo.execute(list.get(0));
        temIdProducto=0;
        cargarImagenTic();
    }
    public void buscar(View view){
        sinInternet();
        mostrar("Buscando...");
        list.clear();
        refrescar();
        String sql = consulta.getText().toString();
        sql = aBuscar(sql);
        String asql[] = sql.split(" ");
        int j=0;
        int lmax = 100;
        temIBusqueda = new ArrayList<Integer>();
        for(int i1=0;i1<asql.length && j<lmax;i1++){
            String s = asql[i1];
            for(int i2=0;i2<listTodo.size()&&j<lmax;i2++){
                Producto p = listTodo.get(i2);
                String pp = aBuscar(p.getNombreProducto()+p.getDescripcion());
                if(pp.contains(s)){
                    j++;
                    temIBusqueda.add(new Integer(i2));
                }
            }
        }
        for(int i1=0;i1<temMaxProducto;i1++){
            if(i1<temIBusqueda.size()&&temIBusqueda.get(i1).intValue()<listTodo.size()){
                list.add(listTodo.get(temIBusqueda.get(i1).intValue()));
            }else break;
        }
        mostrar(list.size()+" resultados de "+temIBusqueda.size());
        cargarImagen(list);
    }
    public void refrescarBusqueda(){
        sinInternet();
        mostrar("....");
        //quitar el icono
        refrescar();
        if(list.size()<temIBusqueda.size()) {
            int t = list.size();
            for (int i1 = t; i1 < temIBusqueda.size() && i1 < t + temMaxProducto; i1++) {
                if (temIBusqueda.get(i1).intValue() < listTodo.size()) {
                    list.add(0, listTodo.get(temIBusqueda.get(i1).intValue()));
                }
            }
            cargarImagen(list);
        }
        mostrar(list.size()+" resultados de "+temIBusqueda.size());
    }

    public String aBuscar(String string){
        String s = string.toLowerCase();
        s = s.replace('á','a');
        s = s.replace('é','e');
        s = s.replace('í','i');
        s = s.replace('ó','o');
        s = s.replace('ú','u');
        return s;
    }
    public void sinInternet(){
        if(listTodo.size()<1){
            Hilo hilo = new Hilo();
            hilo.execute("obtenerTodo");
        }
    }
    public void refrescar(){
        listView.invalidateViews();;
        swipeRefreshLayout.setRefreshing(false);
    }
    public void refrescar2(){
        cargarImagen(list);
        refrescar();
    }
    public void cargarImagen(ArrayList<Producto> arrayList){
        temArrayProducto = new ArrayList<Producto>();
        temArrayProducto.addAll(arrayList);
        temIdProducto=0;
        cargarImagenTic();
    }
    public void cargarImagenTic(){
        if(temIdProducto>=temArrayProducto.size() || temIdProducto>=temMaxProducto){
            return;
        }
        HiloImagen hilo = new HiloImagen();
        hilo.execute(temArrayProducto.get(temIdProducto));
        temIdProducto+=1;
    }
    public ArrayList<Producto> crear(JSONObject json){
        ArrayList<Producto>al=new ArrayList<Producto>();
        try {
            if(json==null)return al;
            JSONArray ja = json.getJSONArray("respuesta");
            for(int i=0;i<ja.length();i++){
                JSONObject o = ja.getJSONObject(i);
                Producto p = new Producto();
                String s = "id";
                if(o.has(s) && !o.isNull(s))p.setId(o.getString(s));
                s="id_tienda";
                if(o.has(s) && !o.isNull(s))p.setIdTienda(o.getString(s));
                s="id_imagen";
                if(o.has(s) && !o.isNull(s))p.setIdImagen(o.getString(s));
                s="ruta_imagen";
                if(o.has(s) && !o.isNull(s))p.setRutaImagen(o.getString(s));
                s="nombre_producto";
                if(o.has(s) && !o.isNull(s))p.setNombreProducto(o.getString(s));
                s="precio";
                if(o.has(s) && !o.isNull(s))p.setPrecio(o.getString(s));
                s="descripcion";
                if(o.has(s) && !o.isNull(s))p.setDescripcion(o.getString(s));
                s="mi_url";
                if(o.has(s) && !o.isNull(s))p.setMiUrl(o.getString(s));
                s="tienda";
                if(o.has(s) && !o.isNull(s))p.setTienda(o.getString(s));
                s="latitud";
                if(o.has(s) && !o.isNull(s))p.setLatitud(o.getString(s));
                s="longitud";
                if(o.has(s) && !o.isNull(s))p.setLongitud(o.getString(s));

                al.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return al;
    }
    public void inicarLista(ArrayList<Producto>arrayList){

    }
    private void mostrar(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }
    public class HiloImagen extends AsyncTask<Producto,Integer,String> {
        @Override
        protected String doInBackground(Producto... parametro) {
            Producto p = parametro[0];
            if(p==null)return null;
            String url = p.getRutaImagen();
            if(url.length()<5)return null;
            p.setImagen(obtenerImagen(url));
            p.setExisteImagen(true);
            return "correcto";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String string) {
            if(string==null)return;
            if(string.equals("correcto")){
                refrescar();
                // cargar imagen
                cargarImagenTic();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        Bitmap obtenerImagen(String cadena){
            try {
                URL url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class Hilo extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... parametro) {
            if(parametro[0].equals("obtenerTodo")) {
                try {
                    JSONObject salida = new JSONObject();
                    salida.put("salida","Inicio");
                    Productos datos = new Productos();
                    String s = datos.obtenerTodo();
                    JSONObject json = new JSONObject(s);
                    if(json == null) {
                        salida.put("salida","JSON null");;
                    }else if(json.getString("estado").equals("1")){
                        salida = json;
                        salida.put("salida","correcto");;
                    }else if(json.getString("estado").equals("2")){
                        salida.put("salida","Error");
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
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if(json==null){
                return;
            }
            try {
                String s =json.getString("salida");
                if(s.equals("correcto")){

                    listTodo.addAll(crear(json));
                    primeraVista();
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

    class AdapterProducto extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<Producto> items;

        public AdapterProducto (Activity activity, ArrayList<Producto> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void clear() {
            items.clear();
        }

        public void addAll(ArrayList<Producto> category) {
            for (int i = 0; i < category.size(); i++) {
                items.add(category.get(i));
            }
        }

        @Override
        public Object getItem(int arg0) {
            return items.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //v = inf.inflate(R.layout.item_category, null);
                v = inf.inflate(R.layout.producto_item, null);
            }

            Producto dir = items.get(position);

            TextView title = (TextView) v.findViewById(R.id.nombre);
            title.setText(dir.getNombreProducto());

            TextView precio = (TextView) v.findViewById(R.id.precio);
            precio.setText("s/. "+dir.getPrecio());

            TextView description = (TextView) v.findViewById(R.id.descripcion);
            description.setText(dir.getDescripcion());

            ImageView imagen = (ImageView) v.findViewById(R.id.imageView);

            imagen.setImageBitmap(dir.getImagen());

            return v;
        }
    }
}
