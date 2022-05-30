package premium.conservadores.sistema_conservadores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.Clases.clsGastos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductosMermas;

public class MermasActivity extends AppCompatActivity {
    Adapter_ProductosMermas adapterMerma;
    ArrayList<clsProductosMermas> listaMermas;
    clsProductosMermas Merma;
    SQLocal dbLocal;
    String producto = "", Motivo = "";
    Toolbar tblMerma;
    EditText txtTotalM;
    RecyclerView rvMermas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mermas);

        Spinner spProdsMermas = findViewById(R.id.spProdMermas);
        Spinner spMotMermas = findViewById(R.id.spMotivoM);

        txtTotalM = findViewById(R.id.txtPiezasM);
        Button btnAgregarM = findViewById(R.id.btnAgregarMerma);
        rvMermas = (RecyclerView) findViewById(R.id.rvMermasF);
        tblMerma = (Toolbar) findViewById(R.id.tblMermasInfo);


        dbLocal = new SQLocal(this);
        rvMermas.setLayoutManager(new LinearLayoutManager(this));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_drop, new String[]{"Motivo 1", "Motivo 2",
                "Motivo 3", "Motivo 4"});

        setSupportActionBar(tblMerma);
        getSupportActionBar().setTitle("Mermas de producto");
        getSupportActionBar().setSubtitle("Agrega las mermas que se generaron durante la ruta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tblMerma.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listaMermas = new ArrayList<clsProductosMermas>();
        listaMermas = dbLocal.getProdsMermas();

        rvMermas.setLayoutManager(new LinearLayoutManager(this));
        adapterMerma = new Adapter_ProductosMermas(this, listaMermas);
        rvMermas.setAdapter(adapterMerma);

        spMotMermas.setAdapter(adapter);

        spProdsMermas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtTotalM.requestFocus();
                producto = spProdsMermas.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAgregarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtTotalM.getText().toString().isEmpty()) {
                    Merma = new clsProductosMermas();
                    Merma.IdProducto = 1;
                    Merma.Piezas = Integer.parseInt(txtTotalM.getText().toString());
                    Merma.Motivo = Motivo;
                }else
                    Toast.makeText(getApplicationContext(), "Agrega un valor al rubro", Toast.LENGTH_SHORT).show();
            }
        });
    }
}