package premium.conservadores.sistema_conservadores.Clases;

public class clsMotivos {
    public clsMotivos() {
    }
    public clsMotivos(int Id_Mot, String Mot, String Desc) {
        this.Id_Motivo = Id_Mot;
        this.Motivo = Mot;
        this.Descripcion = Desc;
    }
    public int Id_Motivo;
    public String Motivo;
    public String Descripcion;

    public int getId_Motivo() {
        return Id_Motivo;
    }
    public String getMotivo() {
        return Motivo;
    }
    public String getDescripcion() {
        return Descripcion;
    }
}
