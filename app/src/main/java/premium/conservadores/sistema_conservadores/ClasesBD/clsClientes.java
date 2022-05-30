package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsClientes {
    public clsClientes() {

    }
    public clsClientes(int Id_Cli, String Nom, String Ubi, String Lat, String Long, int Id_Lis, int Tip_pa) {
        this.Id_Cliente = Id_Cli;
        this.Id_Lista = Id_Lis;
        this.Nombre = Nom;
        this.Ubicacion = Ubi;
        this.Latitud = Lat;
        this.Longitud = Long;
        this.Tipo_Pago = Tip_pa;
    }
    public int Id_Cliente, Id_Lista, Tipo_Pago;
    public String Nombre, Ubicacion, Latitud, Longitud;

    public int getId_Cliente() {
        return Id_Cliente;
    }
    public int getId_Lista() {
        return Id_Lista;
    }
    public int getTipo_Pago() {
        return Tipo_Pago;
    }
    public String getNombre() {
        return Nombre;
    }
    public String getUbicacion() {
        return Ubicacion;
    }
    public String getLatitud() {
        return Latitud;
    }
    public String getLongitud() {
        return Longitud;
    }

}
