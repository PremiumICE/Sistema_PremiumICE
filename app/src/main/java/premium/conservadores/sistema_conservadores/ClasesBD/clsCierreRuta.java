package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsCierreRuta {
    public clsCierreRuta() {

    }
    public clsCierreRuta(int Id_Ru, int Id_Usu, String Ho_In, String Ho_F, String Tie, double Tot_gas,
                         double Tot, double Tot_cont, double Tot_cre, double Tot_efec, int Pie,
                         String Lat_Ini,String Long_Ini,String Lat_Fin,String Long_Fin, double KM_Ini, double KM_Fin,
                         int Fec, int Env) {
        this.Id_Ruta = Id_Ru;
        this.Id_Usuario = Id_Usu;
        this.Piezas = Pie;
        this.Fecha = Fec;
        this.Envio  = Env;
        this.Hora_Inicio  = Ho_In;
        this.Hora_Final = Ho_F;
        this.Tiempo = Tie;
        this.Latitud_Inicio = Lat_Ini;
        this.Longitud_Inicio = Long_Ini;
        this.Latitud_Final = Lat_Fin;
        this.Longitud_Final = Long_Fin;
        this.Total_Gasto = Tot_gas;
        this.Total_Contado = Tot_cont;
        this.Total = Tot;
        this.Total_Credito = Tot_cre;
        this.Total_Efectivo = Tot_efec;
        this.Kilometro_Ini = KM_Ini;
        this.Kilometro_Fin = KM_Fin;
    }
    public String Hora_Inicio,Hora_Final,Tiempo, Latitud_Inicio, Longitud_Inicio, Latitud_Final, Longitud_Final;
    public Integer Id_Usuario, Piezas, Fecha, Envio, Id_Ruta;
    public double Total_Gasto,Total, Total_Contado, Total_Credito,Total_Efectivo, Kilometro_Ini, Kilometro_Fin;

    public int getId_Usuario() {
        return Id_Usuario;
    }
    public int getPiezas() {
        return Piezas;
    }
    public int getFecha() {
        return Fecha;
    }
    public int getEnvio() {
        return Envio;
    }

    public double getTotal_Gasto() {
        return Total_Gasto;
    }
    public double getTotal_Contado() {
        return Total_Contado;
    }
    public double getTotal() {
        return Total;
    }
    public double getTotal_Credito() {
        return Total_Credito;
    }
    public double getTotal_Efectivo() {
        return Total_Efectivo;
    }
    public double getKilometro_Ini() {
        return Kilometro_Ini;
    }
    public double getKilometro_Fin() {
        return Kilometro_Fin;
    }

    public String getHora_Inicio() {
        return Hora_Inicio;
    }
    public String getHora_Final() {
        return Hora_Final;
    }
    public String getTiempo() {
        return Tiempo;
    }
    public String getLatitud_Inicio() {
        return Latitud_Inicio;
    }
    public String getLongitud_Inicio() {
        return Longitud_Inicio;
    }
    public String getLatitud_Final() {
        return Latitud_Final;
    }
    public String getLongitud_Final() {
        return Longitud_Final;
    }


}
