package premium.conservadores.sistema_conservadores;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import premium.conservadores.sistema_conservadores.Adaptadores.SpinAdapterMotivos;
import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.Clases.clsGastos;
import premium.conservadores.sistema_conservadores.Clases.clsMotivos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductosMermas;

public class GastosActivity extends AppCompatActivity {
    Adapter_Gastos adapterGastos;
    ArrayList<clsGastos> listaGastos;
    clsGastos gasto;
    SQLocal dbLocal;
    String rubro = "";
    Toolbar tblGastos;
    EditText txtTotal;
    RecyclerView rvGastos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        Spinner spRubros = findViewById(R.id.spRubrosG);
        txtTotal = findViewById(R.id.txtTotalG);
        Button btnAgregarG = findViewById(R.id.btnAgregarGastos);
        rvGastos = (RecyclerView) findViewById(R.id.rvGastosO);
        tblGastos = (Toolbar) findViewById(R.id.tblGastosOp);


        dbLocal = new SQLocal(this);
        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_drop, new String[]{"Comida", "Gasolina",
                "Mecanico", "Hospedaje"});


        setSupportActionBar(tblGastos);
        getSupportActionBar().setTitle("Gastos Operativos");
        getSupportActionBar().setSubtitle("Agrega los gastos que se generan durante la ruta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tblGastos.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listaGastos = new ArrayList<clsGastos>();
        listaGastos = dbLocal.getGastos();

        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        adapterGastos = new Adapter_Gastos(this, listaGastos);
        rvGastos.setAdapter(adapterGastos);

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
                if(!txtTotal.getText().toString().isEmpty()) {
                    gasto = new clsGastos();
                    gasto.Rubro = rubro;
                    gasto.Total = Double.parseDouble(txtTotal.getText().toString());
                    gasto.Fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                    InformacionAdicional();
                }else
                    Toast.makeText(getApplicationContext(), "Agrega un valor al rubro", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void InformacionAdicional(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.informacion_adicional_gasto, null);
        EditText txtInfoAdicional = v.findViewById(R.id.txtInfoAdicional);
        Button btnAceptar = v.findViewById(R.id.btnAceptarInfoA);
        Button btnCancelar = v.findViewById(R.id.btnCancelarInfoA);
        TextView lblInfoProd = v.findViewById(R.id.lblGastoSeleccionado);
        builder.setView(v);
        alertDialog = builder.create();
        txtInfoAdicional.requestFocus();
        lblInfoProd.setText(gasto.getRubro());
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Info = "Sin Información Adicional";
                if(!txtInfoAdicional.getText().toString().isEmpty())
                    Info = txtInfoAdicional.getText().toString();

                gasto.Informacion_Adicional = Info;
                listaGastos.add(gasto);
                adapterGastos.notifyDataSetChanged();
                rvGastos.setAdapter(adapterGastos);

                dbLocal.InsertGastos(rubro, Double.parseDouble(txtTotal.getText().toString()),
                        Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))),
                        Info);
                txtTotal.setText("");
                Toast.makeText(getApplicationContext(), "Costo Agregado Correctamente", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}