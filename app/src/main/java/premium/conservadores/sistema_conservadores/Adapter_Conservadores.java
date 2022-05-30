package premium.conservadores.sistema_conservadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.NoSuchElementException;

import premium.conservadores.sistema_conservadores.Clases.clsAud_Conservador;
import premium.conservadores.sistema_conservadores.ClasesBD.clsAuditoria;

public class Adapter_Conservadores extends RecyclerView.Adapter<Adapter_Conservadores.ViewHolder> {

    private List<clsAuditoria> mData;
    private LayoutInflater mInflater;
    private Adapter_Conservadores.ItemClickListener mClickListener;
    private Context cont;
    // data is passed into the constructor
    Adapter_Conservadores(Context context, List<clsAuditoria> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.cont = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public Adapter_Conservadores.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.conservadoritem_recycler, parent, false);
        return new Adapter_Conservadores.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(Adapter_Conservadores.ViewHolder holder, int position) {
        String Conservador = String.valueOf(mData.get(position).getQR());
        //int idImg = mData.get(position).getId_Imagen();
        byte[] photo = mData.get(position).getImagen_PC();
        holder.lblTConservador.setText(Conservador);
        Drawable d;
        if(photo!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap bmLocal = BitmapFactory.decodeStream(imageStream);
            d = new BitmapDrawable(cont.getResources(), bmLocal);
        }else{
            Bitmap Icon = BitmapFactory.decodeResource(cont.getResources(), R.drawable.sin_imagen);
            d = new BitmapDrawable(cont.getResources(), Icon);
        }
        holder.img.setImageDrawable(scaleImage(d));
        if(mData.get(position).getAuditado()==1)
            holder.llAuditado.setVisibility(View.VISIBLE);
        else
            holder.llAuditado.setVisibility(View.GONE);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lblTConservador;
        ImageView img;
        LinearLayout llAuditado;
        ViewHolder(View itemView) {
            super(itemView);
            lblTConservador = itemView.findViewById(R.id.lblConservador);
            img = itemView.findViewById(R.id.imgConservador);
            llAuditado = itemView.findViewById(R.id.llauditado);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).Etiqueta;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private Drawable scaleImage(Drawable dra) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null; try {
            Drawable drawing = dra;
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) { // Check bitmap is Ion drawable

        } // Get current dimensions AND the desired bounding box
        int width = 0; try { width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        } int height = bitmap.getHeight();
        int bounding = dpToPx(400);
        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        // Apply the scaled bitmap
        // Now change ImageView's dimensions to match the scaled image
        return result;
    }
    private int dpToPx(int dp) {
        float density = cont.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}