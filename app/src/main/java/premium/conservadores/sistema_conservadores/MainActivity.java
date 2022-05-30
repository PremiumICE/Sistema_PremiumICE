package premium.conservadores.sistema_conservadores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import premium.conservadores.sistema_conservadores.Adaptadores.SpinAdapterProductos;
import premium.conservadores.sistema_conservadores.Clases.Cls_Mensaje;
import premium.conservadores.sistema_conservadores.Clases.clsAud_Conservador;
import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.Clases.clsVentasGuardadas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsAuditoria;
import premium.conservadores.sistema_conservadores.ClasesBD.clsClientes;
import premium.conservadores.sistema_conservadores.ClasesBD.clsConservadores;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductosMermas;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Adapter_Menus.ItemClickListener, Adapter_Conservadores.ItemClickListener{
    private static final String url = "jdbc:mysql://162.241.62.225:3306/dmecommx_sistemapi?characterEncoding=latin1";
    private static final String user = "dmecommx_khaf";
    private static final String pass = "Resident123";
    double latitud = 0,longitud = 0;
    private static final int PICTURE_RESULT = 122 ;
    String imageurl;
    int id_cliente_aud;
    //Declaración de componentes y variables
    private static final int REQUEST_CODE_QR_SCAN = 101;
    SQLocal dbLocal;
    SharedPreferences Configuraciones;
    SharedPreferences.Editor EditarConf;
    //Adapter_Menus adapter;
    Adapter_Conservadores AdaptadorConservadores;
    Adapter_VentasG AdaptadorVentas;
    private RequestQueue queue; //objeto sobre el cual se mandan las peticiones post
    Gson Gson_Converter = new Gson();//objeto que permite convertir cadenas JSON en objetos
    private LocationManager locManager;
    private Location loc;

    ImageView imgFoto;
    ContentValues values;
    private Uri imageUri;
    byte[] fotoSubir;

    Marker mCenterMarker = null;
    CameraPosition MarcadorGlobal;
    MarkerOptions mCenterMarkerOptions = null;
//    Adapter_Productos AdaptadorProductos;
    //FloatingActionButton fabRuta, fabInfo;
    Button btnCerrar, btnAgregarEvidencia;
    LinearLayout llOpciones,bottomSheet, llResumenAud,llFotoAud, llConfig;
    TextView lblConexionInternet, lblMonitoreo;
    public TabLayout tabs;
    ProgressBar pbEnviar;
    boolean panelOpciones = true;
//    int contador;
//    int alturarec;
    Adapter_ProductosMermas AdaptadorProductos;
    clsProductos prodselfin;
    ArrayList<clsProductos> prodsmermas;
    clsProductosMermas productoLista;
    ArrayList<clsProductosMermas> productos;
    RecyclerView recyclerCons;
    clsAud_Conservador conservadorxauditar;
    ArrayList<clsAud_Conservador> conservadores;
    ArrayList<clsAuditoria> auditorias;

    //ArrayList<clsMenus> listaTitulos;
    //clsMenus titulos;
    //String absolutePath = "https://conservadores.premiumice.es/Archivos/Documentos/Fotos_Conservadores/";
    ConnectivityManager cm;
    Intent servicio;
    TextView lblSinAuditorias;
    //TextView lblInfoRuta, lblUsuarioIni;
//    clsProductos productoLista;
//    ArrayList<clsProductos> productos;
//
//    private LocationManager locManager;
//    private Location loc;
//    Marker mCenterMarker = null;
//    CameraPosition MarcadorGlobal;
//    MarkerOptions mCenterMarkerOptions = null;
    ArrayList<clsVentasGuardadas> ventasGArray;
    clsVentasGuardadas ventasGclase;
    NetworkInfo activeNetwork;
    Button btnActualizar;
    public ViewPager2 vpInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbLocal = new SQLocal(this);
        solicitarPermisos();
        //iniciacion de elementos UI

        //listaTitulos = new ArrayList<clsMenus>();
        vpInicio = (ViewPager2) findViewById(R.id.vpFragments);
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        servicio = new Intent(getApplicationContext(), BackgroundService.class);
        //fabRuta = (FloatingActionButton) findViewById(R.id.fabRuta);
        //fabInfo = (FloatingActionButton) findViewById(R.id.fabInfo);
        //btnCerrar = (Button) findViewById(R.id.btnCerrar);
        //llOpciones = (LinearLayout) findViewById(R.id.llOpciones);
        //llInfo = (LinearLayout) findViewById(R.id.llInformacionRuta);
        //lblInfoRuta = (TextView) findViewById(R.id.lblHoraRutaIni);
        //lblUsuarioIni = (TextView) findViewById(R.id.lblUsuarioInicio);
        Configuraciones = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EditarConf = Configuraciones.edit();
        bottomSheet = (LinearLayout) findViewById(R.id.bottomSheet);
        llConfig = (LinearLayout) findViewById(R.id.llConfiguracion);
        tabs = (TabLayout) findViewById(R.id.tabInicio);
        lblConexionInternet = (TextView) findViewById(R.id.lblConexionInternet);
        lblMonitoreo = (TextView) findViewById(R.id.txtMonitoreo);
        pbEnviar = (ProgressBar) findViewById(R.id.pbEnviarTodoC);
        Global.handleSSLHandshake();
        queue = Volley.newRequestQueue(this);
        BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bsb.setHideable(false);
        btnActualizar = (Button) findViewById(R.id.btnActualizarAud);
        lblSinAuditorias = (TextView) findViewById(R.id.lblMostrarSinC);
        activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //metodos de los componentes
        /*fabRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RutaMensaje("Iniciar");
            }
        });*/
        /*btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetenerRuta().show();
            }
        });*/
        /*fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llInfo.getVisibility()==View.VISIBLE)
                    llInfo.setVisibility(View.GONE);
                else
                    llInfo.setVisibility(View.VISIBLE);
            }
        });
*/
        //Validacion de ruta
        //if(panelOpciones){
            //fabRuta.setVisibility(View.GONE);
            //llOpciones.setForeground(null);
            //btnCerrar.setVisibility(View.VISIBLE);
        //}
        //CargarMenus();

        //Carga de elementos en la lista para los elementos recyclerview
        //Elementos de opciones
        //titulos.add("Registro de Ventas");
        //titulos.add("Venta distribuidor");
        //titulos.add("Solicitud Cambios");

        //Elementos de conservadores
//        int id = 0;
//        String nomCon = "";
//
//        for(int i = 1; i < 5;i++){
//            conservadorxauditar = new clsAud_Conservador();
//            id = getResources().getIdentifier("premium.conservadores.sistema_conservadores:drawable/con"+i, null, null);
//            nomCon = "Conservador #"+i;
//            conservadorxauditar.Id_Imagen = id;
//            conservadorxauditar.QR = nomCon;
//            conservadores.add(conservadorxauditar);
//        }

        //Colocamos los elementos en los recycler correspondientes
        /*RecyclerView recyclerView = findViewById(R.id.recTitulos);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new Adapter_Menus(this, listaTitulos, this);
        adapter.setClickListener((Adapter_Menus.ItemClickListener) this);
        recyclerView.setAdapter(adapter);*/

        if(isConnected)
            lblConexionInternet.setVisibility(View.GONE);
        vpInicio.setAdapter(createInicioAdapter());
        new TabLayoutMediator(tabs, vpInicio,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position == 0)
                            tab.setText("INICIO");
                        else
                            tab.setText("CONFIGURACIÓN");
                    }
                }).attach();


        /*adapter.setClickListener(new Adapter_Menus.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(listaTitulos.size()>1) {
                    if (position == 0) {
                        Intent Reg = new Intent(MainActivity.this, VentaCliente.class);
                        startActivityForResult(Reg, 0);
                    } else if (position == 1) {
                        //Toast.makeText(getApplicationContext(), String.valueOf(Configuraciones.getBoolean("RutaAct", false)), Toast.LENGTH_SHORT).show();
                        if (Configuraciones.getBoolean("RutaAct", false)) {
                            DetenerRuta().show();
                        } else {
                            RutaMensaje();
                        }

                    } else if (listaTitulos.get(position).getTipo()==3) {
                        Intent ventas = new Intent(MainActivity.this, VentasActivity.class);
                        startActivity(ventas);
                    }
                }else{
                    if (Configuraciones.getBoolean("RutaAct", false)) {
                        DetenerRuta().show();
                    } else {
                        RutaMensaje();
                    }
                }
            }
        });*/

//        if(Configuracionesg.getBoolean("RutaAct", false))
//            EstablecerRuta();

        //Traer todos los datos
        Toast.makeText(getApplicationContext(), String.valueOf(dbLocal.getCoordenadas().size()), Toast.LENGTH_SHORT).show();
        auditorias = dbLocal.getAuditorias();
        recyclerCons = findViewById(R.id.recConservadores);
        recyclerCons.setLayoutManager(new GridLayoutManager(this,1, GridLayoutManager.HORIZONTAL,false));

        if(dbLocal.getAuditorias().size()>0){
            //btnActualizar.setVisibility(View.GONE);
            recyclerCons.setVisibility(View.VISIBLE);
            lblSinAuditorias.setVisibility(View.GONE);
        }else
            recyclerCons.setVisibility(View.GONE);

        //clsAud_Conservador aud_conservador = new clsAud_Conservador();
        AdaptadorConservadores = new Adapter_Conservadores(getApplicationContext(), auditorias);
        AdaptadorConservadores.setClickListener((Adapter_Conservadores.ItemClickListener) this);
        recyclerCons.setAdapter(AdaptadorConservadores);

        AdaptadorConservadores.setClickListener(new Adapter_Conservadores.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(auditorias.get(position).getAuditado()!=1) {
                    pbEnviar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Proceso_Auditoria(auditorias.get(position), position);
                        }
                    });
                }
                else {
                    MostrarAuditoria(position);
                    Toast.makeText(getApplicationContext(), "El conservador ya esta auditado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), String.valueOf(Configuraciones.getInt("Id_Usuario",0)), Toast.LENGTH_SHORT).show();

                TraerAuditorias aud = new TraerAuditorias("");
                aud.execute();
            }
        });
    }
    public class TraerAuditorias extends AsyncTask<String, String, String> {
        public TraerAuditorias(String params) {
        }

        @Override
        protected void onPreExecute() {
            dbLocal.deleteConservadores_Auditar();
            pbEnviar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                // "jdbc:mysql://IP:PUERTO/DB", "USER", "PASSWORD");
                // Si estás utilizando el emulador de android y tenes el mysql en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                Connection conn = DriverManager.getConnection(url, user, pass);
                //En el stsql se puede agregar cualquier consulta SQL deseada.
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("Select Id_ConservadorA, Id_Conservador, Fecha_Inicio, Fecha_Fin, Estatus, " +
                        "Id_CheckList, Latitud, Longitud, Id_Cliente, Imagen_Rev, Id_Usuario, Auditado From Ope_Conservador_Usuario Where Id_Usuario = "+Configuraciones.getInt("Id_Usuario",0));
                while(rs.next()){
                    dbLocal.InsertAuditoria(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4),rs.getInt(5),
                            rs.getInt(6), rs.getString(7), rs.getString(8),rs.getInt(9),
                            rs.getBytes(10),0,"","", rs.getInt(11), rs.getInt(12));
                }
                conn.close();
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
            return "";

        }
        @Override
        protected void onPostExecute(String result) {
            pbEnviar.setVisibility(View.INVISIBLE);
            auditorias = dbLocal.getAuditorias();
            if(dbLocal.getAuditorias().size()>0)
            {
                recyclerCons.setVisibility(View.VISIBLE);
                //btnActualizar.setVisibility(View.GONE);
                lblSinAuditorias.setVisibility(View.GONE);
            }else
                recyclerCons.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(),String.valueOf(dbLocal.getAuditorias().size()),Toast.LENGTH_LONG).show();
            AdaptadorConservadores.notifyDataSetChanged();
            recyclerCons.setAdapter(AdaptadorConservadores);
        }
    }
    private SectionsPagerAdapter createInicioAdapter() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this);
        return adapter;
    }
    /*public void CargarMenus(){
        Bitmap bmLocal = null;
        listaTitulos.clear();
        if(Configuraciones.getBoolean("RutaAct", false)) {
            titulos = new clsMenus();
            titulos.Titulo = "Registro de Ventas";
            titulos.Tipo = 0;
            bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.ventas);
            titulos.Img_Menu = bmLocal;
            listaTitulos.add(titulos);

            titulos = new clsMenus();
            titulos.Titulo = "Iniciar Ruta";
            titulos.Tipo = 2;
            bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.nada);
            titulos.Img_Menu = bmLocal;
            listaTitulos.add(titulos);

            titulos = new clsMenus();
            titulos.Titulo = "Venta distribuidor";
            titulos.Tipo = 0;
            bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.distribuidor);
            titulos.Img_Menu = bmLocal;
            listaTitulos.add(titulos);

            titulos = new clsMenus();
            titulos.Titulo = "Solicitudes Administrativas";
            titulos.Tipo = 0;
            bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.solicitud);
            titulos.Img_Menu = bmLocal;
            listaTitulos.add(titulos);

            if(dbLocal.getVentas().size() > 0) {
                titulos = new clsMenus();
                titulos.Titulo = "Mis Ventas: "+dbLocal.getVentas().size();
                bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.ventas_guardadas);
                titulos.Tipo = 3;
                titulos.Img_Menu = bmLocal;
                listaTitulos.add(titulos);
            }
        }else
        {
            titulos = new clsMenus();
            titulos.Titulo = "Iniciar Ruta";
            titulos.Tipo = 2;
            bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.nada);
            titulos.Img_Menu = bmLocal;
            listaTitulos.add(titulos);
        }

    }*/


    @Override
    public void onItemClick(View view, int position) {

    }

    //Cuadros de dialogo Simples
    public void VolverCerrar(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Cerrar Sesión");
        dialogo1.setMessage("Al volver su sesión se cerrara ¿Esta seguro de salir?");
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                if(Configuraciones.getBoolean("RutaAct", false)) {
                    dialogo1.dismiss();
                    ErrorCerrarRuta();
                }else
                {
                    MainActivity.super.onBackPressed();
                    Intent inten = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(inten);
                    EditarConf.remove("Id_Usuario");
                    EditarConf.remove("Usuario");
                    EditarConf.remove("Id_Rol");
                    EditarConf.commit();
                    dbLocal.deleteConservadores_Auditar();
                    Toast.makeText(getApplicationContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
    public void ErrorCerrarRuta(){
        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);
        dialogo2.setTitle("Cerrar Ruta");
        dialogo2.setMessage("Para cerrar sesión es necesario cerrar la ruta");
        dialogo2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo2, int id) {
                dialogo2.dismiss();
            }
        });
        dialogo2.show();
    }
//    public void RutaMensaje(){
//        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
//        dialogo1.setTitle("Importante");
//        dialogo1.setMessage("¿Esta seguro de iniciar la ruta?");
//        dialogo1.setCancelable(false);
//        dialogo1.setPositiveButton("Iniciar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogo1, int id) {
//                EditarConf.putString("HoraIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//                EditarConf.putString("FechaIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                EditarConf.putString("FechaHoraIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
//                EditarConf.putBoolean("RutaAct", true);
//                EditarConf.commit();
//                CargarMenus();
//                titulos = new clsMenus();
//                titulos.Titulo = "Detener Ruta";
//                titulos.Tipo = 1;
//                titulos.Tiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//                titulos.Fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                listaTitulos.set(1, titulos);
//                adapter.notifyDataSetChanged();
//                getApplicationContext().startService(servicio);
//                lblMonitoreo.setVisibility(View.VISIBLE);
//                //llInfo.setVisibility(View.VISIBLE);
//            }
//        });
//        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogo1, int id) {
//                dialogo1.dismiss();
//            }
//        });
//        dialogo1.show();
//    }
    /*public void EstablecerRuta(){
        //panelOpciones = true;
        //fabRuta.setVisibility(View.GONE);
        //llOpciones.setForeground(null);
        //btnCerrar.setVisibility(View.VISIBLE);
        //llInfo.setVisibility(View.VISIBLE);
        //fabInfo.setVisibility(View.VISIBLE);
        titulos = new clsMenus();
        titulos.Titulo = "Detener Ruta";
        titulos.Tipo = 1;
        titulos.Tiempo = Configuraciones.getString("HoraIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        titulos.Fecha = Configuraciones.getString("FechaIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        listaTitulos.set(1, titulos);
        adapter.notifyItemChanged(1,listaTitulos);
        getApplicationContext().startService(servicio);
        lblMonitoreo.setVisibility(View.VISIBLE);
        //lblInfoRuta.setText(Hora);
    }*/

    //Cuadros de dialogo estructurados
    /*public AlertDialog DetenerRuta() {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        productos = new ArrayList<>();
        prodselfin = new clsProductos();
        SpinAdapterProductos adapterProductosMerm;
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.detener_ruta, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        Button btnAceptar = (Button)v.findViewById(R.id.btnAceptarMerma);
        Button btnCancelar = (Button) v.findViewById(R.id.btnCancelarMerma);
        btnAgregarEvidencia = (Button) v.findViewById(R.id.btnAg_Ev_merma);
        //Spinner spinProdMermas = (Spinner) v.findViewById(R.id.cmbProductosMermas);
        Button btnAgregarProdM = (Button) v.findViewById(R.id.btnAgregarProdMer);
        RecyclerView recProdsMerms = (RecyclerView) v.findViewById(R.id.recProductosMerms);

        //prodsArray.clear();
        prodsmermas = new ArrayList<clsProductos>();
        prodsmermas = dbLocal.getListasPreciosUnica(1);
        adapterProductosMerm = new SpinAdapterProductos(getApplicationContext(), R.layout.spinner_item, prodsmermas);
        adapterProductosMerm.setDropDownViewResource(R.layout.spinner_item_drop);
        //spinProdMermas.setAdapter(adapterProductosMerm);
        recProdsMerms.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

        builder.setView(v);
        builder.setCancelable(false);
        alertDialog = builder.create();
        *//*spinProdMermas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prodselfin = adapterProductosMerm.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*//*
        // Add action buttons
        btnAceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pbEnviar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        //panelOpciones = false;
                        //fabRuta.setVisibility(View.VISIBLE);
                        //Drawable fondogris =  ResourcesCompat.getDrawable(getResources(), android.R.drawable.editbox_dropdown_dark_frame, getTheme());
                        //llOpciones.setForeground(fondogris);
                        //btnCerrar.setVisibility(View.GONE);
                        //llInfo.setVisibility(View.GONE);
                        //fabInfo.setVisibility(View.GONE);
                        //if(dbLocal.getVentas().size()>0)
                            //EnviarVentas();
                        //else
                        //    Toast.makeText(getApplicationContext(),"No hay ventas para enviar",Toast.LENGTH_LONG).show();

                        alertDialog.dismiss();
                    }
                }
        );
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnAgregarProdM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarInformacion(recProdsMerms, alertDialog.getContext());

            }
        });
        btnAgregarEvidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                            int MY_PERMISSIONS_REQUEST_CAMARA = 10;
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA );
                            // MY_PERMISSIONS_REQUEST_CAMARA es una constante definida en la app. El método callback obtiene el resultado de la petición.
                        }
                    }else{ //have permissions
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i,1);
                    }
                }else{ // Pre-Marshmallow
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i,1);
                }
            }
        });
        return alertDialog;
    }*/

    /*private void escribir(String datos, int id_usu, int id_ruta) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        Date now = new Date();
        String fileName = id_usu +"-"+ formatter.format(now) +"_"+ id_ruta +".txt";//like 2016_01_12.txt
        try
        {
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"Coordenadas_Folder", "Coordenadas");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists())
            {
                root.mkdirs();
            }else
                root.delete();
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(datos+"\n\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }*/
    public AlertDialog MostrarVentasGuardadas(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.ventas_guardadas, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        Button btnCancelar = (Button) v.findViewById(R.id.btnCerrarVentasG);
        RecyclerView recVentas = (RecyclerView) v.findViewById(R.id.recVentasGuar);
        RecyclerView.LayoutManager layoutManager;
        ventasGArray = new ArrayList<clsVentasGuardadas>();
        ventasGArray = dbLocal.getVentasGuardadas();
//        for(int i = 1; i < 10; i++) {
//            clientes.add("Cliente #" + i);
//        }
        AdaptadorVentas = new Adapter_VentasG(getApplicationContext(), ventasGArray);
        layoutManager = new LinearLayoutManager(this);
        recVentas.setLayoutManager(layoutManager);
        recVentas.setAdapter(AdaptadorVentas);

        builder.setView(v);
        builder.setCancelable(false);
        alertDialog = builder.create();
        // Add action buttons
        btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                }
        );
        return alertDialog;
    }

    public void AgregarInformacion(RecyclerView recyclerView, Context mermas){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mermas);

        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.informacion_mermas, null);
        EditText txtPiezas = v.findViewById(R.id.txtCantMermas);
        //EditText txtMotivo = v.findViewById(R.id.txtMotivoMerma1);
        Button btnAceptar = v.findViewById(R.id.btnAceptarInfoMerma);
        builder.setView(v);
        alertDialog = builder.create();
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productoLista = new clsProductosMermas();
                productoLista.IdProducto = prodselfin.IdProducto;
                productoLista.Piezas = Integer.parseInt(txtPiezas.getText().toString());
                productoLista.Nombre_P = prodselfin.Nombre_P;
                productoLista.Precio_P = prodselfin.Precio_P;
                //productoLista.Motivo = txtMotivo.getText().toString();
                productos.add(productoLista);
                AdaptadorProductos = new Adapter_ProductosMermas(getApplication().getApplicationContext(), productos);
                recyclerView.setAdapter(AdaptadorProductos);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    public void MostrarAuditoria(int pos){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.info_auditoria, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        ImageView imgMostrarAu = (ImageView) v.findViewById(R.id.imgResumenAudV);
        //Definir controles
        EditText txtUbicacionAuditoria = (EditText) v.findViewById(R.id.txtUbicacionAudV);
        EditText txtQRAuditoria = (EditText) v.findViewById(R.id.txtQRAudV);
        EditText txtEtiqAuditoria = (EditText) v.findViewById(R.id.txtEtiqAudV);
        EditText txtSerieAuditoria = (EditText) v.findViewById(R.id.txtSerieAudV);

        builder.setView(v);
        alertDialog = builder.create();
        clsAuditoria Audi = dbLocal.getAuditorias().get(pos);
        //Metodos
        ArrayList<clsConservadores> conservadorSel = dbLocal.getConservadorID(Audi.getId_Conservador());
        if(conservadorSel.size()>0) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(Audi.getImagen_Ev());
            Bitmap bmLocal = BitmapFactory.decodeStream(imageStream);
            imgMostrarAu.setImageBitmap(bmLocal);
            if(Audi.getId_Cliente_Rev()!=0) {
                txtUbicacionAuditoria.setText(String.valueOf(Audi.getId_Cliente_Rev()));
                txtUbicacionAuditoria.setEnabled(false);
            }
            if(Audi.getQR()!=0) {
                txtQRAuditoria.setText(String.valueOf(Audi.getQR()));
                txtQRAuditoria.setEnabled(false);
            }
            if(!Audi.getEtiqueta().isEmpty()&&!Audi.getEtiqueta().equals("0")) {
                txtEtiqAuditoria.setText(Audi.getEtiqueta());
                txtEtiqAuditoria.setEnabled(false);
            }
            if(!Audi.getNSerie().equals("")) {
                txtSerieAuditoria.setText(Audi.getNSerie());
                txtSerieAuditoria.setEnabled(false);
            }
        }

        alertDialog.show();
    }
    public void Proceso_Auditoria(clsAuditoria Audi, int pos) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.auditoria_conservador, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        latitud = 0;
        longitud = 0;

        ArrayList<clsConservadores> conservadorSel;
        LinearLayout llCL = (LinearLayout) v.findViewById(R.id.llCheckListAuditoria);
        LinearLayout llUbi = (LinearLayout) v.findViewById(R.id.llMapaAuditoria);
        llFotoAud = (LinearLayout) v.findViewById(R.id.llFotoConsAuditoria);
        llResumenAud = (LinearLayout) v.findViewById(R.id.llResumenAuditoria);

        TextView lblInfo = (TextView) v.findViewById(R.id.lblMostrarInfoCliente);

        EditText txtUbicacionAuditoria = (EditText) v.findViewById(R.id.txtUbicacionAud);
        EditText txtQRAuditoria = (EditText) v.findViewById(R.id.txtQRAud);
        EditText txtEtiqAuditoria = (EditText) v.findViewById(R.id.txtEtiqAud);
        EditText txtSerieAuditoria = (EditText) v.findViewById(R.id.txtSerieAud);

        EditText txtObservaciones = (EditText) v.findViewById(R.id.txtInfoCL);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.actxtCliente);
        Adaptador_Clientes adapter = new Adaptador_Clientes(this, R.layout.activity_main, R.id.lbl_name, dbLocal.getClientes());
        autoCompleteTextView.setAdapter(adapter);
        ProgressBar pbEnviar2 = (ProgressBar) v.findViewById(R.id.pbEnviarTodo2);

        Button btnCancelar = (Button) v.findViewById(R.id.btnCancelarAud);
        Button btnUbicacion = (Button) v.findViewById(R.id.btnUbicacionAud);
        Button btnFoto = (Button) v.findViewById(R.id.btnFotoAud);
        Button btnFinalizar = (Button) v.findViewById(R.id.btnFinAuditoria);
        Button btnConfirmarCL = (Button) v.findViewById(R.id.btnConfCL);


        //Botones de lo importado
        Button btnCancUbi = (Button) v.findViewById(R.id.btnCancelarUbi);
        Button btnAcepUBi = (Button) v.findViewById(R.id.btnConfirmarUbi);

        imgFoto = (ImageView) v.findViewById(R.id.imgResumenAud);

        RadioButton rbSi = (RadioButton) v.findViewById(R.id.rbSiAud);
        RadioButton rbNo = (RadioButton) v.findViewById(R.id.rbNoAud);

        builder.setView(v);
        builder.setCancelable(false);
        alertDialog = builder.create();

        id_cliente_aud = 0;
        //Traemos el conservador de la tabla de conservadores
        conservadorSel = dbLocal.getConservadorID(Audi.getId_Conservador());


        //Los quitamos, ya que no nos sirve para esta vista
        btnCancUbi.setVisibility(View.GONE);
        btnAcepUBi.setVisibility(View.GONE);
        lblInfo.setVisibility(View.GONE);
        if(conservadorSel.size()>0){
            if(conservadorSel.get(0).getId_Cliente()!=0){
                autoCompleteTextView.setText(String.valueOf(dbLocal.getNombreCliente(conservadorSel.get(0).getId_Cliente())));
                Toast.makeText(getApplicationContext(), String.valueOf(conservadorSel.get(0).getId_Cliente()), Toast.LENGTH_SHORT).show();
                ArrayList<clsClientes> cliente = dbLocal.getClienteId(conservadorSel.get(0).getId_Cliente());

                if(cliente.size()>0) {
                    if(cliente.get(0).getLatitud() != null && cliente.get(0).getLongitud() != null) {
                        latitud = Double.parseDouble(cliente.get(0).getLatitud());
                        longitud = Double.parseDouble(cliente.get(0).getLongitud());
                    }
                }
            }
        }
        MapView mV = v.findViewById(R.id.map);
        mV.onCreate(alertDialog.onSaveInstanceState());
        mV.onResume();
        mV.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(latitud == 0 && longitud == 0) {
                    latitud = loc.getLatitude();
                    longitud = loc.getLongitude();
                }

                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        if (mCenterMarker != null) {
                            googleMap.clear();
                        }
                        latitud = googleMap.getCameraPosition().target.latitude;
                        longitud = googleMap.getCameraPosition().target.longitude;
                        MarcadorGlobal = googleMap.getCameraPosition();
                        mCenterMarkerOptions = new MarkerOptions()
                                .position(googleMap.getCameraPosition().target)
                                .title("Ubicación del Cliente");
                        mCenterMarker = googleMap.addMarker(mCenterMarkerOptions);
                    }

                });

                locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng ubicacion = new LatLng(latitud, longitud);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(ubicacion)
                        .zoom(15)
                        .build();

                //googleMap.setMyLocationEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mCenterMarkerOptions = new MarkerOptions()
                        .position(ubicacion)
                        .title("Ubicación Cliente");
                googleMap.addMarker(mCenterMarkerOptions);

                try {
                    MapsInitializer.initialize(getApplicationContext());
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });

        // Add action buttons
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llUbi.setVisibility(View.GONE);
                llFotoAud.setVisibility(View.VISIBLE);

                //Busca al cliente, si esta, guarda su id, si no crea una alerta para realizar registro o busqueda
            }
        });
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Evidencia");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);

                if(conservadorSel.size()>0) {
                    if(!autoCompleteTextView.getText().toString().isEmpty()) {
                        txtUbicacionAuditoria.setText(autoCompleteTextView.getText().toString());
                        txtUbicacionAuditoria.setEnabled(false);
                    }
                    if(conservadorSel.get(0).getQR()!=0) {
                        txtQRAuditoria.setText(String.valueOf(conservadorSel.get(0).getQR()));
                        txtQRAuditoria.setEnabled(false);
                    }
                    if(!conservadorSel.get(0).getEtiqueta_Interna().isEmpty()&&!conservadorSel.get(0).getEtiqueta_Interna().equals("0")) {
                        if(!conservadorSel.get(0).getEtiqueta_Interna().split("-")[2].equals("??")) {
                            txtEtiqAuditoria.setText(conservadorSel.get(0).getEtiqueta_Interna().split("-")[2]);
                            txtEtiqAuditoria.setEnabled(false);
                        }
                    }
                    if(!conservadorSel.get(0).getNo_Serie().equals("")) {
                        txtSerieAuditoria.setText(conservadorSel.get(0).getNo_Serie());
                        txtSerieAuditoria.setEnabled(false);
                    }
                }
            }
        });
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbEnviar2.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                //Enviamos
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected)
                {
                    clsAuditoria aud = new clsAuditoria();
                    try {
                        aud.Id_ConservadorA = auditorias.get(pos).Id_ConservadorA;
                        aud.Id_Cliente = id_cliente_aud;
                        //aud.Imagen_Rev = fotoSubir;
                        if(!txtQRAuditoria.getText().toString().isEmpty())
                            aud.QR = Integer.parseInt(txtQRAuditoria.getText().toString());
                        if(!txtEtiqAuditoria.getText().toString().isEmpty())
                            aud.Etiqueta = txtEtiqAuditoria.getText().toString();
                        if(!txtSerieAuditoria.getText().toString().isEmpty())
                            aud.Serie = txtSerieAuditoria.getText().toString();
                        aud.Latitud = String.valueOf(latitud);
                        aud.Longitud = String.valueOf(longitud);
                        aud.Id_CheckList = 1;
                        aud.Auditado = 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String auditoria = Gson_Converter.toJson(aud);
                    Toast.makeText(getApplicationContext(), auditoria, Toast.LENGTH_SHORT).show();
                    String url = Global.url+"paginas/ventas/controladores/Ope_Ventas.asmx/EditarAuditoriaConservador";

                    //objeto hasmap que contendrá los parametros de busqueda con clave string,string
                    Map<String, String> parametros = new HashMap();
                    parametros.put("Auditoria",auditoria);
                    JSONObject data = new JSONObject(parametros);
                    System.out.println(auditoria);
                    System.out.println(url);
                    //creamos el listener para mandar la paticion post al controlador
                    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                            new com.android.volley.Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String respuesta = response.getString("d");

                                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                                        Cls_Mensaje resultado = gson.fromJson(respuesta, Cls_Mensaje.class);

                                        if(resultado.getEstatus().equals("success")){
                                            Toast.makeText(getApplicationContext(), "La auditoria ha sido enviada y guardada correctamente", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Auditoria guardada: " + resultado.getMensaje()+"\\Intente de nuevo",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        try {
                                            dbLocal.UpdateAuditoria(auditorias.get(pos).getId_ConservadorA(), 2, String.valueOf(latitud),
                                                    String.valueOf(longitud), id_cliente_aud, fotoSubir,
                                                    Integer.parseInt(txtQRAuditoria.getText().toString()),txtEtiqAuditoria.getText().toString(),
                                                    txtSerieAuditoria.getText().toString(), Configuraciones.getInt("Id_Usuario",0), 1, 1);
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                        auditorias.get(pos).Auditado = 1;
                                        AdaptadorConservadores.notifyItemChanged(pos, auditorias);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        pbEnviar2.setVisibility(View.INVISIBLE);
                                        alertDialog.dismiss();

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "guardarVenta[onResponse] " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            },
                            new com.android.volley.Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(getApplicationContext(), "error [guardarVenta]: " + error.networkResponse.toString(), Toast.LENGTH_LONG).show();
                                    pbEnviar2.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "error [guardarVenta]: " + parseNetworkError(error).toString(), Toast.LENGTH_LONG).show();
                                }
                                protected VolleyError parseNetworkError(VolleyError volleyError){
                                    if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                                        VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                                        volleyError = error;
                                    }
                                    pbEnviar2.setVisibility(View.INVISIBLE);

                                    return volleyError;
                                }
                            }
                    );
                    queue.add(getRequest);
                }else{
                    auditorias.get(pos).Auditado = 1;
                    AdaptadorConservadores.notifyItemChanged(pos, auditorias);
                    try {
                        dbLocal.UpdateAuditoria(auditorias.get(pos).getId_ConservadorA(), 2, String.valueOf(latitud),
                                String.valueOf(longitud), id_cliente_aud, fotoSubir,Integer.parseInt(txtQRAuditoria.getText().toString()),txtEtiqAuditoria.getText().toString(),
                                txtSerieAuditoria.getText().toString(), Configuraciones.getInt("Id_Usuario",0), 1, 0);
                        alertDialog.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                //Comprueba internet, si hay en automatico envia auditoria

                //Si no hay el dato se guarda localmente

            }
        });
        btnConfirmarCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCL.setVisibility(View.GONE);
                llUbi.setVisibility(View.VISIBLE);
                autoCompleteTextView.requestFocus();
            }
        });
        rbSi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txtObservaciones.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txtObservaciones.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<clsClientes> cliente = dbLocal.getClienteId(adapter.getItem(position).getId_Cliente());
                if(cliente.size()>0) {
                    id_cliente_aud = cliente.get(0).getId_Cliente();
                    Toast.makeText(getApplicationContext(), "Reubique el cliente ingresado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        pbEnviar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        alertDialog.show();
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                fotoSubir = stream.toByteArray();

                imgFoto.setImageBitmap(thumbnail);
                //Obtiene la ruta donde se encuentra guardada la imagen.
                imageurl = getRealPathFromURI(imageUri);
                llFotoAud.setVisibility(View.GONE);
                llResumenAud.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else{
            //Bundle extras = data.getExtras();
            //Toast.makeText(getApplicationContext(), extras.get("data").toString(), Toast.LENGTH_SHORT).show();
            //btnAgregarEvidencia.setCompoundDrawableTintList(ColorStateList.valueOf(Color.GREEN));
        }
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 512;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onBackPressed() {
        VolverCerrar();
    }


    private void solicitarPermisos() {

        if (Build.VERSION.SDK_INT <= 26)
            return;

        try {

            boolean aceptado = false;

            while(!aceptado) {

                int gps_network = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                int gps_hardware = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
                int estado_internet = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
                int leer_archivos = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                int escribir_archivos = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int camara = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

                if (gps_network != PackageManager.PERMISSION_GRANTED ||
                        gps_hardware != PackageManager.PERMISSION_GRANTED ||
                        internet != PackageManager.PERMISSION_GRANTED ||
                        estado_internet != PackageManager.PERMISSION_GRANTED ||
                        leer_archivos != PackageManager.PERMISSION_GRANTED ||
                        escribir_archivos != PackageManager.PERMISSION_GRANTED ||
                        camara != PackageManager.PERMISSION_GRANTED) {
                    //
                    //MandarAviso(this);
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    }, 250);
                }else{

                    aceptado=true;
                }

            }



        }catch (NoSuchMethodError ex){
            Log.e("error", "solicitarPermisos: " + ex.getMessage());
        }

    }

    public void MandarAviso(Activity act){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(act);
        dialogo1.setTitle("Permisos");
        dialogo1.setMessage("Para que la aplicación funcione correctamente\r\nEs necesario asignar algunos permisos ¿Continuar?");
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                ActivityCompat.requestPermissions(act, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, 250);
                dialogo1.dismiss();
            }
        });
        dialogo1.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(getApplicationContext(), "Es necesario activar los permisos", Toast.LENGTH_SHORT).show();
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
}