package premium.conservadores.sistema_conservadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import premium.conservadores.sistema_conservadores.Clases.clsMenus;

class Adapter_Menus extends RecyclerView.Adapter<Adapter_Menus.ViewHolder> {
    private List<clsMenus> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity ac;
    private Context Cont;
    private long TiempoTrans = 0;
    SharedPreferences Configuraciones;
    SharedPreferences.Editor EditarConf;
    public String Tiempo = "";
    // data is passed into the constructor
    Adapter_Menus(Context context, List<clsMenus> data, Activity act) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.ac = act;
        this.Cont = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.menucitem_recycler, parent, false);
        Configuraciones = PreferenceManager.getDefaultSharedPreferences(Cont);
        EditarConf = Configuraciones.edit();
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position).getTitulo();
        holder.txtTitulo.setText(animal);
        final long tiempo = 5000;
        int pos = position;
        if(mData.get(position).getTipo() == 1){
            holder.txtTitulo.setTextColor(ColorStateList.valueOf(Color.WHITE));
            holder.llCard.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(139,0,0)));
            holder.txtHoraIni.setVisibility(View.VISIBLE);
            holder.txtTiempoRuta.setVisibility(View.VISIBLE);
            holder.llInfoR.setVisibility(View.VISIBLE);
            holder.txtHoraIni.setText("Hora de Inicio: "+mData.get(position).getTiempo());
            holder.txtTiempoRuta.setText("Tiempo Ruta: "+Configuraciones.getString("Tiempo","00:00"));
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if(mData.size()>1) {
                        holder.txtTiempoRuta.setText(CalcularTiempo(pos));
                        handler.postDelayed(this, tiempo);
                    }else
                        handler.removeCallbacks(this);
                }
            }, tiempo);
        }else if(mData.get(position).getTipo() == 2){
            holder.txtTitulo.setTextColor(ColorStateList.valueOf(Color.WHITE));
            holder.llInfoR.setVisibility(View.GONE);
            holder.llCard.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,34,146)));
            holder.txtHoraIni.setVisibility(View.GONE);
            holder.llMenu.setImageBitmap(scaleImage(mData.get(position).getImg_Menu()));
        }
        else {
            holder.txtTitulo.setTextColor(ColorStateList.valueOf(Color.BLACK));
            holder.llMenu.setImageBitmap(scaleImage(mData.get(position).getImg_Menu()));
            holder.llCard.setBackgroundTintList(null);
        }
        /*if(animal.contains("Mis Ventas Guardadas:"))
            holder.llMenu.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));*/
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitulo, txtHoraIni, txtTiempoRuta;
        ImageView llMenu;
        LinearLayout llCard,llInfoR;
        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.tvTypeName);
            llMenu = itemView.findViewById(R.id.img_room);
            llCard = itemView.findViewById(R.id.card_view);
            llInfoR = itemView.findViewById(R.id.llInformacionRuta);
            txtHoraIni = itemView.findViewById(R.id.lblHoraInicio);
            txtTiempoRuta = itemView.findViewById(R.id.lblTiempoRuta);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return mData.get(id).getTitulo();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    //Funciones independientes del adaptador

    //Cambiar tamaño de imagen desde su bitmap --Donde esta 60 es la nueva altura
    private Bitmap scaleImage(Bitmap dra) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = dra; try {
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) { // Check bitmap is Ion drawable

        } // Get current dimensions AND the desired bounding box
        int width = 0; try { width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        } int height = bitmap.getHeight();
        int bounding = dpToPx(60);
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
        return scaledBitmap;
    }
    private int dpToPx(int dp) {
        float density = Cont.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
    //Calcular el tiempo que se lleva en ruta, se obtiene la fecha y hora guardadas en el telefono y se saca la diferencia
    //de dias, horas y minutos
    public String CalcularTiempo(int posi){
        String NuevaFecha = "";
        String h1 = mData.get(posi).getTiempo();
        String f1 = mData.get(posi).getFecha();
        String h2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String f2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now = df.parse(f1+" "+h1);
            Date date = df.parse(f2+" "+h2);
            long between = (date.getTime() - now.getTime()) / 1000; // División por 1000 es convertir a segundos

            long day = between/(24*3600);
            long hour = between%(24*3600)/3600;
            long min = between%3600/60;
            //long second = between%60/60;
            //long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            String dia = String.valueOf((day));
            if(Math.abs(day) < 10)
                dia = "0"+dia;
            String hora = String.valueOf((hour));
            if(Math.abs(hour) < 10)
                hora = "0"+hora;
            String minuto = String.valueOf((min));
            if(Math.abs(min) < 10)
                minuto = "0"+minuto;
            NuevaFecha = "Tiempo Ruta: "+dia+" "+hora+":"+minuto;
            EditarConf.putString("Tiempo",dia+" "+hora+":"+minuto);
            EditarConf.commit();
        }catch (Exception ex){
            NuevaFecha = "Tiempo Ruta: 00 00:00";
            EditarConf.putString("Tiempo","00 00:00");
            EditarConf.commit();
        }
        Tiempo = NuevaFecha;
        return NuevaFecha;
    }
}