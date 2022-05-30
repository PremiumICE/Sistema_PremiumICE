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

import premium.conservadores.sistema_conservadores.ClasesBD.clsListasPrecios;

class Adapter_ProductosListaPre extends RecyclerView.Adapter<Adapter_ProductosListaPre.ViewHolder> {

    private List<clsListasPrecios> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    // data is passed into the constructor
    Adapter_ProductosListaPre(Context context, List<clsListasPrecios> data) {
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
        clsListasPrecios producto = mData.get(position);
        int posicionitem = position;
        holder.lblProducto.setText(producto.Producto);
        holder.lblPiezas.setText(String.valueOf(producto.Piezas));
        holder.cbTipoP.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
        boolean tipopago = true;
        if(mData.get(position).getTipoPago()==1)
            tipopago = false;
        holder.cbTipoP.setChecked(tipopago);
        holder.cbTipoP.setText(mData.get(position).getTipo());
        holder.cbTipoP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int ti = 0;
                if(!isChecked)
                    ti = 1;
                mData.get(holder.getAdapterPosition()).setTipoPago(ti);
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
        TextView lblProducto,lblPiezas, lblBorrar;
        CheckBox cbTipoP;
        ViewHolder(View itemView) {
            super(itemView);
            lblProducto = itemView.findViewById(R.id.tr_lblNombre_prod);
            cbTipoP = itemView.findViewById(R.id.tr_cbTipoP_Prod);
            lblPiezas = itemView.findViewById(R.id.lblPiezasP);
            lblBorrar = itemView.findViewById(R.id.tr_lblTotal_Prod_as);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).Producto;
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