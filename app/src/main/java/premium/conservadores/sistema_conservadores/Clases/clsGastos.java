package premium.conservadores.sistema_conservadores.Clases;

import android.graphics.Bitmap;

public class clsGastos {
    public clsGastos() {

    }
    public clsGastos(int Id_Gas, String Rub, String Fec, double Tot, String InfoAd) {
        this.Id_Gasto = Id_Gas;
        this.Rubro = Rub;
        this.Fecha = Fec;
        this.Total = Tot;
        this.Informacion_Adicional = InfoAd;
    }
    public int Id_Gasto;
    public String Rubro, Fecha, Informacion_Adicional;
    public double Total;

    public int getId_Gasto() {
        return Id_Gasto;
    }

    public String getRubro() {
        return Rubro;
    }
    public String getFecha() {
        return Fecha;
    }
    public String getInformacion_Adicional() {
        return Informacion_Adicional;
    }

    public double getTotal() {
        return Total;
    }
}
