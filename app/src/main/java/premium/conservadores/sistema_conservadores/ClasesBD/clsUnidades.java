package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsUnidades {
    public clsUnidades() {

    }
    public clsUnidades(int Id_Uni, String Uni, String Plac) {
        this.Id_Unidad = Id_Uni;
        this.Unidad = Uni;
        this.Placas = Plac;
    }
    private int Id_Unidad;
    private String Unidad, Placas;

    public int getId_Unidad() {
        return Id_Unidad;
    }
    public String getUnidad() {
        return Unidad;
    }
    public String getPlacas() {
        return Placas;
    }

}
