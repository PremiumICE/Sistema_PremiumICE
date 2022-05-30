package premium.conservadores.sistema_conservadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import premium.conservadores.sistema_conservadores.Clases.SQLocal;

public class SplashActivity extends AppCompatActivity {
    private static final String url = "jdbc:mysql://162.241.62.225:3306/dmecommx_sistemapi?characterEncoding=latin1";
    private static final String user = "dmecommx_khaf";
    private static final String pass = "Resident123";
    SQLocal dbLocal;
    SharedPreferences Configuraciones;
    SharedPreferences.Editor EditarConf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbLocal = new SQLocal(this);
        Configuraciones = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EditarConf = Configuraciones.edit();
        if(Configuraciones.getInt("Id_Usuario",0)!=0){
            TraerAuditorias auditorias = new TraerAuditorias("");
            auditorias.execute();
        }else{
            TraerDatos datos = new TraerDatos("");
            datos.execute();
        }
    }

    public class TraerDatos extends AsyncTask<String, String, String> {
        public TraerDatos(String params) {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                dbLocal.deleteProductos();
                dbLocal.deleteClientes();
                dbLocal.deleteListas();
                dbLocal.deleteImagenes_Conservadores();
                dbLocal.deleteListasPrecios();
                dbLocal.deleteConservadores();
                dbLocal.deleteConservadores_Auditar();
                dbLocal.deleteUsuarios();
                dbLocal.deleteUnidades();
                Class.forName("com.mysql.jdbc.Driver");
                // "jdbc:mysql://IP:PUERTO/DB", "USER", "PASSWORD");
                // Si estás utilizando el emulador de android y tenes el mysql en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                Connection conn = DriverManager.getConnection(url, user, pass);
                //En el stsql se puede agregar cualquier consulta SQL deseada.
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("Select Id_Producto, Descripcion From Cat_Productos");
                while(rs.next()){
                    dbLocal.InsertProducto(rs.getInt(1),rs.getString(2),20.00,1);
                }
                rs = st.executeQuery("Select Id_Cliente, Nombre, Calle, Latitud, Longitud, Id_Lista, Tipo_Pago From Cat_Clientes");
                while(rs.next()){
                    dbLocal.InsertCliente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
                            rs.getString(5),rs.getInt(6),rs.getInt(7));
                }
                rs = st.executeQuery("Select * From Cat_Lista");
                while(rs.next()){
                    dbLocal.InsertLista(rs.getInt(1),rs.getString(2),rs.getString(3));
                }
                rs = st.executeQuery("Select * From Ope_Lista");
                while(rs.next()){
                    dbLocal.InsertListaPrecios(rs.getInt(1), rs.getInt(2),rs.getInt(3),rs.getDouble(4));
                }
                rs = st.executeQuery("Select Id_Conservador, Identificador, Id_Factura, No_Serie, Modelo, QR, Id_Cedis, Id_Cliente, Ubicacion From Ope_Conservadores");
                while(rs.next()){
                    dbLocal.InsertConservadores(rs.getInt(1), rs.getString(2), rs.getInt(3),rs.getString(4),
                            rs.getString(5),rs.getInt(6),rs.getInt(7),rs.getInt(8),rs.getString(9));
                }
                rs = st.executeQuery("Select Id_Imagen, Id_Conservador, QR, Img_CPuertaC From Cat_Imagenes");
                while(rs.next()){
                    dbLocal.InsertImgCons(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getBytes(4));
                }
                rs = st.executeQuery("Select Id_ConservadorA, Id_Conservador, Fecha_Inicio, Fecha_Fin, Estatus, " +
                        "Id_CheckList, Latitud, Longitud, Id_Cliente, Imagen_Rev, Id_Usuario, Auditado From Ope_Conservador_Usuario Where Id_Usuario = "+Configuraciones.getInt("Id_Usuario",0));
                while(rs.next()){
                    dbLocal.InsertAuditoria(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4),rs.getInt(5),
                            rs.getInt(6), rs.getString(7), rs.getString(8),rs.getInt(9),
                            rs.getBytes(10),0,"","", rs.getInt(11), rs.getInt(12));
                }
                rs = st.executeQuery("Select Id_Usuario, Usuario From Apl_Usuarios");
                while(rs.next()){
                    dbLocal.InsertUsuarios(rs.getInt(1), rs.getString(2));
                }
                rs = st.executeQuery("Select Id_Unidad, Unidad, Placas From Cat_Unidades");
                while(rs.next()){
                    dbLocal.InsertUnidades(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
                conn.close();
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
            return "";

        }
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public class TraerAuditorias extends AsyncTask<String, String, String> {
        public TraerAuditorias(String params) {
        }

        @Override
        protected void onPreExecute() {
            dbLocal.deleteConservadores_Auditar();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                // "jdbc:mysql://IP:PUERTO/DB", "USER", "PASSWORD");
                // Si estás utilizando el emulador de android y tenes el mysql en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                Connection conn = DriverManager.getConnection(url, user, pass);
                //En el stsql se puede agregar cualquier consulta SQL deseada.
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("Select Id_ConservadorA, Id_Conservador, Fecha_Inicio, Fecha_Fin, Estatus, " +
                        "Id_CheckList, Latitud, Longitud, Id_Cliente, Imagen_Rev, Id_Usuario, Auditado From Ope_Conservador_Usuario Where Id_Usuario = "+Configuraciones.getInt("Id_Usuario",0));

                while(rs.next()){
                    dbLocal.InsertAuditoria(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4),rs.getInt(5),
                            rs.getInt(6), rs.getString(7), rs.getString(8),rs.getInt(9),
                            rs.getBytes(10),0,"","", rs.getInt(11), rs.getInt(12));
                }
                conn.close();
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
            return "";

        }
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}