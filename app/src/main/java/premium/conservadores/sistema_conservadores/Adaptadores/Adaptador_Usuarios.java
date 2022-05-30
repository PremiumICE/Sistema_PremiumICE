package premium.conservadores.sistema_conservadores.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import premium.conservadores.sistema_conservadores.ClasesBD.clsUsuarios;
import premium.conservadores.sistema_conservadores.R;

public class Adaptador_Usuarios extends ArrayAdapter<clsUsuarios> {

    Context context;
    int resource, textViewResourceId;
    List<clsUsuarios> items, tempItems, suggestions;

    public Adaptador_Usuarios(Context context, int resource, int textViewResourceId, List<clsUsuarios> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<clsUsuarios>(items); // this makes the difference.
        suggestions = new ArrayList<clsUsuarios>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_clientes, parent, false);

        }
        clsUsuarios people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getUsuario());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((clsUsuarios) resultValue).getUsuario();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (clsUsuarios people : tempItems) {
                    if (people.getUsuario().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<clsUsuarios> filterList = (ArrayList<clsUsuarios>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (clsUsuarios people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}