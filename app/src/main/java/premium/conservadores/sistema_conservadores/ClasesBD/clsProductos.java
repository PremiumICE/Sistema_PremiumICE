package premium.conservadores.sistema_conservadores.ClasesBD;

import java.util.Objects;

public class clsProductos {
    public int IdProducto;
    public String Nombre_P;
    public double Precio_P;
    public int Tipo_Pago;
    public int Eleccion_Pago;
    public String Tipo;
    public int Piezas;

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

    public int getEleccion_Pago(){
        return Eleccion_Pago;
    }
    public void setEleccion_Pago(int EleTipoP){
        Eleccion_Pago = EleTipoP;
    }

    public String getTipo(){
        return Tipo;
    }
    public void setTipo(String Tip){
        Tipo = Tip;
    }

    public String getNombre_P(){
        return Nombre_P;
    }
    public void setNombre_P(String Nom_P){
        Nombre_P = Nom_P;
    }

    public int getPiezas(){
        return Piezas;
    }
    public void setPiezas(int Pie){
        Piezas = Pie;
    }

    public clsProductos(){}
    public clsProductos(int IdP, String NombreP,double PrecioP, int TipoP, String Ti,int Piez, int Ele_TipPa){
        this.IdProducto = IdP;
        this.Nombre_P = NombreP;
        this.Precio_P = PrecioP;
        this.Tipo_Pago = TipoP;
        this.Tipo = Ti;
        this.Piezas = Piez;
        this.Eleccion_Pago = Ele_TipPa;
    }
}
