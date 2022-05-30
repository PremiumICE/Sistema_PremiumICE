package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsUsuarios {
    public clsUsuarios() {

    }
    public clsUsuarios(int Id_Usu, String Usu) {
        this.Id_Usuario = Id_Usu;
        this.Usuario = Usu;
    }
    private int Id_Usuario;
    private String Usuario;

    public int getId_Usuario() {
        return Id_Usuario;
    }
    public String getUsuario() {
        return Usuario;
    }

}
