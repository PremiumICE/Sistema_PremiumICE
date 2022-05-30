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

class Adapter_ResumenProd extends RecyclerView.Adapter<Adapter_ResumenProd.ViewHolder> {

    private List<clsProductos> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    // data is passed into the constructor
    Adapter_ResumenProd(Context context, List<clsProductos> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.resumenitem_recycler, parent, false);
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        clsProductos producto = mData.get(position);
        int posicionitem = position;
        holder.lblProducto.setText(producto.Nombre_P);
        holder.lblPiezas.setText(String.valueOf(producto.Piezas));
        holder.lblTipo.setText(String.valueOf(producto.Tipo));
        holder.lblPrecio.setText(String.valueOf(producto.Precio_P));
        holder.lblTotal.setText(String.valueOf(producto.Precio_P*producto.Piezas));

        //if true, your checkbox will be selected, else unselected
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lblProducto,lblPiezas,lblTipo, lblPrecio, lblTotal;
        ViewHolder(View itemView) {
            super(itemView);
            lblProducto = itemView.findViewById(R.id.tr_lblNombre_prodR);
            lblTipo = itemView.findViewById(R.id.tr_lblTipoR);
            lblPiezas = itemView.findViewById(R.id.lblPiezasPR);
            lblPrecio = itemView.findViewById(R.id.tr_lblPrecio_prodR);
            lblTotal = itemView.findViewById(R.id.tr_lblTotal_prodR);

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