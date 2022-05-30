package premium.conservadores.sistema_conservadores.Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Guardar_Mysql implements Runnable {
    private static final String url = "jdbc:mysql://162.241.62.225:3306/dmecommx_sistemapi?characterEncoding=latin1";
    private static final String user = "dmecommx_khaf";
    private static final String pass = "Resident123";

    private String Consulta;

    public void setName(String Cons) {
        this.Consulta = Cons;
        /*this.total = Total;
        this.totalCre = TotalCredito;
        this.totalCon = TotalContado;
        this.Piezas = Piez;
        this.Id_Cliente = Id_Cli;
        this.Situacion = Id_Us;
        this.Id_Usuario = Sit;
        this.latitud = Lat;
        this.longitud = Long;
        this.tiempo = Tiem;
*/
    }

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // "jdbc:mysql://IP:PUERTO/DB", "USER", "PASSWORD");
            // Si estás utilizando el emulador de android y tenes el mysql en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
            Connection conn = DriverManager.getConnection(url, user, pass);
            //En el stsql se puede agregar cualquier consulta SQL deseada.
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(Consulta);
                    conn.close();
        } catch (SQLException se) {
            System.out.println("oops! No se puede conectar. Error: " + se.toString());
        } catch (ClassNotFoundException e) {
            System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
        }
    }
}