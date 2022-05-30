package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsConservadores {
    public clsConservadores() {

    }
    public clsConservadores(int Id_Cons, String Eti_Int, int Id_Fac, String No_Se,
                            String Mod, int Q, int Id_Ced, int Id_Cli, String Ubi) {
        this.Id_Conservador = Id_Cons;
        this.Etiqueta_Interna = Eti_Int;
        this.Id_Factura = Id_Fac;
        this.No_Serie = No_Se;
        this.Modelo = Mod;
        this.QR = Q;
        this.Id_Cedis = Id_Ced;
        this.Id_Cliente = Id_Cli;
        this.Ubicacion = Ubi;
    }
    private int Id_Conservador, Id_Factura, Id_Cedis, Id_Cliente, QR;
    private String Etiqueta_Interna, No_Serie, Modelo, Ubicacion;

    public int getId_Conservador() {
        return Id_Conservador;
    }
    public String getEtiqueta_Interna() {
        return Etiqueta_Interna;
    }
    public int getId_Factura() {
        return Id_Factura;
    }
    public String getNo_Serie() {
        return No_Serie;
    }
    public String getModelo() {
        return Modelo;
    }
    public int getQR() {
        return QR;
    }
    public int getId_Cedis() {
        return Id_Cedis;
    }
    public int getId_Cliente() {
        return Id_Cliente;
    }
    public String getUbicacion() {
        return Ubicacion;
    }

}
