package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsDetVentas {
    public clsDetVentas() {

    }
    public clsDetVentas(int Id_DetVen, int Id_Ven, int Id_Prod, int Pie, double pre, int tip) {
        this.Id_DetVentasL = Id_DetVen;
        this.Id_Venta = Id_Ven;
        this.Id_Producto = Id_Prod;
        this.Piezas = Pie;
        this.Precio = pre;
        this.Tipo_Pago = tip;
    }
    private int Id_DetVentasL, Id_Venta, Id_Producto, Piezas, Tipo_Pago;
    private double Precio;

    public int getId_DetVenta() {
        return Id_DetVentasL;
    }
    public int getId_Venta() {
        return Id_Venta;
    }
    public int getId_Producto() {
        return Id_Producto;
    }
    public int getPiezas() {
        return Piezas;
    }
    public double getPrecio() {
        return Precio;
    }
    public int getTipo() {
        return Tipo_Pago;
    }
}
