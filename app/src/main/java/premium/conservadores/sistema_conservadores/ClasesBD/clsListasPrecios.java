package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsListasPrecios {
    public clsListasPrecios() {

    }
    public clsListasPrecios(int Id_Lis, int Id_Prod, Double Pre, String Prod) {
        this.Id_Lista = Id_Lis;
        this.Id_Producto = Id_Prod;
        this.Precios = Pre;
        this.Producto = Prod;
    }
    public int Id_Lista, Id_Producto, Piezas, Tipo_Pago;
    public Double Precios;
    public String Producto, Tipo;

    public int getId_Lista() {
        return Id_Lista;
    }
    public int getId_Producto() {
        return Id_Producto;
    }
    public int getPiezas() {
        return Piezas;
    }
    public int getTipoPago() {
        return Tipo_Pago;
    }
    public String getTipo() {
        return Tipo;
    }
    public void setPiezas(int pie){
        this.Piezas = pie;
    }
    public void setTipoPago(int tippago){
        this.Tipo_Pago = tippago;
    }
    public void setTipo(String Tip){
        this.Tipo = Tip;
    }
    public Double getPrecios() {
        return Precios;
    }
    public String getProducto() {
        return Producto;
    }

}
