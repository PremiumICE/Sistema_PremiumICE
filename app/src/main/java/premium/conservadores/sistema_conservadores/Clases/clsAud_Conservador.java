package premium.conservadores.sistema_conservadores.Clases;

public class clsAud_Conservador {
    public clsAud_Conservador() {

    }
    public clsAud_Conservador(int Id_Img, int Id_Cons, int Q, byte[] Img_1, byte[] Img_2,byte[] Img_3,byte[] Img_4,byte[] Img_5,byte[] Img_6,byte[] Img_7, String Desc) {
        this.Id_Imagen = Id_Img;
        this.Id_Conservador = Id_Cons;
        this.QR = Q;
        this.Img_N = Img_1;
        this.Img_NS = Img_2;
        this.Img_PA = Img_3;
        this.Img_PC = Img_4;
        this.Img_QR1 = Img_5;
        this.Img_QR2 = Img_6;
        this.Img_QR3 = Img_7;
        this.Descripcion = Desc;
    }
    public String Descripcion;
    public Integer Id_Imagen, Id_Conservador, QR;
    public byte[] Img_N, Img_NS, Img_PA, Img_PC, Img_QR1, Img_QR2, Img_QR3;
    public int getId_Imagen() {
        return Id_Imagen;
    }
    public int getQR() {
        return QR;
    }
    public int getId_Conservador() {
        return Id_Conservador;
    }
    public byte[] getImg_N() { return Img_N; }
    public byte[] getImg_NS() {
        return Img_NS;
    }
    public byte[] getImg_PA() {
        return Img_PA;
    }
    public byte[] getImg_PC() { return Img_PC; }
    public byte[] getImg_QR1() {
        return Img_QR1;
    }
    public byte[] getImg_QR2() {
        return Img_QR2;
    }
    public byte[] getImg_QR3() {
        return Img_QR3;
    }
    public String getDescripcion() {
        return Descripcion;
    }

}
