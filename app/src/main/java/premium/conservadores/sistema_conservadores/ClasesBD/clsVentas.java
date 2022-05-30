package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsVentas {
    public clsVentas() {

    }
    public clsVentas(int Id_Ven,Double To ,Double Tot_Cont,Double Tot_Cred ,int Pi_T,int Tip_Pa,int Fec, int Id_Usu,int Id_Cli,int Id_Sit, String Lat,String Long,int Est,
                     String Tiem_Ven, int Env) {
        Id_Venta = Id_Ven;
        Total = To;
        Total_Contado = Tot_Cont;
        Total_Credito = Tot_Cred;
        Piezas = Pi_T;
        Tipo_Pago = Tip_Pa;
        Fecha = Fec;
        Id_Usuario = Id_Usu;
        Id_Cliente = Id_Cli;
        Id_Situacion = Id_Sit;
        Latitud = Lat;
        Longitud = Long;
        Estatus = Est;
        Tiempo_Venta = Tiem_Ven;
        Enviado = Env;
    }
    public int Id_Venta, Id_Cliente, Piezas, Tipo_Pago, Fecha, Id_Usuario, Id_Situacion, Estatus, Enviado;
    public double Total,Total_Contado,Total_Credito;
    public String Latitud,Longitud,Tiempo_Venta;

    public int getId_Venta() {
        return Id_Venta;
    }
    public int getId_Cliente() {
        return Id_Cliente;
    }
    public int getTipo_Pago() {
        return Tipo_Pago;
    }
    public int getFecha() {
        return Fecha;
    }
    public int getId_Usuario() {
        return Id_Usuario;
    }
    public int getId_Situacion() {
        return Id_Situacion;
    }
    public int getPiezas() {
        return Piezas;
    }
    public int getEstatus() {
        return Estatus;
    }
    public int getEnviado() {
        return Enviado;
    }

    public double getTotal() {
        return Total;
    }
    public double getTotal_Contado() {
        return Total_Contado;
    }
    public double getTotal_Credito() {
        return Total_Credito;
    }

    public String getLatitud() {
        return Latitud;
    }
    public String getLongitud() {
        return Longitud;
    }
    public String getTiempo_Venta() {
        return Tiempo_Venta;
    }


}
