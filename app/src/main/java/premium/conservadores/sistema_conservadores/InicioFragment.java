package premium.conservadores.sistema_conservadores;

import android.Manifest;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import premium.conservadores.sistema_conservadores.Adaptadores.SpinAdapterMotivos;
import premium.conservadores.sistema_conservadores.Adaptadores.SpinAdapterProductos;
import premium.conservadores.sistema_conservadores.Clases.Cls_Mensaje;
import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.Clases.clsGastos;
import premium.conservadores.sistema_conservadores.Clases.clsMenus;
import premium.conservadores.sistema_conservadores.Clases.clsMotivos;
import premium.conservadores.sistema_conservadores.Clases.cls_CierreRuta;
import premium.conservadores.sistema_conservadores.ClasesBD.clsCierreRuta;
import premium.conservadores.sistema_conservadores.ClasesBD.clsCoordenadas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsDetMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsDetVentas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductosMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsVentas;

public class InicioFragment extends Fragment {
    Adapter_Menus adapter;
    ArrayList<clsMenus> listaTitulos;

    clsMenus titulos;
    View v;
    SharedPreferences Configuraciones;
    SharedPreferences.Editor EditarConf;
    Adapter_ProductosMermas AdaptadorProductos;
    ArrayList<clsProductos> prodsmermas;
    clsProductos prodselfin;
    clsProductosMermas productoLista;
    ArrayList<clsProductosMermas> productos;
    Intent servicio;
    Button btnCerrar, btnAgregarEvidencia;
    ProgressBar pbEnviar;
    Gson Gson_Converter = new Gson();//objeto que permite convertir cadenas JSON en objetos
    ArrayList<clsProductos> prodsArray;
    TextView lblInfoRuta;
    SQLocal dbLocal;
    private RequestQueue queue; //objeto sobre el cual se mandan las peticiones post
    NetworkInfo activeNetwork;
    ConnectivityManager cm;
    TabLayout tabhost;
    Button btnGastos, btnMermas;
    private ConfiguracionFragment fragmentConfiguracion;

    public InicioFragment() {
    }

    public static InicioFragment newInstance(Integer counter) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putInt("param1", counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inicio, container, false);
        listaTitulos = new ArrayList<clsMenus>();
        //listaGastos = new ArrayList<clsGastos>();
        dbLocal = new SQLocal(getContext());
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();

        Configuraciones = PreferenceManager.getDefaultSharedPreferences(getContext());
        EditarConf = Configuraciones.edit();
        servicio = new Intent(getContext(), BackgroundService.class);
        pbEnviar = (ProgressBar) v.findViewById(R.id.pbEnviarTodo);
        RecyclerView recyclerView = v.findViewById(R.id.recTitulos);
        tabhost = (TabLayout) getActivity().findViewById(R.id.tabInicio);
        lblInfoRuta = (TextView) getActivity().findViewById(R.id.txtMonitoreo);
        Global.handleSSLHandshake();
        queue = Volley.newRequestQueue(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new Adapter_Menus(getContext(), listaTitulos, getActivity());
        adapter.setClickListener((Adapter_Menus.ItemClickListener) getContext());
        recyclerView.setAdapter(adapter);
        CargarMenus();


        if (Configuraciones.getBoolean("RutaAct", false))
            EstablecerRuta();
        adapter.setClickListener(new Adapter_Menus.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (listaTitulos.size() > 1) {
                    if (position == 0) {
                        Intent Reg = new Intent(getContext(), VentaCliente.class);
                        startActivityForResult(Reg, 0);
                    } else if (position == 1) {
                        //Toast.makeText(getApplicationContext(), String.valueOf(Configuraciones.getBoolean("RutaAct", false)), Toast.LENGTH_SHORT).show();
                        if (Configuraciones.getBoolean("RutaAct", false)) {
                            DetenerRuta().show();
                        } else {
                            RutaMensaje();
                        }

                    } else if (listaTitulos.get(position).getTipo() == 3) {
                        Intent ventas = new Intent(getContext(), VentasActivity.class);
                        startActivity(ventas);
                    }
                } else {
                    if (Configuraciones.getBoolean("RutaAct", false)) {
                        DetenerRuta().show();
                    } else {
                        RutaMensaje();
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void MostrarGastos() {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        //View v = inflater.inflate(R.layout.gastos, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        /*Spinner spRubros = v.findViewById(R.id.spRubrosG);
        EditText txtTotal = v.findViewById(R.id.txtTotalG);
        Button btnAgregarG = v.findViewById(R.id.btnAgregarGastos);
        RecyclerView rvGastos = (RecyclerView) v.findViewById(R.id.rvGastosO);*/

        /*rvGastos.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, new String[]{"Comida", "Gasolina",
                "Mecanico", "Hospedaje"});*/
        /*adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listaGastos = new ArrayList<clsGastos>();
        listaGastos = dbLocal.getGastos();

        rvGastos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterGastos = new Adapter_Gastos(getActivity().getApplication().getApplicationContext(), listaGastos);
        rvGastos.setAdapter(adapterGastos);

        builder.setView(v);
        alertDialog = builder.create();
        spRubros.setAdapter(adapter);

        spRubros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtTotal.requestFocus();
                rubro = spRubros.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAgregarG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gasto = new clsGastos();
                gasto.Rubro = rubro;
                gasto.Total = Double.parseDouble(txtTotal.getText().toString());
                gasto.Fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                listaGastos.add(gasto);
                adapterGastos.notifyDataSetChanged();
                rvGastos.setAdapter(adapterGastos);

                dbLocal.InsertGastos(rubro, Double.parseDouble(txtTotal.getText().toString()),
                        Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));

            }
        });*/

        //alertDialog.show();
    }

    public void IniciarRutaAutomatico(){
        EditarConf.putString("HoraIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        EditarConf.putString("FechaIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        EditarConf.putString("FechaHoraIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        EditarConf.putBoolean("RutaAct", true);
        EditarConf.commit();
        CargarMenus();
        titulos = new clsMenus();
        titulos.Titulo = "Detener Ruta";
        lblInfoRuta.setVisibility(View.VISIBLE);
        titulos.Tipo = 1;
        titulos.Tiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        titulos.Fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        listaTitulos.set(1, titulos);
        adapter.notifyDataSetChanged();
        getContext().startService(servicio);
        Toast.makeText(getContext(), "Ruta Iniciada Correctamente", Toast.LENGTH_SHORT).show();
    }
    public void CargarMenus(){
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

            /*itulos = new clsMenus();
            titulos.Titulo = "Gastos Operativos";
            titulos.Tipo = 0;
            bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.gastos);
            titulos.Img_Menu = bmLocal;
            listaTitulos.add(titulos);*/

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
    }
    public void RutaMensaje(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Esta seguro de iniciar la ruta?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Iniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                tabhost.getTabAt(1).select();
                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(getContext());
                dialogo2.setTitle("Leer");
                dialogo2.setMessage("Para iniciar la ruta es necesario configurarla ¿Continuar?");
                dialogo2.setCancelable(false);
                dialogo2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo2, int which) {
                        /*EditarConf.putString("HoraIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        EditarConf.putString("FechaIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        EditarConf.putString("FechaHoraIni", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                        EditarConf.putBoolean("RutaAct", true);
                        EditarConf.commit();
                        CargarMenus();
                        titulos = new clsMenus();
                        titulos.Titulo = "Detener Ruta";
                        titulos.Tipo = 1;
                        titulos.Tiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        titulos.Fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        listaTitulos.set(1, titulos);
                        adapter.notifyDataSetChanged();
                        getContext().startService(servicio);*/
                        dialogo2.dismiss();
                    }
                });
                dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo2, int which) {
                        tabhost.getTabAt(0).select();
                        dialogo2.dismiss();
                    }
                });
                dialogo2.show();

                //lblMonitoreo.setVisibility(View.VISIBLE);
                //llInfo.setVisibility(View.VISIBLE);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
    public void EstablecerRuta(){
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
        getContext().startService(servicio);
        lblInfoRuta.setVisibility(View.VISIBLE);
        //lblMonitoreo.setVisibility(View.VISIBLE);
        //lblInfoRuta.setText(Hora);
    }
    public AlertDialog DetenerRuta() {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        btnGastos = (Button) v.findViewById(R.id.lblGastosOpC);
        btnMermas = (Button) v.findViewById(R.id.btnAgregarMermas);

        TextView lblTotalProdS = (TextView) v.findViewById(R.id.lblTotalProSolC);
        TextView lblContado = (TextView) v.findViewById(R.id.lblContadoC);
        TextView lblCredito = (TextView) v.findViewById(R.id.lblCreditoC);
        TextView lblTotalCi = (TextView) v.findViewById(R.id.lblTotalC);
        EditText txtTotalE = (EditText) v.findViewById(R.id.txtTotalEfeC);
        TextView lblTotalV = (TextView) v.findViewById(R.id.lblTotalVenC);

        //btnAgregarEvidencia = (Button) v.findViewById(R.id.btnAg_Ev_merma);
        //Spinner spinProdMermas = (Spinner) v.findViewById(R.id.cmbProductosMermas);

//        AutoCompleteTextView spProductos = (AutoCompleteTextView) v.findViewById(R.id.atxtProductosC);
//
//
//        spProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                prodselfin = prodsArray.get(position);
//            }
//        });
        //Button btnAgregarProdM = (Button) v.findViewById(R.id.btnAgregarProdMer);
        //RecyclerView recProdsMerms = (RecyclerView) v.findViewById(R.id.recProductosMerms);
        //prodsArray.clear();
        //prodsmermas = new ArrayList<clsProductos>();
        //prodsmermas = dbLocal.getListasPreciosUnica(1);
        //adapterProductosMerm = new SpinAdapterProductos(getContext(), R.layout.spinner_item, prodsmermas);
        //adapterProductosMerm.setDropDownViewResource(R.layout.spinner_item_drop);
        //spinProdMermas.setAdapter(adapterProductosMerm);
        //recProdsMerms.setLayoutManager(new GridLayoutManager(getContext(),1));

        builder.setView(v);
        builder.setCancelable(false);
        alertDialog = builder.create();
        txtTotalE.requestFocus();

        btnGastos.setText("$"+ dbLocal.getTotalGastos());
        lblTotalProdS.setText(String.valueOf(dbLocal.getPiezasRuta()));
        lblContado.setText(String.valueOf(dbLocal.getTotalConRuta()));
        lblCredito.setText(String.valueOf(dbLocal.getTotalCreRuta()));
        lblTotalCi.setText(String.valueOf(dbLocal.getTotalRuta()));
        lblTotalV.setText(String.valueOf(dbLocal.getTotalVentas()));

        /*prodsArray = new ArrayList<clsProductos>();
        prodsArray = dbLocal.getProductos();
        Adaptador_Productos_Combo adapter = new Adaptador_Productos_Combo(getContext(), R.layout.detener_ruta,
                R.id.lbl_name, prodsArray);
        spProductos.setAdapter(adapter);*/
        /*spinProdMermas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prodselfin = adapterProductosMerm.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        // Add action buttons
        btnGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Gastos = new Intent(getContext(), GastosActivity.class);
                startActivityForResult(intent_Gastos,2);
            }
        });
        btnMermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_mermas = new Intent(getContext(), MermasActivity.class);
                startActivityForResult(intent_mermas,3);
            }
        });
        btnAceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Preguntar si esta seguro
                        if(!txtTotalE.getText().toString().isEmpty()) {
                            tabhost.getTabAt(1).select();
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                            dialogo1.setTitle("Importante");
                            dialogo1.setMessage("¿Esta seguro de cerrar la ruta?");
                            dialogo1.setCancelable(false);
                            dialogo1.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    tabhost.getTabAt(0).select();
                                    fragmentConfiguracion = (ConfiguracionFragment) getActivity().getSupportFragmentManager().getFragments().get(1);
                                    lblInfoRuta.setVisibility(View.GONE);
                                    //Guardar las mermas
                                    int totalP = 0;
                                    double total = 0;
                                    for (int i = 0; i < productos.size(); i++) {
                                        totalP += productos.get(i).Piezas;
                                        total += (productos.get(i).Precio_P * productos.get(i).Piezas);
                                    }
                                    dbLocal.InsertMermas(totalP, total, null,
                                            Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))), 0,
                                            Configuraciones.getInt("Id_Usuario", 0));
                                    int IdMer = dbLocal.contarMermas();
                                    for (int i = 0; i < productos.size(); i++) {
                                        dbLocal.InsertDetMermas(IdMer, productos.get(i).IdProducto, productos.get(i).Precio_P, productos.get(i).Piezas,
                                                productos.get(i).Piezas * productos.get(i).Precio_P, productos.get(i).getMotivo());
                                    }
                                    //GUarda cierre local primero
                                    dbLocal.InsertCierreRuta(Configuraciones.getInt("Id_Usuario", 0),
                                            Configuraciones.getString("HoraIni", ""), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                            Configuraciones.getString("Tiempo", "00 00:00"), dbLocal.getTotalGastos(),
                                            dbLocal.getTotalRuta(), dbLocal.getTotalConRuta(), dbLocal.getTotalCreRuta(),
                                            Double.parseDouble(txtTotalE.getText().toString()), dbLocal.getPiezasRuta(), "", "",
                                            "", "", 0, 0, Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))), 0);
                                    boolean isConnected = activeNetwork != null &&
                                            activeNetwork.isConnectedOrConnecting();

                                    fragmentConfiguracion.LimpiarDatosRuta();
                                    if (isConnected) {
                                        pbEnviar.setVisibility(View.VISIBLE);
                                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        //panelOpciones = false;
                                        //fabRuta.setVisibility(View.VISIBLE);
                                        //Drawable fondogris =  ResourcesCompat.getDrawable(getResources(), android.R.drawable.editbox_dropdown_dark_frame, getTheme());
                                        //llOpciones.setForeground(fondogris);
                                        //btnCerrar.setVisibility(View.GONE);
                                        //llInfo.setVisibility(View.GONE);
                                        //fabInfo.setVisibility(View.GONE);
                                        //if(dbLocal.getVentas().size()>0)
                                        EnviarVentas();
                                        //else
                                        //    Toast.makeText(getApplicationContext(),"No hay ventas para enviar",Toast.LENGTH_LONG).show();

                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Es necesario contar con internet estable para enviar el cierre", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    tabhost.getTabAt(0).select();
                                    dialogo1.dismiss();
                                }
                            });
                            dialogo1.show();
                        }
                    }
                }
        );
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        /*btnAgregarProdM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prodselfin!=null){
                    AgregarInformacion(recProdsMerms, alertDialog.getContext());
                    spProductos.setText("");
                }else{
                    Toast.makeText(getContext(),"Es necesario elegir un producto de la lista de productos",Toast.LENGTH_LONG).show();
                }
            }
        });*/
        /*btnAgregarEvidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                            int MY_PERMISSIONS_REQUEST_CAMARA = 10;
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA );
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
        });*/
        return alertDialog;
    }
    public void EnviarVentas() {
        clsVentas[] ven = dbLocal.EnviarVentas();
        clsDetVentas[] detv = dbLocal.EnviarDetVentas();
        clsCoordenadas[] coor = dbLocal.EnviarCoordenadas();
        clsCierreRuta obj_Cierre = dbLocal.EnviarCierre();
        clsMermas merm = dbLocal.EnviarMermas();
        clsDetMermas[] detmerm = dbLocal.EnviarDetMermas();

        String lista_ventas = Gson_Converter.toJson(ven);
        String lista_detventas = Gson_Converter.toJson(detv);
        String lista_coordenadas = Gson_Converter.toJson(coor);
        String lista_detmermas = Gson_Converter.toJson(detmerm);

        //Traemos los datos de cierre de nuestra ruta
        /*obj_Cierre.Id_Usuario = 1;
        obj_Cierre.Fecha_Inicio = Configuraciones.getString("FechaHoraIni",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        obj_Cierre.Fecha_Final = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String tiempo = Configuraciones.getString("Tiempo","00:00");
        obj_Cierre.Tiempo = tiempo;
        Toast.makeText(getContext(),tiempo.toString(), Toast.LENGTH_SHORT).show();
        obj_Cierre.Monto = dbLocal.getTotalRuta();
        obj_Cierre.Piezas = dbLocal.getPiezasRuta();
        obj_Cierre.Latitud_Inicio = "";
        obj_Cierre.Longitud_Inicio = "";
        obj_Cierre.Latitud_Fin = "";
        obj_Cierre.Longitud_Fin = "";*/

        //Toast.makeText(getApplicationContext(),String.valueOf(Gson_Converter.toJson(obj_Cierre)),Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),String.valueOf(lista_detventas),Toast.LENGTH_LONG).show();
        System.out.println(lista_ventas);
        System.out.println(lista_detventas);
        System.out.println(Gson_Converter.toJson(obj_Cierre));
        System.out.println(lista_coordenadas);
        System.out.println(Gson_Converter.toJson(merm));
        System.out.println(lista_detmermas);

        String url = Global.url+"paginas/ventas/controladores/Ope_Ventas.asmx/GuardarVenta";

        //objeto hasmap que contendrá los parametros de busqueda con clave string,string
        Map<String, String> parametros = new HashMap();
        parametros.put("Venta",lista_ventas);
        parametros.put("DetVenta",lista_detventas);
        parametros.put("Ruta", Gson_Converter.toJson(obj_Cierre));
        //Exportar coordenadas

        parametros.put("Coordenadas", lista_coordenadas);
        parametros.put("Mermas", Gson_Converter.toJson(merm));
        parametros.put("DetMermas", lista_detmermas);

        JSONObject data = new JSONObject(parametros);

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
                                Toast.makeText(getContext(), "Las ventas han sido enviadas correctamente", Toast.LENGTH_LONG).show();
                                dbLocal.deleteVentas();
                                dbLocal.deleteDetVentas();
                                dbLocal.deleteCoordenadas();
                                dbLocal.deleteMermas();
                                dbLocal.deleteDetVentas();
                                dbLocal.deleteCierreRutas();
                                ConfirmarCierre(true,0);
                            }else{
                                Toast.makeText(getContext(), "Ha ocurrido un error al enviar la venta: " + resultado.getMensaje()+"\\Intente de nuevo",
                                        Toast.LENGTH_SHORT).show();
                                ConfirmarCierre(false,obj_Cierre.Id_Ruta);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "guardarVenta[onResponse] " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            ConfirmarCierre(false,obj_Cierre.Id_Ruta);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "error [guardarVenta]: " + error.networkResponse.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "error [guardarVenta]: " + parseNetworkError(error).toString(), Toast.LENGTH_LONG).show();
                        ConfirmarCierre(false,obj_Cierre.Id_Ruta);
                    }
                    protected VolleyError parseNetworkError(VolleyError volleyError){
                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                        }
                        ConfirmarCierre(false,obj_Cierre.Id_Ruta);
                        return volleyError;
                    }
                }
        );
        queue.add(getRequest);
    }

    public void ConfirmarCierre(boolean envio, int id_cierre) {
        pbEnviar.setVisibility(View.INVISIBLE);
        EditarConf.putBoolean("RutaAct", false);
        EditarConf.commit();
        getActivity().stopService(servicio);
        CargarMenus();
        //lblMonitoreo.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(!envio){
            dbLocal.UpdateCierre(id_cierre);
        }
    }
    public void AgregarInformacion(RecyclerView recyclerView, Context mermas){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mermas);

        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.informacion_mermas, null);
        EditText txtPiezas = v.findViewById(R.id.txtCantMermas);
        Spinner txtMotivo = v.findViewById(R.id.txtMotivoMerma);
        Button btnAceptar = v.findViewById(R.id.btnAceptarInfoMerma);

        clsMotivos motivoSelec = new clsMotivos();
        builder.setView(v);
        alertDialog = builder.create();
        txtPiezas.requestFocus();
        ArrayList<clsMotivos> motivosList = new ArrayList<clsMotivos>();
        clsMotivos mot = new clsMotivos();
        for(int i = 1;i <= 5; i++) {
            mot.Id_Motivo = i;
            mot.Motivo = "Motivo "+String.valueOf(i);
            mot.Descripcion = "";
            motivosList.add(mot);
        }
        SpinAdapterMotivos adapterProductosMerm = new SpinAdapterMotivos(getContext(), R.layout.spinner_item_motivos, motivosList);
        adapterProductosMerm.setDropDownViewResource(R.layout.spinner_item_drop);
        txtMotivo.setAdapter(adapterProductosMerm);

        txtMotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                motivoSelec.Motivo = adapterProductosMerm.getItem(position).Motivo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productoLista = new clsProductosMermas();
                productoLista.IdProducto = prodselfin.IdProducto;
                productoLista.Piezas = Integer.parseInt(txtPiezas.getText().toString());
                productoLista.Nombre_P = prodselfin.Nombre_P;
                productoLista.Precio_P = prodselfin.Precio_P;
                productoLista.Motivo = motivoSelec.Motivo;
                productos.add(productoLista);
                AdaptadorProductos = new Adapter_ProductosMermas(getActivity().getApplication().getApplicationContext(), productos);
                recyclerView.setAdapter(AdaptadorProductos);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0) {
            if(dbLocal.getVentas().size() > 0) {
                boolean existe = false;
                for(int i = 0; i < listaTitulos.size();i++){
                    if(listaTitulos.get(i).getTipo()==3)
                        existe = true;
                }
                if(existe) {
                    listaTitulos.get(listaTitulos.size() - 1).Titulo = "Mis Ventas: " + dbLocal.getVentas().size();
                    Bitmap bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.ventas_guardadas);
                    titulos.Tipo = 3;
                    titulos.Img_Menu = bmLocal;
                    adapter.notifyItemChanged(listaTitulos.size()-1);
                }else{
                    titulos.Titulo = "Mis Ventas: " + dbLocal.getVentas().size();
                    Bitmap bmLocal = BitmapFactory.decodeResource(getResources(), R.drawable.ventas_guardadas);
                    titulos.Tipo = 3;
                    titulos.Img_Menu = bmLocal;
                    listaTitulos.add(titulos);
                    adapter.notifyDataSetChanged();
                }
            }
        }else if(requestCode == 2){
            btnGastos.setText("$"+ dbLocal.getTotalGastos());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}