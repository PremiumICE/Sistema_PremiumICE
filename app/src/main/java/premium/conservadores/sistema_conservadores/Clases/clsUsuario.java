package premium.conservadores.sistema_conservadores.Clases;

public class clsUsuario {
    public clsUsuario() {
    }
    public clsUsuario(int Id_User, String User, int Id_Rol, String Rut, String Uni, String Plac) {
        this.Id_Usuario = Id_User;
        this.Usuario = User;
        this.Id_Rol = Id_Rol;
        this.Ruta = Rut;
        this.Unidad = Uni;
        this.Placas = Plac;
    }
    public int Id_Usuario;
    public String Usuario, Ruta, Unidad, Placas;
    public int Id_Rol;

    public int getId_Usuario() {
        return Id_Usuario;
    }
    public String getUsuario() {
        return Usuario;
    }
    public String getRuta() {
        return Ruta;
    }
    public String getUnidad() {
        return Unidad;
    }
    public String getPlacas() {
        return Placas;
    }
    public int getId_Rol() {
        return Id_Rol;
    }
}
