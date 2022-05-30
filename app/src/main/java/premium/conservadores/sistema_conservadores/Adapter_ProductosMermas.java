package premium.conservadores.sistema_conservadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductosMermas;

class Adapter_ProductosMermas extends RecyclerView.Adapter<Adapter_ProductosMermas.ViewHolder> {

    private List<clsProductosMermas> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    // data is passed into the constructor
    Adapter_ProductosMermas(Context context, ArrayList<clsProductosMermas> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mermaitem_recycler, parent, false);
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int pos = position;
        clsProductosMermas producto = mData.get(position);
        holder.lblProducto.setText(producto.Nombre_P);
        holder.lblPiezas.setText(String.valueOf(producto.Piezas));
        holder.lblMotivo.setText(producto.Motivo);
        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mData.get(pos).getPiezas() > 1){
                        mData.get(pos).setPiezas(mData.get(pos).getPiezas() - 1);
                        notifyItemChanged(pos, mData.size());
                    }else {
                        mData.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, mData.size());
                    }
                }catch (Exception ex){
                    Toast.makeText(v.getContext(), "Ya no hay mas productos a quitar "+ex,Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.lblProducto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), mData.get(pos).Nombre_P,Toast.LENGTH_LONG);
                return false;
            }
        });
                //holder.lblPiezas.setText(String.valueOf(producto.Piezas));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lblProducto, lblPiezas, lblMotivo;
        Button btnBorrar;

        ViewHolder(View itemView) {
            super(itemView);
            lblPiezas = itemView.findViewById(R.id.lblPiezasMermas);
            lblProducto = itemView.findViewById(R.id.lblProductoMerma);
            lblMotivo = itemView.findViewById(R.id.lblMotivomerma);
            btnBorrar = itemView.findViewById(R.id.btnQuitarMerma);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).Nombre_P;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}