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

import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.ClasesBD.clsListasPrecios;
import premium.conservadores.sistema_conservadores.ClasesBD.clsVentas;

public class Adaptador_Ventas extends RecyclerView.Adapter<Adaptador_Ventas.ViewHolder> {

    private List<clsVentas> mData;
    private LayoutInflater mInflater;
    private Adaptador_Ventas.ItemClickListener mClickListener;
    SQLocal dbLocal;
    // data is passed into the constructor
    Adaptador_Ventas(Context context, List<clsVentas> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        dbLocal = new SQLocal(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public Adaptador_Ventas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ventasitem_recycler, parent, false);
        return new Adaptador_Ventas.ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(Adaptador_Ventas.ViewHolder holder, int position) {
        clsVentas producto = mData.get(position);
        holder.lblPiezasVenG.setText(String.valueOf(producto.Piezas));
        String usu = "";
        if(producto.Id_Cliente!=0)
            usu = dbLocal.getNombreCliente(producto.Id_Cliente);
        else
            usu = "Publico General";
        holder.lblProductoVenG.setText(usu);
        holder.lblTotalVenG.setText(String.valueOf(producto.Total));
        //if true, your checkbox will be selected, else unselected
        holder.cbEnvioVen.setEnabled(false);
        if(mData.get(position).Enviado==1)
            holder.cbEnvioVen.setChecked(true);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lblPiezasVenG,lblProductoVenG, lblTotalVenG;
        CheckBox cbEnvioVen;
        ViewHolder(View itemView) {
            super(itemView);
            lblPiezasVenG = itemView.findViewById(R.id.lblPiezasVentaG);
            lblProductoVenG = itemView.findViewById(R.id.lblProductoVentaG);
            lblTotalVenG = itemView.findViewById(R.id.lblTotalVentaG);
            cbEnvioVen = itemView.findViewById(R.id.cbVentaG);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).getLatitud();
    }

    // allows clicks events to be caught
    void setClickListener(Adaptador_Ventas.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}