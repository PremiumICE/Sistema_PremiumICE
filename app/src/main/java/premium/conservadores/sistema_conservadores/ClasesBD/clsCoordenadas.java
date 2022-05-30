package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsCoordenadas {
    public clsCoordenadas() {

    }
    public clsCoordenadas(int Id_Coor, String Lat, String Long, int Id_Us, String Fec) {
        this.Id_Coordenada = Id_Coor;
        this.Latitud = Lat;
        this.Longitud = Long;
        this.Id_Usuario = Id_Us;
        this.Fecha = Fec;
    }
    private int Id_Coordenada, Id_Usuario;
    private String Latitud, Longitud,Fecha;

    public int getId_Coordenada() {
        return Id_Coordenada;
    }
    public int getId_Usuario() {
        return Id_Usuario;
    }
    public String getFecha() {
        return Fecha;
    }
    public String getLatitud() {
        return Latitud;
    }
    public String getLongitud() {
        return Longitud;
    }
}
