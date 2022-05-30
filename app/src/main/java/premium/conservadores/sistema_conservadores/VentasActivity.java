package premium.conservadores.sistema_conservadores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.ClasesBD.clsVentas;

public class VentasActivity extends AppCompatActivity implements Adaptador_Ventas.ItemClickListener{
    Toolbar tlbVolver;
    RecyclerView recVentas;
    Adaptador_Ventas adapVentas;
    ArrayList<clsVentas> listaVentas;
    SQLocal dbLocal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        tlbVolver = (Toolbar) findViewById(R.id.tlbVolver);
        recVentas = (RecyclerView) findViewById(R.id.recVentasG1);

        dbLocal = new SQLocal(this);
        listaVentas = new ArrayList<clsVentas>();
        listaVentas = dbLocal.getVentas();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recVentas.getContext(),
                layoutManager.getOrientation());
        recVentas.addItemDecoration(dividerItemDecoration);
        recVentas.setLayoutManager(layoutManager);
        adapVentas = new Adaptador_Ventas(getApplicationContext(), listaVentas);
        adapVentas.setClickListener((Adaptador_Ventas.ItemClickListener) this);
        recVentas.setAdapter(adapVentas);

        setSupportActionBar(tlbVolver);
        getSupportActionBar().setTitle("Mis Ventas");
        getSupportActionBar().setSubtitle("Revisa tus ventas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tlbVolver.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Toast.makeText(getApplicationContext(), String.valueOf(dbLocal.getVentas().size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}