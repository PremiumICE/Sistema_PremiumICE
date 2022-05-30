package premium.conservadores.sistema_conservadores.Clases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import premium.conservadores.sistema_conservadores.ClasesBD.clsAuditoria;
import premium.conservadores.sistema_conservadores.ClasesBD.clsAuditoriaMostrar;
import premium.conservadores.sistema_conservadores.ClasesBD.clsCierreRuta;
import premium.conservadores.sistema_conservadores.ClasesBD.clsClientes;
import premium.conservadores.sistema_conservadores.ClasesBD.clsConservadores;
import premium.conservadores.sistema_conservadores.ClasesBD.clsCoordenadas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsDetMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsDetVentas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsDetVentasGuardadas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsListas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsListasPrecios;
import premium.conservadores.sistema_conservadores.ClasesBD.clsMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductos;
import premium.conservadores.sistema_conservadores.ClasesBD.clsProductosMermas;
import premium.conservadores.sistema_conservadores.ClasesBD.clsUnidades;
import premium.conservadores.sistema_conservadores.ClasesBD.clsUsuarios;
import premium.conservadores.sistema_conservadores.ClasesBD.clsVentas;


public class SQLocal extends SQLiteOpenHelper {
    Gson Gson_Converter = new Gson();//objeto que permite convertir cadenas JSON en objetos


    private static final String CLIENTES_TABLE_CREATE = "CREATE TABLE Clientes (Id_Cliente Integer Primary Key," +
            " Nombre TEXT, Ubicacion TEXT, Latitud Text, Longitud Text, Id_Lista Integer, TipoPago Integer)";
    private static final String PRODUCTOS_TABLE_CREATE = "CREATE TABLE Productos (Id_Producto INTEGER Primary Key," +
            " Nombre TEXT, Precio DECIMAL(10,2), TipoVenta INTEGER)";
    private static final String LISTAS_TABLE_CREATE = "CREATE TABLE Listas (Id_Lista INTEGER Primary Key," +
            " Nombre TEXT,Descripcion TEXT)";
    private static final String LISTAS_PRECIOS_TABLE_CREATE = "CREATE TABLE Listas_Precios (Id_Lista_Ope INTEGER Primary Key," +
            " Id_Lista Integer, Id_Producto Integer, Precio DECIMAL(18,2))";
    private static final String CONSERVADORES_TABLE_CREATE = "CREATE TABLE Conservadores (Id_Conservador INTEGER Primary Key," +
            "Etiqueta_Interna TEXT, Id_Factura Integer, No_Serie TEXT, Modelo TEXT, QR Integer, Id_Cedis Integer, " +
            "Id_Cliente Integer, Ubicacion Text)";
    private static final String IMAGENES_TABLE_CREATE = "CREATE TABLE Imagenes_Conservadores (Id_Img INTEGER Primary Key, Id_Conservador INTEGER, Id_QR INTEGER, Imagen_PC BLOB)";

    private static final String AUDITORIA_TABLE_CREATE = "CREATE TABLE Conservadores_Auditar (Id_Auditoria INTEGER Primary key, Id_Conservador Integer, Fecha_Ini Integer, " +
            "Fecha_Lim Integer, Estatus Integer, Id_CheckL Integer, Latitud_Rev Text, Longitud_Rev Text, Id_Cliente_Rev Text, " +
            "Imagen_Ev BLOB,QR Integer, Etiqueta TEXT, Serie TEXT, Id_Usuario Integer, Auditado INTEGER, Enviado INTEGER)";
    private static final String COORDENADAS_TABLE_CREATE = "CREATE TABLE Coordenadas (Id_Coordenada INTEGER Primary Key AUTOINCREMENT, Latitud TEXT, " +
            "Longitud TEXT, Id_Usuario INTEGER, Fecha TEXT)";
    private static final String GASTOS_TABLE_CREATE = "CREATE TABLE Gastos (Id_Gasto INTEGER Primary Key AUTOINCREMENT, Rubro TEXT, " +
            "Total Decimal(18,2), Fecha INTEGER,Info_Adicional TEXT)";
    private static final String CIERRES_TABLE_CREATE = "CREATE TABLE Cierre_Ruta (Id_Rutas INTEGER Primary Key AUTOINCREMENT, Id_Usuario Integer, " +
            "Hora_Inicio TEXT, Hora_Fin TEXT, Tiempo TEXT, Total_Gastos Double, Total Double, Total_Contado Double, Total_Credito Double, " +
            "Total_Cierre Double, Piezas INTEGER, Lat_Ini TEXT, Long_Ini TEXT, Lat_Fin TEXT, Long_Fin TEXT, Kilometraje_Ini Double, " +
            "Kilometraje_Fin Double, Fecha INTEGER, Enviado INTEGER)";
    private static final String USUARIOS_TABLE_CREATE = "CREATE TABLE Usuarios (Id_Usuario INTEGER Primary Key," +
            " Usuario TEXT)";
    private static final String UNIDADES_TABLE_CREATE = "CREATE TABLE Unidades (Id_Unidad INTEGER Primary Key," +
            " Unidad TEXT, Placas TEXT)";
    //Configuracion del checklist
    private static final String PREGUNTAS_TABLE_CREATE = "CREATE TABLE Coordenadas (Id_Coordenada INTEGER Primary Key AUTOINCREMENT, Latitud TEXT, " +
            "Longitud TEXT, Id_Usuario INTEGER, Fecha TEXT)";
    private static final String RESPUESTAS_TABLE_CREATE = "CREATE TABLE Coordenadas (Id_Coordenada INTEGER Primary Key AUTOINCREMENT, Latitud TEXT, " +
            "Longitud TEXT, Id_Usuario INTEGER, Fecha TEXT)";


    //Tablas Venta Temporales
    private static final String VENTAS_GUARDADAS_TABLE_CREATE = "CREATE TABLE Ventas_Guardadas (Id_VentasG Integer Primary Key AUTOINCREMENT," +
            " Id_Cliente Integer, Total Double, Piezas_T Integer)";
    private static final String DETVENTAS_GUARDADAS_TABLE_CREATE = "CREATE TABLE DetVentas_Guardadas (Id_DetVentasG Integer Primary Key AUTOINCREMENT," +
            " Id_VentaG Integer, Id_Producto Integer, Piezas Integer, Precio Double, TipoPago Integer)";
    //Tablas Venta
    private static final String VENTAS_TABLE_CREATE = "CREATE TABLE Ventas (Id_Ventas Integer Primary Key AUTOINCREMENT," +
            " Total Double ,Total_Contado Double, Total_Credito Double, Piezas_T Integer, Tipo_Pago Integer, Fecha Integer, " +
            " Id_Usuario Integer, Id_Cliente Integer, Id_Situacion Integer, Latitud TEXT, Longitud TEXT, Estatus Integer, " +
            " Tiempo_Venta TEXT, Enviada Integer)";

    private static final String DETVENTAS_TABLE_CREATE = "CREATE TABLE DetVentas (Id_DetVentas Integer Primary Key AUTOINCREMENT," +
            " Id_Venta Integer, Id_Producto Integer, Piezas Integer, Precio Double, TipoPago Integer)";
    //Tablas Mermas
    private static final String MERMAS_TABLE_CREATE = "CREATE TABLE Mermas (Id_Mermas Integer Primary Key AUTOINCREMENT," +
            " Piezas Integer, Total Double, Evidencia Blob, Fecha Integer, Enviada Integer, Id_Usuario Integer)";

    private static final String DETMERMAS_TABLE_CREATE = "CREATE TABLE DetMermas (Id_DetMerma Integer Primary Key AUTOINCREMENT," +
            " Id_Merma Integer, Id_Producto Integer, Piezas Integer, Precio Double, Total Double, Motivo TEXT)";


    private static final String DB_NAME = "Auditorias.app";
    private static final int DB_VERSION = 1;
    public SQLocal(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Creacion de tablas
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CLIENTES_TABLE_CREATE);
        db.execSQL(PRODUCTOS_TABLE_CREATE);
        db.execSQL(LISTAS_TABLE_CREATE);
        db.execSQL(LISTAS_PRECIOS_TABLE_CREATE);
        db.execSQL(CONSERVADORES_TABLE_CREATE);
        db.execSQL(IMAGENES_TABLE_CREATE);
        db.execSQL(AUDITORIA_TABLE_CREATE);
        db.execSQL(COORDENADAS_TABLE_CREATE);
        db.execSQL(GASTOS_TABLE_CREATE);
        db.execSQL(CIERRES_TABLE_CREATE);
        db.execSQL(USUARIOS_TABLE_CREATE);
        db.execSQL(UNIDADES_TABLE_CREATE);

        db.execSQL(VENTAS_GUARDADAS_TABLE_CREATE);
        db.execSQL(DETVENTAS_GUARDADAS_TABLE_CREATE);
        db.execSQL(VENTAS_TABLE_CREATE);
        db.execSQL(DETVENTAS_TABLE_CREATE);
        db.execSQL(MERMAS_TABLE_CREATE);
        db.execSQL(DETMERMAS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Clientes");
        db.execSQL("DROP TABLE IF EXISTS Productos");
        db.execSQL("DROP TABLE IF EXISTS Listas");
        db.execSQL("DROP TABLE IF EXISTS Listas_Precios");
        db.execSQL("DROP TABLE IF EXISTS Conservadores");
        db.execSQL("DROP TABLE IF EXISTS Imagenes_Conservadores");
        db.execSQL("DROP TABLE IF EXISTS Conservadores_Auditar");
        db.execSQL("DROP TABLE IF EXISTS Coordenadas");
        db.execSQL("DROP TABLE IF EXISTS Gastos");
        db.execSQL("DROP TABLE IF EXISTS Cierre_Ruta");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Unidades");

        db.execSQL("DROP TABLE IF EXISTS Ventas_Guardadas");
        db.execSQL("DROP TABLE IF EXISTS DetVentas_Guardadas");
        db.execSQL("DROP TABLE IF EXISTS Ventas");
        db.execSQL("DROP TABLE IF EXISTS DetVentas");
        db.execSQL("DROP TABLE IF EXISTS Mermas");
        db.execSQL("DROP TABLE IF EXISTS DetMermas");
        onCreate(db);
    }

    //Metodos para insertar datos
    public boolean InsertCliente(int Id_Cliente, String Nombre, String Ubicacion, String Latitud, String Longitud, int Id_Lista, int TipoPago) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Cliente", Id_Cliente);
        contentValues.put("Nombre", Nombre);
        contentValues.put("Ubicacion", Ubicacion);
        contentValues.put("Latitud", Latitud);
        contentValues.put("Longitud", Longitud);
        contentValues.put("Id_Lista", Id_Lista);
        contentValues.put("TipoPago", TipoPago);

        long result = db.insert("Clientes", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertGastos(String Rubro, double Total, int Fecha, String Info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Rubro", Rubro);
        contentValues.put("Total", Total);
        contentValues.put("Fecha", Fecha);
        contentValues.put("Info_Adicional", Info);

        long result = db.insert("Gastos", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertProducto(int Id_Producto, String Nombre, Double Precio, Integer Tipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Producto", Id_Producto);
        contentValues.put("Nombre", Nombre);
        contentValues.put("Precio", Precio);
        contentValues.put("TipoVenta", Tipo);

        long result = db.insert("Productos", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertImgCons(int Id_Img, int Id_Conservador, int QR, byte[] Imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Img", Id_Img);
        contentValues.put("Id_Conservador", Id_Conservador);
        contentValues.put("Id_QR", QR);
        contentValues.put("Imagen_PC", Imagen);

        long result = db.insert("Imagenes_Conservadores", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean InsertLista(int Id_Lista, String Nombre, String Descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Lista", Id_Lista);
        contentValues.put("Nombre", Nombre);
        contentValues.put("Descripcion", Descripcion);

        long result = db.insert("Listas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertListaPrecios(int Id_Lista_Ope,int Id_Lista, int Id_Producto, Double Precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Lista_Ope", Id_Lista_Ope);
        contentValues.put("Id_Lista", Id_Lista);
        contentValues.put("Id_Producto", Id_Producto);
        contentValues.put("Precio", Precio);

        long result = db.insert("Listas_Precios", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertConservadores(int Id_Conservador, String Etiqueta_Interna, int Id_Factura, String No_Serie,
                                       String Modelo, int QR, int Id_Cedis, int Id_Cliente, String Ubicacion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Conservador", Id_Conservador);
        contentValues.put("Etiqueta_Interna", Etiqueta_Interna);
        contentValues.put("Id_Factura", Id_Factura);
        contentValues.put("No_Serie", No_Serie);
        contentValues.put("Modelo", Modelo);
        contentValues.put("QR", QR);
        contentValues.put("Id_Cedis", Id_Cedis);
        contentValues.put("Id_Cliente", Id_Cliente);
        contentValues.put("Ubicacion", Ubicacion);

        long result = db.insert("Conservadores", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertAuditoria(int Id_Auditoria, int Id_Conservador, int Fecha_Ini, int Fecha_Lim, int Estatus, int Id_CheckL,
                                   String Latitud_Rev, String Longitud_Rev, int Id_Cliente_Rev, byte[] Imagen_Ev, int QR, String Etiqueta,
                                   String Serie, int Id_Us, int Aud){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Auditoria", Id_Auditoria);
        contentValues.put("Id_Conservador", Id_Conservador);
        contentValues.put("Fecha_Ini", Fecha_Ini);
        contentValues.put("Fecha_Lim", Fecha_Lim);
        contentValues.put("Estatus", Estatus);
        contentValues.put("Id_CheckL", Id_CheckL);
        contentValues.put("Latitud_Rev", Latitud_Rev);
        contentValues.put("Longitud_Rev", Longitud_Rev);
        contentValues.put("Id_Cliente_Rev", Id_Cliente_Rev);
        contentValues.put("Imagen_Ev", Imagen_Ev);
        contentValues.put("QR", QR);
        contentValues.put("Etiqueta", Etiqueta);
        contentValues.put("Serie", Serie);
        contentValues.put("Id_Usuario", Id_Us);
        contentValues.put("Auditado", Aud);
        contentValues.put("Enviado", 0);

        long result = db.insert("Conservadores_Auditar", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertCierreRuta(int Id_Usuario, String Hora_Ini, String Hora_Fin, String Tiempo, double Total_gasto,
                                    double Total, double Total_contado, double Total_credito, double Total_efectivo, int Piezas,
                                   String Lat_Ini,String Long_Ini,String Lat_Fin,String Long_Fin, double KM_Ini, double KM_Fin,
                                    int Fecha, int Enviado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Usuario", Id_Usuario);
        contentValues.put("Hora_Inicio", Hora_Ini);
        contentValues.put("Hora_Fin", Hora_Fin);
        contentValues.put("Tiempo", Tiempo);
        contentValues.put("Total_Gastos", Total_gasto);
        contentValues.put("Total", Total);
        contentValues.put("Total_Contado", Total_contado);
        contentValues.put("Total_Credito", Total_credito);
        contentValues.put("Total_Cierre", Total_efectivo);
        contentValues.put("Piezas", Piezas);
        contentValues.put("Lat_Ini", Lat_Ini);
        contentValues.put("Long_Ini", Long_Ini);
        contentValues.put("Lat_Fin", Lat_Fin);
        contentValues.put("Long_Fin", Long_Fin);
        contentValues.put("Kilometraje_Ini", KM_Ini);
        contentValues.put("Kilometraje_Fin", KM_Fin);
        contentValues.put("Fecha", Fecha);
        contentValues.put("Enviado", Enviado);

        long result = db.insert("Cierre_Ruta", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    //Guardar Ventas Temporales
    public boolean InsertVentaGuardar(Integer Id_Cliente, Double Total, Integer PiezasT) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Cliente", Id_Cliente);
        contentValues.put("Total", Total);
        contentValues.put("Piezas_T", PiezasT);

        long result = db.insert("Ventas_Guardadas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertDetVentaGuardar(Integer Id_VentaG, Integer Id_Producto, Double Precio, Integer Piezas, Integer TipoPago) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_VentasG", Id_VentaG);
        contentValues.put("Id_Producto", Id_Producto);
        contentValues.put("Precio", Precio);
        contentValues.put("Piezas", Piezas);
        contentValues.put("TipoPago", TipoPago);

        long result = db.insert("DetVentas_Guardadas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    //Guardar Ventas
    public boolean InsertVenta(Double Total, Double TotalCon, Double TotalCre, Integer PiezasT, Integer TipoPago, Integer Fecha, Integer Id_Usuario,
                               Integer Id_Cliente, Integer Id_Situacion, String Latitud, String Longitud, Integer Estatus, String Tiempo_V, Integer Enviada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Total", Total);
        contentValues.put("Total_Contado", TotalCon);
        contentValues.put("Total_Credito", TotalCre);
        contentValues.put("Piezas_T", PiezasT);
        contentValues.put("Tipo_Pago", TipoPago);
        contentValues.put("Fecha", Fecha);
        contentValues.put("Id_Usuario", Id_Usuario);
        contentValues.put("Id_Cliente", Id_Cliente);
        contentValues.put("Id_Situacion", Id_Situacion);
        contentValues.put("Latitud", Latitud);
        contentValues.put("Longitud", Longitud);
        contentValues.put("Estatus", Estatus);
        contentValues.put("Tiempo_Venta", Tiempo_V);
        contentValues.put("Enviada", Enviada);
        long result = db.insert("Ventas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertDetVenta(Integer Id_VentaG, Integer Id_Producto, Double Precio, Integer Piezas, Integer TipoPago) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Venta", Id_VentaG);
        contentValues.put("Id_Producto", Id_Producto);
        contentValues.put("Precio", Precio);
        contentValues.put("Piezas", Piezas);
        contentValues.put("TipoPago", TipoPago);

        long result = db.insert("DetVentas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //Guardar Mermas
    public boolean InsertMermas(Integer Piezas, Double Total, byte[] Evidencia, Integer Fecha,  Integer Envio, Integer Id_Us) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Piezas", Piezas);
        contentValues.put("Total", Total);
        contentValues.put("Evidencia", Evidencia);
        contentValues.put("Fecha", Fecha);
        contentValues.put("Enviada", Envio);
        contentValues.put("Id_Usuario", Id_Us);
        long result = db.insert("Mermas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertDetMermas(Integer Id_VentaG, Integer Id_Producto, Double Precio, Integer Piezas, Double Total, String Mot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Merma", Id_VentaG);
        contentValues.put("Id_Producto", Id_Producto);
        contentValues.put("Piezas", Piezas);
        contentValues.put("Precio", Precio);
        contentValues.put("Total", Total);
        contentValues.put("Motivo", Mot);

        long result = db.insert("DetMermas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean InsertCoordenadas(String Latitud, String Longitud, int Id_Usuario, String Fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Latitud", Latitud);
        contentValues.put("Longitud", Longitud);
        contentValues.put("Id_Usuario", Id_Usuario);
        contentValues.put("Fecha", Fecha);
        long result = db.insert("Coordenadas", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertUsuarios(int Id_Usuario, String Usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Usuario", Id_Usuario);
        contentValues.put("Usuario", Usuario);

        long result = db.insert("Usuarios", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean InsertUnidades(int Id_Unidad, String Unidad, String Placas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_Unidad", Id_Unidad);
        contentValues.put("Unidad", Unidad);
        contentValues.put("Placas", Placas);

        long result = db.insert("Unidades", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean UpdateAuditoria(int Id_Auditoria, int Id_CheckL, String Latitud_Rev, String Longitud_Rev, int Id_Cliente_Rev,
                                   byte[] Imagen_Ev,int QR, String Eti_Int, String No_Ser, int Id_Us, int Aud, int Enviado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id_CheckL", Id_CheckL);
        contentValues.put("Latitud_Rev", Latitud_Rev);
        contentValues.put("Longitud_Rev", Longitud_Rev);
        contentValues.put("Id_Cliente_Rev", Id_Cliente_Rev);
        contentValues.put("Imagen_Ev", Imagen_Ev);
        contentValues.put("QR", QR);
        contentValues.put("Etiqueta", Eti_Int);
        contentValues.put("Serie", No_Ser);
        contentValues.put("Id_Usuario", Id_Us);
        contentValues.put("Auditado", Aud);
        contentValues.put("Enviado", Enviado);

        long result = db.update("Conservadores_Auditar", contentValues, "Id_Auditoria = ?", new String[]{ String.valueOf(Id_Auditoria) });
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean UpdateVenta(int Id_Venta){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Enviada", 0);

        long result = db.update("Ventas", contentValues, "Id_Ventas = ?", new String[]{ String.valueOf(Id_Venta) });
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean UpdateCierre(int Id_Cierre){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Enviado", 0);

        long result = db.update("Cierre_Ruta", contentValues, "Id_Rutas = ?", new String[]{ String.valueOf(Id_Cierre) });
        if (result == -1)
            return false;
        else
            return true;
    }

    //Metodos para lectura de datos
    public ArrayList<clsClientes> getClientes(){
        ArrayList<clsClientes> list = new ArrayList<clsClientes>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Clientes";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {

                do {
                    int Id_Cliente = cursor.getInt(0);
                    String Nombre = cursor.getString(1);
                    String Ubicacion = cursor.getString(2);
                    String Latitud = cursor.getString(3);
                    String Longitud = cursor.getString(4);
                    int Id_Lis = cursor.getInt(5);
                    int TipoPago = cursor.getInt(6);

                    clsClientes clientes = new clsClientes(Id_Cliente, Nombre, Ubicacion, Latitud, Longitud, Id_Lis,TipoPago);
                    list.add(clientes);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsClientes> getClienteId(int idC){
        String[] args = new String[]{String.valueOf(idC)};
        String[] campos = new String[]{"*"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Clientes",campos,"Id_Cliente=?",args, null,null,null);

        ArrayList<clsClientes> list = new ArrayList<clsClientes>();
        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {

                do {
                    int Id_Cliente = cursor.getInt(0);
                    String Nombre = cursor.getString(1);
                    String Ubicacion = cursor.getString(2);
                    String Latitud = cursor.getString(3);
                    String Longitud = cursor.getString(4);
                    int Id_Lis = cursor.getInt(5);
                    int TipoPago = cursor.getInt(6);

                    clsClientes clientes = new clsClientes(Id_Cliente, Nombre, Ubicacion, Latitud, Longitud, Id_Lis,TipoPago);
                    list.add(clientes);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsProductos> getProductos(){

        ArrayList<clsProductos> list = new ArrayList<clsProductos>();
        // Select All Query
        String selectQuery = "SELECT Id_Producto, Nombre, Precio, TipoVenta FROM Productos";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Producto = cursor.getInt(0);
                    String Nombre = cursor.getString(1);
                    Double Precio = cursor.getDouble(2);
                    int TipoVenta = cursor.getInt(3);
                    clsProductos productos = new clsProductos(Id_Producto, Nombre,Precio,TipoVenta,"",0,0);
                    list.add(productos);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsGastos> getGastos(){

        ArrayList<clsGastos> list = new ArrayList<clsGastos>();
        // Select All Query
        String selectQuery = "SELECT Id_Gasto, Rubro, Total, Fecha, Info_Adicional FROM Gastos";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Gasto = cursor.getInt(0);
                    String Rubro = cursor.getString(1);
                    Double Total = cursor.getDouble(2);
                    int Fecha = cursor.getInt(3);
                    String InfoAdi = cursor.getString(4);

                    clsGastos gasto = new clsGastos(Id_Gasto,Rubro,String.valueOf(Fecha),Total, InfoAdi);
                    list.add(gasto);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsUsuarios> getUsuarios(){

        ArrayList<clsUsuarios> list = new ArrayList<clsUsuarios>();
        // Select All Query
        String selectQuery = "SELECT Id_Usuario, Usuario FROM Usuarios";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Usu = cursor.getInt(0);
                    String Usu = cursor.getString(1);

                    clsUsuarios usuario = new clsUsuarios(Id_Usu,Usu);
                    list.add(usuario);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsUnidades> getUnidades(){

        ArrayList<clsUnidades> list = new ArrayList<clsUnidades>();
        // Select All Query
        String selectQuery = "SELECT Id_Unidad, Unidad, Placas FROM Unidades";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Uni = cursor.getInt(0);
                    String Uni = cursor.getString(1);
                    String Plac = cursor.getString(2);

                    clsUnidades unidad = new clsUnidades(Id_Uni, Uni, Plac);
                    list.add(unidad);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsListas> getListas(int idL){
        String[] args = new String[]{String.valueOf(idL)};
        String[] campos = new String[]{"*"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Listas",campos,"Id_Lista=?",args, null,null,null);

        ArrayList<clsListas> list = new ArrayList<clsListas>();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Lista = cursor.getInt(0);
                    String Nombre = cursor.getString(1);
                    String Descripcion = cursor.getString(2);

                    clsListas listas = new clsListas(Id_Lista, Nombre, Descripcion);
                    list.add(listas);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsListasPrecios> getListasPrecios(int idL){
        String[] args = new String[]{String.valueOf(idL)};
        String[] campos = new String[]{"*"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Listas_Precios",campos,"Id_Lista=?",args, null,null,null);

        ArrayList<clsListasPrecios> list = new ArrayList<clsListasPrecios>();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Lista = cursor.getInt(0);
                    int Id_Producto = cursor.getInt(1);
                    Double Precio = cursor.getDouble(2);

                    clsListasPrecios listas = new clsListasPrecios(Id_Lista, Id_Producto, Precio,"");
                    list.add(listas);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsProductos> getListasPreciosUnica(int idL){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[1];
        param[0]= Integer.toString(idL,1);

        Cursor cursor = db.rawQuery("Select lispre.Id_Lista, lispre.Id_Producto, lispre.Precio, prod.Nombre From Listas_Precios lispre " +
                "INNER JOIN Productos prod ON lispre.Id_Producto = prod.Id_Producto Where lispre.Id_Lista = ?", param);

        ArrayList<clsProductos> list = new ArrayList<clsProductos>();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Producto = cursor.getInt(1);
                    Double Precio = cursor.getDouble(2);
                    String Descripcion = cursor.getString(3);

                    clsProductos listas = new clsProductos(Id_Producto, Descripcion, Precio, 0,"Contado",0,0);
                    list.add(listas);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsProductosMermas> getListasPreciosMermas(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select lispre.Id_Lista, lispre.Id_Producto, lispre.Precio, prod.Nombre From Listas_Precios lispre " +
                "INNER JOIN Productos prod ON lispre.Id_Producto = prod.Id_Producto", null);

        ArrayList<clsProductosMermas> list = new ArrayList<clsProductosMermas>();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Producto = cursor.getInt(1);
                    Double Precio = cursor.getDouble(2);
                    String Descripcion = cursor.getString(3);

                    clsProductosMermas listas = new clsProductosMermas(Id_Producto, Descripcion, Precio, 0,"Contado",0,"");
                    list.add(listas);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsConservadores> getConservadores(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select Id_Conservador, Etiqueta_Interna, Id_Factura, No_Serie,Modelo, QR, Id_Cedis, Id_Cliente, Ubicacion From Conservadores",
                null);

        ArrayList<clsConservadores> list = new ArrayList<clsConservadores>();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Con = cursor.getInt(0);
                    String Et_Int = cursor.getString(1);
                    int Id_Fac = cursor.getInt(2);
                    String No_S = cursor.getString(3);
                    String Mod = cursor.getString(4);
                    int QR = cursor.getInt(5);
                    int Id_Ced = cursor.getInt(6);
                    int Id_Cli = cursor.getInt(7);
                    String Ubic = cursor.getString(8);

                    clsConservadores conservadores = new clsConservadores(Id_Con, Et_Int, Id_Fac, No_S,Mod,QR,Id_Ced,Id_Cli,Ubic);
                    list.add(conservadores);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsConservadores> getConservadorQR(int NumQR){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[1];
        param[0]= Integer.toString(NumQR,1);

        ArrayList<clsConservadores> list = new ArrayList<clsConservadores>();

        Cursor cursor = db.rawQuery("Select Id_Conservador, Etiqueta_Interna, Id_Factura, No_Serie,Modelo, QR, Id_Cedis, Id_Cliente, Ubicacion From Conservadores Where QR = ?", param);

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Con = cursor.getInt(0);
                    String Et_Int = cursor.getString(1);
                    int Id_Fac = cursor.getInt(2);
                    String No_S = cursor.getString(3);
                    String Mod = cursor.getString(4);
                    int QR = cursor.getInt(5);
                    int Id_Ced = cursor.getInt(6);
                    int Id_Cli = cursor.getInt(7);
                    String Ubic = cursor.getString(8);

                    clsConservadores conservadores = new clsConservadores(Id_Con, Et_Int, Id_Fac, No_S,Mod,QR,Id_Ced,Id_Cli,Ubic);
                    list.add(conservadores);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsConservadores> getConservadorID(int Id_Cons){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[1];
        param[0]= Integer.toString(Id_Cons,1);

        ArrayList<clsConservadores> list = new ArrayList<clsConservadores>();

        Cursor cursor = db.rawQuery("Select Id_Conservador, Etiqueta_Interna, Id_Factura, No_Serie,Modelo, QR, Id_Cedis, Id_Cliente, Ubicacion From Conservadores Where Id_Conservador = ?", param);

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Con = cursor.getInt(0);
                    String Et_Int = cursor.getString(1);
                    int Id_Fac = cursor.getInt(2);
                    String No_S = cursor.getString(3);
                    String Mod = cursor.getString(4);
                    int QR = cursor.getInt(5);
                    int Id_Ced = cursor.getInt(6);
                    int Id_Cli = cursor.getInt(7);
                    String Ubic = cursor.getString(8);

                    clsConservadores conservadores = new clsConservadores(Id_Con, Et_Int, Id_Fac, No_S,Mod,QR,Id_Ced,Id_Cli,Ubic);
                    list.add(conservadores);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsConservadores> getConservadorEtiqueta(String Etiqu){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<clsConservadores> list = new ArrayList<clsConservadores>();

        Cursor cursor = db.rawQuery("Select Id_Conservador, Etiqueta_Interna, Id_Factura, No_Serie,Modelo, QR, Id_Cedis, Id_Cliente, Ubicacion From Conservadores Where Etiqueta_Interna = ?", new String[] { Etiqu });

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Con = cursor.getInt(0);
                    String Et_Int = cursor.getString(1);
                    int Id_Fac = cursor.getInt(2);
                    String No_S = cursor.getString(3);
                    String Mod = cursor.getString(4);
                    int QR = cursor.getInt(5);
                    int Id_Ced = cursor.getInt(6);
                    int Id_Cli = cursor.getInt(7);
                    String Ubic = cursor.getString(8);

                    clsConservadores conservadores = new clsConservadores(Id_Con, Et_Int, Id_Fac, No_S,Mod,QR,Id_Ced,Id_Cli,Ubic);
                    list.add(conservadores);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsConservadores> getConservadorCliente(int IdCli){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[1];
        param[0]= Integer.toString(IdCli,1);

        ArrayList<clsConservadores> list = new ArrayList<clsConservadores>();

        Cursor cursor = db.rawQuery("Select Id_Conservador, Etiqueta_Interna, Id_Factura, No_Serie,Modelo, QR, Id_Cedis, Id_Cliente, Ubicacion From Conservadores Where Id_Cliente = ?", param);

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Con = cursor.getInt(0);
                    String Et_Int = cursor.getString(1);
                    int Id_Fac = cursor.getInt(2);
                    String No_S = cursor.getString(3);
                    String Mod = cursor.getString(4);
                    int QR = cursor.getInt(5);
                    int Id_Ced = cursor.getInt(6);
                    int Id_Cli = cursor.getInt(7);
                    String Ubic = cursor.getString(8);

                    clsConservadores conservadores = new clsConservadores(Id_Con, Et_Int, Id_Fac, No_S,Mod,QR,Id_Ced,Id_Cli,Ubic);
                    list.add(conservadores);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsAud_Conservador> getImagenes(){
        ArrayList<clsAud_Conservador> list = new ArrayList<clsAud_Conservador>();

        // Select All Query
        String selectQuery = "SELECT Id_Img, Id_Conservador, Id_QR, Imagen_PC FROM Imagenes_Conservadores";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Img = cursor.getInt(0);
                    int Id_Cons = cursor.getInt(1);
                    int Id_QR = cursor.getInt(2);
                    byte[] Imagen = cursor.getBlob(3);

                    clsAud_Conservador clientes = new clsAud_Conservador(Id_Img,Id_Cons,Id_QR,null,null,null,Imagen,null,null,null,"");
                    list.add(clientes);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsAuditoria> getAuditorias(){
        ArrayList<clsAuditoria> list = new ArrayList<clsAuditoria>();
        // Select All Query
        String selectQuery = "SELECT aud.Id_Auditoria, aud.Id_Conservador, con.QR, con.Etiqueta_Interna,con.No_Serie, aud.Fecha_Ini, aud.Fecha_Lim, aud.Estatus, aud.Id_CheckL, aud.Latitud_Rev," +
                "aud.Longitud_Rev, aud.Id_Cliente_Rev, aud.Imagen_Ev, img.Imagen_PC, aud.Auditado From Conservadores_Auditar aud INNER JOIN Conservadores con ON aud.Id_Conservador = con.Id_Conservador INNER JOIN " +
                "Imagenes_Conservadores img ON aud.Id_Conservador = img.Id_Conservador";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Aud = cursor.getInt(0);
                    int Id_Conservador = cursor.getInt(1);
                    int QR = cursor.getInt(2);
                    String Etiqueta = cursor.getString(3);
                    String No_Serie = cursor.getString(4);
                    int Fecha_Ini = cursor.getInt(5);
                    int Fecha_Lim = cursor.getInt(6);
                    int Estatus = cursor.getInt(7);
                    int Id_CheckL = cursor.getInt(8);
                    String Latitud_Rev = cursor.getString(9);
                    String Longitud_Rev = cursor.getString(10);
                    int Id_Cliente_Rev = cursor.getInt(11);
                    byte[] Imagen_Ev = cursor.getBlob(12);
                    byte[] Imagen_PC = cursor.getBlob(13);
                    int Auditado = cursor.getInt(14);

                    clsAuditoria clientes = new clsAuditoria(Id_Aud,Id_Conservador,QR, Etiqueta, No_Serie,Fecha_Ini,Fecha_Lim,Estatus,Id_CheckL,Latitud_Rev,Longitud_Rev,
                            Id_Cliente_Rev,Imagen_Ev, Imagen_PC,Auditado);
                    list.add(clientes);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsCierreRuta> getCierreRuta(){
        ArrayList<clsCierreRuta> listCierre = new ArrayList<clsCierreRuta>();
        // Select All Query
        String selectQuery = "SELECT * From Cierre_Ruta";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Ruta = cursor.getInt(0);
                    int Id_Usuario = cursor.getInt(1);
                    String Hora_Inicio= cursor.getString(2);
                    String Hora_Fin= cursor.getString(3);
                    String Tiempo= cursor.getString(4);
                    double Total_Gastos = cursor.getDouble(5);
                    double Total= cursor.getDouble(6);
                    double Total_Contado= cursor.getDouble(7);
                    double Total_Credito= cursor.getDouble(8);
                    double Total_Cierre= cursor.getDouble(9);
                    int Piezas= cursor.getInt(10);
                    String Lat_Ini= cursor.getString(11);
                    String Long_Ini= cursor.getString(12);
                    String Lat_Fin= cursor.getString(13);
                    String Long_Fin= cursor.getString(14);
                    double Kilometraje_Ini= cursor.getDouble(15);
                    double Kilometraje_Fin= cursor.getDouble(16);
                    int Fecha = cursor.getInt(17);
                    int Enviado = cursor.getInt(18);

                    clsCierreRuta cierres = new clsCierreRuta(Id_Ruta, Id_Usuario,Hora_Inicio,Hora_Fin, Tiempo, Total_Gastos,Total,
                            Total_Contado,Total_Credito,Total_Cierre,Piezas,Lat_Ini,Long_Ini, Lat_Fin,Long_Fin, Kilometraje_Ini,
                            Kilometraje_Fin, Fecha,Enviado);
                    listCierre.add(cierres);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return listCierre;
    }
    public ArrayList<clsCoordenadas> getCoordenadas(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<clsCoordenadas> list = new ArrayList<clsCoordenadas>();

        Cursor cursor = db.rawQuery("Select Id_Coordenada, Latitud, Longitud, Id_Usuario, Fecha From Coordenadas", null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Coor = cursor.getInt(0);
                    String Lat = cursor.getString(1);
                    String Lon = cursor.getString(2);
                    int Id_Us = cursor.getInt(3);
                    String Fech = cursor.getString(4);

                    clsCoordenadas coordenadas = new clsCoordenadas(Id_Coor, Lat, Lon,Id_Us,Fech);
                    list.add(coordenadas);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    //Lectura de ventas temporales
    public ArrayList<clsVentasGuardadas> getVentasGuardadas(){

        ArrayList<clsVentasGuardadas> list = new ArrayList<clsVentasGuardadas>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Ventas_Guardadas";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Venta = cursor.getInt(0);
                    int Id_Cliente = cursor.getInt(1);
                    Double Total = cursor.getDouble(2);
                    int PiezasT = cursor.getInt(3);
                    clsVentasGuardadas ventasg = new clsVentasGuardadas(Id_Venta, Id_Cliente,Total,PiezasT);
                    list.add(ventasg);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    public ArrayList<clsDetVentasGuardadas> getDetVentasGuardadas(int IdV){
        String[] args = new String[]{String.valueOf(IdV)};
        String[] campos = new String[]{"*"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("DetVentas_Guardadas",campos,"Id_VentaG=?",args, null,null,null);

        ArrayList<clsDetVentasGuardadas> list = new ArrayList<clsDetVentasGuardadas>();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_DetVenta = cursor.getInt(0);
                    int Id_Venta = cursor.getInt(1);
                    int Id_Prod = cursor.getInt(2);
                    int Piezas = cursor.getInt(3);
                    double Precio = cursor.getDouble(4);
                    int Tipo = cursor.getInt(5);
                    clsDetVentasGuardadas ventasg = new clsDetVentasGuardadas(Id_DetVenta, Id_Venta, Id_Prod,Piezas,Precio, Tipo);
                    list.add(ventasg);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    //Lectura de Ventas
    public ArrayList<clsVentas> getVentas(){
        ArrayList<clsVentas> ventasGuardar = new ArrayList<clsVentas>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Ventas";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Venta = cursor.getInt(0);
                    Double Total = cursor.getDouble(1);
                    Double Total_Contado = cursor.getDouble(2);
                    Double Total_Credito = cursor.getDouble(3);
                    int Piezas_T = cursor.getInt(4);
                    int TipoPago = cursor.getInt(5);
                    int Fecha = cursor.getInt(6);
                    int Id_Usuario = cursor.getInt(7);
                    int Id_Cliente = cursor.getInt(8);
                    int Id_Situacion = cursor.getInt(9);
                    String Latitud = cursor.getString(10);
                    String Longitud = cursor.getString(11);
                    int Estatus = cursor.getInt(12);
                    String Tiempo_Venta = cursor.getString(13);
                    int Enviado = cursor.getInt(14);

                    clsVentas venta = new clsVentas(Id_Venta, Total, Total_Contado, Total_Credito, Piezas_T, TipoPago, Fecha, Id_Usuario, Id_Cliente, Id_Situacion, Latitud,
                            Longitud,Estatus, Tiempo_Venta, Enviado);
                    ventasGuardar.add(venta);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return ventasGuardar;
    }
    public ArrayList<clsVentas> getVentasSinEnviar(){
        ArrayList<clsVentas> ventasGuardar = new ArrayList<clsVentas>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Ventas Where Enviada = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Venta = cursor.getInt(0);
                    Double Total = cursor.getDouble(1);
                    Double Total_Contado = cursor.getDouble(2);
                    Double Total_Credito = cursor.getDouble(3);
                    int Piezas_T = cursor.getInt(4);
                    int TipoPago = cursor.getInt(5);
                    int Fecha = cursor.getInt(6);
                    int Id_Usuario = cursor.getInt(7);
                    int Id_Cliente = cursor.getInt(8);
                    int Id_Situacion = cursor.getInt(9);
                    String Latitud = cursor.getString(10);
                    String Longitud = cursor.getString(11);
                    int Estatus = cursor.getInt(12);
                    String Tiempo_Venta = cursor.getString(13);
                    int Enviado = cursor.getInt(14);

                    clsVentas venta = new clsVentas(Id_Venta, Total, Total_Contado, Total_Credito, Piezas_T, TipoPago, Fecha, Id_Usuario, Id_Cliente, Id_Situacion, Latitud,
                            Longitud,Estatus, Tiempo_Venta, Enviado);
                    ventasGuardar.add(venta);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return ventasGuardar;
    }
    public ArrayList<clsDetVentas> getDetVentas(int IdV){
        String[] args = new String[]{String.valueOf(IdV)};
        String[] campos = new String[]{"*"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("DetVentas",campos,"Id_Venta=?",args, null,null,null);

        ArrayList<clsDetVentas> list = new ArrayList<clsDetVentas>();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_DetVenta = cursor.getInt(0);
                    int Id_Venta = cursor.getInt(1);
                    int Id_Prod = cursor.getInt(2);
                    int Piezas = cursor.getInt(3);
                    double Precio = cursor.getDouble(4);
                    int Tipo = cursor.getInt(5);
                    clsDetVentas ventasg = new clsDetVentas(Id_DetVenta, Id_Venta, Id_Prod,Piezas,Precio, Tipo);
                    list.add(ventasg);//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }
    //Lectura de mermas
    public ArrayList<clsMermas> getMermas(){
        ArrayList<clsMermas> mermasGuardar = new ArrayList<clsMermas>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Mermas";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Merma = cursor.getInt(0);
                    int Piezas_T = cursor.getInt(1);
                    Double Total = cursor.getDouble(2);
                    byte[] Evid = cursor.getBlob(3);
                    int Fecha = cursor.getInt(4);
                    int Enviado = cursor.getInt(5);
                    int Id_Usuario = cursor.getInt(6);

                    clsMermas venta = new clsMermas(Id_Merma, Piezas_T, Total, Evid, Fecha, Enviado, Id_Usuario);
                    mermasGuardar.add(venta);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return mermasGuardar;
    }

    public ArrayList<clsProductosMermas> getProdsMermas(){
        ArrayList<clsProductosMermas> mermasGuardar = new ArrayList<clsProductosMermas>();
        // Select All Query
        String selectQuery = "SELECT  * FROM DetMermas";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Merma = cursor.getInt(1);
                    int IdProducto=cursor.getInt(2);
                    int Piezas = cursor.getInt(3);
                    double Precio_P=cursor.getDouble(4);
                    double Total = cursor.getInt(3)*cursor.getDouble(4);
                    String Motivo = cursor.getString(6);

                    clsProductosMermas venta = new clsProductosMermas(Id_Merma,"",Precio_P,0,"",Piezas, Motivo);
                    mermasGuardar.add(venta);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return mermasGuardar;
    }

    //Contar ventas y detalle de ventas temporales para siguiente ID
    public int contarVentasG(){
        Integer idv =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select MAX(Id_VentasG) As IdVenta From Ventas_Guardadas", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                idv = c.getInt(0);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return idv;
    }
    public int contarDetVentasG(){
        Integer idv =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select MAX(Id_DetVentasG) As IdDetVenta From DetVentas_Guardadas", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                idv = c.getInt(0);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return idv;
    }
    public int contarVentas(){
        Integer idv =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select MAX(Id_Ventas) As IdVenta From Ventas", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                idv = c.getInt(0);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return idv;
    }
    public int contarDetVentas(){
        Integer idv =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select MAX(Id_DetVentas) As IdDetVenta From DetVentas", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                idv = c.getInt(0);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return idv;
    }
    public int contarMermas(){
        Integer idv =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select MAX(Id_Mermas) As IdMerma From Mermas", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                idv = c.getInt(0);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return idv;
    }
    public int contarDetMermas(){
        Integer idv =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select MAX(Id_DetMermas) As IdDetMerma From DetMermas", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                idv = c.getInt(0);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return idv;
    }

    public String getNombreCliente(int IdCliente){
        String[] args = new String[]{String.valueOf(IdCliente)};
        String[] campos = new String[]{"Nombre"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Clientes",campos,"Id_Cliente=?",args, null,null,null);
        String nombre = "";

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    nombre = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return nombre;
    }
    public Double getTotalRuta(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select SUM(Total) From Ventas",null);
        Double SumaTotal = 0.00;

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    SumaTotal = cursor.getDouble(0);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return SumaTotal;
    }
    public int getTotalVentas(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select COUNT(Id_Ventas) From Ventas",null);
        int SumaVentas = 0;

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    SumaVentas = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return SumaVentas;
    }
    public Double getTotalCreRuta(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select SUM(Total_Credito) From Ventas",null);
        Double SumaTotal = 0.00;

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    SumaTotal = cursor.getDouble(0);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return SumaTotal;
    }
    public Double getTotalConRuta(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select SUM(Total_Contado) From Ventas",null);
        Double SumaTotal = 0.00;

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    SumaTotal = cursor.getDouble(0);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return SumaTotal;
    }

    //Datos Cierre de Ruta
    public double getTotalGastos(){

        double total = 0;
        // Select All Query
        String selectQuery = "SELECT SUM(Total) FROM Gastos";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    total = cursor.getDouble(0);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return total;
    }

    public int getPiezasRuta(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select SUM(Piezas_T) From Ventas",null);
        int SumaPiezas = 0;

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    SumaPiezas = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return SumaPiezas;
    }
    public clsClientes[] getClientesObjeto(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Clientes", null);
        clsClientes[] clientesGuard = new clsClientes[cursor.getCount()];

        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Cliente = cursor.getInt(0);
                    String Nombre = cursor.getString(1);
                    String Ubicacion = cursor.getString(2);
                    String Latitud = cursor.getString(3);
                    String Longitud = cursor.getString(4);
                    int Id_Lis = cursor.getInt(5);
                    int TipoPago = cursor.getInt(6);

                    clsClientes clientes = new clsClientes(Id_Cliente, Nombre, Ubicacion, Latitud, Longitud, Id_Lis,TipoPago);
                    clientesGuard[i] = clientes;
                    //parametros.put("Venta", Gson_Converter.toJson(venta));
                    //data = new JSONObject(parametros);
                    //ventasGuardar.add(venta);
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return clientesGuard;
    }

    public int deleteClientes() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Clientes", "1", null);
    }
    public int deleteProductos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Productos", "1", null);
    }
    public int deleteListas() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Listas", "1", null);
    }
    public int deleteListasPrecios() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Listas_Precios", "1", null);
    }
    public int deleteConservadores() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Conservadores", "1", null);
    }
    public int deleteImagenes_Conservadores() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Imagenes_Conservadores", "1", null);
    }
    public int deleteConservadores_Auditar() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Conservadores_Auditar", "1", null);
    }
    public int deleteCoordenadas() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Coordenadas", "1", null);
    }

    public int deleteGasto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Gastos", "Id_Gasto = "+id, null);
    }

    //Borrar ventas temporales
    public int deleteVentasG() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Ventas_Guardadas", "1", null);
    }
    public int deleteDetVentasG() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("DetVentas_Guardadas", "1", null);
    }
    //Borrar ventas
    public int deleteVentas() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Ventas", "1", null);
    }
    public int deleteDetVentas() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("DetVentas", "1", null);
    }
    public int deleteMermas() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Mermas", "1", null);
    }
    public int deleteDetMermas() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("DetMermas", "1", null);
    }
    public int deleteCierreRutas() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Cierre_Ruta", "1", null);
    }
    public int deleteUsuarios() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Usuarios", "1", null);
    }
    public int deleteUnidades() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Unidades", "1", null);
    }

    //Interaccion SQLite y MySQL
    public clsVentas[] EnviarVentas(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Ventas", null);
        clsVentas[] ventasGuard = new clsVentas[cursor.getCount()];

        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Venta = cursor.getInt(0);
                    Double Total = cursor.getDouble(1);
                    Double Total_Contado = cursor.getDouble(2);
                    Double Total_Credito = cursor.getDouble(3);
                    int Piezas_T = cursor.getInt(4);
                    int TipoPago = cursor.getInt(5);
                    int Fecha = cursor.getInt(6);
                    int Id_Usuario = cursor.getInt(7);
                    int Id_Cliente = cursor.getInt(8);
                    int Id_Situacion = cursor.getInt(9);
                    String Latitud = cursor.getString(10);
                    String Longitud = cursor.getString(11);
                    int Estatus = cursor.getInt(12);
                    String Tiempo_Venta = cursor.getString(13);
                    int Enviar = cursor.getInt(14);

                    //Map<String, Object> parametros = new HashMap();
                    clsVentas venta = new clsVentas(Id_Venta, Total, Total_Contado, Total_Credito, Piezas_T, TipoPago, Fecha, Id_Usuario, Id_Cliente, Id_Situacion, Latitud,
                            Longitud,Estatus, Tiempo_Venta, Enviar);
                    ventasGuard[i] = venta;
                    //parametros.put("Venta", Gson_Converter.toJson(venta));
                    //data = new JSONObject(parametros);
                    //ventasGuardar.add(venta);
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return ventasGuard;
    }
    public clsVentas[] EnviarVentaInternet(int IdV){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = new String[]{String.valueOf(IdV)};
        String[] campos = new String[]{"*"};
        Cursor cursor = db.query("Ventas",campos,"Id_Ventas=?",args, null,null,null);
        clsVentas[] ventasGuard = new clsVentas[cursor.getCount()];

        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Venta = cursor.getInt(0);
                    Double Total = cursor.getDouble(1);
                    Double Total_Contado = cursor.getDouble(2);
                    Double Total_Credito = cursor.getDouble(3);
                    int Piezas_T = cursor.getInt(4);
                    int TipoPago = cursor.getInt(5);
                    int Fecha = cursor.getInt(6);
                    int Id_Usuario = cursor.getInt(7);
                    int Id_Cliente = cursor.getInt(8);
                    int Id_Situacion = cursor.getInt(9);
                    String Latitud = cursor.getString(10);
                    String Longitud = cursor.getString(11);
                    int Estatus = cursor.getInt(12);
                    String Tiempo_Venta = cursor.getString(13);
                    int Enviar = cursor.getInt(14);

                    //Map<String, Object> parametros = new HashMap();
                    clsVentas venta = new clsVentas(Id_Venta, Total, Total_Contado, Total_Credito, Piezas_T, TipoPago, Fecha, Id_Usuario, Id_Cliente, Id_Situacion, Latitud,
                            Longitud,Estatus, Tiempo_Venta, Enviar);
                    ventasGuard[i] = venta;
                    //parametros.put("Venta", Gson_Converter.toJson(venta));
                    //data = new JSONObject(parametros);
                    //ventasGuardar.add(venta);
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return ventasGuard;
    }
    public clsDetVentas[] EnviarDetVentaInternet(int IdV){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = new String[]{String.valueOf(IdV)};
        String[] campos = new String[]{"*"};
        Cursor cursor = db.query("DetVentas",campos,"Id_Venta=?",args, null,null,null);
        clsDetVentas[] detventasGuard = new clsDetVentas[cursor.getCount()];
        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_DetVenta = cursor.getInt(0);
                    int Id_Venta = cursor.getInt(1);
                    int Id_Producto = cursor.getInt(2);
                    int Piezas = cursor.getInt(3);
                    Double Precio = cursor.getDouble(4);
                    int TipoPago = cursor.getInt(5);

                    clsDetVentas detventa = new clsDetVentas(Id_DetVenta, Id_Venta, Id_Producto, Piezas, Precio, TipoPago);
                    detventasGuard[i] = detventa;
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return detventasGuard;
    }
    public clsDetVentas[] EnviarDetVentas(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From DetVentas", null);
        clsDetVentas[] detventasGuard = new clsDetVentas[cursor.getCount()];
        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_DetVenta = cursor.getInt(0);
                    int Id_Venta = cursor.getInt(1);
                    int Id_Producto = cursor.getInt(2);
                    int Piezas = cursor.getInt(3);
                    Double Precio = cursor.getDouble(4);
                    int TipoPago = cursor.getInt(5);

                    clsDetVentas detventa = new clsDetVentas(Id_DetVenta, Id_Venta, Id_Producto, Piezas, Precio, TipoPago);
                    detventasGuard[i] = detventa;
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return detventasGuard;
    }
    public clsCoordenadas[] EnviarCoordenadas(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Coordenadas", null);
        clsCoordenadas[] coordenadasGuar = new clsCoordenadas[cursor.getCount()];
        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Coor = cursor.getInt(0);
                    String Lat = cursor.getString(1);
                    String Lon = cursor.getString(2);
                    int Id_Us = cursor.getInt(3);
                    String Fech = cursor.getString(4);

                    clsCoordenadas coord = new clsCoordenadas(Id_Coor, Lat, Lon,Id_Us, Fech);
                    coordenadasGuar[i] = coord;
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return coordenadasGuar;
    }
    public clsAuditoria EnviarAuditoria(){
        int i = 0;
        String selectQuery = "SELECT aud.Id_Auditoria, aud.Latitud_Rev, aud.Longitud_Rev,aud.Id_CheckL, aud.Id_Cliente_Rev, aud.Imagen_Ev," +
                " aud.Auditado, aud.QR, aud.Etiqueta, aud.Serie From Conservadores_Auditar aud INNER JOIN Conservadores con ON aud.Id_Conservador = con.Id_Conservador " +
                "INNER JOIN Imagenes_Conservadores img ON aud.Id_Conservador = img.Id_Conservador";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        clsAuditoria auditoriaGuar = new clsAuditoria();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Aud = cursor.getInt(0);
                    String Latitud_Rev = cursor.getString(1);
                    String Longitud_Rev = cursor.getString(2);
                    int Id_CheckL = cursor.getInt(3);
                    int Id_Cliente_Rev = cursor.getInt(4);
                    byte[] Imagen_Ev = cursor.getBlob(5);
                    int Auditado = cursor.getInt(6);
                    int QR = cursor.getInt(7);
                    String Etiqueta = cursor.getString(8);
                    String Serie = cursor.getString(9);


                    clsAuditoria aud = new clsAuditoria(Id_Aud, 0,QR, Etiqueta, Serie,0,0,0,
                            Id_CheckL,Latitud_Rev,Longitud_Rev,Id_Cliente_Rev,Imagen_Ev, null,Auditado);
                    auditoriaGuar=aud;//adding 2nd column data
                    i++;
                } while (cursor.moveToNext());
            }
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return auditoriaGuar;
    }
    public clsMermas EnviarMermas(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Mermas", null);
        clsMermas merma = new clsMermas();

        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Merma = cursor.getInt(0);
                    int Piezas_T = cursor.getInt(1);
                    Double Total = cursor.getDouble(2);
                    byte[] Evid = cursor.getBlob(3);
                    int Fecha = cursor.getInt(4);
                    int Enviado = cursor.getInt(5);
                    int Id_Usuario = cursor.getInt(6);

                    //Map<String, Object> parametros = new HashMap();
                    merma = new clsMermas(Id_Merma, Piezas_T, Total, Evid, Fecha, Enviado, Id_Usuario);
                    //parametros.put("Venta", Gson_Converter.toJson(venta));
                    //data = new JSONObject(parametros);
                    //ventasGuardar.add(venta);
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return merma;
    }
    public clsDetMermas[] EnviarDetMermas(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From DetMermas", null);
        clsDetMermas[] detmermasGuard = new clsDetMermas[cursor.getCount()];
        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_DetMermas = cursor.getInt(0);
                    int Id_Merma = cursor.getInt(1);
                    int Id_Producto = cursor.getInt(2);
                    int Piezas = cursor.getInt(3);
                    Double Precio = cursor.getDouble(4);
                    Double Total = cursor.getDouble(5);

                    clsDetMermas detventa = new clsDetMermas(Id_DetMermas, Id_Merma, Id_Producto, Piezas, Precio, Total);
                    detmermasGuard[i] = detventa;
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return detmermasGuard;
    }
    public clsMermas[] EnviarMermaInternet(int IdM){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = new String[]{String.valueOf(IdM)};
        String[] campos = new String[]{"*"};
        Cursor cursor = db.query("Mermas",campos,"Id_Mermas=?",args, null,null,null);
        clsMermas[] mermasGuard = new clsMermas[cursor.getCount()];

        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Merma = cursor.getInt(0);
                    int Piezas_T = cursor.getInt(1);
                    Double Total = cursor.getDouble(2);
                    byte[] Evid = cursor.getBlob(3);
                    int Fecha = cursor.getInt(4);
                    int Enviado = cursor.getInt(5);
                    int Id_Usuario = cursor.getInt(6);

                    //Map<String, Object> parametros = new HashMap();
                    clsMermas merma = new clsMermas(Id_Merma, Piezas_T, Total, Evid, Fecha, Enviado, Id_Usuario);

                    mermasGuard[i] = merma;
                    //parametros.put("Venta", Gson_Converter.toJson(venta));
                    //data = new JSONObject(parametros);
                    //ventasGuardar.add(venta);
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return mermasGuard;
    }
    public clsCierreRuta EnviarCierre(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Cierre_Ruta", null);
        clsCierreRuta cierreGuard = new clsCierreRuta();
        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    int Id_Ruta = cursor.getInt(0);
                    int Id_Usuario = cursor.getInt(1);
                    String Hora_Inicio= cursor.getString(2);
                    String Hora_Fin= cursor.getString(3);
                    String Tiempo= cursor.getString(4);
                    double Total_Gastos = cursor.getDouble(5);
                    double Total= cursor.getDouble(6);
                    double Total_Contado= cursor.getDouble(7);
                    double Total_Credito= cursor.getDouble(8);
                    double Total_Cierre= cursor.getDouble(9);
                    int Piezas= cursor.getInt(10);
                    String Lat_Ini= cursor.getString(11);
                    String Long_Ini= cursor.getString(12);
                    String Lat_Fin= cursor.getString(13);
                    String Long_Fin= cursor.getString(14);
                    double Kilometraje_Ini= cursor.getDouble(15);
                    double Kilometraje_Fin= cursor.getDouble(16);
                    int Fecha = cursor.getInt(17);
                    int Enviado = cursor.getInt(18);

                    cierreGuard = new clsCierreRuta(Id_Ruta, Id_Usuario,Hora_Inicio,Hora_Fin, Tiempo, Total_Gastos,Total,
                            Total_Contado,Total_Credito,Total_Cierre,Piezas,Lat_Ini,Long_Ini, Lat_Fin,Long_Fin, Kilometraje_Ini,
                            Kilometraje_Fin, Fecha,Enviado);
                    i++;
                } while (cursor.moveToNext());
            }
        }
        return cierreGuard;
    }
}