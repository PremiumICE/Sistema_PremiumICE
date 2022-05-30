package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsDetVentasGuardadas {
    public clsDetVentasGuardadas() {

    }
    public clsDetVentasGuardadas(int Id_DetVen, int Id_Ven, int Id_Prod, int Pie, double pre, int tip) {
        this.Id_DetVenta = Id_DetVen;
        this.Id_Venta = Id_Ven;
        this.Id_Cliente = Id_Prod;
        this.Piezas = Pie;
        this.Precio = pre;
        this.Tipo = tip;
    }
    private int Id_DetVenta, Id_Venta, Id_Cliente, Piezas, Tipo;
    private double Precio;

    public int getId_DetVenta() {
        return Id_DetVenta;
    }
    public int getId_Venta() {
        return Id_Venta;
    }
    public int getId_Cliente() {
        return Id_Cliente;
    }
    public int getPiezas() {
        return Piezas;
    }
    public double getPrecio() {
        return Precio;
    }
    public int getTipo() {
        return Tipo;
    }
}
