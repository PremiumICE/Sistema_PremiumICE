package premium.conservadores.sistema_conservadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;

class Adapter_Productos extends RecyclerView.Adapter<Adapter_Productos.ViewHolder> {

    private List<clsProductos> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    // data is passed into the constructor
    Adapter_Productos(Context context, List<clsProductos> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.productoitem_recycler, parent, false);
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        clsProductos producto = mData.get(position);
        int posicionitem = position;
        holder.lblProducto.setText(producto.Nombre_P);
        holder.lblPiezas.setText(String.valueOf(producto.Piezas));
        holder.lblPrecioP.setText(String.valueOf(producto.Precio_P));
        holder.lblTotal.setText(String.valueOf(producto.Piezas*producto.Precio_P));
        holder.cbTipoP.setOnCheckedChangeListener(null);
        if(mData.get(position).getTipoPago()!=2)
            holder.cbTipoP.setEnabled(false);
        //if true, your checkbox will be selected, else unselected
        boolean tipopago = true;
        if(mData.get(position).getEleccion_Pago()==1)
            tipopago = false;
        holder.cbTipoP.setChecked(tipopago);
        holder.cbTipoP.setText(mData.get(position).getTipo());
        holder.cbTipoP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int ti = 0;
                if(!isChecked)
                    ti = 1;
                mData.get(holder.getAdapterPosition()).setEleccion_Pago(ti);
                if(isChecked)
                    mData.get(holder.getAdapterPosition()).setTipo("Contado");
                else
                    mData.get(holder.getAdapterPosition()).setTipo("Credito");
                holder.cbTipoP.setText(mData.get(posicionitem).getTipo());
            }
        });
        /*holder.lblBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mData.get(holder.getAdapterPosition()).getPiezas() > 1){
                        mData.get(holder.getAdapterPosition()).setPiezas(mData.get(holder.getAdapterPosition()).getPiezas() - 1);
                        notifyItemChanged(posicionitem, mData.size());
                    }else {
                        mData.remove(posicionitem);
                        notifyItemRemoved(posicionitem);
                        notifyItemRangeChanged(posicionitem, mData.size());
                    }
                }catch (Exception ex){
                    Toast.makeText(v.getContext(), "Ya no hay mas productos a quitar "+ex,Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lblProducto,lblPiezas, lblTotal, lblPrecioP;
        CheckBox cbTipoP;
        ViewHolder(View itemView) {
            super(itemView);
            lblProducto = itemView.findViewById(R.id.tr_lblNombre_prod);
            cbTipoP = itemView.findViewById(R.id.tr_cbTipoP_Prod);
            lblPiezas = itemView.findViewById(R.id.lblPiezasP);
            lblPrecioP = itemView.findViewById(R.id.tr_lblPrecio_Prod_as);
            lblTotal = itemView.findViewById(R.id.tr_lblTotal_Prod_as);
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