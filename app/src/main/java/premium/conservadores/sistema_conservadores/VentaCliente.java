package premium.conservadores.sistema_conservadores;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.activity.CaptureActivity;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import premium.conservadores.sistema_conservadores.Adaptadores.Adaptador_Clientes_Combo;
import premium.conservadores.sistema_conservadores.Adaptadores.Adaptador_Productos_Combo;
import premium.conservadores.sistema_conservadores.Adaptadores.Adapter_Clientes;
import premium.conservadores.sistema_conservadores.Adaptadores.SpinAdapterProductos;
import premium.conservadores.sistema_conservadores.Clases.Cls_Mensaje;
import premium.conservadores.sistema_conservadores.Clases.RecyclerItemClickListener;
import premium.conservadores.sistema_conservadores.Clases.SimpleDividerItemDecoration;
import premium.conservadores.sistema_conservadores.ClasesBD.clsClientes;
import premium.conservadores.sistema_conservadores.ClasesBD.clsConservadores;
import premium.conservadores.sistema_conservadores.ClasesBD.clsDetVentas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;
import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.ClasesBD.clsVentas;
import premium.conservadores.sistema_conservadores.library.src.main.java.com.github.techisfun.android.topsheet.TopSheetDialog;

public class VentaCliente extends AppCompatActivity {

    private RequestQueue queue; //objeto sobre el cual se mandan las peticiones post
    public static final int MY_DEFAULT_TIMEOUT = 15000;

    int contador = 0;
    int tiempomins = 0;
    int intervalo = 0;
    clsClientes clienteSel = new clsClientes();
    int piezastot = 0;
    int tipo_pago = 0;
    int proceso = 0;
    int codigo_qr = 0;
    int codigo_qr_cliente = 0;
    double totcon,totcre,latitud = 0,longitud = 0;
    boolean venta = false, enviado = false;
    String etiqueta_interna = "",rutaImg,Est_Conservador = "",imageurl;
    private static final int PICTURE_RESULT = 122 ;
    int seleccionTipo = 0;
    int previaseleccion = 0;
    SharedPreferences Configuraciones;
    SharedPreferences.Editor EditarConf;
    ArrayList<clsProductos> productos;
    List<clsClientes> clientesFilt = new ArrayList<clsClientes>();
    SpinAdapterProductos adapterProductos;
    Gson Gson_Converter = new Gson();//objeto que permite convertir cadenas JSON en objetos

    Adapter_Productos AdaptadorProductos;
    Adapter_ProductosListaPre AdaptadorProductosListaPre;
    Adapter_Clientes AdaptadorClientes;
    private LocationManager locManager;
    private Location loc;
    Marker mCenterMarker = null;
    CameraPosition MarcadorGlobal;
    MarkerOptions mCenterMarkerOptions = null;
    List<String> clientes;
    List<String> productosSpinner;
    TextView lblClienteSelec, lblTotal, lblTiempo, lblPiezasTot, lblClienteQR;
    Switch swConservador,swQR;
    ImageButton imgbtnClientes, imgLocalizar;
    Button btnGuardarV, btnVenta, btnCancelarV, btnContinuar, btnAgregarP;
    ProgressBar Cargando;
    MapView mV;
    RecyclerView recyclerProds;
    LinearLayout layoutImagenE, llSeleccionarCliente;
    ScrollView scrollView;
    AutoCompleteTextView spProductos;
    EditText txtPiezas;
    RadioButton rbVentaCliente, rbVentaPublicoGral, rbOtrosClientes;
    //EditText txtEtiInt; llQR,llEtiquetaInt,llClienteQR,
    //CheckBox cbEtiInt;
    SQLocal dbLocal;
    private MagicalCamera magicalCamera;
    private MagicalPermissions magicalPermissions;
    private final int REDIMENCIONAR_IMAGEN_PORCENTAGE = 40;
    private Bitmap imagenTomada = null;
    ImageView imgFoto;
    ContentValues values;
    private Uri imageUri;
    //Llenar Base de datos Local
    //Productos
    ArrayList<clsProductos> prodsArray;
    //clsProductos prodsClase;
    //Productos por listas
    //ArrayList<clsListasPrecios> lisprodArray;
    //clsListasPrecios lisprodClase;
    //Clientes
    ArrayList<clsClientes> clientsArray;
    List<clsClientes> clientesOtros = new ArrayList<clsClientes>();
    clsClientes clientsClase;
    clsClientes clienSelec = new clsClientes();

    ArrayList<clsConservadores> conservadorEnc;


    clsProductos productoLista;
    clsProductos prodselfin;

    //Vista de proceso de conservador
    Adapter_ResumenProd AdaptadorProdRes;
    ArrayList<clsProductos> resumenProds;

    //ArrayList<clsListasPrecios> productosListaPrecios;
    //clsListasPrecios productoPrecioLista;
    //clsListasPrecios prodPrecioselfin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_cliente);
        dbLocal = new SQLocal(this);
        String[] permissions = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        magicalPermissions = new MagicalPermissions(this, permissions);

        Global.handleSSLHandshake();
        queue = Volley.newRequestQueue(this);

        btnGuardarV = (Button) findViewById(R.id.btnGuardarVenta);
        btnVenta = (Button) findViewById(R.id.btnVentaCliente);
        btnCancelarV = (Button) findViewById(R.id.btnCancelarVenta);
        lblTiempo  = (TextView) findViewById(R.id.lblTiempoRestante);
        lblClienteSelec = (TextView) findViewById(R.id.lblClienteSelec);
        //scrollView = (ScrollView) findViewById(R.id.scrollview);
        Cargando = (ProgressBar) findViewById(R.id.pbCargando);
        //lblClienteQR = (TextView) findViewById(R.id.lblQRCliente);
        swConservador = (Switch) findViewById(R.id.swtConservador);
        swQR = (Switch) findViewById(R.id.swtQR);
        btnAgregarP = (Button) findViewById(R.id.btnAgregarPro);
        rbVentaCliente = (RadioButton) findViewById(R.id.rbVentaCliente);
        rbVentaPublicoGral = (RadioButton) findViewById(R.id.rbVentaPublicoGral);
        rbOtrosClientes = (RadioButton) findViewById(R.id.rbVentaOtroCliente);
        llSeleccionarCliente = (LinearLayout) findViewById(R.id.llSeleccionarCliente);
        //llQR = (LinearLayout) findViewById(R.id.llIngresarQR);
        //llEtiquetaInt = (LinearLayout) findViewById(R.id.llEtiquetaInterna);
        //llClienteQR = (LinearLayout) findViewById(R.id.llMostrarClienteCcon);
        recyclerProds = findViewById(R.id.recProductos);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerProds.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerProds.addItemDecoration(dividerItemDecoration);
        recyclerProds.setLayoutManager(new LinearLayoutManager(this));
        lblTotal = findViewById(R.id.lblTotalVenta);
        lblPiezasTot = findViewById(R.id.lblPiezasVenta);
        spProductos = findViewById(R.id.cmbProductos);
        txtPiezas = findViewById(R.id.txtPiezasPro);
        //txtEtiInt = findViewById(R.id.etnEI);
        //cbEtiInt = findViewById(R.id.cbNoEtiqueta);

        //Cargar_Seleccion_Productos();

        imgbtnClientes = (ImageButton) findViewById(R.id.imgbtnClientes);
        imgLocalizar = (ImageButton) findViewById(R.id.imgLocalizacion);
        productos = new ArrayList<clsProductos>();
        IniciarContador(3,1000);
        recyclerProds.setLayoutManager(new LinearLayoutManager(this));
        recyclerProds.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        spProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prodselfin = prodsArray.get(position);
                txtPiezas.requestFocus();
                //AgregarProducto();
            }
        });

        lblTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerTipos();
            }
        });
        rbVentaCliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(productos.size()>0){
                        if(previaseleccion==1) {
                            previaseleccion = 0;
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(VentaCliente.this);
                            dialogo1.setTitle("Productos");
                            dialogo1.setMessage("Tiene productos agregados, ¿Continuar?");
                            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    EliminarVenta();
                                    llSeleccionarCliente.setVisibility(View.VISIBLE);
                                    lblClienteSelec.setEnabled(true);
                                    imgbtnClientes.setEnabled(true);
                                }
                            });
                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    if (seleccionTipo == 0)
                                        rbVentaCliente.setChecked(true);
                                    else if (seleccionTipo == 1)
                                        rbVentaPublicoGral.setChecked(true);
                                    else if (seleccionTipo == 2)
                                        rbOtrosClientes.setChecked(true);
                                    previaseleccion = 1;
                                    dialogo1.dismiss();
                                }
                            });
                            dialogo1.show();
                        }
                    }else{
                        seleccionTipo = 0;
                        previaseleccion = 1;
                        llSeleccionarCliente.setVisibility(View.VISIBLE);
                        lblClienteSelec.setEnabled(true);
                        imgbtnClientes.setEnabled(true);
                    }
                }
            }
        });
        rbVentaPublicoGral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(productos.size()>0) {
                        if(previaseleccion == 1) {
                            previaseleccion = 0;
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(VentaCliente.this);
                            dialogo1.setTitle("Productos");
                            dialogo1.setMessage("Tiene productos agregados, ¿Continuar?");
                            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    EliminarVenta();
                                    llSeleccionarCliente.setVisibility(View.GONE);
                                    clsClientes clientegral = new clsClientes();
                                    clientegral.Id_Cliente = 0;
                                    clientegral.Id_Lista = 0;
                                    clientegral.Tipo_Pago = 0;
                                    clientegral.Nombre = "Publico General";
                                    clienteSel = clientegral;

                                    prodsArray = new ArrayList<clsProductos>();
                                    prodsArray = dbLocal.getProductos();
                                    Adaptador_Productos_Combo adapter = new Adaptador_Productos_Combo(getApplicationContext(), R.layout.activity_venta_cliente,
                                            R.id.lbl_name, prodsArray);
                                    spProductos.setAdapter(adapter);
                                }
                            });
                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    if (seleccionTipo == 0)
                                        rbVentaCliente.setChecked(true);
                                    else if (seleccionTipo == 1)
                                        rbVentaPublicoGral.setChecked(true);
                                    else if (seleccionTipo == 2)
                                        rbOtrosClientes.setChecked(true);
                                    previaseleccion = 1;
                                    dialogo1.dismiss();
                                }
                            });
                            dialogo1.show();
                        }
                    }else{
                        llSeleccionarCliente.setVisibility(View.GONE);
                        clsClientes clientegral = new clsClientes();
                        clientegral.Id_Cliente = 0;
                        clientegral.Id_Lista = 0;
                        clientegral.Tipo_Pago = 0;
                        clientegral.Nombre = "Publico General";
                        clienteSel = clientegral;
                        previaseleccion = 1;
                        prodsArray = new ArrayList<clsProductos>();
                        prodsArray = dbLocal.getProductos();
                        Adaptador_Productos_Combo adapter = new Adaptador_Productos_Combo(getApplicationContext(), R.layout.activity_venta_cliente,
                                R.id.lbl_name, prodsArray);
                        spProductos.setAdapter(adapter);
                        seleccionTipo = 1;

                    }
                }
            }
        });
        rbOtrosClientes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(productos.size()>0) {
                        if(previaseleccion == 1) {
                            previaseleccion = 0;
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(VentaCliente.this);
                            dialogo1.setTitle("Productos");
                            dialogo1.setMessage("Tiene productos agregados, ¿Continuar?");
                            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    EliminarVenta();
                                    llSeleccionarCliente.setVisibility(View.VISIBLE);
                                    ElegirOtroCliente().show();
                                    lblClienteSelec.setEnabled(false);
                                    imgbtnClientes.setEnabled(false);
                                }
                            });
                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    if (seleccionTipo == 0)
                                        rbVentaCliente.setChecked(true);
                                    else if (seleccionTipo == 1)
                                        rbVentaPublicoGral.setChecked(true);
                                    else if (seleccionTipo == 2)
                                        rbOtrosClientes.setChecked(true);
                                    previaseleccion = 1;
                                    dialogo1.dismiss();
                                }
                            });
                            dialogo1.show();
                        }
                    }else{
                        llSeleccionarCliente.setVisibility(View.VISIBLE);
                        ElegirOtroCliente().show();
                        lblClienteSelec.setEnabled(false);
                        imgbtnClientes.setEnabled(false);
                        seleccionTipo = 2;
                        previaseleccion = 1;
                    }
                    //Enviar Mensaje para que seleccione clientes en general
                }
            }
        });
        //Proceso para registrar venta
        //Si el cliente tiene conservador mostramos la opcion para QR
        /*swConservador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    llEtiquetaInt.setVisibility(View.VISIBLE);
                    //swQR.setVisibility(View.VISIBLE);
                }else{
                    llEtiquetaInt.setVisibility(View.GONE);
                    swQR.setVisibility(View.GONE);
                    cbEtiInt.setChecked(false);
                    //swQR.setVisibility(View.GONE);
                }
            }
        });
        swQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    swQR.setVisibility(View.VISIBLE);
                    llQR.setVisibility(View.VISIBLE);
                }else{
                    llQR.setVisibility(View.GONE);
                }
            }
        });*/
        lblClienteSelec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarClientes().show();
            }
        });
        imgbtnClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarClientes().show();
            }
        });
        /*cbEtiInt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    swQR.setVisibility(View.VISIBLE);
                    txtEtiInt.setEnabled(false);
                }
                else{
                    swQR.setVisibility(View.GONE);
                    txtEtiInt.setEnabled(true);
                }
            }
        });*/
        btnGuardarV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lblClienteSelec.getText()!="Seleccione el cliente:"&&clienteSel!=null) {
                    if(productos.size()>0) {
                        Guardar_Venta_T();
                    }else
                        Toast.makeText(getApplicationContext(),"Es necesario agregar al menos un producto",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(),"Es necesario seleccionar un cliente",Toast.LENGTH_SHORT).show();
            }
        });
        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lblClienteSelec.getText()!="Seleccione el cliente:" && lblClienteSelec.getText()!="") {
                    if(productos.size()>0) {
                        ProcesoConservador().show();
                        //Guardar_Venta();
                    }else
                        Toast.makeText(getApplicationContext(),"Es necesario agregar al menos un producto",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(),"Es necesario seleccionar un cliente",Toast.LENGTH_SHORT).show();
            }
        });
        btnCancelarV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productos.size()>0)
                    CancelarVenta();
                else
                    Toast.makeText(getApplicationContext(), "No hay productos para quitar en la venta",Toast.LENGTH_LONG).show();
            }
        });
        imgLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarMapa("1", clienteSel);
            }
        });
        btnAgregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarProducto();
            }
        });
    }

    public void AgregarProducto(){
        try {
            if (!spProductos.getText().toString().isEmpty() && !spProductos.getText().toString().equals("") && prodselfin!=null) {
                if (!txtPiezas.getText().toString().equals("") && !txtPiezas.getText().toString().isEmpty()) {
                int busqueda = BuscarProducto(prodselfin.IdProducto);
                if(busqueda < 0 || clienteSel.getTipo_Pago() == 2) {
                    productoLista = new clsProductos();
                    productoLista.IdProducto = prodselfin.IdProducto;
                    productoLista.Nombre_P = prodselfin.Nombre_P;
                    productoLista.Piezas = Integer.parseInt(txtPiezas.getText().toString());
                    productoLista.Precio_P = prodselfin.Precio_P;
                    if(clienteSel.getTipo_Pago()==0||clienteSel.getTipo_Pago()==2) {
                        productoLista.Eleccion_Pago = 0;
                        productoLista.Tipo = "Contado";
                    }
                    else {
                        productoLista.Eleccion_Pago = 1;
                        productoLista.Tipo = "Credito";
                    }
                    productoLista.Tipo_Pago = clienteSel.getTipo_Pago();
                    productos.add(productoLista);
                }else{
                    productos.get(busqueda).Piezas = (productos.get(busqueda).Piezas + 1);
                }
                AdaptadorProductos = new Adapter_Productos(getApplication().getApplicationContext(), productos);
                recyclerProds.setAdapter(AdaptadorProductos);
                txtPiezas.setText("");
                spProductos.requestFocus();
                AdaptadorProductos.setClickListener(new Adapter_Productos.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //TextView lblBorrar = (TextView) view.findViewById(R.id.lblBorrarItem);
                        ModificarProducto(position);
                        /*
                        lblBorrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ModificarProducto(position);
                                }catch (Exception ex){
                                    Toast.makeText(v.getContext(), "Ya no hay mas productos a quitar "+ex,Toast.LENGTH_LONG).show();
                                }
                            }
                        });*/
                    }
                });
                ModificarTotal();
                spProductos.setText("");
                } else {
                    _Mostrar_Mensaje("Error Piezas", "Es necesario ingresar la cantidad de piezas", "Acepar", "");
                }
            } else {
                _Mostrar_Mensaje("Error Producto", "Es necesario ingresar el producto y la cantidad", "Acepar", "");
            }
        }catch (Exception e){
            _Mostrar_Mensaje("Error: "+e.getLocalizedMessage(),"Ha ocurrido un error "+e.getMessage() , "Acepar", "");
        }
    }
    public void IniciarContador(int tiempominsIni, int inte){
        CountDownTimer countDownTimer = new CountDownTimer(tiempominsIni*60*1000, inte) {
            int minutos = (int) (tiempominsIni);

            public void onTick(long millisUntilFinished) {
                if(contador==0){
                    minutos = (int) ((millisUntilFinished/1000L)/60);
                    contador = 60;
                }
                contador--;

                String minutosMos = String.valueOf(minutos);
                String segundosMos = String.valueOf(contador);

                if(minutos < 10) {
                    minutosMos = "0" + minutos;
                }
                if(contador < 10){
                    segundosMos = "0" + contador;
                }
                if(minutos == 0 && contador <= 30)
                    lblTiempo.setTextColor(Color.RED);
                lblTiempo.setText(minutosMos+":"+segundosMos);
            }

            @Override
            public void onFinish() {
                //No candadear
                TerminoTiempo();
            }
        }.start();
    }

    public void ModificarProducto(int pos){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.modificar_producto, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        ImageButton btnEditar = (ImageButton) v.findViewById(R.id.imgEditarPiezas);
        ImageButton btnQuitar = (ImageButton) v.findViewById(R.id.imgQuitarProducto);
        Button btnGuardar = (Button) v.findViewById(R.id.btnAceptarPieza);
        EditText txtPiezas = (EditText) v.findViewById(R.id.txtPiezasNuevo);
        LinearLayout linOpciones = (LinearLayout) v.findViewById(R.id.llMenuOpciones);
        LinearLayout linEditarP = (LinearLayout) v.findViewById(R.id.llEditarPiezas);

        builder.setView(v);
        alertDialog = builder.create();
        // Add action buttons
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linOpciones.setVisibility(View.GONE);
                linEditarP.setVisibility(View.VISIBLE);
                txtPiezas.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                /*if(productos.get(position).getPiezas() > 1){
                                        productos.get(position).setPiezas(productos.get(position).getPiezas() - 1);
                                        AdaptadorProductos.notifyItemChanged(position, productos.size());
                                    }else {
                                        productos.remove(position);
                                        AdaptadorProductos.notifyItemRemoved(position);
                                        AdaptadorProductos.notifyItemRangeChanged(position, productos.size());
                                    }*/
            }
        });
        btnQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productos.remove(pos);
                AdaptadorProductos.notifyItemRemoved(pos);
                alertDialog.dismiss();
                ModificarTotal();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtPiezas.getText().toString().isEmpty()){
                    if(Integer.parseInt(txtPiezas.getText().toString())>0){
                        productos.get(pos).setPiezas(Integer.parseInt(txtPiezas.getText().toString()));
                        AdaptadorProductos.notifyItemChanged(pos, productos.size());
                        ModificarTotal();
                        alertDialog.dismiss();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar un numero mayor a 0", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(), "Es necesario ingresar un numero", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        if(productos.size()>0){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Tabla con Productos");
            dialogo1.setMessage("Tienes productos en la tabla, ¿Salir?");
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    VentaCliente.super.onBackPressed();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
            dialogo1.show();
        }else {
            super.onBackPressed();
        }
    }

    public void ModificarTotal(){
        int contarcontado = 0;
        int contarcredito = 0;
        tipo_pago = 0;
        totcon = 0;
        totcre = 0;
        piezastot = 0;
        for(int i = 0; i < productos.size(); i ++){
            if(productos.get(i).Eleccion_Pago==0) {
                totcon += productos.get(i).Precio_P * productos.get(i).Piezas;
                contarcontado++;
            }
            else {
                totcre += productos.get(i).Precio_P * productos.get(i).Piezas;
                contarcredito++;
            }
            piezastot += productos.get(i).Piezas;
        }
        if(contarcontado > 0) {
            if (contarcredito > 0)
                tipo_pago = 2;
        }else if(contarcredito > 0)
            tipo_pago = 1;

        double total = totcon+totcre;
        lblTotal.setText("$"+String.valueOf(total).toString());
        lblPiezasTot.setText(String.valueOf(piezastot).toString());
    }

    public int BuscarProducto(int idprodbus){
        int encontrado = -1;
        for (int x = 0; x < productos.size(); x++) {
            clsProductos p = productos.get(x);
            if (p.getIdProducto() == idprodbus) {
                encontrado = x;
                break; // Terminar ciclo, pues ya lo encontramos
            }
        }
        return encontrado;
    }

    //Ventanas emergentes basicas
    public void _Mostrar_Mensaje(String Titulo, String Mensaje, String positivo, String negativo){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(Titulo);
        dialogo1.setMessage(Mensaje);

        if(positivo!=null) {
            dialogo1.setPositiveButton(positivo, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
        }
        if(negativo!=null) {
            dialogo1.setNegativeButton(negativo, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
        }
        dialogo1.show();
    }

    public void TerminoTiempo(){
        try {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Finalizar");
            dialogo1.setMessage("El tiempo asignado para realizar la venta termino, ¿Continuar con la venta?");

            dialogo1.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                    finish();

                }
            });
            dialogo1.show();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Es necesario elegir una opción",Toast.LENGTH_LONG).show();
        }
    }
    public void Guardar_Venta_T(){
        venta = false;
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Guardar Venta");
        dialogo1.setMessage("¿Esta seguro de guardar y cerrar la venta?\r\n" +
                "Puede consultarla despues");

        dialogo1.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dbLocal.InsertVentaGuardar(clienteSel.getId_Cliente(), totcon+totcre, piezastot);
                int idVenta = dbLocal.contarVentasG();
                for(int i = 0; i < productos.size(); i++){
                    int tipo = productos.get(i).Eleccion_Pago;
                    dbLocal.InsertDetVentaGuardar(idVenta,productos.get(i).IdProducto, productos.get(i).Precio_P,
                            productos.get(i).Piezas, tipo);
                }
                Toast.makeText(getApplicationContext(), "Venta Guardada Correctamente", Toast.LENGTH_SHORT).show();
                dialogo1.dismiss();
                finish();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });

        dialogo1.show();
    }

    public void Guardar_Venta(){
        venta = false;
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Guardar Venta");
        dialogo1.setMessage("¿Esta seguro de guardar y cerrar la venta?");
        dialogo1.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                ModificarTotal();
                String tiempo = ObtenerTiempo(lblTiempo.getText().toString(),"03:00");
                //Comprobamos si tiene internet
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                //Si tiene la enviamos directamente a mysql y guardamos local
                if(isConnected){
                    dbLocal.InsertVenta(totcon+totcre,totcon,totcre, piezastot, tipo_pago,
                            Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))),1,
                            clienteSel.getId_Cliente(), Integer.parseInt(Est_Conservador.split(" ")[1]),
                            String.valueOf(latitud), String.valueOf(longitud),1,tiempo,1);
                    int idVenta = dbLocal.contarVentas();
                    for(int i = 0; i < productos.size(); i++){
                        int tipo = productos.get(i).Eleccion_Pago;
                        dbLocal.InsertDetVenta(idVenta,productos.get(i).IdProducto, productos.get(i).Precio_P,
                                productos.get(i).Piezas, tipo);
                    }
                    clsVentas[] ventaEnviar = dbLocal.EnviarVentaInternet(idVenta);
                    clsDetVentas[] detventaEnviar = dbLocal.EnviarDetVentaInternet(idVenta);
                    String lista_ventas = Gson_Converter.toJson(ventaEnviar);
                    String lista_detventas = Gson_Converter.toJson(detventaEnviar);
                    EnviarVentaSeparada(lista_ventas, lista_detventas, dialogo1, idVenta);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }else{
                    dbLocal.InsertVenta(totcon+totcre,totcon,totcre, piezastot, tipo_pago,
                            Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))),1,
                            clienteSel.getId_Cliente(), Integer.parseInt(Est_Conservador.split(" ")[1]),
                            String.valueOf(latitud), String.valueOf(longitud),1,tiempo,0);
                    int idVenta = dbLocal.contarVentas();
                    for(int i = 0; i < productos.size(); i++){
                        int tipo = productos.get(i).Tipo_Pago;
                        dbLocal.InsertDetVenta(idVenta,productos.get(i).IdProducto, productos.get(i).Precio_P,
                                productos.get(i).Piezas, tipo);
                    }
                    Toast.makeText(getApplicationContext(), "Venta Guardada Correctamente", Toast.LENGTH_SHORT).show();

                    dialogo1.dismiss();
                    EliminarVenta();
                    finish();
                }
                //Si no tiene guardamos local

            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });

        dialogo1.show();
    }
    public Boolean EnviarVentaSeparada(String Venta, String DetVenta, DialogInterface di, int id_ve){
        String url = Global.url+"paginas/ventas/controladores/Ope_Ventas.asmx/GuardarVentaSeparada";
        //objeto hasmap que contendrá los parametros de busqueda con clave string,string
        Map<String, String> parametros = new HashMap();
        parametros.put("Venta",Venta);
        parametros.put("DetVenta",DetVenta);
        Cargando.setVisibility(View.VISIBLE);

        System.out.println(Venta);
        System.out.println(DetVenta);

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
                                di.dismiss();
                                Toast.makeText(getApplicationContext(), "Venta Guardada y Enviada Correctamente", Toast.LENGTH_SHORT).show();
                                FinalizarEnvio(true,0);
                                //Toast.makeText(getApplicationContext(), "La venta ha sido enviada correctamente", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Venta Guardada Correctamente", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), "Ha ocurrido un error al enviar la venta: " + resultado.getMensaje()+"\\Intente de nuevo",
                                //        Toast.LENGTH_SHORT).show();
                                FinalizarEnvio(false,id_ve);

                            }
                        } catch (Exception e) {
                            FinalizarEnvio(false,id_ve);
                            Toast.makeText(getApplicationContext(), "Venta Guardada Correctamente (Error al enviar: "+e.getMessage()+")", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), "guardarVenta[onResponse] " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error [guardarVenta]: " + String.valueOf(error.networkResponse), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "error [guardarVenta]: " + String.valueOf(parseNetworkError(error)), Toast.LENGTH_LONG).show();
                        FinalizarEnvio(false,id_ve);
                    }
                    protected VolleyError parseNetworkError(VolleyError volleyError){
                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                        }
                        FinalizarEnvio(false,id_ve);
                        return volleyError;
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(getRequest);
        return enviado;
    }
    public void FinalizarEnvio(Boolean envio, int idv){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Cargando.setVisibility(View.INVISIBLE);
        if(!envio){
            dbLocal.UpdateVenta(idv);
        }
        finish();
    }
    public String ObtenerTiempo(String tiempo, String minutos){
        LocalTime hora_1 = LocalTime.parse(tiempo);
        LocalTime hora_2 = LocalTime.parse(minutos);
        long diff = ChronoUnit.MINUTES.between(hora_1, hora_2);
        Double h = diff / 60.00;
        Long m = diff % 60;
        LocalTime hf = LocalTime.of(h.intValue(), m.intValue());
        return hf.toString();
    }
    public void VerTipos(){
        ModificarTotal();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Contado/Credito");
        dialogo1.setMessage("Total Contado: $"+totcon+"\r\nTotal Credito: $"+totcre);
        dialogo1.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
    public void CancelarVenta(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Cancelar Venta");
        dialogo1.setMessage("¿Esta seguro de cancelar y borrar la venta?");
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EliminarVenta();
                Toast.makeText(getApplicationContext(),"Venta eliminada correctamente",Toast.LENGTH_LONG).show();
            }
        });
        dialogo1.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }

    //Ventanas emergentes personalizadas
    public AlertDialog MostrarClientes() {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.buscar_cliente, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        ImageButton imgCerrar = (ImageButton) v.findViewById(R.id.imgbCerrarCliente);
        EditText txtBuscarCliente = (EditText)  v.findViewById(R.id.txtBuscarCliente);
        RecyclerView recClientesBus;
        RecyclerView.LayoutManager layoutManager;
        recClientesBus = v.findViewById(R.id.recClientesBuscar);
        clientsArray = new ArrayList<clsClientes>();
        clientsArray = dbLocal.getClientes();
//        for(int i = 1; i < 10; i++) {
//            clientes.add("Cliente #" + i);
//        }
        clientesFilt = clientsArray;
        AdaptadorClientes = new Adapter_Clientes(clientsArray, this);
        layoutManager = new LinearLayoutManager(this);
        recClientesBus.setLayoutManager(layoutManager);
        recClientesBus.setAdapter(AdaptadorClientes);
        builder.setView(v);
        alertDialog = builder.create();
        recClientesBus.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        txtBuscarCliente.requestFocus();
        txtBuscarCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AdaptadorClientes.getFilter().filter(s.toString());
                clientesFilt = AdaptadorClientes.getClientesFilter();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recClientesBus.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recClientesBus ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                EliminarVenta();
                lblClienteSelec.setText(String.valueOf(clientesFilt.get(position).getNombre()));
                ArrayList<clsConservadores> clienteconservador = dbLocal.getConservadorCliente(clientesFilt.get(position).getId_Cliente());
                if(clienteconservador.size()>0){
                    codigo_qr_cliente = clienteconservador.get(0).getQR();
                    //llClienteQR.setVisibility(View.VISIBLE);
                }else{
                    codigo_qr_cliente = 0;
                    //llClienteQR.setVisibility(View.GONE);
                }
                spProductos.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                clienteSel = clientesFilt.get(position);
                //prodsArray.clear();
                prodsArray = new ArrayList<clsProductos>();
                prodsArray = dbLocal.getListasPreciosUnica(clientesFilt.get(position).getId_Lista());
                Adaptador_Productos_Combo adapter = new Adaptador_Productos_Combo(getApplicationContext(), R.layout.activity_venta_cliente,
                        R.id.lbl_name, prodsArray);
                spProductos.setAdapter(adapter);
                imgLocalizar.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }

            @Override public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));
        imgCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
    public void MostrarMapa(String param, clsClientes cliente){
        Button btnEstablecerUbicacion, btnCancelarUbicacion;
        TextView lblInfoCliente;
        latitud = 0;
        longitud = 0;
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.customDialog));
        AlertDialog alertDialog = builder.create();
        switch (param){
            case "1" :
                final View rootMV = inflater.inflate(R.layout.activity_maps, null);
                btnEstablecerUbicacion = rootMV.findViewById(R.id.btnConfirmarUbi);
                btnCancelarUbicacion = rootMV.findViewById(R.id.btnCancelarUbi);
                lblInfoCliente = (TextView) rootMV.findViewById(R.id.lblMostrarInfoCliente);
                lblInfoCliente.setText("Confirma la ubicación del cliente: "+String.valueOf(cliente.getNombre()));

                alertDialog.setView(rootMV);
                MapView mV = rootMV.findViewById(R.id.map);
                mV.onCreate(alertDialog.onSaveInstanceState());
                mV.onResume();
                mV.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        if(!cliente.getLatitud().equals("") && !cliente.getLongitud().equals("")) {
                            latitud = Double.parseDouble(cliente.getLatitud());
                            longitud = Double.parseDouble(cliente.getLongitud());
                        }else{
                            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            latitud = loc.getLatitude();
                            longitud = loc.getLongitude();
                            googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                                @Override
                                public void onCameraMove() {
                                    if(mCenterMarker != null){
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
                        }
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

                btnEstablecerUbicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*
                        productosSpinner = new ArrayList<>();
                        for(int i = 0; i < 10; i++) {
                            prods[i] = new clsProductos();
                            prods[i].IdProducto = i;
                            prods[i].Nombre_P = "Producto "+(i+1);
                            prods[i].Precio_P = (i+1)*10.00;
                        }
                        */
                        //Toast.makeText(getApplicationContext(), String.valueOf(clienteSel.getTipo_Pago()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Coordenadas guardadas: \r\n Latitud= "+String.valueOf(latitud)+"\r\n Longitud= "+String.valueOf(longitud), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), String.valueOf(dbLocal.getListasPreciosUnica(clientesFilt.get(pos).getId_Lista()).size()), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        imgLocalizar.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                        //alerCliente.dismiss();
                    }
                });
                btnCancelarUbicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
        }
    }

    public BottomSheetDialog ProcesoConservador(){
        BottomSheetDialog dialog = new BottomSheetDialog(VentaCliente.this);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.proceso_conservador, null);
        btnContinuar = v.findViewById(R.id.btnContinuarInfo);
        Button btnVolver = v.findViewById(R.id.btnVolverInfo);
        Button btnTomarEvidencia = v.findViewById(R.id.btnAgregarEvidenciaAnex);
        Button btnQuitarEvidencia = v.findViewById(R.id.btnQuitarImgE);
        ImageButton btnQR = v.findViewById(R.id.btnQR);
        RadioButton rbTieneCon = v.findViewById(R.id.rbSiCons);
        RadioButton rbNoTieneCon = v.findViewById(R.id.rbNoCons);
        RadioButton rbTieneQR = v.findViewById(R.id.rbSiQR);
        RadioButton rbNoTieneQR = v.findViewById(R.id.rbNoQR);
        RadioButton rbTieneEI = v.findViewById(R.id.rbSiEI);
        RadioButton rbNoTieneEI = v.findViewById(R.id.rbNoEI);
        LinearLayout llPTieneCons = v.findViewById(R.id.llPTieneCon);
        LinearLayout llPTieneQR = v.findViewById(R.id.llPTieneQR);
        LinearLayout llInfoCon = v.findViewById(R.id.llInfoCon);
        LinearLayout llPTieneEI = v.findViewById(R.id.llPTieneEI);
        LinearLayout llQR = v.findViewById(R.id.llIngresarQR);
        LinearLayout llEt_IntP = v.findViewById(R.id.llEt_Int);
        LinearLayout llGRep = v.findViewById(R.id.llGReporte);
        LinearLayout llMostrarR = v.findViewById(R.id.llResumenVenta);
        layoutImagenE = (LinearLayout) v.findViewById(R.id.rlImagenEv);
        imgFoto = v.findViewById(R.id.img_foto);

        RecyclerView recResumen = v.findViewById(R.id.recResVenta);
        EditText txtQR = v.findViewById(R.id.etnQR);
        //Informacion del conservador encontrado
        EditText txtEti = v.findViewById(R.id.txtEt_IntNQR);
        EditText txtEtiInfo = v.findViewById(R.id.txtEt_Int);
        TextView lblNo_Serie = v.findViewById(R.id.lblNoSerie);
        TextView lblModelo = v.findViewById(R.id.lblModelo);
        TextView lblQR = v.findViewById(R.id.lblQR);
        TextView lblUbicacion = v.findViewById(R.id.lblUbicacion);
        TextView lblStatusConservador = v.findViewById(R.id.lblStatusVenta);
        TextView lblTieneCons = v.findViewById(R.id.lblConservador);
        Intent intent = new Intent();

        dialog.setContentView(v);
        dialog.setCancelable(false);
        //if(codigo_qr_cliente!=0)
        //    lblTieneCons.setText("¿El QR "+codigo_qr_cliente+" del conservador es correcto?");
        if(clienteSel.getId_Cliente()==0) {
            llPTieneCons.setVisibility(View.GONE);
            llMostrarR.setVisibility(View.VISIBLE);
            btnVolver.setText("Cancelar");
            btnContinuar.setText("Finalizar");
            lblStatusConservador.setText("Venta Publico Gral.");
            Est_Conservador = "Situacion 11";
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recResumen.getContext(),
                    new LinearLayoutManager(getApplicationContext()).getOrientation());
            recResumen.addItemDecoration(dividerItemDecoration);
            recResumen.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            AdaptadorProdRes = new Adapter_ResumenProd(getApplication().getApplicationContext(), productos);
            recResumen.setAdapter(AdaptadorProdRes);
        }
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnContinuar.getText().equals("Continuar")) {
                    if (rbTieneCon.isChecked()) {
                        proceso = 1;
                        rbNoTieneCon.setChecked(true);
                    } else if (rbTieneQR.isChecked() && proceso == 1) {
                        if (codigo_qr != 0) {
                            txtQR.setText(String.valueOf(codigo_qr));
                            codigo_qr = 0;
                        }
                        if (!txtQR.getText().toString().isEmpty()) {
                            conservadorEnc = VerificarQR(Integer.parseInt(txtQR.getText().toString()));
                            if (conservadorEnc.size() > 0) {
                                if(conservadorEnc.get(0).getQR() == codigo_qr_cliente)
                                    Est_Conservador = "Situacion 0"; //El Qr del conservador en ruta coincide con el o alguno del cliente
                                else
                                    Est_Conservador = "Situacion 5"; //El Qr del conservador en ruta no coincide con el o ninguno del cliente
                                                                    //pero si existe en el sistema
                                proceso = 2;
                                rbNoTieneQR.setChecked(true);
                                //Toast.makeText(getApplicationContext(), String.valueOf(conservadorEnc.size()), Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(VentaCliente.this);
                                dialogo1.setTitle("QR");
                                dialogo1.setMessage("El QR " + txtQR.getText().toString() +" no existe ¿Continuar?");
                                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Est_Conservador = "Situacion 9"; //El Qr del conservador en ruta no coincide con el o ninguno del cliente y no esta en el sistema
                                        proceso = 4;
                                        rbNoTieneQR.setChecked(true);
                                        codigo_qr = Integer.parseInt(txtQR.getText().toString());
                                        llPTieneEI.setVisibility(View.VISIBLE);
                                        llPTieneQR.setVisibility(View.GONE);
                                    }
                                });
                                dialogo1.setNegativeButton("Volver a Ingresar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        dialogo1.dismiss();
                                    }
                                });
                                dialogo1.show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Es necesario ingresar un QR si la opcion Si esta marcada", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (proceso == 4) { //Vista para ingresar la etiqueta interna y buscarla
                        if (rbTieneEI.isChecked()) {
                            conservadorEnc = VerificarEtInt(txtEti.getText().toString());
                            if (conservadorEnc.size() > 0) {
                                proceso = 2;
                                rbNoTieneEI.setChecked(true);
                                txtEti.setText("");
                                //Toast.makeText(getApplicationContext(), String.valueOf(conservadorEnc.size()), Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(VentaCliente.this);
                                dialogo1.setTitle("Etiqueta Interna");
                                dialogo1.setMessage("La etiqueta interna " + txtEti.getText().toString()+" no existe ¿Continuar?");
                                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Est_Conservador = "Situacion 8"; //La Etiqueta del conservador en ruta no esta en el sistema
                                        proceso = 8;
                                        rbNoTieneEI.setChecked(true);
                                        etiqueta_interna = txtEti.getText().toString();
                                        llMostrarR.setVisibility(View.VISIBLE);
                                        llPTieneEI.setVisibility(View.GONE);
                                        lblStatusConservador.setText(Est_Conservador);
                                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recResumen.getContext(),
                                                new LinearLayoutManager(getApplicationContext()).getOrientation());
                                        recResumen.addItemDecoration(dividerItemDecoration);
                                        recResumen.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        AdaptadorProdRes = new Adapter_ResumenProd(getApplication().getApplicationContext(), productos);
                                        recResumen.setAdapter(AdaptadorProdRes);
                                    }
                                });
                                dialogo1.setNegativeButton("Volver a Ingresar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        dialogo1.dismiss();
                                    }
                                });
                                dialogo1.show();
                            }
                        } else if (rbNoTieneEI.isChecked()) { //Si no tiene etiqueta interna ni qr se toma evidencia del conservador (No. Serie u otro dato identificador)
                            proceso = 5;
                            Est_Conservador = "Situacion 3"; //El conservador no tiene Etiqueta Interna ni qr
                        }
                    }
                    else if (rbNoTieneQR.isChecked() && proceso == 1) {
                        proceso = 4;
                        Est_Conservador = "Situacion 2"; //El conservador no tiene QR
                        llPTieneQR.setVisibility(View.GONE);
                        llPTieneEI.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (proceso > 0) {
                        btnVolver.setText("Volver");
                        //btnVolver.setVisibility(View.VISIBLE);
                        if (proceso == 1) { //Vista de pregunta ¿Tiene QR?
                            llPTieneCons.setVisibility(View.GONE);
                            llPTieneQR.setVisibility(View.VISIBLE);
                        } else if (proceso == 2) { //Vista para mostrar los datos del conservador encontrado
                            String etiqueta = "";
                            if (!conservadorEnc.get(0).getEtiqueta_Interna().equals("0"))
                                etiqueta = conservadorEnc.get(0).getEtiqueta_Interna();
                            txtEtiInfo.setText(etiqueta);
                            lblNo_Serie.setText(conservadorEnc.get(0).getNo_Serie());
                            lblModelo.setText(conservadorEnc.get(0).getModelo());
                            if(codigo_qr != conservadorEnc.get(0).getQR()&&codigo_qr!=0)
                                lblQR.setText(String.valueOf(codigo_qr)+" -> "+String.valueOf(conservadorEnc.get(0).getQR()));
                            else
                                lblQR.setText(String.valueOf(conservadorEnc.get(0).getQR()));
                            lblUbicacion.setText(conservadorEnc.get(0).getUbicacion());

                            llPTieneQR.setVisibility(View.GONE);
                            llPTieneEI.setVisibility(View.GONE);
                            llInfoCon.setVisibility(View.VISIBLE);
                            proceso = 3;
                        } else if (proceso == 3) { //Mostrar el resumen de la venta
                            if (!lblQR.getText().toString().isEmpty()) {
                                    if (conservadorEnc.get(0).getEtiqueta_Interna().contains("?")) {
                                        if (!txtEti.getText().toString().equals(""))
                                            Est_Conservador = "Situacion 6"; //El conservador no tenia Etiqueta Interna, se asigno
                                        else
                                            Est_Conservador = "Situacion 7"; //El conservador no tenia Etiqueta Interna y no se asigno
                                    } else {
                                        //alertDialog.dismiss();
                                        proceso = 7;
                                        Est_Conservador = "Situacion 0";
                                        llMostrarR.setVisibility(View.VISIBLE);
                                        llInfoCon.setVisibility(View.GONE);
                                        btnContinuar.setText("Finalizar");
                                    }

                                //EliminarVenta();
                                //Toast.makeText(getApplicationContext(),"La venta se ha enviado",Toast.LENGTH_LONG).show();
                            }else if(codigo_qr != 0 || !etiqueta_interna.equals("")){
                                if(codigo_qr != 0 && !etiqueta_interna.equals(""))
                                    Est_Conservador = "Situacion 11"; //Se puso qr y etiqueta
                                proceso = 8;

                                llMostrarR.setVisibility(View.VISIBLE);
                                llPTieneEI.setVisibility(View.GONE);
                                rbNoTieneEI.setChecked(true);
                                btnContinuar.setText("Finalizar");
                            }else{
                                Toast.makeText(getApplicationContext(), "El QR no ha traido ningun conservador", Toast.LENGTH_SHORT).show();
                            }
                        } else if (proceso == 5) {
                            if(llGRep.getVisibility()==View.VISIBLE){
                                if(codigo_qr != 0)
                                    Est_Conservador = "Situacion 10"; //Se agregaron qr sin validar y no tiene etiqueta
                                else
                                    Est_Conservador = "Situacion 4"; //Reporte de no QR y no EI generado
                                llMostrarR.setVisibility(View.VISIBLE);
                                llGRep.setVisibility(View.GONE);
                                btnContinuar.setText("Finalizar");
                            }else {
                                llGRep.setVisibility(View.VISIBLE);
                                llPTieneEI.setVisibility(View.GONE);
                                rbNoTieneEI.setChecked(true);
                            }
                        }
                    } else { //Mostrar resumen desde el apartado tiene conservador o no
                        proceso = 6;
                        if(codigo_qr_cliente!=0)
                            Est_Conservador = "Situacion 1"; //El cliente tiene conservador asignado en el sistema pero no esta en el local
                        else
                            Est_Conservador = "Situacion 0"; //Cliente sin Conservador
                        llMostrarR.setVisibility(View.VISIBLE);
                        llPTieneCons.setVisibility(View.GONE);
                        btnVolver.setVisibility(View.VISIBLE);
                        btnVolver.setText("Volver");
                        btnContinuar.setText("Finalizar");
                    }
                    if (proceso == 5 || proceso == 6 || proceso == 7 || proceso == 8) { //Llenar la tabla de resumen de venta
                        lblStatusConservador.setText(Est_Conservador);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recResumen.getContext(),
                                new LinearLayoutManager(getApplicationContext()).getOrientation());
                        recResumen.addItemDecoration(dividerItemDecoration);
                        recResumen.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        AdaptadorProdRes = new Adapter_ResumenProd(getApplication().getApplicationContext(), productos);
                        recResumen.setAdapter(AdaptadorProdRes);
                    }
                }else //Manejo de situaciones y envio de venta
                {
                    //Envio de la venta
                    Guardar_Venta();
                    dialog.dismiss();
                    proceso = 0;
                }
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Configuraciones para la opcion de volver segun los diferentes menus
                if(btnVolver.getText().equals("Volver")) {
                    if (proceso == 0) {
                        llPTieneCons.setVisibility(View.VISIBLE);
                        llPTieneQR.setVisibility(View.GONE);
                        btnVolver.setText("Cancelar");
                    } else if (proceso == 1) {
                        llPTieneCons.setVisibility(View.VISIBLE);
                        llPTieneQR.setVisibility(View.GONE);
                        btnVolver.setText("Cancelar");
                        rbNoTieneCon.setChecked(true);
                        proceso = 0;
                    } else if (proceso == 2 || proceso == 3) {
                        llPTieneQR.setVisibility(View.VISIBLE);
                        rbNoTieneQR.setChecked(true);
                        txtEti.setText("");
                        txtQR.setText("");
                        llInfoCon.setVisibility(View.GONE);
                        if (proceso == 2)
                            proceso = 1;
                        else if (proceso == 3) {
                            txtEti.setText("");
                            lblNo_Serie.setText("");
                            lblModelo.setText("");
                            lblQR.setText("");
                            lblUbicacion.setText("");
                            proceso = 1;
                            llPTieneEI.setVisibility(View.GONE);
                        }
                    } else if (proceso == 4) {
                        llPTieneQR.setVisibility(View.VISIBLE);
                        llPTieneEI.setVisibility(View.GONE);
                        rbNoTieneEI.setChecked(true);
                        proceso = 1;
                    } else if (proceso == 5) {
                        llPTieneEI.setVisibility(View.VISIBLE);
                        llGRep.setVisibility(View.GONE);
                        llMostrarR.setVisibility(View.GONE);
                        btnContinuar.setText("Continuar");
                        proceso = 4;
                    } else if (proceso == 6) {
                        btnVolver.setText("Cancelar");
                        btnContinuar.setText("Continuar");
                        llPTieneCons.setVisibility(View.VISIBLE);
                        llMostrarR.setVisibility(View.GONE);
                        rbNoTieneCon.setChecked(true);
                        proceso = 0;
                    }
                    else if (proceso == 7) {
                        llInfoCon.setVisibility(View.VISIBLE);
                        llMostrarR.setVisibility(View.GONE);
                        btnContinuar.setText("Continuar");
                        proceso = 2;
                    }
                    else if (proceso == 8) {
                        llPTieneEI.setVisibility(View.VISIBLE);
                        llMostrarR.setVisibility(View.GONE);
                        btnContinuar.setText("Continuar");
                        proceso = 4;
                    }
                }else{
                    codigo_qr = 0;
                    codigo_qr_cliente = 0;
                    etiqueta_interna = "";
                    dialog.dismiss();
                }
            }
        });
        rbTieneQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    llQR.setVisibility(View.VISIBLE);
                    intent.setClass(getApplicationContext(), CaptureActivity.class);
                }
            }
        });
        rbNoTieneQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    llQR.setVisibility(View.GONE);
            }
        });
        rbTieneEI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    llEt_IntP.setVisibility(View.VISIBLE);
                    txtEtiInfo.requestFocus();
                }
            }
        });
        rbNoTieneEI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    llEt_IntP.setVisibility(View.GONE);
                    txtEti.setText("");
            }
        });
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent,2);
            }
        });
        btnTomarEvidencia.setOnClickListener(new View.OnClickListener() {
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
                //magicalCamera.takePhoto();
            }
        });
        btnQuitarEvidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = new ContentValues();
                imgFoto.setImageBitmap(null);
                layoutImagenE.setVisibility(View.GONE);
                //Obtiene la ruta donde se encuentra guardada la imagen.
                imageurl = "";
            }
        });
        return dialog;
    }
    public void Cargar_Seleccion_Productos(){
        Adaptador_Productos_Combo adapter = new Adaptador_Productos_Combo(this, R.layout.activity_venta_cliente,
                R.id.lbl_name, prodsArray);
        spProductos.setAdapter(adapter);
        /*prodsArray = new ArrayList<clsProductos>();
        prodsClase = new clsProductos();
        prodsClase.IdProducto = 0;
        prodsClase.Nombre_P = "Seleccione el cliente";
        //prods[0] = new clsProductos();
        //prods.add().Nombre_P = "Seleccione el cliente";
        prodsArray.add(prodsClase);

        adapterProductos = new SpinAdapterProductos(getApplicationContext(), R.layout.spinner_item, prodsArray);
        adapterProductos.setDropDownViewResource(R.layout.spinner_item_drop);
        spProductos.setAdapter(adapterProductos);
        spProductos.setSelection(0);*/
    }

    public AlertDialog ElegirOtroCliente(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.venta_otrocliente, null);

        AutoCompleteTextView txtOtroCliente = v.findViewById(R.id.atxtOtrosClientes);
        Button btnCancelar = v.findViewById(R.id.btnCancelarOtroC);
        Button btnAceptar = v.findViewById(R.id.btnAceptarOtroC);
        clienSelec = new clsClientes();
        clientesOtros = dbLocal.getClientes();

        builder.setView(v);

        builder.setCancelable(false);
        alertDialog = builder.create();

        Adaptador_Clientes_Combo adapter = new Adaptador_Clientes_Combo(getApplicationContext(), R.layout.activity_venta_cliente,
                R.id.lbl_name, clientesOtros);
        txtOtroCliente.setAdapter(adapter);

        txtOtroCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
                clientesOtros = adapter.getClientesFilter();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtOtroCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clienSelec = clientesOtros.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clienSelec!=null) {
                    lblClienteSelec.setEnabled(false);
                    imgbtnClientes.setEnabled(false);
                    lblClienteSelec.setText(clienSelec.getNombre());
                    clienteSel = clienSelec;
                    prodsArray = new ArrayList<clsProductos>();
                    prodsArray = dbLocal.getProductos();
                    Adaptador_Productos_Combo adapter = new Adaptador_Productos_Combo(getApplicationContext(), R.layout.activity_venta_cliente,
                            R.id.lbl_name, prodsArray);
                    spProductos.setAdapter(adapter);
                    alertDialog.dismiss();
                }else
                    Toast.makeText(getApplicationContext(),"Es necesario elegir un cliente para realizar venta",Toast.LENGTH_SHORT).show();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbVentaCliente.setChecked(true);
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    public void EliminarVenta(){
        lblClienteSelec.setText("Seleccionar Cliente");
        clienteSel = new clsClientes();
        piezastot = 0;
        prodselfin = new clsProductos();
        //Cargar_Seleccion_Productos();
        //txtPiezas.setText("");
        if(productos.size()>0) {
            productos.clear();
            ModificarTotal();
            imgLocalizar.setVisibility(View.GONE);
            AdaptadorProductos.notifyDataSetChanged();
        }
    }
    public ArrayList<clsConservadores> VerificarQR(int QR){
        return dbLocal.getConservadorQR(QR);
    }
    public ArrayList<clsConservadores> VerificarEtInt(String Etiqueta){
        return dbLocal.getConservadorEtiqueta("PRE-CON-"+Etiqueta);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            if (isNumeric(extras.getString("codigo"))) {
                codigo_qr = Integer.parseInt(extras.getString("codigo"));
                btnContinuar.callOnClick();
                //Toast.makeText(getApplicationContext(), "Codigo Obtenido Correctamente", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imgFoto.setImageBitmap(thumbnail);
                    layoutImagenE.setVisibility(View.VISIBLE);
                    //Obtiene la ruta donde se encuentra guardada la imagen.
                    imageurl = getRealPathFromURI(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
        else if(requestCode == 3 && resultCode == 4) {
            try {
                Toast.makeText(getApplicationContext(), "Editamos", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == 3 && resultCode == 5) {
            try {
                Toast.makeText(getApplicationContext(), "Eliminamos", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Bitmap im = ManipuladorImagen.redimencionar(getApplicationContext(),imageBitmap,200,200);
            //rutaImg = magicalCamera.savePhotoInMemoryDevice(
            //        imageBitmap,// bitmap de la foto a guardar
            //        "img",// nombre con el que se guardará la imgImagen
            //        "Imagenes",// nombre de la carpeta donde se guardarán las fotos
            //        MagicalCamera.JPEG,// formato de compresion
            //        true // true: le agrega la fecha al nombre de la foto para no replicarlo
            //);
            //imgFoto.setImageBitmap(img);

            //magicalCamera.resultPhoto(requestCode, resultCode, data);
        //Toast.makeText(getApplicationContext(), String.valueOf(requestCode), Toast.LENGTH_LONG).show();
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
}