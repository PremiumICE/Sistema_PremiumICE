package premium.conservadores.sistema_conservadores.Clases;

import android.graphics.Bitmap;

import com.google.type.DateTime;

import java.time.LocalDateTime;

public class cls_CierreRuta {
    public cls_CierreRuta() {

    }
    public cls_CierreRuta(int Id_R, int Id_Us, String Inicio, String Fin, String tiem, Double Tot, int Pie, String Lat_Ini,
                          String Lon_Ini, String Lat_Fin, String Lon_Fin, int Id_Mermas) {
        this.Id_Ruta = Id_R;
        this.Id_Usuario = Id_Us;
        this.Fecha_Inicio = Inicio;
        this.Fecha_Final = Fin;
        this.Tiempo = tiem;
        this.Monto = Tot;
        this.Piezas = Pie;
        this.Latitud_Inicio = Lat_Ini;
        this.Longitud_Inicio = Lon_Ini;
        this.Latitud_Fin = Lat_Fin;
        this.Longitud_Fin = Lon_Fin;
        this.Id_Mermas = Id_Mermas;
    }
    public String Tiempo, Latitud_Inicio, Latitud_Fin, Longitud_Inicio, Longitud_Fin,Fecha_Inicio,Fecha_Final;
    public Double Monto;
    public Integer Id_Ruta, Id_Usuario, Piezas, Id_Mermas;


    public String getTiempo() {
        return Tiempo;
    }
    public String getLatitud_Inicio() {
        return Latitud_Inicio;
    }
    public String getLongitud_Inicio(){return Longitud_Inicio;}
    public String getLatitud_Fin(){return Latitud_Fin;}
    public String getLongitud_Fin(){return Longitud_Fin;}
    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }
    public String getFecha_Final() {
        return Fecha_Final;
    }

    public Integer getId_Ruta() {
        return Id_Ruta;
    }
    public Integer getId_Usuario() {
        return Id_Usuario;
    }
    public Integer getPiezas() {
        return Piezas;
    }
    public Integer getId_Mermas() {
        return Id_Mermas;
    }


    public Double getTotal() {
        return Monto;
    }

}
