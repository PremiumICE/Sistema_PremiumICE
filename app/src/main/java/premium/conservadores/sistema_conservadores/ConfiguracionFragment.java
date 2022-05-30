package premium.conservadores.sistema_conservadores;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import premium.conservadores.sistema_conservadores.Adaptadores.Adaptador_Unidades;
import premium.conservadores.sistema_conservadores.Adaptadores.Adaptador_Usuarios;
import premium.conservadores.sistema_conservadores.Adaptadores.SpinAdapterMotivos;
import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.Clases.clsMotivos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductosMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsUnidades;
import premium.conservadores.sistema_conservadores.ClasesBD.clsUsuarios;

public class ConfiguracionFragment extends Fragment {
    SharedPreferences Configuraciones;
    SharedPreferences.Editor EditarConf;
    LinearLayout llDatos;
    AutoCompleteTextView atxtChoferes, atxtUnidades;
    EditText txtKMInicial;
    TextView lblNumRuta,lblPlacasRuta;
    Button btnGuardar, btnCancelarVista;
    TabLayout tabhost;
    int cargar_ruta;
    Handler handler;
    ArrayList<clsUsuarios> usuarios;
    ArrayList<clsUnidades> unidades;

    private InicioFragment fragmentInicio;
    SQLocal dbLocal;

    public ConfiguracionFragment(){

    }

    public static ConfiguracionFragment newInstance(Integer counter){
        ConfiguracionFragment fragment = new ConfiguracionFragment();
        Bundle args = new Bundle();
        args.putInt("param1", counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Configuraciones = PreferenceManager.getDefaultSharedPreferences(getContext());
        EditarConf = Configuraciones.edit();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_configuracion, container, false);
        llDatos = v.findViewById(R.id.llDatosruta);
        atxtChoferes = v.findViewById(R.id.atxtEncargados);
        atxtUnidades = v.findViewById(R.id.atxtUnidades);
        btnGuardar = v.findViewById(R.id.btnGuardarInformacion);
        txtKMInicial = v.findViewById(R.id.txtKMinicial);
        lblNumRuta = v.findViewById(R.id.lblDatosNumR);
        lblPlacasRuta = v.findViewById(R.id.lblPlacasRuta);
        dbLocal = new SQLocal(getContext());
        tabhost = (TabLayout) getActivity().findViewById(R.id.tabInicio);
        fragmentInicio = (InicioFragment) getActivity().getSupportFragmentManager().getFragments().get(0);
        handler = new Handler();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!atxtUnidades.getText().toString().isEmpty()&&!atxtChoferes.getText().toString().isEmpty()&&
                        !txtKMInicial.getText().toString().isEmpty()){
                    IniciarRuta();
                }else
                {
                    Toast.makeText(getContext(),"Es necesario ingresar toda la información",Toast.LENGTH_SHORT).show();
                }
            }
        });
        usuarios = new ArrayList<clsUsuarios>();
        usuarios = dbLocal.getUsuarios();
        Adaptador_Usuarios adapterUsuarios = new Adaptador_Usuarios(getContext(), R.layout.fragment_configuracion,
                R.id.lbl_name, usuarios);
        atxtChoferes.setAdapter(adapterUsuarios);
        atxtChoferes.setText(Configuraciones.getString("Usuario","Sin usuario"));
        lblNumRuta.setText("Revisa los datos de la ruta: \r\n"+Configuraciones.getString("Ruta","Sin Ruta"));
        unidades = new ArrayList<clsUnidades>();
        unidades = dbLocal.getUnidades();
        Adaptador_Unidades adapterUnidades = new Adaptador_Unidades(getContext(), R.layout.fragment_configuracion,
                R.id.lbl_name, unidades);
        atxtUnidades.setAdapter(adapterUnidades);
        atxtUnidades.setText(Configuraciones.getString("Unidad","Sin Unidad"));
        txtKMInicial.requestFocus();
        lblPlacasRuta.setText(Configuraciones.getString("Placas","Sin Placas"));
        if(Configuraciones.getBoolean("RutaAct", false)) {
            llDatos.setForeground(getResources().getDrawable(android.R.drawable.screen_background_dark_transparent));
            atxtUnidades.setEnabled(false);
            atxtChoferes.setEnabled(false);
            btnGuardar.setEnabled(false);
            txtKMInicial.setEnabled(false);
        }
        return v;
    }

    public AlertDialog MostrarCargaRuta(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.iniciar_ruta, null);

        builder.setView(v);
        alertDialog = builder.create();

        btnCancelarVista = v.findViewById(R.id.btnCancelarCarga);
        alertDialog.setCancelable(false);
        btnCancelarVista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                cargar_ruta = 1;
            }
        });
        return alertDialog;
    }

    public void IniciarRuta(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
        dialogo1.setTitle("Inicio de Ruta");
        dialogo1.setMessage("¿Esta seguro de iniciar la ruta?");
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
                MostrarCargaRuta().show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(cargar_ruta==1) {
                            handler.removeCallbacks(this);
                            cargar_ruta = 0;
                        }
                        else {
                            EditarConf.putString("Encargado",atxtChoferes.getText().toString());
                            EditarConf.putString("Unidad",atxtUnidades.getText().toString());
                            EditarConf.putString("Placas",lblPlacasRuta.getText().toString());
                            EditarConf.putString("KM_Inicial",txtKMInicial.getText().toString());
                            EditarConf.commit();
                            tabhost.getTabAt(0).select();
                            fragmentInicio.IniciarRutaAutomatico();
                            btnCancelarVista.callOnClick();
                            llDatos.setForeground(getResources().getDrawable(android.R.drawable.screen_background_dark_transparent));
                            atxtUnidades.setEnabled(false);
                            atxtChoferes.setEnabled(false);
                            btnGuardar.setEnabled(false);
                            txtKMInicial.setEnabled(false);

                        }
                    }
                }, 3000);
            }
        });
        dialogo1.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogo1, int which) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }

    public void LimpiarDatosRuta(){
        llDatos.setForeground(null);
        atxtUnidades.setEnabled(true);
        atxtChoferes.setEnabled(true);
        btnGuardar.setEnabled(true);
        txtKMInicial.setEnabled(true);
    }
}