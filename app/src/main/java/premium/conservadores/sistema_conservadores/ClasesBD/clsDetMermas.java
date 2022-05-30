package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsDetMermas {
    public clsDetMermas() {

    }
    public clsDetMermas(int Id_DetMer, int Id_Mer, int Id_Prod, int Pie, double pre, double total) {
        this.Id_DetMermasL = Id_DetMer;
        this.Id_Merma = Id_Mer;
        this.Id_Producto = Id_Prod;
        this.Piezas = Pie;
        this.Precio = pre;
        this.Total = total;
    }
    private int Id_DetMermasL, Id_Merma, Id_Producto, Piezas;
    private double Precio, Total;

    public int getId_DetMermasL() {
        return Id_DetMermasL;
    }
    public int getId_Venta() {
        return Id_Merma;
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
    public double getTotal() {
        return Total;
    }
}
