package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsAuditoria {
    public clsAuditoria() {

    }
    public clsAuditoria(int Id_Conserva, int Id_Conserv, int Q, String Etiq, String Ser, int Fe_Ini, int Fe_Lim, int Nec, int Id_Che,
                        String Lat_Rev, String Long_Rev, int Id_Cli_Rev, byte[] Img_Ev, byte[] Img_PC, int Aud) {
        this.Id_ConservadorA = Id_Conserva;
        this.Id_Conservador = Id_Conserv;
        this.QR = Q;
        this.Etiqueta  = Etiq;
        this.Serie  = Ser;
        this.Fecha_Ini = Fe_Ini;
        this.Fecha_Lim = Fe_Lim;
        this.Necesidad = Nec;
        this.Id_CheckList = Id_Che;
        this.Latitud = Lat_Rev;
        this.Longitud = Long_Rev;
        this.Id_Cliente = Id_Cli_Rev;
        this.Imagen_Rev = Img_Ev;
        this.Imagen_PC = Img_PC;
        this.Auditado = Aud;
    }
    public String Etiqueta,Latitud,Longitud, Serie;
    public Integer Id_ConservadorA, Id_Conservador, QR,Fecha_Ini,Fecha_Lim,Necesidad,Id_CheckList,Id_Cliente, Auditado;
    public byte[] Imagen_Rev,Imagen_PC;

    public int getId_ConservadorA() {
        return Id_ConservadorA;
    }
    public int getId_Conservador() {
        return Id_Conservador;
    }
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
        return Id_CheckList;
    }
    public int getId_Cliente_Rev() {
        return Id_Cliente;
    }
    public int getAuditado() {
        return Auditado;
    }

    public byte[] getImagen_Ev() {
        return Imagen_Rev;
    }
    public byte[] getImagen_PC() {
        return Imagen_PC;
    }

    public String getEtiqueta() {
        return Etiqueta;
    }
    public String getNSerie() {
        return Serie;
    }
    public String getLatitud_Rev() {
        return Latitud;
    }
    public String getLongitud_Rev() {
        return Longitud;
    }

}
