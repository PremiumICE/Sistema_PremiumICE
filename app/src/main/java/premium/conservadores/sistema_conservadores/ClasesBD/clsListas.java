package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsListas {
    public clsListas() {

    }
    public clsListas(int Id_Lis, String Nom, String Desc) {
        this.Id_Lista = Id_Lis;
        this.Nom = Nom;
        this.Desc = Desc;
    }
    public int Id_Lista;
    public String Nom, Desc;

    public int getId_Lista() {
        return Id_Lista;
    }
    public String getNom() {
        return Nom;
    }
    public String getDesc() {
        return Desc;
    }
}
