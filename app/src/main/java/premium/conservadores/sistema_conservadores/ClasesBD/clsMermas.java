package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsMermas {
    public clsMermas() {

    }
    public clsMermas(int Id_Mer, int Pi_T, Double To , byte[] Evi, int Fec, int Env, int Id_Usu) {
        Id_Merma = Id_Mer;
        Piezas = Pi_T;
        Total = To;
        Fecha = Fec;
        Id_Usuario = Id_Usu;
        Enviado = Env;
        Evidencia = Evi;
    }
    public int Id_Merma, Piezas, Fecha, Id_Usuario, Enviado;
    public double Total;
    public byte[] Evidencia;

    public int getId_Merma() {
        return Id_Merma;
    }
    public int getFecha() {
        return Fecha;
    }
    public int getId_Usuario() {
        return Id_Usuario;
    }
    public int getPiezas() {
        return Piezas;
    }
    public int getEnviado() {
        return Enviado;
    }
    public byte[] getEvidencia() {
        return Evidencia;
    }
    public double getTotal() {
        return Total;
    }



}
