package premium.conservadores.sistema_conservadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import premium.conservadores.sistema_conservadores.Clases.SQLocal;
import premium.conservadores.sistema_conservadores.Clases.clsGastos;

class Adapter_Gastos extends RecyclerView.Adapter<Adapter_Gastos.ViewHolder> {
    private List<clsGastos> mData;
    private LayoutInflater mInflater;
    private Adapter_Gastos.ItemClickListener mClickListener;
    SQLocal dbLocal;
    Context cont;
    Adapter_Gastos(Context context, List<clsGastos> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        dbLocal = new SQLocal(context);
        this.cont = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.itemgastos_recycler, parent, false);

        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(Adapter_Gastos.ViewHolder holder, int position) {
        int pos = position;
        holder.txtRubro.setText(mData.get(position).getRubro());
        holder.txtTotal.setText(String.valueOf(mData.get(position).getTotal()));

        holder.txtInfoAd.setText(String.valueOf(mData.get(position).getInformacion_Adicional()));

        holder.btnQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbLocal.deleteGasto(mData.get(pos).getId_Gasto());
                mData.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtRubro, txtTotal, txtInfoAd;
        Button btnQuitar;
        ViewHolder(View itemView) {
            super(itemView);
            txtRubro = itemView.findViewById(R.id.lblRubroG);
            txtTotal = itemView.findViewById(R.id.lblTotalG);
            txtInfoAd = itemView.findViewById(R.id.lblDescripcionAdicionalG);
            btnQuitar = itemView.findViewById(R.id.btnQuitarG);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return mData.get(id).getRubro();
    }

    // allows clicks events to be caught
    void setClickListener(Adapter_Gastos.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}