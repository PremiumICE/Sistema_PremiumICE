package premium.conservadores.sistema_conservadores.Clases;

public class clsVentasGuardadas {
    public clsVentasGuardadas() {

    }
    public clsVentasGuardadas(int Id_Ven, int Id_Cli, double tot, int Pie) {
        this.Id_Venta = Id_Ven;
        this.Id_Cliente = Id_Cli;
        this.Total = tot;
        this.Piezas = Pie;
    }
    public int Id_Venta, Id_Cliente, Piezas;
    public double Total;
    public int getId_Venta() {
        return Id_Venta;
    }
    public int getId_Cliente() {
        return Id_Cliente;
    }
    public double getTotal() {
        return Total;
    }
    public int getPiezas() {
        return Piezas;
    }
}
