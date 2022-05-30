package premium.conservadores.sistema_conservadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.Clases.clsVentasGuardadas;

class Adapter_VentasG extends RecyclerView.Adapter<Adapter_VentasG.ViewHolder> {
    SQLocal dbLocal;
    private List<clsVentasGuardadas> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    // data is passed into the constructor
    Adapter_VentasG(Context context, List<clsVentasGuardadas> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        dbLocal = new SQLocal(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ventagitem_recycler, parent, false);
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        clsVentasGuardadas venta = mData.get(position);
        holder.lblIdProd.setText(String.valueOf(venta.getId_Venta()));
        holder.lblCliente.setText(dbLocal.getNombreCliente(venta.getId_Cliente()));
        holder.lblTotal.setText(String.valueOf(venta.getTotal()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lblIdProd, lblCliente, lblTotal;
        ViewHolder(View itemView) {
            super(itemView);
            lblIdProd = itemView.findViewById(R.id.lblIdVentaG);
            lblCliente = itemView.findViewById(R.id.lblClienteVentaG);
            lblTotal = itemView.findViewById(R.id.lblTotProdVentaG);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    Integer getItem(int id) {
        return mData.get(id).Id_Venta;
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