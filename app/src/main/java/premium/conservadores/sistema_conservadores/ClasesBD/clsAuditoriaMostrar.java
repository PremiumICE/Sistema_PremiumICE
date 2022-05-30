package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsAuditoriaMostrar {
    public clsAuditoriaMostrar() {

    }
    public clsAuditoriaMostrar(int Id_Conserv, int Q, String Etiq, int Fe_Ini, int Fe_Lim, int Nec, int Id_Che,
                               String Lat_Rev, String Long_Rev, int Id_Cli_Rev, byte[] Img_Ev, byte[] Img_PC) {
        this.Id_Conservador = Id_Conserv;
        this.QR = Q;
        this.Etiqueta  = Etiq;
        this.Fecha_Ini = Fe_Ini;
        this.Fecha_Lim = Fe_Lim;
        this.Necesidad = Nec;
        this.Id_CheckL = Id_Che;
        this.Latitud_Rev = Lat_Rev;
        this.Longitud_Rev = Long_Rev;
        this.Id_Cliente_Rev = Id_Cli_Rev;
        this.Imagen_Ev = Img_Ev;
        this.Imagen_PC = Img_PC;
    }
    public String Etiqueta,Latitud_Rev,Longitud_Rev;
    public Integer Id_Conservador, QR,Fecha_Ini,Fecha_Lim,Necesidad,Id_CheckL,Id_Cliente_Rev;
    public byte[] Imagen_Ev,Imagen_PC;

    public int getQR() {
        return QR;
    }
    public int getFecha_Ini() {
        return Fecha_Ini;
    }
    public int getFecha_Lim() {
        return Fecha_Lim;
    }
    public int getNecesidad() {
        return Necesidad;
    }
    public int getId_CheckL() {
        return Id_CheckL;
    }
    public int getId_Cliente_Rev() {
        return Id_Cliente_Rev;
    }

    public byte[] getImagen_Ev() {
        return Imagen_Ev;
    }
    public byte[] getImagen_PC() {
        return Imagen_PC;
    }

    public String getEtiqueta() {
        return Etiqueta;
    }
    public String getLatitud_Rev() {
        return Latitud_Rev;
    }
    public String getLongitud_Rev() {
        return Longitud_Rev;
    }

}
