package premium.conservadores.sistema_conservadores.Clases;

import android.graphics.Bitmap;

import java.sql.Time;

public class clsMenus {
    public clsMenus() {

    }
    public clsMenus(String Tit, Bitmap Img, int Tip, String tiem, String Fech) {
        this.Titulo = Tit;
        this.Img_Menu = Img;
        this.Tipo = Tip;
        this.Tiempo = tiem;
        this.Fecha = Fech;
    }
    public String Tiempo, Fecha;
    public String Titulo;
    public Bitmap Img_Menu;
    public Integer Tipo;
    public String getTitulo() {
        return Titulo;
    }
    public Bitmap getImg_Menu() {
        return Img_Menu;
    }
    public Integer getTipo() {
        return Tipo;
    }
    public String getTiempo() {
        return Tiempo;
    }
    public String getFecha(){return Fecha;}
}
