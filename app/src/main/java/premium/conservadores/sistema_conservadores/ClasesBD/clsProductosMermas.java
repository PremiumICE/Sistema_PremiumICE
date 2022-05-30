package premium.conservadores.sistema_conservadores.ClasesBD;

public class clsProductosMermas {
    public int IdProducto;
    public String Nombre_P;
    public double Precio_P;
    public int Tipo_Pago;
    public String Tipo;
    public int Piezas;
    public String Motivo;

    public int getIdProducto(){
        return IdProducto;
    }
    public void setIdProducto(int IdProd){
        IdProducto = IdProd;
    }

    public int getTipoPago(){
        return Tipo_Pago;
    }
    public void setTipoPago(int TipoPa){
        Tipo_Pago = TipoPa;
    }

    public String getTipo(){
        return Tipo;
    }
    public void setTipo(String Tip){
        Tipo = Tip;
    }

    public int getPiezas(){
        return Piezas;
    }
    public void setPiezas(int Pie){
        Piezas = Pie;
    }

    public String getMotivo(){
        return Motivo;
    }
    public void setMotivo(String Mot){
        Motivo = Mot;
    }

    public clsProductosMermas(){}
    public clsProductosMermas(int IdP, String NombreP, double PrecioP, int TipoP, String Ti, int Piez,String Mot){
        this.IdProducto = IdP;
        this.Nombre_P = NombreP;
        this.Precio_P = PrecioP;
        this.Tipo_Pago = TipoP;
        this.Tipo = Ti;
        this.Piezas = Piez;
        this.Motivo = Mot;
    }
}
