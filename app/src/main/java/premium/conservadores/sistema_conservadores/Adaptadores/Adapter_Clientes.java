package premium.conservadores.sistema_conservadores.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.ClasesBD.clsClientes;
import premium.conservadores.sistema_conservadores.R;

public class Adapter_Clientes extends RecyclerView.Adapter<Adapter_Clientes.ViewHolder> implements Filterable {
    private List<clsClientes> clientes;
    private List<clsClientes> clientesFilter;
    private CustomFilter mFilter;
    private SQLocal dbLocal;

    public Adapter_Clientes(List<clsClientes> strings, Context context) {
        clientes = strings;
        clientesFilter = new ArrayList<>();
        clientesFilter.addAll(clientes);
        dbLocal = new SQLocal(context);
        this.mFilter = new CustomFilter(Adapter_Clientes.this);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clienteitem_recycler, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ViewHolder)holder).words.setText(clientesFilter.get(position).getNombre());
        int total = dbLocal.getListasPrecios(dbLocal.getClienteId(clientesFilter.get(position).getId_Cliente()).get(0).getId_Lista()).size();
        ((ViewHolder)holder).listaTotal.setText(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return clientesFilter.size();
    }

    public List<clsClientes> getClientesFilter(){
        return clientesFilter;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView words, listaTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            words=(TextView)itemView.findViewById(R.id.lblClienteRec);
            listaTotal=(TextView)itemView.findViewById(R.id.lblListaPrecios);

        }
    }



    public class CustomFilter extends Filter {
        private Adapter_Clientes listAdapter;

        private CustomFilter(Adapter_Clientes listAdapter) {
            super();
            this.listAdapter = listAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            clientesFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                clientesFilter.addAll(clientes);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final clsClientes cliente : clientes) {
                    if (cliente.getNombre().toLowerCase().contains(filterPattern)) {
                        clientesFilter.add(cliente);
                    }
                }
            }
            results.values = clientesFilter;
            results.count = clientesFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}


