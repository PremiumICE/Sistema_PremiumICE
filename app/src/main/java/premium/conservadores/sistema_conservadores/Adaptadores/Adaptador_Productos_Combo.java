package premium.conservadores.sistema_conservadores.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.core.view.MenuCompat;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;
import premium.conservadores.sistema_conservadores.R;

public class Adaptador_Productos_Combo extends ArrayAdapter<clsProductos> {

    Context context;
    int resource, textViewResourceId;
    List<clsProductos> items, tempItems, suggestions;

    public Adaptador_Productos_Combo(Context context, int resource, int textViewResourceId, List<clsProductos> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<clsProductos>(items); // this makes the difference.
        suggestions = new ArrayList<clsProductos>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_clientes, parent, false);

        }
        clsProductos people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getNombre_P());
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
            String str = ((clsProductos) resultValue).getNombre_P();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (clsProductos people : tempItems) {
                    if (people.getNombre_P().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<clsProductos> filterList = (ArrayList<clsProductos>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (clsProductos people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}